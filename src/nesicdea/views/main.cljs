(ns nesicdea.views.main
  (:require [reagent.core :as r]
            [nesicdea.styles :as s]
            [nesicdea.components :as c]
            [re-frame.core :as rf]))

(defn main-view []
  (let [photos @(rf/subscribe [:photos])]
    [c/view {:style (:container s/common-styles)}
     [c/text {:style (:headerText s/common-styles)} "영수증 관리"]
     ;; 점심/저녁 선택 버튼 영역
     [c/view {:style {:flex-direction "row" :justify-content "space-between" :margin-bottom 20}}
      [c/touchable-highlight
       {:style (merge (:button s/common-styles)
                      {:background-color (:primary s/colors)
                       :flex 0.48})
        :on-press #(do
                    (rf/dispatch [:set-meal-type :lunch])
                    (rf/dispatch [:set-view :camera]))}
       [c/text {:style (:buttonText s/common-styles)} "점심"]]
      [c/touchable-highlight
       {:style (merge (:button s/common-styles)
                      {:background-color (:secondary s/colors)
                       :flex 0.48})
        :on-press #(do
                    (rf/dispatch [:set-meal-type :dinner])
                    (rf/dispatch [:set-view :camera]))}
       [c/text {:style (:buttonText s/common-styles)} "저녁"]]]
     ;; 청구서 내보내기 버튼
     [c/touchable-highlight
      {:style (merge (:button s/common-styles)
                     {:background-color "#ff5722"
                      :align-self "center"
                      :margin-bottom 20})
       :on-press #(rf/dispatch [:set-view :export])}
      [c/text {:style (:buttonText s/common-styles)} "청구서 내보내기"]]
     ;; 저장된 영수증 목록
     [c/text {:style {:font-size 18 :margin-bottom 10 :margin-left 4 :color "#555"}}
      "저장된 영수증:"]
     [c/flat-list {:data photos
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
                    :style {:flex 1}}]]))