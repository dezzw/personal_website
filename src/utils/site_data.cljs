(ns utils.site-data
  (:require ["react" :as react]))

(defn use-json [url]
  (let [[state set-state] (react/useState #js {:data nil :loading true :error nil})]
    (react/useEffect
     (fn []
       (let [mounted (atom true)]
         (-> (js/fetch url)
             (.then #(.json %))
             (.then (fn [json]
                      (when @mounted
                        (set-state #js {:data json
                                        :loading false
                                        :error nil}))))
             (.catch (fn [err]
                       (when @mounted
                         (set-state #js {:data nil
                                         :loading false
                                         :error (.-message err)})))))
         (fn [] (reset! mounted false))))
     #js [url])
    state))

(defn use-projects []
  (use-json "/data/projects.json"))

(defn use-experience []
  (use-json "/data/experience.json"))

(defn use-blog-manifest []
  (use-json "/blog/manifest.json"))

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
                      (set-data json)
                      (set-loading false)))
             (.catch (fn [err]
                       (set-error (.-message err))
                       (set-loading false))))
         (do (set-loading false) (set-data nil))))
     #js [slug])
    {:data data :loading loading :error error}))
