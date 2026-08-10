(ns components.todo
  (:require ["framer-motion" :refer [motion]]
            ["lucide-react" :refer [ListTodo CheckCircle2]]))

(defn todo-expanded []
  #jsx [:div {:className "p-8 md:p-12"}
        [:h2 {:className "text-3xl font-bold text-text-primary mb-8"} "Learning Roadmap"]
        
        [:div {:className "space-y-6 max-w-2xl"}
         [:div {:className "bg-gray-50 p-6 rounded-2xl"}
          [:h3 {:className "text-xl font-bold text-text-primary mb-4 flex items-center gap-2"}
           #jsx [ListTodo {:className "text-accent-blue"}]
           "In Progress"]
          [:div {:className "space-y-3"}
           [:div {:className "flex items-center gap-3"}
            [:div {:className "w-2 h-2 rounded-full bg-orange-500 animate-pulse"}]
            [:span {:className "font-medium"} "Rust Systems Programming"]]
           [:div {:className "flex items-center gap-3"}
            [:div {:className "w-2 h-2 rounded-full bg-green-500 animate-pulse"}]
            [:span {:className "font-medium"} "Advanced Clojure & Lisp"]]
           [:div {:className "flex items-center gap-3"}
            [:div {:className "w-2 h-2 rounded-full bg-blue-500 animate-pulse"}]
            [:span {:className "font-medium"} "Embedded Systems & IoT"]]]]
         
         [:div {:className "bg-gray-50 p-6 rounded-2xl opacity-70"}
          [:h3 {:className "text-xl font-bold text-text-primary mb-4 flex items-center gap-2"}
           #jsx [CheckCircle2 {:className "text-green-500"}]
           "Completed Recently"]
          [:div {:className "space-y-3 text-text-secondary line-through"}
           [:div "React & Tailwind CSS Mastery"]
           [:div "NixOS System Configuration"]
           [:div "Basic 3D Modeling"]]]]])

(defn todo [{:keys [layoutId]}]
  #jsx [motion.div {:layoutId layoutId
                    :className "bento-card h-full flex flex-col justify-between p-5 bg-black text-white relative group"}
        [:div {:className "absolute top-0 right-0 p-4 opacity-0 group-hover:opacity-100 transition-opacity duration-300"}
         #jsx [ListTodo {:size 16 :className "text-gray-400"}]]
        
        [:h3 {:className "text-lg font-bold text-gray-400"} "Learning"]
        [:div {:className "space-y-2"}
         [:div {:className "flex items-center gap-2"}
          [:div {:className "w-1.5 h-1.5 rounded-full bg-orange-500"}]
          [:span {:className "font-medium"} "Rust"]]
         [:div {:className "flex items-center gap-2"}
          [:div {:className "w-1.5 h-1.5 rounded-full bg-green-500"}]
          [:span {:className "font-medium"} "Clojure"]]
         [:div {:className "flex items-center gap-2"}
          [:div {:className "w-1.5 h-1.5 rounded-full bg-blue-500"}]
          [:span {:className "font-medium"} "Embedded"]]]])
