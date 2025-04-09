(ns nesicdea.ui
  (:require [re-frame.core :as rf]
            [nesicdea.components :as c]
            [nesicdea.views.main :as main]
            [nesicdea.views.camera :as camera]
            [nesicdea.views.input-name :as input-name]
            [nesicdea.views.export :as export]))

(defn root-view []
  (let [current-view @(rf/subscribe [:view])]
    (case current-view
      :main [main/main-view]
      :camera [:f> camera/camera-view] ;; Use :f> for functional component with hooks
      :input-name [input-name/input-name-view]
      :export [export/export-view]
      ;; Default or fallback view
      [c/view [c/text "알 수 없는 뷰입니다."]])))
