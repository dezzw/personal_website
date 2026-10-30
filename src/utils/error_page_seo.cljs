(ns utils.error-page-seo
  (:require ["react" :as react]))

(defn use-error-page-seo! [active?]
  (react/useEffect
   (fn []
     (if active?
       (let [prev-title (.-title js/document)
             existing (.querySelector js/document "meta[name=\"robots\"]")
             meta (or existing (.createElement js/document "meta"))
             created? (nil? existing)]
         (when created?
           (.setAttribute meta "name" "robots")
           (.appendChild (.-head js/document) meta))
         (.setAttribute meta "content" "noindex")
         (set! (.-title js/document) "404 | Desmond Wang")
         (fn []
           (.remove meta)
           (set! (.-title js/document) prev-title)))
       (fn [] js/undefined)))
   #js [active?]))
