(ns pages.blog-list
  (:require ["framer-motion" :refer [motion]]
            ["react-router-dom" :refer [Link]]
            [utils.site-data :as site-data]
            [utils.motion :as motion-utils]))

(defn blog-list []
  (let [{:keys [data loading error]} (site-data/use-blog-manifest)
        posts (when data (:posts data))
        reduced-motion (motion-utils/use-reduced-motion)
        container-v (motion-utils/list-container-variants reduced-motion)
        item-v (motion-utils/list-item-variants reduced-motion)]
    #jsx [:div {:className "min-h-screen bg-bg font-sans"}
          #jsx [motion.div {:className "max-w-4xl mx-auto px-6 py-16"
                            :variants container-v
                            :initial "hidden"
                            :animate "visible"}
                #jsx [motion.div {:variants item-v}
                      #jsx [Link {:to "/"
                                  :className "text-accent-blue hover:underline mb-8 inline-block"}
                            "← Back to home"]]

                #jsx [motion.div {:variants item-v}
                      [:h1 {:className "text-4xl md:text-5xl font-bold text-text-primary mb-4"} "Blog"]]

                #jsx [motion.div {:variants item-v}
                      [:p {:className "text-xl text-text-secondary mb-12"}
                       "Notes on software, systems, and things I'm learning."]]

                (cond
                  loading
                  #jsx [motion.p {:variants item-v :className "text-text-secondary"} "Loading..."]
                  error
                  #jsx [motion.p {:variants item-v :className "text-red-500"} error]
                  (empty? posts)
                  #jsx [motion.p {:variants item-v :className "text-text-secondary"} "No posts yet."]
                  :else
                  #jsx [motion.div {:className "grid grid-cols-1 md:grid-cols-2 gap-6"
                                    :variants container-v
                                    :initial "hidden"
                                    :animate "visible"}
                        (-> posts
                            (.map (fn [post]
                                    #jsx [motion.div {:key (:slug post)
                                                      :variants item-v
                                                      :whileHover (when-not reduced-motion {:scale 1.02})}
                                          #jsx [Link {:to (str "/blog/" (:slug post))
                                                      :className "bento-card p-6 block h-full"}
                                                [:h2 {:className "text-xl font-bold text-text-primary mb-2"}
                                                 (:title post)]
                                                (when (:date post)
                                                  [:p {:className "text-sm text-text-secondary mb-2"} (:date post)])
                                                [:p {:className "text-sm text-accent-blue"} "Read more →"]]])))])]]))
