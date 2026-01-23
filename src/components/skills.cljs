(ns components.skills
  (:require ["lucide-react" :refer [Code2 Database Terminal Cpu Globe]]
            ["react-icons/fa" :refer [FaJava]]
            ["react-icons/si" :refer [SiHtml5 SiCss3 SiJavascript SiDocker SiPython SiGit SiLinux SiReact SiTypescript SiPostgresql]]))

;; Fallback for Neo4j since it's not in standard icon sets
(defn Neo4jIcon [{:keys [size className]}]
  #jsx [:svg {:xmlns "http://www.w3.org/2000/svg" 
              :viewBox "0 0 24 24" 
              :width size 
              :height size 
              :fill "currentColor" 
              :className className}
        [:path {:d "M1.511 23.336l5.712-2.855a.423.423 0 0 0 .23-.377V8.71a.423.423 0 0 0-.612-.378L1.13 11.187a.423.423 0 0 0-.23.378v11.392a.423.423 0 0 0 .611.379zM8.32 7.664l5.712-2.856a.423.423 0 0 0 .23-.378V.664a.423.423 0 0 0-.612-.378L7.94 3.142a.423.423 0 0 0-.23.378v11.392a.423.423 0 0 0 .611.379zm6.81 15.672l5.712-2.856a.423.423 0 0 0 .23-.378V8.71a.423.423 0 0 0-.612-.378l-5.712 2.856a.423.423 0 0 0-.23.378v11.392a.423.423 0 0 0 .611.378zM22.87 7.664l.23-.116V.664a.423.423 0 0 0-.612-.378l-5.712 2.856a.423.423 0 0 0-.23.378v3.794l5.712-2.856a.423.423 0 0 1 .612.378v2.828z"}]])

(def skills-list 
  [{:icon SiTypescript :color "text-[#3178C6]"}
   {:icon SiJavascript :color "text-[#F7DF1E]"}
   {:icon SiHtml5 :color "text-[#E34F26]"}
   {:icon SiCss3 :color "text-[#1572B6]"}
   {:icon SiReact :color "text-[#61DAFB]"}
   {:icon SiPython :color "text-[#3776AB]"}
   {:icon FaJava :color "text-[#007396]"}
   {:icon SiDocker :color "text-[#2496ED]"}
   {:icon SiGit :color "text-[#F05032]"}
   {:icon SiLinux :color "text-[#FCC624]"}
   {:icon Neo4jIcon :color "text-[#008CC1]"}
   {:icon SiPostgresql :color "text-[#4169E1]"}])

(defn SkillCategory [{:keys [title icon items color]}]
  #jsx [:div {:className "bg-gray-50 rounded-3xl p-6 md:p-8"}
        [:div {:className "flex items-center gap-3 mb-6"}
         [:div {:className (str "w-10 h-10 rounded-full flex items-center justify-center " color)}
          icon]
         [:h3 {:className "font-bold text-xl text-text-primary"} title]]
        [:div {:className "flex flex-wrap gap-3"}
         (map (fn [item]
                #jsx [:span {:key item :className "px-4 py-2 rounded-xl bg-white border border-gray-200 text-sm font-medium text-text-secondary shadow-sm"}
                      item])
              items)]])

(defn skills-expanded []
  #jsx [:div {:className "p-8 md:p-12"}
        [:div {:className "max-w-3xl mb-12"}
         [:h2 {:className "text-4xl md:text-5xl font-bold text-text-primary mb-4"} "Skills"]
         [:p {:className "text-xl text-text-secondary leading-relaxed"}
          "A comprehensive overview of my technical expertise and toolset."]]
        
        [:div {:className "grid grid-cols-1 md:grid-cols-2 gap-6"}
         [SkillCategory {:title "Languages"
                         :icon #jsx [Code2 {:size 20 :className "text-white"}]
                         :items ["Java" "Python" "JavaScript" "TypeScript" "Clojure" "ClojureScript" "Rust" "C/C++" "SQL"]
                         :color "bg-blue-500"}]
         
         [SkillCategory {:title "Frontend & Web"
                         :icon #jsx [Globe {:size 20 :className "text-white"}]
                         :items ["React" "Next.js" "Tailwind CSS" "HTML5" "CSS3" "Vite" "Squint CLJS"]
                         :color "bg-green-500"}]
         
         [SkillCategory {:title "Backend & Database"
                         :icon #jsx [Database {:size 20 :className "text-white"}]
                         :items ["PostgreSQL" "Neo4j" "Node.js" "Express" "FastAPI" "Django" "Redis"]
                         :color "bg-purple-500"}]
         
         [SkillCategory {:title "Tools & DevOps"
                         :icon #jsx [Terminal {:size 20 :className "text-white"}]
                         :items ["Git" "Docker" "Kubernetes" "Linux" "Nix" "Emacs" "Bash" "AWS"]
                         :color "bg-orange-500"}]]])

(defn skills []
  #jsx [:div {:className "bento-card h-full flex flex-col relative group"}
        [:div {:className "absolute top-0 right-0 p-6 opacity-0 group-hover:opacity-100 transition-opacity duration-300"}
         #jsx [Cpu {:size 20 :className "text-text-secondary"}]]
        
        [:h2 {:className "text-xl font-bold text-text-primary mb-6"} "Skills"]
        [:div {:className "grid grid-cols-3 gap-4 overflow-y-auto pr-1 pb-2"}
         (map (fn [{:keys [icon color]}]
                (let [Icon icon]
                  #jsx [:div {:className "aspect-square flex items-center justify-center bg-bg rounded-2xl p-3 hover:bg-gray-100 transition-colors" :key color}
                        [Icon {:size 32 :className color}]]))
              skills-list)]])
