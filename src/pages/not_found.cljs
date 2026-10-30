(ns pages.not-found
  (:require ["react" :as react]
            ["react-router-dom" :refer [Link useLocation]]
            ["lucide-react" :refer [ChevronRight]]))

(def memoji-img (js* "new URL('../../assets/Image.png', import.meta.url).href"))

(defn not-found []
  (let [pathname (.-pathname (useLocation))]
    (react/useEffect
     (fn []
       (set! (.-title js/document) "404 | Desmond Wang")
       js/undefined)
     #js [])
    #jsx [:div {:className "min-h-screen bg-bg font-sans px-4 py-8 md:px-8 md:py-12"}
          [:div {:className "max-w-4xl mx-auto grid grid-cols-1 md:grid-cols-2 gap-4 md:gap-5"}
           [:div {:className "bento-card !border !border-gray-100 !shadow-[0_4px_24px_rgba(0,0,0,0.06)] flex flex-col items-center text-center px-6 py-10 md:py-12 md:row-span-2"}
            [:p {:className "text-6xl md:text-7xl font-bold text-text-primary mb-6"} "404"]
            [:img {:src memoji-img
                   :alt ""
                   :className "w-40 h-40 md:w-48 md:h-48 object-contain mb-6"}]
            [:h1 {:className "text-2xl md:text-3xl font-bold text-text-primary mb-3"}
             "This page doesn't exist."]
            [:p {:className "hidden md:block text-text-secondary mb-2 max-w-sm"}
             "The link may be old, or the address has a typo."]
            [:p {:className "md:hidden text-text-secondary mb-2 max-w-sm"}
             "The link may be old, or there's a typo."]
            [:code {:className "mt-4 inline-block bg-gray-100 text-text-secondary text-sm font-mono px-4 py-2 rounded-full"}
             pathname]]

           [:div {:className "hidden md:grid md:col-start-2 grid-cols-2 gap-4"}
            [:Link {:to "/blog"
                    :className "bento-card !border-0 !shadow-none flex flex-col items-center justify-center bg-accent-blue text-white min-h-[140px] hover:!shadow-md transition-shadow"}
             [:div {:className "text-4xl font-bold"} "dw."]
             [:p {:className "text-sm mt-2 opacity-90"} "Read the blog"]]
            [:Link {:to "/"
                    :className "bento-card !border-0 !shadow-none flex flex-col items-center justify-center bg-black text-white min-h-[140px] hover:!shadow-md transition-shadow"}
             [:p {:className "text-xl font-bold"} "Back home"]
             [:p {:className "text-sm mt-2 text-gray-400"} "dezzw.com"]]]

           [:div {:className "hidden md:flex md:col-start-2 bento-card !bg-gray-50 !border !border-gray-200 !shadow-none flex-col justify-between min-h-[120px] p-6"}
            [:div {:className "space-y-3"}
             [:div {:className "h-3 bg-gray-200 rounded-full w-full"}]
             [:div {:className "h-3 bg-gray-200 rounded-full w-4/5"}]
             [:div {:className "h-3 bg-gray-200 rounded-full w-3/5"}]]
            [:p {:className "text-sm text-text-secondary mt-6"} "Nothing here yet."]]

           [:Link {:to "/blog"
                   :className "md:hidden bento-card !border-0 !shadow-none flex flex-col items-center justify-center bg-accent-blue text-white py-10 hover:!shadow-md transition-shadow"}
            [:div {:className "text-5xl font-bold"} "dw."]
            [:p {:className "text-sm mt-2 opacity-90"} "Read the blog"]]

           [:Link {:to "/"
                   :className "md:hidden bento-card !border-0 !shadow-none flex items-center justify-between bg-black text-white px-6 py-8 hover:!shadow-md transition-shadow"}
            [:span {:className "text-xl font-bold"} "Back home"]
            #jsx [ChevronRight {:size 24 :className "text-white"}]]]]))
