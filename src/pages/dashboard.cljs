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
  #jsx [motion.div {:layoutId id
                    :variants variants
                    :className (str "cursor-pointer " className)
                    :onClick #(on-click id)
                    :whileHover {:scale 1.02}
                    :transition {:duration 0.2}}
        children])

(defn ExpandedOverlay [{:keys [id on-close component]}]
  #jsx [motion.div {:className "fixed inset-0 z-50 flex items-center justify-center p-4 md:p-8 bg-black/40 backdrop-blur-md"
                    :initial {:opacity 0}
                    :animate {:opacity 1}
                    :exit {:opacity 0}
                    :onClick on-close}
        #jsx [motion.div {:layoutId id
                          :className "bg-white w-full max-w-5xl h-[85vh] rounded-[2rem] shadow-2xl overflow-hidden flex flex-col relative"
                          :onClick #(.stopPropagation %)}
              [:button {:className "absolute top-6 right-6 p-2 rounded-full bg-gray-100 hover:bg-gray-200 transition-colors z-10"
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
   "todo" todo/todo-expanded
   "icon" icon/icon-expanded})

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
    #jsx [:div {:className "min-h-screen w-full p-4 md:p-8 lg:p-12 flex justify-center bg-bg font-sans"}
          [motion.div {:className "grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 w-full max-w-[1400px]"
                       :variants container-variants
                       :initial "hidden"
                       :animate (if heroMode "hidden" "visible")}
           
           ;; Info/Profile
           ;; We use a placeholder div when in heroMode to maintain grid layout
           (if heroMode
             #jsx [:div {:className "col-span-1 md:col-span-2 lg:col-span-2 row-span-2"}]
             #jsx [CardWrapper {:id "info" 
                                :className "col-span-1 md:col-span-2 lg:col-span-2 row-span-2 h-full"
                                :on-click set-selected-id}
                   [info/info {:layoutId "info-card"}]])

           ;; Icon
           [CardWrapper {:id "icon" 
                         :className "col-span-1 row-span-1"
                         :variants item-variants
                         :on-click set-selected-id}
            [icon/icon]]

           ;; Todo
           [CardWrapper {:id "todo" 
                         :className "col-span-1 row-span-1"
                         :variants item-variants
                         :on-click set-selected-id}
            [todo/todo]]

           ;; Projects
           [CardWrapper {:id "projects" 
                         :className "col-span-1 md:col-span-2 lg:col-span-2 row-span-2"
                         :variants item-variants
                         :on-click set-selected-id}
            [projects/projects]]

           ;; Nix
           [CardWrapper {:id "nix" 
                         :className "col-span-1 row-span-1"
                         :variants item-variants
                         :on-click set-selected-id}
            [nix/nix]]

           ;; Emacs
           [CardWrapper {:id "emacs" 
                         :className "col-span-1 row-span-1"
                         :variants item-variants
                         :on-click set-selected-id}
            [emacs/emacs]]

           ;; Skills
           [CardWrapper {:id "skills" 
                         :className "col-span-1 row-span-2"
                         :variants item-variants
                         :on-click set-selected-id}
            [skills/skills]]

           ;; Experience
           [CardWrapper {:id "experience" 
                         :className "col-span-1 row-span-2"
                         :variants item-variants
                         :on-click set-selected-id}
            [experience/experience]]

           ;; Education
           [CardWrapper {:id "edu" 
                         :className "col-span-1 md:col-span-2 row-span-1"
                         :variants item-variants
                         :on-click set-selected-id}
            [edu/edu]]

           ;; Contact
           [CardWrapper {:id "contact" 
                         :className "col-span-1 md:col-span-2 row-span-1"
                         :variants item-variants
                         :on-click set-selected-id}
            [contact/contact]]]

          [AnimatePresence
           (when selected-id
             #jsx [ExpandedOverlay {:id selected-id 
                                    :on-close #(set-selected-id nil)
                                    :component (when SelectedComponent
                                                 (react/createElement SelectedComponent))}])]]))
