(ns pages.blog-post
  (:require ["react" :as react]
            ["react-router-dom" :refer [Link useParams]]
            [utils.site-data :as site-data]))

(defn blog-post []
  (let [slug (.-slug (useParams))
        {:keys [data loading error]} (site-data/use-blog-post slug)]
    (react/useEffect
     (fn []
       (when (:title data)
         (set! (.-title js/document) (str (:title data) " | Desmond Wang")))
       js/undefined)
     #js [data])
    #jsx [:div {:className "min-h-screen bg-bg font-sans"}
          [:div {:className "max-w-3xl mx-auto px-6 py-16"}
           [:Link {:to "/blog"
                   :className "text-accent-blue hover:underline mb-8 inline-block max-md:inline-flex max-md:items-center max-md:h-11 max-md:px-2 max-md:-mx-2 max-md:-my-[10px] max-md:text-base"}
            "← Back to blog"]
           (cond
             loading
             #jsx [:p {:className "text-text-secondary"} "Loading..."]
             error
             #jsx [:p {:className "text-red-500"} error]
             (not data)
             #jsx [:p {:className "text-text-secondary"} "Post not found."]
             :else
             #jsx [:article
                   [:h1 {:className "text-4xl md:text-5xl font-bold text-text-primary mb-4"}
                    (:title data)]
                   (when (:date data)
                     #jsx [:p {:className "text-text-secondary mb-8"} (:date data)])
                   [:div {:className "prose prose-lg prose-slate max-w-none prose-headings:text-text-primary prose-p:text-text-secondary prose-strong:text-text-primary prose-a:text-accent-blue prose-ul:text-text-secondary"
                          :dangerouslySetInnerHTML {:__html (:html data)}}]])]]))
