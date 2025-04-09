(ns nesicdea.subs
  (:require [re-frame.core :as rf]))

;; 구독 정의
(rf/reg-sub
 :view
 (fn [db _]
   (:view db)))

(rf/reg-sub
 :meal-type
 (fn [db _]
   (:meal-type db)))

(rf/reg-sub
 :photos
 (fn [db _]
   (:photos db)))

(rf/reg-sub
 :current-photo
 (fn [db _]
   (:current-photo db)))

(rf/reg-sub
 :export-dates
 (fn [db _]
   (:export-dates db))) 