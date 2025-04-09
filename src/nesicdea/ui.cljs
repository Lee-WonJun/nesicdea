(ns nesicdea.ui
  (:require [reagent.core :as r]
            [nesicdea.styles :as s]
            [nesicdea.components :as c]
            [nesicdea.fs :as fs]))
            

(defn main-view [state]
  [c/view {:style (:container s/common-styles)}
   [c/text {:style (:headerText s/common-styles)} "영수증 관리"]
   ;; 점심/저녁 선택 버튼 영역
   [c/view {:style {:flex-direction "row" :justify-content "space-between" :margin-bottom 20}}
    [c/touchable-highlight
     {:style (merge (:button s/common-styles)
                    {:background-color (:primary s/colors)
                     :flex 0.48})
      :on-press #(swap! state assoc :meal-type :lunch :view :camera)}
     [c/text {:style (:buttonText s/common-styles)} "점심"]]
    [c/touchable-highlight
     {:style (merge (:button s/common-styles)
                    {:background-color (:secondary s/colors)
                     :flex 0.48})
      :on-press #(swap! state assoc :meal-type :dinner :view :camera)}
     [c/text {:style (:buttonText s/common-styles)} "저녁"]]]
   ;; 청구서 내보내기 버튼
   [c/touchable-highlight
    {:style (merge (:button s/common-styles)
                   {:background-color "#ff5722"
                    :align-self "center"
                    :margin-bottom 20})
     :on-press #(swap! state assoc :view :export)}
    [c/text {:style (:buttonText s/common-styles)} "청구서 내보내기"]]
   ;; 저장된 영수증 목록
   [c/text {:style {:font-size 18 :margin-bottom 10 :margin-left 4 :color "#555"}}
    "저장된 영수증:"]
   [c/flat-list {:data (:photos @state)
                  :key-extractor (fn [item] 
                                  (let [item-map (js->clj item :keywordize-keys true)]
                                    (or (:id item-map) 
                                        (str "photo-" (random-uuid)))))
                  :render-item (fn [params]
                                 (let [item (js->clj (.-item params) :keywordize-keys true)]
                                   (r/as-element
                                    [c/view {:style (merge (:card s/common-styles)
                                                          {:flex-direction "row"
                                                           :align-items "center"
                                                           :padding 10})}
                                     ;; 썸네일 이미지
                                     [c/image {:source {:uri (:photo item)}
                                              :style {:width 60
                                                      :height 60
                                                      :border-radius 8
                                                      :margin-right 10}}]
                                     ;; 텍스트 정보
                                     [c/view {:style {:flex 1}}
                                      [c/text {:style {:font-size 16 :color "#333"}}
                                       (str (:date item) " - " (:meal-type item))]
                                      [c/text {:style {:font-size 14 :color "#777"}}
                                       (:person item)]]])))
                  :style {:flex 1}}]])
(defn camera-view [state] 
  (let [device (c/use-camera-device "back")
        _ (js/console.log device)
        permission (c/use-camera-permission)
        _ (js/console.log permission)
        has-permission (.-hasPermission permission)
        _ (when (= has-permission false)
            (.requestPermission permission)) 
        cam-ref (r/atom nil)
        ]
    [c/view {:style {:flex 1}}
     (when device
       [c/camera
        {:ref #(reset! cam-ref %)
         :style {:flex 1}
         :device device
         :photo true
         :isActive true}])
     [c/view {:style {:position "absolute"
                      :bottom 30
                      :left 0
                      :right 0
                      :align-items "center"}}
      [c/touchable-highlight
       {:style (merge (:button s/common-styles)
                      {:background-color (:primary s/colors)
                       :width "80%"})
        :on-press
        (fn []
          (js/console.log "사진 찍기 버튼 클릭됨")
          (js/console.log @cam-ref)
          (when-let [camera @cam-ref]
            (-> (.takeSnapshot camera (clj->js {:flash "off"}))
                (.then (fn [photo]
                         (let [photo-path (str "file://" (.-path photo))
                               _ (js/console.log "사진 경로: " photo-path)
                               photo-path-fs (fs/copy-photo-to-storage photo-path (:meal-type @state) (:person @state))]
                               
                           (swap! state assoc
                                  :current-photo photo-path
                                  :view :input-name)))))))}
       [c/text {:style (:buttonText s/common-styles)} "사진 찍기"]]]]))

(defn input-name-view [state]
  (let [person-name (r/atom "")
        show-picker (r/atom false)
        selected-date (r/atom (js/Date.))]
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
           "날짜: " (.toLocaleDateString @selected-date "ko-KR")]
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
         :on-press #(swap! state assoc :view :main)}
        [c/text {:style (:buttonText s/common-styles)} "취소"]]
       [c/touchable-highlight
        {:style (merge (:button s/common-styles)
                       {:background-color (:primary s/colors)
                        :width "80%"
                        :margin-bottom 10})
         :on-press #(do
                      (swap! state update :photos conj
                             {:id (str (random-uuid))
                              :photo (:current-photo @state)
                              :meal-type (:meal-type @state)
                              :person @person-name
                              :date (.toLocaleDateString (js/Date.))})
                      (swap! state assoc :view :main))}
        [c/text {:style (:buttonText s/common-styles)} "저장"]]])))

(defn export-view [state]
  [c/view {:style (merge (:container s/common-styles)
                         {:align-items "center"
                          :justify-content "center"})}
   [c/text {:style {:font-size 20 :color "#333" :margin-bottom 20}}
    "날짜 범위 선택"]
   (let [show-start-picker (r/atom false)
         show-end-picker (r/atom false)
         start-date (r/atom (doto (js/Date.) (.setDate (.getDate (js/Date.)) - 30)))
         end-date (r/atom (js/Date.))]
     [c/view {:style {:width "80%" :margin-bottom 20}}
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
        [c/text {:style {:color (:primary s/colors)}} "변경"]]]
   
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
        [c/text {:style {:color (:primary s/colors)}} "변경"]]]])
      


   [c/touchable-highlight
    {:style (merge (:button s/common-styles)
                   {:background-color (:secondary s/colors)
                    :width "80%"
                    :margin-bottom 10})
     :on-press #(js/console.log "CSV 생성하기 로직 실행")}
    [c/text {:style (:buttonText s/common-styles)} "CSV 생성하기"]]
   [c/touchable-highlight
    {:style (merge (:button s/common-styles)
                   {:background-color "#b00020"
                    :width "80%"})
     :on-press #(swap! state assoc :view :main)}
    [c/text {:style (:buttonText s/common-styles)} "취소"]]])
