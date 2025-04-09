(ns nesicdea.fs
  (:require [clojure.string :as str]))

(def rn-fs (js/require "react-native-fs"))

;; 앱 문서 디렉토리 경로 가져오기
(defn get-document-directory []
  (.-DocumentDirectoryPath rn-fs))

;; 사진 저장 디렉토리 경로 가져오기
(defn get-photos-directory []
  (let [doc-dir (get-document-directory)
        _ (js/console.log "사진 저장 디렉토리 경로: " doc-dir)
        photos-dir (str doc-dir "/photos")]
    photos-dir))

;; 사진 저장 디렉토리 생성
(defn ensure-photos-directory []
  (let [_ (js/console.log "사진 저장 디렉토리 생성")
        photos-dir (get-photos-directory)
        _ (js/console.log "사진 저장 디렉토리 경로: " photos-dir)
        _ (js/console.log "rn-fs: " rn-fs)
        _ (js/console.log "exists 메서드: " (.-exists rn-fs))
        is_exist (try 
                   (-> (.exists rn-fs photos-dir)
                       (.then #(do 
                                (js/console.log "디렉토리 존재 여부 확인 결과: " %)
                                %))
                       (.catch #(do
                                 (js/console.error "exists 오류: " %)
                                 false)))
                   (catch :default e
                     (js/console.error "exists 호출 오류: " e)
                     false))
        _ (js/console.log "사진 저장 디렉토리 존재 여부: " is_exist)]
    (when-not is_exist
      (try
        (.mkdir rn-fs photos-dir #js {:recursive true})
        (catch :default e
          (js/console.error "mkdir 오류: " e))))))

;; 파일 이름 생성 (날짜와 시간 포함)
(defn generate-photo-filename [meal-type person]
  (let [now (js/Date.)
        year (.getFullYear now)
        month (inc (.getMonth now))
        day (.getDate now)
        hours (.getHours now)
        minutes (.getMinutes now)
        seconds (.getSeconds now)
        timestamp (str year "-" 
                      (when (< month 10) "0") month "-"
                      (when (< day 10) "0") day "_"
                      (when (< hours 10) "0") hours "-"
                      (when (< minutes 10) "0") minutes "-"
                      (when (< seconds 10) "0") seconds)]
    (str timestamp "_" (name meal-type) "_" person ".jpg")))

;; 사진 파일 복사
(defn copy-photo-to-storage [photo-path meal-type person]
  (let [photos-dir (get-photos-directory)
        _ (js/console.log "사진 저장 디렉토리 경로: " photos-dir)
        filename (generate-photo-filename meal-type person) 
        _ (js/console.log "사진 파일 이름: " filename)
        target-path (str photos-dir "/" filename)
        _ (js/console.log "사진 파일 저장 경로: " target-path)]
        
    (ensure-photos-directory)
    (js/console.log "사진 파일 복사 시작")
    (try
      (-> (.copyFile rn-fs photo-path target-path)
          (.then #(js/console.log "사진이 저장되었습니다: " target-path))
          (.catch #(js/console.error "사진 저장 중 오류 발생: " %)))
      (catch :default e
        (js/console.error "copyFile 오류: " e)))
    target-path))

;; 저장된 사진 목록 가져오기
(defn get-saved-photos []
  (let [photos-dir (get-photos-directory)]
    (ensure-photos-directory)
    (try
      (-> (.readDir rn-fs photos-dir)
          (.then (fn [result]
                   (js->clj result :keywordize-keys true)))
          (.catch #(js/console.error "사진 목록 가져오기 오류: " %)))
      (catch :default e
        (js/console.error "readDir 오류: " e)
        [])))) 