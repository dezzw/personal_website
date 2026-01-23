(ns app
  (:require ["react" :as react]
            ["framer-motion" :refer [motion AnimatePresence]]
            ["lucide-react" :refer [ChevronDown]]
            [pages.dashboard :as dashboard]
            [components.info :as info]))

(def hero-bg (js* "new URL('./assets/hero-bg.png', import.meta.url).href"))

(defn app []
  (let [[heroMode setHeroMode] (react/useState true)
        isAnimating (react/useRef false)
        dashboardRef (react/useRef nil)
        touchStart (react/useRef nil)] ;; To store touch start position
    
    (react/useEffect
     (fn []
       (let [handle-wheel (fn [e]
                            (when-not (.-current isAnimating)
                              (if heroMode
                                (when (> (.-deltaY e) 0) ;; Scroll Down
                                  (set! (.-current isAnimating) true)
                                  (setHeroMode false)
                                  (js/setTimeout #(set! (.-current isAnimating) false) 1500)) ;; Lock for duration of animation
                                (when (and (< (.-deltaY e) 0) ;; Scroll Up
                                           (<= (.-scrollTop (.-current dashboardRef)) 0))
                                  (set! (.-current isAnimating) true)
                                  (setHeroMode true)
                                  (js/setTimeout #(set! (.-current isAnimating) false) 1500)))))
             
             handle-touch-start (fn [e]
                                  (set! (.-current touchStart) (-> e .-touches (aget 0) .-clientY)))
             
             handle-touch-move (fn [e]
                                 (when (and (not (.-current isAnimating))
                                            (.-current touchStart))
                                   (let [touch-y (-> e .-touches (aget 0) .-clientY)
                                         delta-y (- (.-current touchStart) touch-y)]
                                     
                                     (if heroMode
                                       (when (> delta-y 50) ;; Swipe Up (Scroll Down)
                                         (set! (.-current isAnimating) true)
                                         (setHeroMode false)
                                         (set! (.-current touchStart) nil) ;; Reset to prevent multiple triggers
                                         (js/setTimeout #(set! (.-current isAnimating) false) 1500))
                                       
                                       ;; In Dashboard mode
                                       (when (and (< delta-y -50) ;; Swipe Down (Scroll Up)
                                                  (<= (.-scrollTop (.-current dashboardRef)) 0))
                                         (set! (.-current isAnimating) true)
                                         (setHeroMode true)
                                         (set! (.-current touchStart) nil) ;; Reset
                                         (js/setTimeout #(set! (.-current isAnimating) false) 1500))))))]

         (.addEventListener js/window "wheel" handle-wheel)
         (.addEventListener js/window "touchstart" handle-touch-start)
         (.addEventListener js/window "touchmove" handle-touch-move)
         
         (fn [] 
           (.removeEventListener js/window "wheel" handle-wheel)
           (.removeEventListener js/window "touchstart" handle-touch-start)
           (.removeEventListener js/window "touchmove" handle-touch-move))))
     #js [heroMode])

    #jsx [:div {:className "relative h-screen w-full overflow-hidden bg-bg"}
          
          ;; Fixed Background
          [:div {:className "fixed inset-0 z-0"}
           [:img {:src hero-bg 
                  :className "w-full h-full object-cover blur-sm scale-105"
                  :alt "Hero Background"}]
           [:div {:className "absolute inset-0 bg-gradient-to-b from-black/30 via-transparent to-bg"}]]

          ;; Hero Info Card (Fixed)
          [AnimatePresence
           (when heroMode
             #jsx [motion.div {:className "fixed inset-0 z-20 flex flex-col items-center justify-end pb-32 px-4 pointer-events-none"
                               :initial {:opacity 1}
                               :exit {:opacity 0 :transition {:duration 0.5}}}
                   [:div {:className "w-full max-w-3xl pointer-events-auto"}
                    [info/info {:layoutId "info-card" :className "shadow-2xl"}]]
                   
                   ;; Scroll Indicator (Arrow)
                   [motion.div {:className "absolute bottom-12 animate-bounce text-white"
                                :initial {:opacity 1}
                                :exit {:opacity 0}}
                    #jsx [ChevronDown {:size 48 :strokeWidth 1.5}]]])]

          ;; Dashboard Content (Sliding Panel)
          [motion.div {:ref dashboardRef
                       :className "absolute inset-0 z-30 w-full h-full overflow-y-auto bg-bg/95 backdrop-blur-xl rounded-t-[3rem] shadow-[0_-20px_60px_-15px_rgba(0,0,0,0.3)] border-t border-white/20"
                       :initial {:y "100vh"}
                       :animate {:y (if heroMode "100vh" "0")}
                       :transition {:type "spring" :stiffness 30 :damping 15 :mass 1.2}}
           [:div {:className "flex items-center justify-center flex-col pt-24 pb-20 min-h-full"}
            [:div {:className "w-full"}
             [dashboard/dashboard {:heroMode heroMode}]]]]]))
