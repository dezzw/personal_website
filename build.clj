#!/usr/bin/env bb
(require '[clojure.string :as str]
         '[clojure.edn :as edn]
         '[clojure.java.io :as io]
         '[cheshire.core :as json])

(def content-dir "content")
(def public-dir "public")

(defn ensure-dir [path]
  (.mkdirs (io/file path)))

(defn read-org-title [lines]
  (some (fn [line]
          (when (str/starts-with? line "#+TITLE:")
            (str/trim (subs line (count "#+TITLE:")))))
        lines))

(defn read-org-date [lines]
  (some (fn [line]
          (when (str/starts-with? line "#+DATE:")
            (str/trim (subs line (count "#+DATE:")))))
        lines))

(defn escape-html [s]
  (-> s
      (str/replace "&" "&amp;")
      (str/replace "<" "&lt;")
      (str/replace ">" "&gt;")
      (str/replace "\"" "&quot;")))

(defn inline-format [s]
  (-> s escape-html
      (str/replace #"\*\*(.+?)\*\*" "<strong>$1</strong>")))

(defn org-body->html [lines]
  (loop [lines lines
         html []
         in-list false]
    (if (empty? lines)
      (str (str/join "\n" html) (when in-list "</ul>"))
      (let [line (str/trim (first lines))
            rest-lines (rest lines)]
        (cond
          (str/blank? line)
          (recur rest-lines html in-list)

          (str/starts-with? line "* ")
          (recur rest-lines
                 (conj html (str "<h2>" (inline-format (subs line 2)) "</h2>"))
                 false)

          (str/starts-with? line "- ")
          (let [item (str "<li>" (inline-format (subs line 2)) "</li>")]
            (if in-list
              (recur rest-lines (conj html item) true)
              (recur rest-lines (conj html "<ul>" item) true)))

          :else
          (recur rest-lines
                 (conj html (str "<p>" (inline-format line) "</p>"))
                 in-list))))))

(defn slug-from-filename [file]
  (-> file .getName (str/replace #"\.org$" "")))

(defn parse-org-file [file]
  (let [lines (line-seq (io/reader file))
        lines (doall lines)
        title (or (read-org-title lines) (slug-from-filename file))
        date (read-org-date lines)
        body-lines (remove #(or (str/starts-with? % "#+")
                                 (str/blank? %))
                           lines)
        html (org-body->html body-lines)
        slug (slug-from-filename file)]
    {:slug slug
     :title title
     :date date
     :html html}))

(defn edn->json [data]
  (json/generate-string data {:pretty true}))

(defn build-blog! []
  (let [blog-dir (str content-dir "/blog")
        out-dir (str public-dir "/blog")]
    (ensure-dir out-dir)
    (when (.exists (io/file blog-dir))
      (let [posts (->> (file-seq (io/file blog-dir))
                       (filter #(.endsWith (.getName %) ".org"))
                       (map parse-org-file)
                       (sort-by :slug)
                       vec)]
        (doseq [post posts]
          (spit (str out-dir "/" (:slug post) ".json")
                (edn->json post)))
        (spit (str out-dir "/manifest.json")
              (edn->json {:posts (mapv #(select-keys % [:slug :title :date]) posts)}))
        (println "Built" (count posts) "blog posts")))))

(defn build-site-data! []
  (let [out-dir (str public-dir "/data")]
    (ensure-dir out-dir)
    (doseq [file ["projects.edn" "experience.edn"]]
      (let [path (str content-dir "/data/" file)
            data (edn/read-string (slurp path))
            out-name (str/replace file #"\.edn$" ".json")]
        (spit (str out-dir "/" out-name) (edn->json data))
        (println "Built" out-name)))))

(defn build-seo! []
  (spit (str public-dir "/robots.txt")
        "User-agent: *\nAllow: /\n\nSitemap: https://www.dezzw.com/sitemap.xml\n")
  (let [blog-manifest (when (.exists (io/file (str public-dir "/blog/manifest.json")))
                        (json/parse-string (slurp (str public-dir "/blog/manifest.json")) true))
        posts (or (:posts blog-manifest) [])
        urls (concat ["/"]
                     ["/blog"]
                     (map #(str "/blog/" (:slug %)) posts))
        sitemap (str "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                     "<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n"
                     (str/join "\n"
                               (map (fn [url]
                                      (str "  <url><loc>https://www.dezzw.com"
                                           url
                                           "</loc></url>"))
                                    urls))
                     "\n</urlset>\n")]
    (spit (str public-dir "/sitemap.xml") sitemap)
    (println "Built robots.txt and sitemap.xml")))

(defn -main []
  (ensure-dir public-dir)
  (build-blog!)
  (build-site-data!)
  (build-seo!)
  (println "Build complete."))

(-main)
