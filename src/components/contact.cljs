(ns components.contact
  (:require ["lucide-react" :refer [Mail Github Linkedin FileText Send]]))

(defn ContactCard [{:keys [icon title value link color]}]
  #jsx [:a {:href link 
            :target "_blank"
            :rel "noopener noreferrer"
            :className "flex items-center gap-4 p-6 rounded-2xl bg-gray-50 hover:bg-white hover:shadow-md transition-all duration-300 border border-transparent hover:border-gray-100 group w-full"} ;; Added w-full
        [:div {:className (str "w-12 h-12 rounded-full flex items-center justify-center text-white transition-transform group-hover:scale-110 shrink-0 " color)} ;; Added shrink-0
         icon]
        [:div {:className "min-w-0"} ;; Added min-w-0 for text truncation if needed
         [:h3 {:className "font-bold text-text-primary"} title]
         [:p {:className "text-text-secondary text-sm break-all"} value]]]) ;; Added break-all for long emails

(defn contact-expanded []
  #jsx [:div {:className "p-8 md:p-12"}
        [:div {:className "max-w-3xl mb-12"}
         [:h2 {:className "text-4xl md:text-5xl font-bold text-text-primary mb-4"} "Get in Touch"]
         [:p {:className "text-xl text-text-secondary leading-relaxed"}
          "I'm always open to discussing new projects, creative ideas, or opportunities to be part of your visions."]]
        
        [:div {:className "grid grid-cols-1 md:grid-cols-2 gap-6 max-w-4xl mx-auto"}
         [ContactCard {:icon #jsx [Mail {:size 24}]
                       :title "Email"
                       :value "dw@dezzw.com"
                       :link "mailto:dw@dezzw.com"
                       :color "bg-gray-800"}]
         
         [ContactCard {:icon #jsx [Github {:size 24}]
                       :title "GitHub"
                       :value "@dezzw"
                       :link "https://github.com/dezzw"
                       :color "bg-gray-700"}]
         
         [ContactCard {:icon #jsx [Linkedin {:size 24}]
                       :title "LinkedIn"
                       :value "Desmond Wang"
                       :link "https://linkedin.com/in/desmond-wang"
                       :color "bg-[#0077b5]"}]
         
         [ContactCard {:icon #jsx [FileText {:size 24}]
                       :title "Resume"
                       :value "View PDF"
                       :link "/resume.pdf"
                       :color "bg-accent-blue"}]]])

(defn contact []
  #jsx [:div {:className "bento-card h-full flex flex-col justify-center items-center p-6 relative group"}
        [:div {:className "absolute top-0 right-0 p-6 opacity-0 group-hover:opacity-100 transition-opacity duration-300"}
         #jsx [Send {:size 20 :className "text-text-secondary"}]]
        
        [:h2 {:className "text-2xl font-bold text-text-primary mb-2"} "Get in Touch"]
        [:p {:className "text-text-secondary mb-6 text-center max-w-md"} 
         "Feel free to reach out for collaborations or just a friendly hello."]
        
        [:div {:className "flex flex-wrap justify-center gap-4"}
         [:a {:href "mailto:dw@dezzw.com" 
              :className "px-6 py-3 rounded-full bg-text-primary text-white font-medium hover:bg-gray-800 transition-colors shadow-sm min-w-[120px] text-center" ;; Added min-w and text-center
              :onClick #(.stopPropagation %)} 
          "Email"]
         [:a {:href "https://github.com/dezzw" 
              :className "px-6 py-3 rounded-full bg-gray-100 text-text-primary font-medium hover:bg-gray-200 transition-colors shadow-sm min-w-[120px] text-center"
              :onClick #(.stopPropagation %)} 
          "GitHub"]
         [:a {:href "https://linkedin.com/in/desmond-wang" 
              :className "px-6 py-3 rounded-full bg-[#0077b5] text-white font-medium hover:bg-[#006396] transition-colors shadow-sm min-w-[120px] text-center"
              :onClick #(.stopPropagation %)} 
          "LinkedIn"]
         [:a {:href "/resume.pdf" 
              :target "_blank"
              :rel "noopener noreferrer"
              :className "px-6 py-3 rounded-full bg-accent-blue text-white font-medium hover:bg-blue-600 transition-colors shadow-sm flex items-center justify-center gap-2 min-w-[120px]"
              :onClick #(.stopPropagation %)} 
          "Resume"
          [:span {:className "text-lg"} "→"]]]])
