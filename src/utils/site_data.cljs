(ns utils.site-data
  (:require ["react" :as react]))

(defn use-json [url]
  (let [[data set-data] (react/useState nil)
        [loading set-loading] (react/useState true)
        [error set-error] (react/useState nil)]
    (react/useEffect
     (fn []
       (-> (js/fetch url)
           (.then #(.json %))
           (.then (fn [json]
                    (set-data (js->clj json :keywordize-keys true))
                    (set-loading false)))
           (.catch (fn [err]
                     (set-error (.-message err))
                     (set-loading false)))))
     #js [url])
    {:data data :loading loading :error error}))

(defn use-projects []
  (:data (use-json "/data/projects.json")))

(defn use-experience []
  (:data (use-json "/data/experience.json")))

(defn use-blog-manifest []
  (:data (use-json "/blog/manifest.json")))

(defn use-blog-post [slug]
  (let [[data set-data] (react/useState nil)
        [loading set-loading] (react/useState true)
        [error set-error] (react/useState nil)]
    (react/useEffect
     (fn []
       (if slug
         (-> (js/fetch (str "/blog/" slug ".json"))
             (.then #(.json %))
             (.then (fn [json]
                      (set-data (js->clj json :keywordize-keys true))
                      (set-loading false)))
             (.catch (fn [err]
                       (set-error (.-message err))
                       (set-loading false))))
         (do (set-loading false) (set-data nil))))
     #js [slug])
    {:data data :loading loading :error error}))
