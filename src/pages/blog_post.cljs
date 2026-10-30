(ns pages.blog-post
  (:require ["react" :as react]
            ["react-router-dom" :refer [Link useParams useLocation]]
            ["lucide-react" :refer [ChevronRight]]
            [utils.site-data :as site-data]))

(defn missing-post-view [pathname posts]
  #jsx [:div {:className "min-h-screen bg-bg font-sans px-4 py-8 md:px-6 md:py-16"}
        [:div {:className "max-w-2xl mx-auto"}
         [:Link {:to "/blog"
                 :className "text-accent-blue hover:underline mb-6 inline-block text-sm md:text-base"}
          "← Back to blog"]
         [:div {:className "bento-card p-6 md:p-10"}
          [:p {:className "text-xs font-medium uppercase tracking-wide text-text-secondary mb-4"}
           "404 · POST"]
          [:h1 {:className "text-3xl md:text-4xl font-bold text-text-primary mb-4"}
           "This post doesn't exist."]
          [:code {:className "inline-block bg-gray-100 text-text-secondary text-sm font-mono px-4 py-2 rounded-full mb-8"}
           pathname]
          [:p {:className "text-text-secondary mb-4"} "Try one of these instead:"]
          [:ul {:className "space-y-3 mb-8"}
           (-> posts
               (.map (fn [post]
                       #jsx [:li {:key (:slug post)}
                             [:Link {:to (str "/blog/" (:slug post))
                                     :className "flex items-center justify-between gap-4 bg-gray-100 hover:bg-gray-200/80 rounded-2xl px-4 py-4 transition-colors"}
                              [:span {:className "font-semibold text-text-primary text-left"}
                               (:title post)]
                              [:span {:className "flex items-center gap-2 shrink-0 text-text-secondary text-sm"}
                               (when (:date post) (:date post))
                               #jsx [ChevronRight {:size 18}]]]])))]
          [:Link {:to "/blog"
                  :className "inline-block bg-accent-blue text-white font-medium px-6 py-3 rounded-full hover:opacity-90 transition-opacity"}
           "All posts"]]]])

(defn blog-post []
  (let [slug (.-slug (useParams))
        pathname (.-pathname (useLocation))
        {:keys [data manifest-loading manifest-error]} (site-data/use-blog-manifest)
        posts (when data (:posts data))
        slug-known? (boolean (some #(= (:slug %) slug) posts))
        {:keys [data post-loading post-error]} (site-data/use-blog-post (when slug-known? slug))]
    (react/useEffect
     (fn []
       (when (and slug-known? (:title data))
         (set! (.-title js/document) (str (:title data) " | Desmond Wang")))
       js/undefined)
     #js [data slug-known?])
    (react/useEffect
     (fn []
       (when (and (not manifest-loading) (not slug-known?))
         (set! (.-title js/document) "404 | Desmond Wang")
         (let [existing (.querySelector js/document "meta[name=\"robots\"]")
               meta (or existing (.createElement js/document "meta"))]
           (.setAttribute meta "name" "robots")
           (.setAttribute meta "content" "noindex")
           (when-not existing (.appendChild (.-head js/document) meta))))
       js/undefined)
     #js [manifest-loading slug-known?])
    (cond
      manifest-loading
      #jsx [:div {:className "min-h-screen bg-bg font-sans px-6 py-16"}
            [:p {:className "text-text-secondary max-w-2xl mx-auto"} "Loading..."]]

      manifest-error
      #jsx [:div {:className "min-h-screen bg-bg font-sans px-6 py-16"}
            [:p {:className "text-red-500 max-w-2xl mx-auto"} manifest-error]]

      (not slug-known?)
      (missing-post-view pathname (or posts #js []))

      post-loading
      #jsx [:div {:className "min-h-screen bg-bg font-sans px-6 py-16"}
            [:div {:className "max-w-3xl mx-auto"}
             [:Link {:to "/blog"
                     :className "text-accent-blue hover:underline mb-8 inline-block"}
              "← Back to blog"]
             [:p {:className "text-text-secondary"} "Loading..."]]]

      post-error
      #jsx [:div {:className "min-h-screen bg-bg font-sans px-6 py-16"}
            [:div {:className "max-w-3xl mx-auto"}
             [:Link {:to "/blog"
                     :className "text-accent-blue hover:underline mb-8 inline-block"}
              "← Back to blog"]
             [:p {:className "text-red-500"} post-error]]]

      :else
      #jsx [:div {:className "min-h-screen bg-bg font-sans"}
            [:div {:className "max-w-3xl mx-auto px-6 py-16"}
             [:Link {:to "/blog"
                     :className "text-accent-blue hover:underline mb-8 inline-block"}
              "← Back to blog"]
             [:article
              [:h1 {:className "text-4xl md:text-5xl font-bold text-text-primary mb-4"}
               (:title data)]
              (when (:date data)
                #jsx [:p {:className "text-text-secondary mb-8"} (:date data)])
              [:div {:className "prose prose-lg prose-slate max-w-none prose-headings:text-text-primary prose-p:text-text-secondary prose-strong:text-text-primary prose-a:text-accent-blue prose-ul:text-text-secondary"
                     :dangerouslySetInnerHTML {:__html (:html data)}}]]]])))
