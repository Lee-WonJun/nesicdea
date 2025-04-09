(ns nesicdea.views.camera
  (:require [reagent.core :as r]
            [nesicdea.styles :as s]
            [nesicdea.components :as c]
            [nesicdea.fs :as fs]
            [re-frame.core :as rf]))

(defn camera-view []
  (let [device (c/use-camera-device "back")
        _ (js/console.log device)
        permission (c/use-camera-permission)
        _ (js/console.log permission)
        has-permission (.-hasPermission permission)
        _ (when (= has-permission false)
            (.requestPermission permission))
        cam-ref (r/atom nil)
        meal-type @(rf/subscribe [:meal-type])]
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
                               photo-path-fs (fs/copy-photo-to-storage photo-path meal-type nil)]
                           (rf/dispatch [:set-current-photo photo-path])
                           (rf/dispatch [:set-view :input-name])))))))}
       [c/text {:style (:buttonText s/common-styles)} "사진 찍기"]]]])) 