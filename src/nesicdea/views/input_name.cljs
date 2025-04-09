(ns nesicdea.views.input-name
   (:require [reagent.core :as r]
             [nesicdea.styles :as s]
             [nesicdea.components :as c]
             [re-frame.core :as rf]))

 (defn input-name-view []
   (let [person-name (r/atom "")
         show-picker (r/atom false)
         selected-date (r/atom (js/Date.))
         current-photo @(rf/subscribe [:current-photo])
         meal-type @(rf/subscribe [:meal-type])]
     (fn []
       [c/view {:style (merge (:container s/common-styles)
                              {:justify-content "center"
                               :align-items "center"})}
        [c/text {:style {:font-size 20 :color "#333" :margin-bottom 20}}
         "사진 촬영 완료"]
        [c/text-input {:style {:height 50
                               :border-color (:border s/colors)
                               :border-width 1
                               :border-radius 8
                               :padding-horizontal 12
                               :width "80%"
                               :margin-bottom 20
                               :background-color "#fff"}
                       :placeholder "이름 입력"
                       :on-change-text #(reset! person-name %)}]

        [c/view {:style {:width "80%" :margin-bottom 20}}
         [c/touchable-opacity {:on-press #(reset! show-picker true)}
          [c/view {:style {:flex-direction "row"
                           :padding 10
                           :border-color (:border s/colors)
                           :border-width 1
                           :border-radius 8
                           :background-color "#fff"
                           :align-items "center"
                           :justify-content "space-between"}}
           [c/text {:style {:font-size 16 :color "#555"}}
            (str "날짜: " (.toLocaleDateString @selected-date "ko-KR"))] ; Added str
           [c/text {:style {:color (:primary s/colors)}} "변경"]]]]

       ;; Modal-style calendar picker
        (when @show-picker
          [c/modal {:animation-type "slide"
                    :transparent true
                    :visible @show-picker
                    :on-request-close #(reset! show-picker false)}
           [c/view {:style {:flex 1
                            :justify-content "center"
                            :align-items "center"
                            :background-color "rgba(0, 0, 0, 0.5)"}}
            [c/view {:style {:background-color "white"
                             :padding 20
                             :border-radius 8
                             :width "80%"}}
             [c/calendar {:current (.toISOString @selected-date)
                          :monthFormat "yyyy년 MM월"
                          :onDayPress (fn [day]
                                        (let [selected-date-obj (js/Date. (.-timestamp day))]
                                          (reset! selected-date selected-date-obj)
                                          (reset! show-picker false)))
                          :theme {:todayTextColor (:primary s/colors)
                                  :selectedDayBackgroundColor (:primary s/colors)}}]

             [c/touchable-opacity {:style {:align-items "center"
                                           :padding 10
                                           :margin-top 10}
                                   :on-press #(reset! show-picker false)}
              [c/text {:style {:color (:primary s/colors)
                               :font-weight "bold"}}
               "닫기"]]]]])

        [c/touchable-highlight
         {:style (merge (:button s/common-styles)
                        {:background-color (:secondary s/colors)
                         :width "80%"
                         :margin-bottom 10})
          :on-press #(rf/dispatch [:set-view :main])}
         [c/text {:style (:buttonText s/common-styles)} "취소"]]
        [c/touchable-highlight
         {:style (merge (:button s/common-styles)
                        {:background-color (:primary s/colors)
                         :width "80%"
                         :margin-bottom 10})
          :on-press #(do
                       (rf/dispatch [:add-photo {:id (str (random-uuid))
                                                 :photo current-photo
                                                 :meal-type meal-type
                                                 :person @person-name
                                                 :date (.toLocaleDateString @selected-date)}]) ; Corrected closing brace for map
                       (rf/dispatch [:set-view :main]))}
         [c/text {:style (:buttonText s/common-styles)} "저장"]]]))) ; Added one closing parenthesis for the fn
