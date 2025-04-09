(ns nesicdea.views.export
  (:require [reagent.core :as r]
            [nesicdea.styles :as s]
            [nesicdea.components :as c]
            [re-frame.core :as rf]))

(defn- date-picker-modal [visible? date-atom show-picker-atom on-close]
  [c/modal {:animation-type "slide"
            :transparent true
            :visible visible?
            :on-request-close on-close}
   [c/view {:style {:flex 1
                    :justify-content "center"
                    :align-items "center"
                    :background-color "rgba(0, 0, 0, 0.5)"}}
    [c/view {:style {:background-color "white"
                     :padding 20
                     :border-radius 8
                     :width "80%"}}
     [c/calendar {:current (.toISOString @date-atom)
                  :monthFormat "yyyy년 MM월"
                  :onDayPress (fn [day]
                                (let [selected-date-obj (js/Date. (.-timestamp day))]
                                  (reset! date-atom selected-date-obj)
                                  (reset! show-picker-atom false)))
                  :theme {:todayTextColor (:primary s/colors)
                          :selectedDayBackgroundColor (:primary s/colors)}}]
     [c/touchable-opacity {:style {:align-items "center"
                                    :padding 10
                                    :margin-top 10}
                           :on-press on-close}
      [c/text {:style {:color (:primary s/colors)
                       :font-weight "bold"}}
       "닫기"]]]]]) ; <-- Closing brackets for date-picker-modal seem correct

(defn export-view []
  (let [show-start-picker (r/atom false)
        show-end-picker (r/atom false)
        start-date (r/atom (doto (js/Date.) (.setDate (.getDate (js/Date.)) - 30))) ; Default start date: 30 days ago
        end-date (r/atom (js/Date.))] ; Default end date: today
    (fn [] ; Return render function
      [c/view {:style (merge (:container s/common-styles)
                             {:align-items "center"
                              :justify-content "center"})}
       ; Title
       [c/text {:style {:font-size 20 :color "#333" :margin-bottom 20}}
        "날짜 범위 선택"]

       ; Date Selection Area
       [c/view {:style {:width "80%" :margin-bottom 20}}
        ; Start Date Display and Trigger
        [c/text {:style {:font-size 16 :margin-bottom 10 :align-self "flex-start"}} "시작 날짜:"]
        [c/touchable-opacity {:on-press #(reset! show-start-picker true)}
         [c/view {:style {:flex-direction "row"
                          :padding 10
                          :border-color (:border s/colors)
                          :border-width 1
                          :border-radius 8
                          :background-color "#fff"
                          :align-items "center"
                          :justify-content "space-between"
                          :margin-bottom 15}}
          [c/text {:style {:font-size 16 :color "#555"}}
           (.toLocaleDateString @start-date)]
          [c/text {:style {:color (:primary s/colors)}} "변경"]]] ; <-- Close inner view and touchable opacity for start date

        ; End Date Display and Trigger
        [c/text {:style {:font-size 16 :margin-bottom 10 :align-self "flex-start"}} "종료 날짜:"]
        [c/touchable-opacity {:on-press #(reset! show-end-picker true)}
         [c/view {:style {:flex-direction "row"
                          :padding 10
                          :border-color (:border s/colors)
                          :border-width 1
                          :border-radius 8
                          :background-color "#fff"
                          :align-items "center"
                          :justify-content "space-between"
                          :margin-bottom 15}}
          [c/text {:style {:font-size 16 :color "#555"}}
           (.toLocaleDateString @end-date)]
          [c/text {:style {:color (:primary s/colors)}} "변경"]]]] 

       ; Start Date Picker Modal (Conditionally Rendered)
       (when @show-start-picker
         (date-picker-modal @show-start-picker start-date show-start-picker #(reset! show-start-picker false)))

       ; End Date Picker Modal (Conditionally Rendered)
       (when @show-end-picker
         (date-picker-modal @show-end-picker end-date show-end-picker #(reset! show-end-picker false)))

       ; Export Button
       [c/touchable-highlight
        {:style (merge (:button s/common-styles)
                       {:background-color (:secondary s/colors)
                        :width "80%"
                        :margin-bottom 10})
         :on-press #(do
                      (rf/dispatch [:set-export-dates @start-date @end-date])
                      ;; TODO: Implement CSV generation logic
                      (js/console.log "CSV 생성하기 로직 실행 예정")
                      (rf/dispatch [:generate-csv]))}
        [c/text {:style (:buttonText s/common-styles)} "CSV 생성하기"]]

       ; Cancel Button
       [c/touchable-highlight
        {:style (merge (:button s/common-styles)
                       {:background-color "#b00020" ; Consider using a color from s/colors if available
                        :width "80%"})
         :on-press #(rf/dispatch [:set-view :main])}
        [c/text {:style (:buttonText s/common-styles)} "취소"]]]

      ))) 