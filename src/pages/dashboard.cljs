(ns pages.dashboard
  (:require ["react" :as react]
            ["react-dom" :as react-dom]
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

(defn Portal [{:keys [children]}]
  (react-dom/createPortal children js/document.body))

(defn- event-from-interactive? [e]
  (when-let [t (.-target e)]
    (.closest t "a, button")))

(defn CardWrapper [{:keys [id className children on-click on-save-viewport variants reduced-motion disable-hover]}]
  #jsx [motion.div {:variants variants
                    :role "button"
                    :tabIndex 0
                    :data-card-id id
                    :className (str "cursor-pointer " className)
                    :style #js {:outline "none" :outlineOffset "0"}
                    :onPointerDown (fn [e]
                                     (when (and (zero? (.-button e))
                                                (not (event-from-interactive? e)))
                                       (.preventDefault e)
                                       (on-save-viewport id)
                                       (on-click id)))
                    :onKeyDown (fn [e]
                                 (when (and (contains? #{"Enter" " "} (.-key e))
                                            (not (event-from-interactive? e)))
                                   (.preventDefault e)
                                   (on-save-viewport id)
                                   (on-click id)))
                    :whileHover (when (and (not reduced-motion) (not disable-hover)) {:scale 1.02})
                    :transition (motion-utils/fade-transition reduced-motion 0.2)}
        children])

(defn ExpandedBackdrop [{:keys [on-close reduced-motion]}]
  #jsx [motion.div {:className "fixed inset-0 z-[100] bg-black/40 backdrop-blur-md"
                    :role "presentation"
                    :initial {:opacity 0}
                    :animate {:opacity 1
                              :transition (motion-utils/fade-transition reduced-motion 0.2)}
                    :exit #js {:opacity 0
                               :transition (motion-utils/close-fade-transition reduced-motion 0.08)}
                    :onPointerDown (fn [e]
                                     (.preventDefault e)
                                     (.stopPropagation e)
                                     (on-close "backdrop"))}])

(defn- dialog-motion-props [skip-layout-morph layout-id reduced-motion]
  (let [props #js {:role "dialog"
                   :aria-modal "true"
                   :className "relative w-full max-w-5xl h-[85vh] bg-white rounded-[2rem] shadow-2xl overflow-hidden flex flex-col pointer-events-auto"
                   :style #js {:originX 0.5 :originY 0.5}
                   :exit #js {:opacity 0
                               :transition (motion-utils/close-fade-transition reduced-motion 0.08)}}]
    (when-not skip-layout-morph
      (set! (.-layoutId props) layout-id)
      (set! (.-layout props) (motion-utils/layout-transition reduced-motion)))
    props))

