(ns main
  (:require ["react-dom/client" :as rdom]
            ["./index.css"]
            [app :as app]))

(let [root (rdom/createRoot (.getElementById js/document "root"))]
  (.render root #jsx [app/app]))
