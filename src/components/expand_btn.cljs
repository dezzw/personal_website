(ns components.expand-btn)

(defn expand-btn [{:keys [expanded onToggle]}]
  #jsx [:div {:className "absolute bottom-4 left-4 box-content h-4 p-[2px] w-4 lg:h-8 lg:w-8 rounded-full border-1 lg:border-2 border-solid border-[rgba(0,0,0,0.05)] bg-white shadow transition-all duration-200 ease-in hover:shadow-btn"}
        [:button {:type "button"
                  :className "flex h-full w-full items-center justify-center text-[10px] lg:text-xs"
                  :onClick onToggle}
         (if expanded "Shrink" "Expand")]])
