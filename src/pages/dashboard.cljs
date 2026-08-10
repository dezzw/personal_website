(ns pages.dashboard
  (:require ["react" :as react]
            ["framer-motion" :refer [motion AnimatePresence LayoutGroup]]
            ["lucide-react" :refer [X]]
            [components.info :as info]
            [components.icon :as icon]
            [components.contact :as contact]
            [components.edu :as edu]
            [components.emacs :as emacs]
            [components.nix :as nix]
            [components.skills :as skills]
            [components.experience :as experience]
            [components.projects :as projects]
            [components.todo :as todo]
            [utils.motion :as motion-utils]))

(defn CardWrapper [{:keys [id className children on-click variants reduced-motion]}]
  #jsx [motion.div {:variants variants
                    :role "button"
                    :tabIndex 0
                    :className (str "cursor-pointer " className)
                    :onClick #(on-click id)
                    :onKeyDown (fn [e]
                                 (when (contains? #{"Enter" " "} (.-key e))
                                   (.preventDefault e)
                                   (on-click id)))
                    :whileHover (when-not reduced-motion {:scale 1.02})
                    :transition (motion-utils/fade-transition reduced-motion 0.2)}
        children])

(defn ExpandedBackdrop [{:keys [on-close reduced-motion]}]
  #jsx [motion.div {:className "fixed inset-0 z-[100] bg-black/40 backdrop-blur-md"
                    :role "presentation"
                    :initial {:opacity 0}
                    :animate {:opacity 1}
                    :exit {:opacity 0}
                    :transition (motion-utils/fade-transition reduced-motion 0.2)
                    :onClick on-close}])

(defn ExpandedOverlay [{:keys [id on-close component reduced-motion]}]
  (let [layout-id (motion-utils/card-layout-id id)
        content-delay (motion-utils/content-fade-delay reduced-motion)]
    (react/useEffect
     (fn []
       (let [handle-key (fn [e]
                          (when (= (.-key e) "Escape")
                            (on-close)))]
         (.addEventListener js/document "keydown" handle-key)
         (fn [] (.removeEventListener js/document "keydown" handle-key))))
     #js [on-close])
    #jsx [motion.div {:className "fixed inset-0 z-[101] flex items-center justify-center p-4 md:p-8 pointer-events-none"
                      :initial {:opacity 0}
                      :animate {:opacity 1}
                      :exit {:opacity 0}
                      :transition (motion-utils/fade-transition reduced-motion 0.2)}
          #jsx [motion.div {:role "dialog"
                            :aria-modal "true"
                            :layoutId layout-id
                            :layout (motion-utils/layout-transition reduced-motion)
                            :className "relative w-full max-w-5xl h-[85vh] bg-white rounded-[2rem] shadow-2xl overflow-hidden flex flex-col pointer-events-auto"
                            :style {:originX 0.5 :originY 0.5}}
                [:button {:type "button"
                          :aria-label "Close"
                          :className "absolute top-6 right-6 p-2 rounded-full bg-gray-100 hover:bg-gray-200 transition-colors z-10"
                          :onClick on-close}
                 #jsx [X {:size 24 :className "text-gray-600"}]]
                #jsx [motion.div {:className "h-full overflow-y-auto"
                                  :initial {:opacity (if reduced-motion 1 0)}
                                  :animate {:opacity 1}
                                  :transition (if reduced-motion
                                                {:duration 0}
                                                {:delay content-delay :duration 0.2})}
                      component]]]))

(def expanded-components
  {"info" info/info-expanded
   "projects" projects/projects-expanded
   "skills" skills/skills-expanded
   "experience" experience/experience-expanded
   "edu" edu/edu-expanded
   "contact" contact/contact-expanded
   "nix" nix/nix-expanded
   "emacs" emacs/emacs-expanded
   "todo" todo/todo-expanded})

