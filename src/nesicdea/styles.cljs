(ns nesicdea.styles)

(def colors
  {:primary "#6200ee"
   :secondary "#03dac6"
   :background "#f2f2f2"
   :border "#dddddd"})

(def common-styles
  {:container {:flex 1
               :background-color (:background colors)
               :padding 16}
   :button {:padding 12
            :border-radius 8
            :align-items "center"
            :justify-content "center"
            :border-width 1
            :border-color (:border colors)}
   :buttonText {:color "#fff"
                :font-size 16
                :font-weight "600"}
   :headerText {:font-size 20
                :font-weight "700"
                :margin-bottom 12}
   :card {:background-color "#fff"
          :padding 12
          :margin-vertical 6
          :border-radius 8
          :shadow-color "#000"
          :shadow-offset {:width 0 :height 1}
          :shadow-opacity 0.2
          :shadow-radius 2
          :elevation 3}})
