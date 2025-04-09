(ns nesicdea.events
  (:require [re-frame.core :as rf]))

;; 이벤트 정의
(rf/reg-event-fx
 :initialize-db
 (fn [_ _]
   {:db {:view :main
         :meal-type nil
         :photos []
         :current-photo nil ; Initialize current-photo as nil
         :export-dates {:start nil :end nil}}}))

(rf/reg-event-fx
 :set-view
 (fn [{:keys [db]} [_ view]]
   {:db (assoc db :view view)}))

(rf/reg-event-fx
 :set-meal-type
 (fn [{:keys [db]} [_ meal-type]]
   {:db (assoc db :meal-type meal-type)}))

(rf/reg-event-fx
 :add-photo
 (fn [{:keys [db]} [_ photo]]
   ;; Ensure photos is always a vector before conjing
   {:db (update db :photos #(conj (or % []) photo))}))

(rf/reg-event-fx
 :set-current-photo
 (fn [{:keys [db]} [_ photo-path]] ; Expecting photo path
   {:db (assoc db :current-photo photo-path)}))

(rf/reg-event-fx
 :set-export-dates
 (fn [{:keys [db]} [_ start end]]
   {:db (assoc db :export-dates {:start start :end end})}))

(rf/reg-event-fx
 :generate-csv ; Placeholder for CSV generation logic
 (fn [{:keys [db]} _]
   (js/console.log "Generating CSV with data:" (pr-str (:photos db)) "and dates:" (pr-str (:export-dates db)))
   ;; TODO: Add actual CSV generation and export/sharing logic
   ;; {:dispatch [:csv-generation-complete]}
   {:db db})) ; Return original db state for now 