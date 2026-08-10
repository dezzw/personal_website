(ns pages.dashboard
  (:require ["react" :as react]
            ["framer-motion" :refer [motion AnimatePresence]]
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
            [components.todo :as todo]))

(defn CardWrapper [{:keys [id className children on-click variants]}]
  #jsx [motion.div {:variants variants
                    :role "button"
                    :tabIndex 0
                    :className (str "cursor-pointer " className)
                    :onClick #(on-click id)
                    :onKeyDown (fn [e]
                                 (when (contains? #{"Enter" " "} (.-key e))
                                   (.preventDefault e)
                                   (on-click id)))
                    :whileHover {:scale 1.02}
                    :transition {:duration 0.2}}
        children])

(defn ExpandedBackdrop [{:keys [on-close]}]
  #jsx [motion.div {:className "fixed inset-0 z-[100] bg-black/40 backdrop-blur-md"
                    :role "presentation"
                    :initial {:opacity 0}
                    :animate {:opacity 1}
                    :transition {:duration 0.2}
                    :onClick on-close}])

(defn ExpandedOverlay [{:keys [id on-close component]}]
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
                    :transition {:duration 0.2}}
        #jsx [motion.div {:role "dialog"
                          :aria-modal "true"
                          :className "relative w-full max-w-5xl h-[85vh] bg-white rounded-[2rem] shadow-2xl overflow-hidden flex flex-col pointer-events-auto"
                          :initial {:opacity 0 :scale 0.96}
                          :animate {:opacity 1 :scale 1}
                          :exit {:opacity 0 :scale 0.96}
                          :transition {:type "spring" :stiffness 400 :damping 32}}
              [:button {:type "button"
                        :aria-label "Close"
                        :className "absolute top-6 right-6 p-2 rounded-full bg-gray-100 hover:bg-gray-200 transition-colors z-10"
                        :onClick on-close}
               #jsx [X {:size 24 :className "text-gray-600"}]]
              [:div {:className "h-full overflow-y-auto"}
               component]]])

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

(def container-variants
  {:hidden {:opacity 0}
   :visible {:opacity 1
             :transition {:staggerChildren 0.1
                          :delayChildren 0.2}}})

(def item-variants
  {:hidden {:opacity 0 :y 20}
   :visible {:opacity 1 :y 0}})

(defn dashboard [{:keys [heroMode]}]
  (let [[selected-id set-selected-id] (react/useState nil)
        SelectedComponent (get expanded-components selected-id)]
    
    (react/useEffect
     (fn []
       (if selected-id
         (set! (.-overflow (.-style js/document.body)) "hidden")
         (set! (.-overflow (.-style js/document.body)) "unset"))
       (fn [] (set! (.-overflow (.-style js/document.body)) "unset")))
     #js [selected-id])

    #jsx [:div {:className "min-h-screen w-full p-4 md:p-8 lg:p-12 flex justify-center bg-bg font-sans"}
          [motion.div {:className "grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 w-full max-w-[1400px]"
                       :variants container-variants
                       :initial "hidden"
                       :animate (if heroMode "hidden" "visible")}
           
           (if heroMode
             #jsx [:div {:className "col-span-1 md:col-span-2 lg:col-span-2 row-span-2"}]
             #jsx [CardWrapper {:id "info" 
                                :className "col-span-1 md:col-span-2 lg:col-span-2 row-span-2 h-full"
                                :on-click set-selected-id}
                   [info/info {:layoutId "info-card"}]])
           
           #jsx [motion.div {:variants item-variants
                             :className "col-span-1 row-span-1"}
                 [icon/icon]]

           [CardWrapper {:id "todo" 
                         :className "col-span-1 row-span-1"
                         :variants item-variants
                         :on-click set-selected-id}
            [todo/todo]]

           [CardWrapper {:id "projects" 
                         :className "col-span-1 md:col-span-2 lg:col-span-2 row-span-2"
                         :variants item-variants
                         :on-click set-selected-id}
            [projects/projects]]

           [CardWrapper {:id "nix" 
                         :className "col-span-1 row-span-1"
                         :variants item-variants
                         :on-click set-selected-id}
            [nix/nix]]

           [CardWrapper {:id "emacs" 
                         :className "col-span-1 row-span-1"
                         :variants item-variants
                         :on-click set-selected-id}
            [emacs/emacs]]

           [CardWrapper {:id "skills" 
                         :className "col-span-1 row-span-2"
                         :variants item-variants
                         :on-click set-selected-id}
            [skills/skills]]

           [CardWrapper {:id "experience" 
                         :className "col-span-1 row-span-2"
                         :variants item-variants
                         :on-click set-selected-id}
            [experience/experience]]

           [CardWrapper {:id "edu" 
                         :className "col-span-1 md:col-span-2 row-span-1"
                         :variants item-variants
                         :on-click set-selected-id}
            [edu/edu]]

           [CardWrapper {:id "contact" 
                         :className "col-span-1 md:col-span-2 row-span-1"
                         :variants item-variants
                         :on-click set-selected-id}
            [contact/contact]]]

          (when selected-id
            #jsx [ExpandedBackdrop {:on-close #(set-selected-id nil)}])

          [AnimatePresence
           (when selected-id
             #jsx [ExpandedOverlay {:key selected-id
                                    :id selected-id
                                    :on-close #(set-selected-id nil)
                                    :component (when SelectedComponent
                                                 (react/createElement SelectedComponent))}])]]))
