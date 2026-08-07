(ns main
  (:require ["react-dom/client" :as rdom]
            ["react-router-dom" :refer [BrowserRouter Routes Route]]
            ["../index.css"]
            [app :as app]
            [pages.blog-list :as blog-list]
            [pages.blog-post :as blog-post]))

(let [root (rdom/createRoot (.getElementById js/document "root"))]
  (.render root
           #jsx [BrowserRouter
                 #jsx [Routes
                       #jsx [Route {:path "/" :element #jsx [app/app]}]
                       #jsx [Route {:path "/blog" :element #jsx [blog-list/blog-list]}]
                       #jsx [Route {:path "/blog/:slug" :element #jsx [blog-post/blog-post]}]]]))
