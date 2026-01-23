(ns components.info
  (:require ["lucide-react" :refer [MapPin Mail User]]
            ["framer-motion" :refer [motion]]))

(def memoji (js* "new URL('../assets/Image.png', import.meta.url).href"))

(defn info-expanded []
  #jsx [:div {:className "p-8 md:p-12 flex flex-col items-center text-center"}
        [:div {:className "relative mb-8"}
         [:div {:className "absolute inset-0 bg-blue-200 rounded-full blur-3xl opacity-50"}]
         [:img {:src memoji :className "relative w-48 h-48 md:w-64 md:h-64 object-cover drop-shadow-2xl"}]]
        
        [:h1 {:className "text-4xl md:text-6xl font-bold text-text-primary mb-4 tracking-tight"} "Pengcheng Wang"]
        [:p {:className "text-2xl text-accent-blue font-medium mb-8"} "Information Security Student @ UofT"]
        
        [:div {:className "max-w-2xl space-y-6 text-lg text-text-secondary leading-relaxed"}
         [:p "I am a passionate developer and security researcher based in Toronto. My journey in tech is driven by a curiosity for how systems work at a fundamental level, from the kernel to the web browser."]
         [:p "I specialize in cybersecurity, focusing on software verification and vulnerability analysis. When I'm not auditing code, I'm building full-stack applications or tweaking my Emacs configuration."]
         [:p "I believe in open source, reproducible systems (NixOS ftw!), and the power of functional programming."]]
        
        [:div {:className "flex flex-wrap justify-center gap-6 mt-12"}
         [:div {:className "flex items-center gap-2 text-text-secondary"}
          #jsx [MapPin {:size 20}]
          "Toronto, Canada"]
         [:div {:className "flex items-center gap-2 text-text-secondary"}
          #jsx [Mail {:size 20}]
          "dw@dezzw.com"]]])

(defn info [{:keys [layoutId className]}]
  #jsx [motion.div {:layoutId layoutId
                    ;; Updated classes for glassmorphism effect
                    :className (str "bento-card h-full flex flex-col md:flex-row items-center justify-center md:justify-start p-8 gap-8 relative group bg-white/30 backdrop-blur-md border border-white/20 shadow-xl " className)}
        [:div {:className "absolute top-0 right-0 p-6 opacity-0 group-hover:opacity-100 transition-opacity duration-300"}
         #jsx [User {:size 20 :className "text-text-secondary"}]]
        
        [:div {:className "relative shrink-0"}
         [:div {:className "absolute inset-0 bg-blue-100 rounded-full blur-2xl opacity-50"}]
         [:img {:src memoji :className "relative w-40 h-40 md:w-48 md:h-48 object-cover drop-shadow-xl"}]]
        [:div {:className "text-center md:text-left space-y-4 max-w-lg"}
         [:div
          [:h1 {:className "text-3xl md:text-4xl font-bold text-text-primary tracking-tight"} "Hi, I'm Desmond."]
          [:p {:className "text-lg text-accent-blue font-medium"} "Information Security Student"]]
         [:p {:className "text-text-secondary leading-relaxed text-lg"}
          "I study at the University of Toronto, specializing in cybersecurity and full-stack development. "
          "I'm passionate about building secure, efficient systems and exploring the depths of Emacs."]]])
