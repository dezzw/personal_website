(ns utils.blog-router
  (:require ["react" :as react]))

(def blog-manifest
  [{:slug "about" :title "About This Website" :path "/about.org"}
   {:slug "welcome" :title "Welcome to My Blog" :path "/welcome.org"}])

(defn use-blog-router []
  (let [[route set-route] (react/useState "list")
        [selected-slug set-selected-slug] (react/useState nil)]
    
    {:route route
     :selected-slug selected-slug
     :reset #(do (set-route "list") (set-selected-slug nil))
     :navigate-to-list #(do (set-route "list") (set-selected-slug nil))
     :navigate-to-post #(do (set-route "post") (set-selected-slug %))
     :navigate-to-component #(do (set-route "component") (set-selected-slug %))
     :blogs blog-manifest}))