(defn ExpandedOverlay [{:keys [id on-close component reduced-motion]}]
  (let [layout-id (motion-utils/card-layout-id id)
        content-delay (motion-utils/content-fade-delay reduced-motion)
        dialog-props (dialog-motion-props false layout-id reduced-motion)]
    (react/useEffect
     (fn []
       (let [handle-key (fn [e]
                          (when (= (.-key e) "Escape")
                            (.preventDefault e)
                            (.stopPropagation e)
                            (on-close "esc")))]
         (.addEventListener js/document "keydown" handle-key true)
         (fn [] (.removeEventListener js/document "keydown" handle-key true))))
     #js [on-close])
    #jsx [motion.div {:className "fixed inset-0 z-[101] flex items-center justify-center p-4 md:p-8 pointer-events-none"
                      :initial {:opacity 0}
                      :animate {:opacity 1
                                :transition (motion-utils/fade-transition reduced-motion 0.2)}
                      :exit #js {:opacity 0
                                 :transition (motion-utils/close-fade-transition reduced-motion 0.08)}}
          (react/createElement
           motion/div
           dialog-props
           (react/createElement
            "button"
            #js {:type "button"
                 :aria-label "Close"
                 :className "absolute top-6 right-6 z-10 p-2 max-md:rounded-none md:rounded-full max-md:bg-transparent max-md:[-webkit-tap-highlight-color:transparent] max-md:hover:bg-transparent max-md:active:bg-transparent max-md:before:content-[''] max-md:before:absolute max-md:before:-inset-[2px] max-md:before:z-10 max-md:after:content-[''] max-md:after:absolute max-md:after:inset-0 max-md:after:rounded-full max-md:after:bg-gray-100 max-md:after:pointer-events-none max-md:active:after:bg-gray-200 md:bg-gray-100 md:hover:bg-gray-200 transition-colors"
                 :onClick #(on-close "button")}
            (react/createElement X #js {:size 24 :className "text-gray-600 max-md:relative max-md:z-[1]"}))
           (react/createElement
            motion/div
            #js {:className "h-full overflow-y-auto"
                 :initial #js {:opacity (if reduced-motion 1 0)}
                 :animate #js {:opacity 1}
                 :transition (if reduced-motion
                                #js {:duration 0}
                                #js {:delay content-delay :duration 0.2})}
            component))]))

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

(defn ExpandedLayer [{:keys [selected-id skip-layout-exit on-exit-complete on-close reduced-motion]}]
  (when (or selected-id skip-layout-exit)
    (let [Selected (get expanded-components selected-id)]
      (Portal
       {:children
        (react/createElement
         react/Fragment
         nil
         (react/createElement
          AnimatePresence
          nil
          (when selected-id
            (react/createElement
             ExpandedBackdrop
             #js {:key "backdrop"
                  :on-close on-close
                  :reduced-motion reduced-motion})))
         (react/createElement
          AnimatePresence
          #js {:onExitComplete on-exit-complete}
          (when selected-id
            (react/createElement
             ExpandedOverlay
             #js {:key selected-id
                  :id selected-id
                  :on-close on-close
                  :reduced-motion reduced-motion
                  :component (when Selected
                               (react/createElement Selected
                                                    #js {:reducedMotion reduced-motion}))}))))}))))

(defn- restore-scroll-top! [scroll-container-ref saved-scroll-top]
  (when-let [scroll-el (.-current scroll-container-ref)]
    (set! (.-scrollTop scroll-el) (.-current saved-scroll-top))))

(defn- restore-scroll-top-after-layout! [scroll-container-ref saved-scroll-top on-done]
  (let [restore! #(restore-scroll-top! scroll-container-ref saved-scroll-top)]
    (js/requestAnimationFrame
     #(js/requestAnimationFrame
       (fn []
         (restore!)
         (when on-done (on-done)))))))

(defn- blur-active-element! []
  (when-let [active (.-activeElement js/document)]
    (when (.-blur active)
      (.blur active))))

(defn- restore-focus-after-close! [closing-card-id close-via]
  (let [via (.-current close-via)
        id (.-current closing-card-id)]
    (cond
      (= via "esc")
      (when id
        (when-let [el (.querySelector js/document (str "[data-card-id=\"" id "\"]"))]
          (.focus el #js {:preventScroll true})))
      (#{"backdrop" "button"} via)
      (blur-active-element!))))

(defn dashboard [{:keys [heroMode reduced-motion scroll-container-ref]}]
  (let [[selected-id set-selected-id] (react/useState nil)
        [skip-layout-exit set-skip-layout-exit] (react/useState false)
        saved-scroll-top (react/useRef 0)
        closing-card-id (react/useRef nil)
        close-via (react/useRef nil)
        container-v (motion-utils/container-variants reduced-motion)
        item-v (motion-utils/item-variants reduced-motion)
        card-hover-disabled? (or selected-id skip-layout-exit)
        save-scroll-top! (fn [_id]
                           (when-let [el (.-current scroll-container-ref)]
                             (set! (.-current saved-scroll-top) (.-scrollTop el))))
        unlock-scroll! (fn []
                         (set! (.-overflow (.-style js/document.body)) "")
                         (when-let [el (.-current scroll-container-ref)]
                           (.removeProperty (.-style el) "overflow")))
        exit-complete! (fn []
                         (unlock-scroll!)
                         (restore-scroll-top! scroll-container-ref saved-scroll-top)
                         (restore-scroll-top-after-layout!
                          scroll-container-ref
                          saved-scroll-top
                          (fn []
                            (restore-focus-after-close! closing-card-id close-via)
                            (set-skip-layout-exit false))))
        open-card! (fn [id]
                     (set-skip-layout-exit false)
                     (set-selected-id id))
        close-card! (fn [via]
                      (set! (.-current close-via) via)
                      (set! (.-current closing-card-id) selected-id)
                      (set-skip-layout-exit true)
                      (set-selected-id nil))]

    (react/useEffect
     (fn []
       (if (or selected-id skip-layout-exit)
         (when-let [el (.-current scroll-container-ref)]
           (set! (.-overflow (.-style js/document.body)) "hidden")
           (set! (.-overflow (.-style el)) "hidden")
           (set! (.-scrollTop el) (.-current saved-scroll-top))
           (let [lock-scroll! (fn []
                                (set! (.-scrollTop el) (.-current saved-scroll-top)))]
             (.addEventListener el "scroll" lock-scroll!)
             (fn []
               (.removeEventListener el "scroll" lock-scroll!))))
         js/undefined))
     #js [selected-id skip-layout-exit scroll-container-ref])

    #jsx [:div {:className "min-h-screen w-full p-4 md:p-8 lg:p-12 flex justify-center bg-bg font-sans"}
          #jsx [LayoutGroup
                #jsx [motion.div {:className (str "grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 w-full max-w-[1400px]"
                                                   (when (or selected-id skip-layout-exit)
                                                     " pointer-events-none"))
                                  :variants container-v
                                  :initial "hidden"
                                  :animate (if heroMode "hidden" "visible")}

                     (if heroMode
                       #jsx [:div {:className "col-span-1 md:col-span-2 lg:col-span-2 row-span-2"}]
                       #jsx [CardWrapper {:id "info"
                                          :className "col-span-1 md:col-span-2 lg:col-span-2 row-span-2 h-full"
                                          :on-click open-card!
                                          :on-save-viewport save-scroll-top!
                                          :reduced-motion reduced-motion
                                          :disable-hover card-hover-disabled?}
                             [info/info {:layoutId "info-card"}]])

                     #jsx [motion.div {:variants item-v
                                       :className "col-span-1 row-span-1"}
                           [icon/icon]]

                     [CardWrapper {:id "todo"
                                   :className "col-span-1 row-span-1"
                                   :variants item-v
                                   :on-click open-card!
                                   :on-save-viewport save-scroll-top!
                                   :reduced-motion reduced-motion
                                   :disable-hover card-hover-disabled?}
                      [todo/todo {:layoutId "todo-card"}]]

                     [CardWrapper {:id "projects"
                                   :className "col-span-1 md:col-span-2 lg:col-span-2 row-span-2"
                                   :variants item-v
                                   :on-click open-card!
                                   :on-save-viewport save-scroll-top!
                                   :reduced-motion reduced-motion
                                   :disable-hover card-hover-disabled?}
                      [projects/projects {:layoutId "projects-card"}]]

                     [CardWrapper {:id "nix"
                                   :className "col-span-1 row-span-1"
                                   :variants item-v
                                   :on-click open-card!
                                   :on-save-viewport save-scroll-top!
                                   :reduced-motion reduced-motion
                                   :disable-hover card-hover-disabled?}
                      [nix/nix {:layoutId "nix-card"}]]

                     [CardWrapper {:id "emacs"
                                   :className "col-span-1 row-span-1"
                                   :variants item-v
                                   :on-click open-card!
                                   :on-save-viewport save-scroll-top!
                                   :reduced-motion reduced-motion
                                   :disable-hover card-hover-disabled?}
                      [emacs/emacs {:layoutId "emacs-card"}]]

                     [CardWrapper {:id "skills"
                                   :className "col-span-1 row-span-2"
                                   :variants item-v
                                   :on-click open-card!
                                   :on-save-viewport save-scroll-top!
                                   :reduced-motion reduced-motion
                                   :disable-hover card-hover-disabled?}
                      [skills/skills {:layoutId "skills-card"}]]

                     [CardWrapper {:id "experience"
                                   :className "col-span-1 row-span-2"
                                   :variants item-v
                                   :on-click open-card!
                                   :on-save-viewport save-scroll-top!
                                   :reduced-motion reduced-motion
                                   :disable-hover card-hover-disabled?}
                      [experience/experience {:layoutId "experience-card"}]]

                     [CardWrapper {:id "edu"
                                   :className "col-span-1 md:col-span-2 row-span-1"
                                   :variants item-v
                                   :on-click open-card!
                                   :on-save-viewport save-scroll-top!
                                   :reduced-motion reduced-motion
                                   :disable-hover card-hover-disabled?}
                      [edu/edu {:layoutId "edu-card"}]]

                     [CardWrapper {:id "contact"
                                   :className "col-span-1 md:col-span-2 row-span-1"
                                   :variants item-v
                                   :on-click open-card!
                                   :on-save-viewport save-scroll-top!
                                   :reduced-motion reduced-motion
                                   :disable-hover card-hover-disabled?}
                      [contact/contact {:layoutId "contact-card"}]]]

                [ExpandedLayer {:selected-id selected-id
                                :skip-layout-exit skip-layout-exit
                                :on-exit-complete exit-complete!
                                :on-close close-card!
                                :reduced-motion reduced-motion}]]]))
