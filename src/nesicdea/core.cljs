(ns nesicdea.core
  (:require [reagent.core :as r]
            [reagent.react-native :as rn]
            [nesicdea.ui :as ui]))

(defonce state (r/atom {:view :main
                        :meal-type nil
                        :photos []
                        :current-photo 1
                        :export-dates {:start nil :end nil}}))

(defn container-component []
  (let [current-view (:view @state)]
    (case current-view
      :main [ui/main-view state]
      :camera [:f> ui/camera-view state] ; use f> for functional component
      :input-name [ui/input-name-view state]
      :export [ui/export-view state]
      [ui/main-view state])))


(defn ^:export -main [& args]
  (r/as-element [container-component]))
