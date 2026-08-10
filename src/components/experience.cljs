(ns components.experience
  (:require ["framer-motion" :refer [motion]]
            ["lucide-react" :refer [Briefcase Calendar MapPin]]
            [utils.site-data :as site-data]
            [utils.motion :as motion-utils]))

(defn ExperienceItem [{:keys [role company date location desc color]}]
  #jsx [:div {:className "relative pl-8 md:pl-10 py-2 group"}
        [:div {:className "absolute left-[11px] top-3 bottom-0 w-[2px] bg-gray-100 group-last:hidden"}]
        [:div {:className (str "absolute left-[4px] top-3 w-4 h-4 rounded-full border-4 border-white shadow-sm " color)}]
        [:div {:className "bg-gray-50 rounded-3xl p-6 hover:bg-white hover:shadow-md transition-all duration-300 border border-transparent hover:border-gray-100"}
         [:div {:className "flex flex-col md:flex-row md:items-center justify-between mb-4 gap-2"}
          [:div
           [:h3 {:className "font-bold text-xl text-text-primary"} role]
           [:div {:className "flex items-center gap-2 text-text-secondary font-medium"}
            #jsx [Briefcase {:size 16}]
            company]]
          [:div {:className "flex flex-col md:items-end text-sm text-text-secondary"}
           [:div {:className "flex items-center gap-2"}
            #jsx [Calendar {:size 14}]
            date]
           [:div {:className "flex items-center gap-2"}
            #jsx [MapPin {:size 14}]
            location]]]
         [:p {:className "text-text-secondary leading-relaxed"} desc]]])

(defn experience-expanded [{:keys [reducedMotion]}]
  (let [{:keys [data loading]} (site-data/use-experience)
        container-v (motion-utils/list-container-variants reducedMotion)
        item-v (motion-utils/list-item-variants reducedMotion)]
    #jsx [:div {:className "p-8 md:p-12"}
          [:div {:className "max-w-3xl mb-12"}
           [:h2 {:className "text-4xl md:text-5xl font-bold text-text-primary mb-4"} "Experience"]
           [:p {:className "text-xl text-text-secondary leading-relaxed"}
            "My professional journey in software engineering and security research."]]
          (if loading
            #jsx [:p {:className "text-text-secondary"} "Loading..."]
            #jsx [motion.div {:className "max-w-4xl"
                              :variants container-v
                              :initial "hidden"
                              :animate "visible"}
                  (map (fn [item]
                         #jsx [motion.div {:key (:role item) :variants item-v}
                               [ExperienceItem {:role (:role item)
                                                :company (:company item)
                                                :date (:date item)
                                                :location (:location item)
                                                :desc (:desc item)
                                                :color (:color item)}]])
                       (or data []))])]))

(defn experience [{:keys [layoutId]}]
  (let [{:keys [data loading]} (site-data/use-experience)
        preview (take 2 (or data []))]
    #jsx [motion.div {:layoutId layoutId
                      :className "bento-card h-full flex flex-col relative group"}
          [:div {:className "absolute top-0 right-0 p-6 opacity-0 group-hover:opacity-100 transition-opacity duration-300"}
           #jsx [Briefcase {:size 20 :className "text-text-secondary"}]]
          [:h2 {:className "text-xl font-bold text-text-primary mb-6"} "Experience"]
          (if loading
            #jsx [:p {:className "text-sm text-text-secondary"} "Loading..."]
            #jsx [:div {:className "flex flex-col gap-6 overflow-y-auto pr-2"}
                  (map-indexed (fn [idx item]
                                 #jsx [:div {:key (:role item)
                                             :className "relative pl-4 border-l-2 border-gray-200"}
                                       [:div {:className (str "absolute -left-[5px] top-2 w-2 h-2 rounded-full "
                                                              (if (zero? idx) "bg-accent-blue" "bg-gray-300"))}]
                                       [:div {:className "font-bold text-text-primary"} (:role item)]
                                       [:div {:className "text-xs font-medium text-accent-blue mb-1"} (:short-company item)]
                                       [:p {:className "text-sm text-text-secondary leading-relaxed line-clamp-2"}
                                        (:short-desc item)]])
                               preview)])]))
