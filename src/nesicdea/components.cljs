(ns nesicdea.components
  (:require [react-native :as rn]
            [reagent.core :as r]))
            

(def button (r/adapt-react-class rn/Button))
(def flat-list (r/adapt-react-class rn/FlatList))
(def image (r/adapt-react-class rn/Image))
(def image-background (r/adapt-react-class rn/ImageBackground))
(def keyboard-avoiding-view (r/adapt-react-class rn/KeyboardAvoidingView))
(def modal (r/adapt-react-class rn/Modal))
(def refresh-control (r/adapt-react-class rn/RefreshControl))
(def safe-area-view (r/adapt-react-class rn/SafeAreaView))
(def scroll-view (r/adapt-react-class rn/ScrollView))
(def section-list (r/adapt-react-class rn/SectionList))
(def status-bar (r/adapt-react-class rn/StatusBar))
(def text (r/adapt-react-class rn/Text))
(def text-input (r/adapt-react-class rn/TextInput))
(def touchable-highlight (r/adapt-react-class rn/TouchableHighlight))
(def touchable-without-feedback (r/adapt-react-class rn/TouchableWithoutFeedback))
(def view (r/adapt-react-class rn/View))
(def virtualized-list (r/adapt-react-class rn/VirtualizedList))

(def touchable-opacity (r/adapt-react-class rn/TouchableOpacity))

(def rc (js/require "react-native-calendars"))
(def calendar (r/adapt-react-class (.-Calendar rc))) 

(let [LocaleConfig (.-LocaleConfig rc)]
  (aset LocaleConfig "locales" "ko" (clj->js
                                     {:monthNames
                                      ["01월" "02월" "03월" "04월" "05월" "06월" "07월" "08월" "09월" "10월" "11월" "12월"]
                                      :monthNamesShort
                                      ["1월" "2월" "3월" "4월" "5월" "6월" "7월" "8월" "9월" "10월" "11월" "12월"]
                                      :dayNames
                                      ["일요일" "월요일" "화요일" "수요일" "목요일" "금요일" "토요일"]
                                      :dayNamesShort
                                      ["일" "월" "화" "수" "목" "금" "토"]
                                      :today "오늘"}))
  (aset LocaleConfig "defaultLocale" "ko"))



(def rn-vision-camera (js/require "react-native-vision-camera"))
(def use-camera-permission (.-useCameraPermission ^js/RNVisionCamera rn-vision-camera))
(def use-camera-device (.-useCameraDevice ^js/RNVisionCamera rn-vision-camera))
(def camera (r/adapt-react-class (.-Camera ^js/RNVisionCamera rn-vision-camera)))
  