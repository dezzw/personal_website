(ns components.edu
  (:require ["framer-motion" :refer [motion]]
            ["lucide-react" :refer [GraduationCap BookOpen Award]]))

(def uoft (js* "new URL('../../assets/uoft.png', import.meta.url).href"))

(defn edu-expanded []
  #jsx [:div {:className "p-8 md:p-12"}
        [:div {:className "flex flex-col md:flex-row items-center gap-8 mb-12"}
         [:img {:src uoft :alt "University of Toronto logo" :className "w-32 h-auto object-contain"}]
         [:div {:className "text-center md:text-left"}
          [:h2 {:className "text-4xl md:text-5xl font-bold text-text-primary mb-2"} "University of Toronto"]
          [:p {:className "text-2xl text-accent-blue font-medium"} "Bachelor of Science"]
          [:p {:className "text-xl text-text-secondary"} "2021 - 2026"]]]
        
        [:div {:className "grid grid-cols-1 md:grid-cols-2 gap-8"}
         [:div {:className "bg-gray-50 rounded-3xl p-8"}
          [:div {:className "flex items-center gap-3 mb-6"}
           #jsx [BookOpen {:size 24 :className "text-accent-blue"}]
           [:h3 {:className "text-2xl font-bold text-text-primary"} "Key Coursework"]]
          [:ul {:className "space-y-3 text-text-secondary"}
           [:li "• Operating Systems Design & Implementation"]
           [:li "• Computer Security & Cryptography"]
           [:li "• Software Verification & Testing"]
           [:li "• Distributed Systems"]
           [:li "• Database Systems"]
           [:li "• Algorithm Design & Analysis"]
           [:li "• Artificial Intelligence"]]]
         
         [:div {:className "bg-gray-50 rounded-3xl p-8"}
          [:div {:className "flex items-center gap-3 mb-6"}
           #jsx [Award {:size 24 :className "text-accent-blue"}]
           [:h3 {:className "text-2xl font-bold text-text-primary"} "Achievements"]]
          [:ul {:className "space-y-3 text-text-secondary"}
           [:li "• Dean's List Scholar (2021-2024)"]
           [:li "• CGPA: 3.71 / 4.0"]
           [:li "• Research Assistantship in Formal Methods"]
           [:li "• Hackathon Winner (UofT Hacks 2023)"]]]]])

(defn edu [{:keys [layoutId]}]
  #jsx [motion.div {:layoutId layoutId
                    :className "bento-card h-full flex flex-col md:flex-row items-center p-6 gap-6 relative group"}
        [:div {:className "absolute top-0 right-0 p-6 opacity-0 group-hover:opacity-100 transition-opacity duration-300"}
         #jsx [GraduationCap {:size 20 :className "text-text-secondary"}]]
        
        [:img {:src uoft :alt "University of Toronto logo" :className "w-16 h-auto md:w-20 object-contain"}]
        [:div {:className "flex-1 text-center md:text-left"}
         [:h2 {:className "text-xl font-bold text-text-primary mb-2"} "University of Toronto"]
         [:div {:className "space-y-1 text-sm text-text-secondary"}
          [:p [:span {:className "font-medium text-text-primary"} "Major: "] "Computer Science & InfoSec"]
          [:p [:span {:className "font-medium text-text-primary"} "Minor: "] "Mathematics"]
          [:p {:className "text-accent-blue font-medium mt-2"} "CGPA: 3.71 / 4.0"]]]])
