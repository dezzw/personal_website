(ns components.emacs
  (:require ["lucide-react" :refer [Command]]))

(def emacs-img (js* "new URL('../../assets/emacs.png', import.meta.url).href"))

(defn emacs-expanded []
  #jsx [:div {:className "p-8 md:p-12 flex flex-col items-center text-center"}
        [:img {:src emacs-img :alt "GNU Emacs logo" :className "w-32 h-32 mb-8"}]
        [:h2 {:className "text-3xl font-bold text-text-primary mb-4"} "Emacs Workflow"]
        [:p {:className "text-lg text-text-secondary max-w-2xl leading-relaxed mb-8"}
         "Emacs is more than just an editor; it's my operating system. I use Evil mode for modal editing, Org mode for task management and note-taking, and Magit for Git operations."]
        [:div {:className "grid grid-cols-1 md:grid-cols-3 gap-4 w-full max-w-3xl text-left"}
         [:div {:className "bg-gray-50 p-4 rounded-xl"}
          [:h3 {:className "font-bold text-purple-600 mb-2"} "Evil Mode"]
          [:p {:className "text-sm text-text-secondary"} "Vim keybindings for efficiency."]]
         [:div {:className "bg-gray-50 p-4 rounded-xl"}
          [:h3 {:className "font-bold text-purple-600 mb-2"} "Org Mode"]
          [:p {:className "text-sm text-text-secondary"} "Literate programming & GTD."]]
         [:div {:className "bg-gray-50 p-4 rounded-xl"}
          [:h3 {:className "font-bold text-purple-600 mb-2"} "Magit"]
          [:p {:className "text-sm text-text-secondary"} "The best Git client ever made."]]]])

(defn emacs []
  #jsx [:div {:className "bento-card h-full flex items-center justify-center bg-gradient-to-br from-[#e6e4ff] to-[#f3e6ff] hover:from-[#dcd9ff] hover:to-[#ebd9ff] transition-all group relative"}
        [:div {:className "absolute top-0 right-0 p-4 opacity-0 group-hover:opacity-100 transition-opacity duration-300"}
         #jsx [Command {:size 16 :className "text-purple-400"}]]
        [:img {:src emacs-img :alt "GNU Emacs logo" :className "w-16 h-16 md:w-20 md:h-20 opacity-90 group-hover:opacity-100 group-hover:rotate-12 transition-all duration-300"}]])
