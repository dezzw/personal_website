(ns components.icon)

(defn icon-expanded []
  #jsx [:div {:className "h-full flex flex-col items-center justify-center p-8 text-center bg-accent-blue text-white"}
        [:h1 {:className "text-8xl font-bold mb-4"} "dw."]
        [:p {:className "text-2xl font-light opacity-90"} "Design. Develop. Deploy."]
        [:p {:className "mt-8 text-sm opacity-70"} "© 2024 Desmond Wang. All rights reserved."]])

(defn icon []
  #jsx [:div {:className "bento-card h-full flex items-center justify-center bg-accent-blue text-white group cursor-pointer"}
        [:div {:className "text-4xl md:text-5xl font-bold group-hover:scale-110 transition-transform duration-300"}
         "dw."]])
