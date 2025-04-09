(ns nesicdea.core
  (:require [reagent.core :as r]
            [nesicdea.ui :as ui]
            [re-frame.core :as rf]
            [nesicdea.events]
            [nesicdea.subs]))

;; 이벤트 및 구독 정의는 events.cljs 와 subs.cljs 로 이동됨

(defn ^:export -main [& _]
  ;; Initialize db first (use async dispatch)
  (rf/dispatch [:initialize-db])
  ;; Return the root component element for Krell
  (r/as-element [ui/root-view]))
