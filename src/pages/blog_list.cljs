(ns pages.blog-list
  (:require ["react-router-dom" :refer [Link]]
            [utils.site-data :as site-data]))

(defn blog-list []
  (let [{:keys [data loading error]} (site-data/use-blog-manifest)
        posts (when data (:posts data))]
    #jsx [:div {:className "min-h-screen bg-bg font-sans"}
          [:div {:className "max-w-4xl mx-auto px-6 py-16"}
           [:Link {:to "/"
                   :className "text-accent-blue hover:underline mb-8 inline-block"}
            "← Back to home"]
           [:h1 {:className "text-4xl md:text-5xl font-bold text-text-primary mb-4"} "Blog"]
           [:p {:className "text-xl text-text-secondary mb-12"}
            "Notes on software, systems, and things I'm learning."]
           (cond
             loading
             #jsx [:p {:className "text-text-secondary"} "Loading..."]
             error
             #jsx [:p {:className "text-red-500"} error]
             (empty? posts)
             #jsx [:p {:className "text-text-secondary"} "No posts yet."]
             :else
             #jsx [:div {:className "grid grid-cols-1 md:grid-cols-2 gap-6"}
                   (-> posts
                       (.map (fn [post]
                               #jsx [:Link {:key (:slug post)
                                            :to (str "/blog/" (:slug post))
                                            :className "bento-card p-6 hover:scale-[1.02] transition-transform block"}
                                     [:h2 {:className "text-xl font-bold text-text-primary mb-2"}
                                      (:title post)]
                                     (when (:date post)
                                       [:p {:className "text-sm text-text-secondary mb-2"} (:date post)])
                                     [:p {:className "text-sm text-accent-blue"} "Read more →"]])))])]]))
