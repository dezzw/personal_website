(ns components.projects
  (:require ["framer-motion" :refer [motion]]
            ["lucide-react" :refer [Github ExternalLink FolderGit2 CheckCircle2 Clock AlertCircle]]
            [utils.site-data :as site-data]
            [utils.motion :as motion-utils]))

(defn status-icon [status]
  (case status
    "in-progress" #jsx [Clock {:size 12 :className "text-yellow-500"}]
    "finished" #jsx [CheckCircle2 {:size 12 :className "text-green-500"}]
    "archived" #jsx [AlertCircle {:size 12 :className "text-gray-400"}]
    nil))

(defn status-label [status]
  (case status
    "in-progress" "In Progress"
    "finished" "Finished"
    "archived" "Archived"
    ""))

(defn status-color [status]
  (case status
    "in-progress" "text-yellow-600"
    "finished" "text-green-600"
    "archived" "text-gray-500"
    "text-text-secondary"))

(defn ProjectCard [{:keys [title desc tags color link github status]}]
  #jsx [:div {:className "group p-6 rounded-3xl bg-gray-50 border border-gray-100 hover:border-gray-200 hover:shadow-lg transition-all duration-300 flex flex-col h-full"}
        [:div {:className "flex items-center justify-between mb-4"}
         [:div {:className "flex items-center gap-3"}
          [:div {:className (str "w-10 h-10 rounded-full flex items-center justify-center " color)}
           #jsx [FolderGit2 {:size 20 :className "text-white"}]]
          [:div
           [:h3 {:className "font-bold text-xl text-text-primary"} title]
           (when status
             [:div {:className "flex items-center gap-1 mt-1"}
              (status-icon status)
              [:span {:className (str "text-xs font-medium " (status-color status))}
               (status-label status)]])]]
         [:div {:className "flex gap-2"}
          (when github
            #jsx [:a {:href github :target "_blank" :rel "noopener noreferrer"
                      :className "p-2 rounded-full bg-white hover:bg-gray-200 transition-colors text-text-secondary hover:text-text-primary"}
                  #jsx [Github {:size 18}]])
          (when link
            #jsx [:a {:href link :target "_blank" :rel "noopener noreferrer"
                      :className "p-2 rounded-full bg-white hover:bg-gray-200 transition-colors text-text-secondary hover:text-text-primary"}
                  #jsx [ExternalLink {:size 18}]])]]
        [:p {:className "text-text-secondary leading-relaxed mb-6 flex-grow"} desc]
        [:div {:className "flex flex-wrap gap-2 mt-auto"}
         (map (fn [tag]
                #jsx [:span {:key tag :className "px-3 py-1 rounded-full bg-white border border-gray-200 text-xs font-medium text-text-secondary"}
                      tag])
              tags)]])

(defn dot-color [color]
  (when color
    (cond
      (.includes color "green") "bg-green-500"
      (.includes color "purple") "bg-purple-500"
      (.includes color "blue") "bg-blue-500"
      (.includes color "red") "bg-red-500"
      :else "bg-gray-400")))
(defn projects-expanded [{:keys [reducedMotion]}]
  (let [{:keys [data loading]} (site-data/use-projects)
        container-v (motion-utils/list-container-variants reducedMotion)
        item-v (motion-utils/list-item-variants reducedMotion)]
    #jsx [:div {:className "p-8 md:p-12"}
          [:div {:className "max-w-3xl mb-12"}
           [:h2 {:className "text-4xl md:text-5xl font-bold text-text-primary mb-4"} "Projects"]
           [:p {:className "text-xl text-text-secondary leading-relaxed"}
            "A collection of my work in software development, security research, and system configuration."]]
          (if loading
            #jsx [:p {:className "text-text-secondary"} "Loading..."]
            #jsx [motion.div {:className "grid grid-cols-1 md:grid-cols-2 gap-6"
                              :variants container-v
                              :initial "hidden"
                              :animate "visible"}
                  (map (fn [project]
                         #jsx [motion.div {:key (:title project) :variants item-v}
                               #jsx [ProjectCard {:title (:title project)
                                                  :desc (:desc project)
                                                  :tags (:tags project)
                                                  :color (:color project)
                                                  :status (:status project)
                                                  :github (:github project)
                                                  :link (:link project)}]])
                       (or data []))])]))

(defn projects [{:keys [layoutId]}]
  (let [{:keys [data loading]} (site-data/use-projects)]
    #jsx [motion.div {:layoutId layoutId
                      :className "bento-card h-full flex flex-col overflow-hidden relative group"}
          [:div {:className "absolute top-0 right-0 p-6 opacity-0 group-hover:opacity-100 transition-opacity duration-300"}
           #jsx [ExternalLink {:size 20 :className "text-text-secondary"}]]
          [:div {:className "flex items-center justify-between mb-6"}
           [:h2 {:className "text-2xl font-bold text-text-primary"} "Projects"]]
          (if loading
            #jsx [:p {:className "text-sm text-text-secondary"} "Loading..."]
            #jsx [:div {:className "grid grid-cols-1 md:grid-cols-2 gap-4 overflow-y-auto pr-2 pb-2 scrollbar-hide"}
                  (map (fn [project]
                         #jsx [:div {:key (:title project)
                                    :className "p-4 rounded-2xl bg-bg border border-transparent hover:border-gray-200 transition-all duration-200"}
                               [:div {:className "flex items-center gap-2 mb-2"}
                                [:div {:className (str "w-2 h-2 rounded-full " (dot-color (:color project)))}]
                                [:h3 {:className "font-bold text-text-primary"} (:title project)]]
                               [:div {:className "flex items-center gap-1 mb-2"}
                                (status-icon (:status project))
                                [:span {:className (str "text-[10px] font-medium " (status-color (:status project)))}
                                 (status-label (:status project))]]
                               [:p {:className "text-sm text-text-secondary line-clamp-2"} (:short-desc project)]])
                       (or data []))])]))
