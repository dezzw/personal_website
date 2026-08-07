(ns components.icon
  (:require ["react-router-dom" :refer [Link]]))

(defn icon []
  #jsx [:Link {:to "/blog"
               :className "bento-card h-full flex items-center justify-center bg-accent-blue text-white group cursor-pointer block"
               :aria-label "Open blog"}
        [:div {:className "text-center"}
         [:div {:className "text-4xl md:text-5xl font-bold group-hover:scale-110 transition-transform duration-300"}
          "dw."]
         [:p {:className "text-sm mt-2 opacity-80"} "Blog"]]])
