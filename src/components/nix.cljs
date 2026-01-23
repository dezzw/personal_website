(ns components.nix
  (:require ["lucide-react" :refer [Settings]]))

(def nix-img (js* "new URL('../assets/nix.svg', import.meta.url).href"))

(def config-snippet (str
  "{\n"
  "  description = \"Desmond's NixOS Configuration\";\n"
  "\n"
  "  inputs = {\n"
  "    nixpkgs.url = \"github:nixos/nixpkgs/nixos-unstable\";\n"
  "    home-manager.url = \"github:nix-community/home-manager\";\n"
  "  };\n"
  "\n"
  "  outputs = { self, nixpkgs, ... }: {\n"
  "    nixosConfigurations.desktop = nixpkgs.lib.nixosSystem {\n"
  "      system = \"x86_64-linux\";\n"
  "      modules = [ ./hosts/desktop ];\n"
  "    };\n"
  "  };\n"
  "}"))

(defn nix-expanded []
  #jsx [:div {:className "p-8 md:p-12 flex flex-col items-center text-center"}
        [:img {:src nix-img :className "w-32 h-32 mb-8"}]
        [:h2 {:className "text-3xl font-bold text-text-primary mb-4"} "NixOS Configuration"]
        [:p {:className "text-lg text-text-secondary max-w-2xl leading-relaxed mb-8"}
         "I use NixOS as my daily driver. My configuration is fully reproducible, managed with Home Manager and Flakes. It allows me to synchronize my development environment across multiple machines with ease."]
        [:div {:className "bg-gray-900 text-gray-100 p-6 rounded-2xl text-left w-full max-w-2xl overflow-x-auto font-mono text-sm"}
         [:pre config-snippet]]])

(defn nix []
  #jsx [:div {:className "bento-card h-full flex items-center justify-center bg-[#f0f8ff] hover:bg-[#e6f2ff] transition-colors group relative"}
        [:div {:className "absolute top-0 right-0 p-4 opacity-0 group-hover:opacity-100 transition-opacity duration-300"}
         #jsx [Settings {:size 16 :className "text-blue-400"}]]
        [:img {:src nix-img :className "w-16 h-16 md:w-20 md:h-20 opacity-80 group-hover:opacity-100 group-hover:scale-110 transition-all duration-300"}]])
