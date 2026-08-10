(ns utils.motion
  (:require ["react" :as react]))

(def stagger-container-variants
  {:hidden {:opacity 0}
   :visible {:opacity 1
             :transition {:staggerChildren 0.1
                          :delayChildren 0.2}}})

(def stagger-item-variants
  {:hidden {:opacity 0 :y 20}
   :visible {:opacity 1 :y 0}})

(def instant-container-variants
  {:hidden {:opacity 1}
   :visible {:opacity 1}})

(def instant-item-variants
  {:hidden {:opacity 1 :y 0}
   :visible {:opacity 1 :y 0}})

(def list-stagger-container-variants
  {:hidden {:opacity 0}
   :visible {:opacity 1
             :transition {:staggerChildren 0.08}}})

(def list-stagger-item-variants
  {:hidden {:opacity 0 :y 12}
   :visible {:opacity 1 :y 0}})

(defn card-layout-id [id]
  (str id "-card"))

(defn container-variants [reduced?]
  (if reduced? instant-container-variants stagger-container-variants))

(defn item-variants [reduced?]
  (if reduced? instant-item-variants stagger-item-variants))

(defn list-container-variants [reduced?]
  (if reduced? instant-container-variants list-stagger-container-variants))

(defn list-item-variants [reduced?]
  (if reduced? instant-item-variants list-stagger-item-variants))

(defn fade-transition [reduced? duration]
  (if reduced? #js {:duration 0} #js {:duration duration}))

(defn spring-transition [reduced? {:keys [stiffness damping mass]}]
  (if reduced?
    #js {:duration 0}
    (if (some? mass)
      #js {:type "spring" :stiffness stiffness :damping damping :mass mass}
      #js {:type "spring" :stiffness stiffness :damping damping})))

(defn layout-transition [reduced?]
  (if reduced? #js {:duration 0} #js {:type "spring" :stiffness 400 :damping 32}))

(defn content-fade-delay [reduced?]
  (if reduced? 0 0.12))

(defn use-reduced-motion []
  (let [[reduced? set-reduced?] (react/useState false)]
    (react/useEffect
     (fn []
       (let [media (js/window.matchMedia "(prefers-reduced-motion: reduce)")]
         (set-reduced? (.-matches media))
         (let [handler (fn [e] (set-reduced? (.-matches e)))]
           (.addEventListener media "change" handler)
           (fn [] (.removeEventListener media "change" handler)))))
     #js [])
    reduced?))
