(ns components.projects
  (:require ["lucide-react" :refer [Github ExternalLink FolderGit2]]))

(defn ProjectCard [{:keys [title desc tags color link github]}]
  #jsx [:div {:className "group p-6 rounded-3xl bg-gray-50 border border-gray-100 hover:border-gray-200 hover:shadow-lg transition-all duration-300 flex flex-col h-full"}
        [:div {:className "flex items-center justify-between mb-4"}
         [:div {:className "flex items-center gap-3"}
          [:div {:className (str "w-10 h-10 rounded-full flex items-center justify-center " color)}
           #jsx [FolderGit2 {:size 20 :className "text-white"}]]
          [:h3 {:className "font-bold text-xl text-text-primary"} title]]
         [:div {:className "flex gap-2"}
          (when github
            #jsx [:a {:href github :target "_blank" :className "p-2 rounded-full bg-white hover:bg-gray-200 transition-colors text-text-secondary hover:text-text-primary"}
                  #jsx [Github {:size 18}]])
          (when link
            #jsx [:a {:href link :target "_blank" :className "p-2 rounded-full bg-white hover:bg-gray-200 transition-colors text-text-secondary hover:text-text-primary"}
                  #jsx [ExternalLink {:size 18}]])]]
        [:p {:className "text-text-secondary leading-relaxed mb-6 flex-grow"} desc]
        [:div {:className "flex flex-wrap gap-2 mt-auto"}
         (map (fn [tag]
                #jsx [:span {:key tag :className "px-3 py-1 rounded-full bg-white border border-gray-200 text-xs font-medium text-text-secondary"}
                      tag])
              tags)]])

(defn projects-expanded []
  #jsx [:div {:className "p-8 md:p-12"}
        [:div {:className "max-w-3xl mb-12"}
         [:h2 {:className "text-4xl md:text-5xl font-bold text-text-primary mb-4"} "Projects"]
         [:p {:className "text-xl text-text-secondary leading-relaxed"}
          "A collection of my work in software development, security research, and system configuration."]]
        
        [:div {:className "grid grid-cols-1 md:grid-cols-2 gap-6"}
         [ProjectCard {:title "Personal Website"
                       :desc "A high-performance personal portfolio built with Squint CLJS, a lightweight ClojureScript dialect. Features a responsive Bento grid layout, Apple-inspired design system, and smooth Framer Motion animations."
                       :tags ["ClojureScript" "React" "Tailwind" "Vite"]
                       :color "bg-green-500"
                       :github "https://github.com/dezzw/personal_website"}]
         
         [ProjectCard {:title "Emacs Configuration"
                       :desc "A comprehensive, opinionated Emacs setup focused on productivity, modal editing (Evil), and development workflows. Optimized for startup time and extensibility."
                       :tags ["Elisp" "Org Mode" "Evil" "Lisp"]
                       :color "bg-purple-500"
                       :github "https://github.com/dezzw/.emacs.d"}]
         
         [ProjectCard {:title "NixOS Dotfiles"
                       :desc "Reproducible system configuration using NixOS and Home Manager. Manages everything from system packages to user dotfiles, ensuring a consistent environment across machines."
                       :tags ["Nix" "NixOS" "Home Manager" "Shell"]
                       :color "bg-blue-500"
                       :github "https://github.com/dezzw/dotfiles"}]
         
         [ProjectCard {:title "Security Tooling"
                       :desc "A suite of automated vulnerability scanning and analysis tools. Includes scripts for network reconnaissance, static analysis, and fuzzing."
                       :tags ["Python" "Rust" "Security" "Automation"]
                       :color "bg-red-500"
                       :github "https://github.com/dezzw/sec-tools"}]]])

(defn projects []
  #jsx [:div {:className "bento-card h-full flex flex-col overflow-hidden relative group"}
        [:div {:className "absolute top-0 right-0 p-6 opacity-0 group-hover:opacity-100 transition-opacity duration-300"}
         #jsx [ExternalLink {:size 20 :className "text-text-secondary"}]]
        
        [:div {:className "flex items-center justify-between mb-6"}
         [:h2 {:className "text-2xl font-bold text-text-primary"} "Projects"]]
        
        [:div {:className "grid grid-cols-1 md:grid-cols-2 gap-4 overflow-y-auto pr-2 pb-2 scrollbar-hide"}
         [:div {:className "p-4 rounded-2xl bg-bg border border-transparent hover:border-gray-200 transition-all duration-200"}
          [:div {:className "flex items-center gap-2 mb-2"}
           [:div {:className "w-2 h-2 rounded-full bg-green-500"}]
           [:h3 {:className "font-bold text-text-primary"} "Personal Website"]]
          [:p {:className "text-sm text-text-secondary line-clamp-2"} "High-performance portfolio with Squint CLJS."]]

         [:div {:className "p-4 rounded-2xl bg-bg border border-transparent hover:border-gray-200 transition-all duration-200"}
          [:div {:className "flex items-center gap-2 mb-2"}
           [:div {:className "w-2 h-2 rounded-full bg-purple-500"}]
           [:h3 {:className "font-bold text-text-primary"} "Emacs Config"]]
          [:p {:className "text-sm text-text-secondary line-clamp-2"} "Productivity-focused Emacs setup."]]

         [:div {:className "p-4 rounded-2xl bg-bg border border-transparent hover:border-gray-200 transition-all duration-200"}
          [:div {:className "flex items-center gap-2 mb-2"}
           [:div {:className "w-2 h-2 rounded-full bg-blue-500"}]
           [:h3 {:className "font-bold text-text-primary"} "NixOS Dotfiles"]]
          [:p {:className "text-sm text-text-secondary line-clamp-2"} "Reproducible system configuration."]]

         [:div {:className "p-4 rounded-2xl bg-bg border border-transparent hover:border-gray-200 transition-all duration-200"}
          [:div {:className "flex items-center gap-2 mb-2"}
           [:div {:className "w-2 h-2 rounded-full bg-red-500"}]
           [:h3 {:className "font-bold text-text-primary"} "Security Tooling"]]
          [:p {:className "text-sm text-text-secondary line-clamp-2"} "Vulnerability scanning tools."]]]])
