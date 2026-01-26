(ns components.icon
  (:require ["react" :as react]
            [utils.org-parser :as org-parser]
            [utils.blog-router :as blog-router]))

(defn BlogCard [{:keys [title slug on-click]}]
  #jsx [:div {:className "bento-card p-6 cursor-pointer hover:scale-105 transition-transform duration-200 bg-white/50 backdrop-blur-sm border border-white/20"
              :onClick on-click}
        [:h3 {:className "text-xl font-bold text-text-primary mb-2"} title]
        [:p {:className "text-sm text-text-secondary"} "Click to read →"]])

(defn BlogPostView [{:keys [path on-back]}]
  (let [{:keys [content loading error]} (org-parser/use-org-content path)]
    (cond
      loading
      #jsx [:div {:className "h-full flex flex-col p-8 md:p-12 overflow-y-auto"}
            [:button {:className "mb-6 text-accent-blue hover:underline self-start"
                      :onClick on-back}
             "← Back to blog"]
            [:div {:className "text-text-secondary text-center py-8"} "Loading..."]]
      
      error
      #jsx [:div {:className "h-full flex flex-col p-8 md:p-12 overflow-y-auto"}
            [:button {:className "mb-6 text-accent-blue hover:underline self-start"
                      :onClick on-back}
             "← Back to blog"]
            [:div {:className "text-red-500 text-center py-8"} error]]
      
      :else
      #jsx [:div {:className "h-full flex flex-col p-8 md:p-12 overflow-y-auto"}
            [:button {:className "mb-6 text-accent-blue hover:underline self-start"
                      :onClick on-back}
             "← Back to blog"]
            [:div {:className "max-w-3xl w-full mx-auto prose prose-lg prose-slate max-w-none prose-headings:text-text-primary prose-p:text-text-secondary prose-strong:text-text-primary prose-a:text-accent-blue prose-a:no-underline hover:prose-a:underline prose-ul:text-text-secondary prose-li:text-text-secondary"
                   :dangerouslySetInnerHTML {:__html content}}]])))

(defn BlogListView [{:keys [blogs on-select-post]}]
  #jsx [:div {:className "h-full flex flex-col p-8 md:p-12 overflow-y-auto"}
        [:h2 {:className "text-3xl font-bold text-text-primary mb-8"} "Blog"]
        (if (empty? blogs)
          #jsx [:div {:className "text-text-secondary text-center py-8"} "No blog posts yet."]
          #jsx [:div {:className "grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6"}
                (-> blogs
                    (.map (fn [blog]
                            #jsx [BlogCard {:key (:slug blog)
                                            :title (:title blog)
                                            :slug (:slug blog)
                                            :on-click #(on-select-post (:slug blog))}])))])])

(defn icon-expanded []
  (let [{:keys [route selected-slug navigate-to-list navigate-to-post blogs]} (blog-router/use-blog-router)
        selected-blog (when selected-slug
                        (-> blogs
                            (.find (fn [blog] (= (:slug blog) selected-slug)))))]
    (cond
      (= route "post")
      (if selected-blog
        #jsx [BlogPostView {:path (:path selected-blog)
                           :on-back navigate-to-list}]
        #jsx [:div {:className "p-8"} "Blog not found"])
      
      (= route "component")
      #jsx [:div {:className "p-8"} "Component view - coming soon"]
      
      :else
      #jsx [BlogListView {:blogs blogs
                         :on-select-post navigate-to-post}])))

(defn icon []
  #jsx [:div {:className "bento-card h-full flex items-center justify-center bg-accent-blue text-white group cursor-pointer"}
        [:div {:className "text-4xl md:text-5xl font-bold group-hover:scale-110 transition-transform duration-300"}
         "dw."]])
