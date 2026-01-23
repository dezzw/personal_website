(ns components.experience
  (:require ["lucide-react" :refer [Briefcase Calendar MapPin]]))

(defn ExperienceItem [{:keys [role company date location desc color]}]
  #jsx [:div {:className "relative pl-8 md:pl-10 py-2 group"}
        ;; Timeline line
        [:div {:className "absolute left-[11px] top-3 bottom-0 w-[2px] bg-gray-100 group-last:hidden"}]
        ;; Dot
        [:div {:className (str "absolute left-[4px] top-3 w-4 h-4 rounded-full border-4 border-white shadow-sm " color)}]
        
        [:div {:className "bg-gray-50 rounded-3xl p-6 hover:bg-white hover:shadow-md transition-all duration-300 border border-transparent hover:border-gray-100"}
         [:div {:className "flex flex-col md:flex-row md:items-center justify-between mb-4 gap-2"}
          [:div
           [:h3 {:className "font-bold text-xl text-text-primary"} role]
           [:div {:className "flex items-center gap-2 text-text-secondary font-medium"}
            #jsx [Briefcase {:size 16}]
            company]]
          [:div {:className "flex flex-col md:items-end text-sm text-text-secondary"}
           [:div {:className "flex items-center gap-2"}
            #jsx [Calendar {:size 14}]
            date]
           [:div {:className "flex items-center gap-2"}
            #jsx [MapPin {:size 14}]
            location]]]
         [:p {:className "text-text-secondary leading-relaxed"} desc]]])

(defn experience-expanded []
  #jsx [:div {:className "p-8 md:p-12"}
        [:div {:className "max-w-3xl mb-12"}
         [:h2 {:className "text-4xl md:text-5xl font-bold text-text-primary mb-4"} "Experience"]
         [:p {:className "text-xl text-text-secondary leading-relaxed"}
          "My professional journey in software engineering and security research."]]
        
        [:div {:className "max-w-4xl"}
         [ExperienceItem {:role "Research Assistant"
                          :company "University of Toronto"
                          :date "Sep 2024 - Present"
                          :location "Toronto, ON"
                          :desc "Conducting research on software security and formal verification methods. Developing tools to analyze and detect vulnerabilities in large-scale systems."
                          :color "bg-accent-blue"}]
         
         [ExperienceItem {:role "Full Stack Developer Intern"
                          :company "Tech Corp"
                          :date "May 2023 - Aug 2023"
                          :location "Remote"
                          :desc "Built and maintained web applications using React and ClojureScript. Collaborated with the design team to implement new features and improve user experience. Optimized database queries for better performance."
                          :color "bg-purple-500"}]
         
         [ExperienceItem {:role "Security Analyst Intern"
                          :company "CyberSec Inc."
                          :date "May 2022 - Aug 2022"
                          :location "Toronto, ON"
                          :desc "Performed penetration testing and vulnerability assessments on client networks. Assisted in incident response and forensic analysis. Developed scripts to automate security checks."
                          :color "bg-green-500"}]]])

(defn experience []
  #jsx [:div {:className "bento-card h-full flex flex-col relative group"}
        [:div {:className "absolute top-0 right-0 p-6 opacity-0 group-hover:opacity-100 transition-opacity duration-300"}
         #jsx [Briefcase {:size 20 :className "text-text-secondary"}]]
        
        [:h2 {:className "text-xl font-bold text-text-primary mb-6"} "Experience"]
        [:div {:className "flex flex-col gap-6 overflow-y-auto pr-2"}
         [:div {:className "relative pl-4 border-l-2 border-gray-200"}
          [:div {:className "absolute -left-[5px] top-2 w-2 h-2 rounded-full bg-accent-blue"}]
          [:div {:className "font-bold text-text-primary"} "Research Assistant"]
          [:div {:className "text-xs font-medium text-accent-blue mb-1"} "UofT • 2024 - Present"]
          [:p {:className "text-sm text-text-secondary leading-relaxed line-clamp-2"} 
           "Software security and formal verification research."]]
         
         [:div {:className "relative pl-4 border-l-2 border-gray-200"}
          [:div {:className "absolute -left-[5px] top-2 w-2 h-2 rounded-full bg-gray-300"}]
          [:div {:className "font-bold text-text-primary"} "Full Stack Intern"]
          [:div {:className "text-xs font-medium text-accent-blue mb-1"} "Tech Corp • 2023"]
          [:p {:className "text-sm text-text-secondary leading-relaxed line-clamp-2"} 
           "Built web apps with React and ClojureScript."]]]])
