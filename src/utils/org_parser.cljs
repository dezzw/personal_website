(ns utils.org-parser
  (:require ["unified" :refer [unified]]
            ["uniorg-parse" :as uniorg-parse-module]
            ["uniorg-rehype" :as uniorg-rehype-module]
            ["rehype-stringify" :as rehype-stringify-module]
            ["react" :as react]))

(def uniorgParse (.-default uniorg-parse-module))
(def uniorgRehype (.-default uniorg-rehype-module))
(def rehypeStringify (.-default rehype-stringify-module))

(defn create-processor []
  (-> (unified)
      (.use uniorgParse)
      (.use uniorgRehype)
      (.use rehypeStringify)))

(defn parse-org-file [org-content processor]
  (-> (.process processor org-content)
      (.then (fn [file]
               (.-value file)))
      (.catch (fn [error]
                ""))))

(defn use-org-content [org-path]
  (let [[content set-content] (react/useState nil)
        [loading set-loading] (react/useState true)
        [error set-error] (react/useState nil)]
    
    (react/useEffect
     (fn []
       (set-loading true)
       (set-error nil)
       (-> (js/fetch org-path)
           (.then (fn [response]
                    (if (.-ok response)
                      (.text response)
                      (throw (js/Error (str "Failed to load org file: " (.-status response)))))))
           (.then (fn [text]
                    (let [processor (create-processor)]
                      (parse-org-file text processor))))
           (.then (fn [html]
                    (set-content html)
                    (set-loading false)))
           (.catch (fn [err]
                     (set-error (str "Failed to load content: " (.-message err)))
                     (set-loading false)))))
     #js [org-path])
    
    {:content content
     :loading loading
     :error error}))