(defn dashboard [{:keys [heroMode reduced-motion]}]
  (let [[selected-id set-selected-id] (react/useState nil)
        SelectedComponent (get expanded-components selected-id)
        container-v (motion-utils/container-variants reduced-motion)
        item-v (motion-utils/item-variants reduced-motion)]

    (react/useEffect
     (fn []
       (if selected-id
         (set! (.-overflow (.-style js/document.body)) "hidden")
         (set! (.-overflow (.-style js/document.body)) "unset"))
       (fn [] (set! (.-overflow (.-style js/document.body)) "unset")))
     #js [selected-id])

    #jsx [:div {:className "min-h-screen w-full p-4 md:p-8 lg:p-12 flex justify-center bg-bg font-sans"}
          #jsx [LayoutGroup
                #jsx [motion.div {:className "grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 w-full max-w-[1400px]"
                                  :variants container-v
                                  :initial "hidden"
                                  :animate (if heroMode "hidden" "visible")}

                     (if heroMode
                       #jsx [:div {:className "col-span-1 md:col-span-2 lg:col-span-2 row-span-2"}]
                       #jsx [CardWrapper {:id "info"
                                          :className "col-span-1 md:col-span-2 lg:col-span-2 row-span-2 h-full"
                                          :on-click set-selected-id
                                          :reduced-motion reduced-motion}
                             [info/info {:layoutId "info-card"}]])

                     #jsx [motion.div {:variants item-v
                                       :className "col-span-1 row-span-1"}
                           [icon/icon]]

                     [CardWrapper {:id "todo"
                                   :className "col-span-1 row-span-1"
                                   :variants item-v
                                   :on-click set-selected-id
                                   :reduced-motion reduced-motion}
                      [todo/todo {:layoutId "todo-card"}]]

                     [CardWrapper {:id "projects"
                                   :className "col-span-1 md:col-span-2 lg:col-span-2 row-span-2"
                                   :variants item-v
                                   :on-click set-selected-id
                                   :reduced-motion reduced-motion}
                      [projects/projects {:layoutId "projects-card"}]]

                     [CardWrapper {:id "nix"
                                   :className "col-span-1 row-span-1"
                                   :variants item-v
                                   :on-click set-selected-id
                                   :reduced-motion reduced-motion}
                      [nix/nix {:layoutId "nix-card"}]]

                     [CardWrapper {:id "emacs"
                                   :className "col-span-1 row-span-1"
                                   :variants item-v
                                   :on-click set-selected-id
                                   :reduced-motion reduced-motion}
                      [emacs/emacs {:layoutId "emacs-card"}]]

                     [CardWrapper {:id "skills"
                                   :className "col-span-1 row-span-2"
                                   :variants item-v
                                   :on-click set-selected-id
                                   :reduced-motion reduced-motion}
                      [skills/skills {:layoutId "skills-card"}]]

                     [CardWrapper {:id "experience"
                                   :className "col-span-1 row-span-2"
                                   :variants item-v
                                   :on-click set-selected-id
                                   :reduced-motion reduced-motion}
                      [experience/experience {:layoutId "experience-card"}]]

                     [CardWrapper {:id "edu"
                                   :className "col-span-1 md:col-span-2 row-span-1"
                                   :variants item-v
                                   :on-click set-selected-id
                                   :reduced-motion reduced-motion}
                      [edu/edu {:layoutId "edu-card"}]]

                     [CardWrapper {:id "contact"
                                   :className "col-span-1 md:col-span-2 row-span-1"
                                   :variants item-v
                                   :on-click set-selected-id
                                   :reduced-motion reduced-motion}
                      [contact/contact {:layoutId "contact-card"}]]]

                (when selected-id
                  #jsx [ExpandedBackdrop {:on-close #(set-selected-id nil)
                                           :reduced-motion reduced-motion}])

                #jsx [AnimatePresence
                      (when selected-id
                        #jsx [ExpandedOverlay {:key selected-id
                                               :id selected-id
                                               :on-close #(set-selected-id nil)
                                               :reduced-motion reduced-motion
                                               :component (when SelectedComponent
                                                            (react/createElement SelectedComponent
                                                                                 #js {:reducedMotion reduced-motion}))}])]]]))
