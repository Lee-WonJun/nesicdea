# 트러블슈팅 기록

## krell tcp socket 버전 문제
```sh
clj -M -m cljs.main --install-deps
```
하면
```js
"react-native-tcp-socket": "^4.2.0"
```
자동으로 이렇게 생기는데, 버전이 너무 낮아서 gradle 과 호환 안되므로,

```js
"react-native-tcp-socket": "^6.2.0"
```
로 수정해줘야 한다.
```sh
clj -M -m cljs.main --install-deps
```
다시 실행하면 복구되므로 주의

이케저케 실행하면 키자마자 여러가지.. 에러 뜨는데 혼자쓸 app 이니까 그냥 무시함.

## krell realworld example 
[OneKeyPass](https://github.com/OneKeePass/mobile) 가뭄의 단비와도 같은 krell 의 realworld example. 많이 참고함..
[krell 영상](https://www.youtube.com/watch?v=GaSHnVlK6HM) 옛날 영상이라 많이 다르긴 함..

## npm modules 사용법

```cljs
(ns nesicdea.components
  (:require [react-native :as rn]
            [reagent.core :as r]))
```
이런식으로 require 해서 사용하지 말고
```cljs
(def rc (js/require "react-native-calendars"))
(def calendar (r/adapt-react-class (.-Calendar rc))) 
```
이런식으로 (js/require "module") 로 로드 하면 된다.

(OneKeyPass 에서는 잘만쓰던데 왜 나는 안되는지 모르겠음.., 혹시 로드한것도 REPL 상에서 nil 이면 다끄고 다 재 빌드)

## reframe 사용법
모르겟다. 난 왜 로드만 해도 안되냐;;
-> 다 끄고 krell build 부터 다 해보니까 된다.


## React (React Native) 관련 처리 할것들 (Regeant 사용시)
### hook
React 관련한 친구 hook 은 react 컴포넌트 안에서만 사용 가능하므로, repl 에서 돌리면 그냥 오류가 뜬다.
-> 이거 repl 에서 나는 오류는 개뿔 아무 도움 안되므로 js 로그 보자

### ref 처리
ref 넘겨주는 Hook 은 
`cam-ref (r/atom nil)`  이런식으로 선언하고, 

```
[c/camera
        {:ref #(reset! cam-ref %)
         :style {:flex 1}
         :device device
         :photo true
         :isActive true}])
```
이런식으로 `reset!` 해주는 fn 넘겨주면 된다.

### [:f>](https://github.com/reagent-project/reagent/blob/master/examples/functional-components-and-hooks/src/example/core.cljs)
hiccup 최상단에 넣어도, hook 은 컴포넌트 어쩌고 하고뭐라 카는데
:f> 로 감싸면 funtional component 로 인식해서 hook 을 쓸수 있다.

아래 처럼 camera-view 는 그냥 hiccup 구조 (걍 HTML) 이고
```cljs
(defn camera-view [state] 
  (let [device (c/use-camera-device "back")
        ;; ....
        ]
    [c/view {:style {:flex 1}}
    ;; ...
    ] )
```

아래처럼 :f> 로 감싸면 functional component 로 인식한다.
```cljs
(defn container-component []
  (let [current-view (:view @state)]
    (case current-view
      :main [ui/main-view state]
      :camera [:f> ui/camera-view state] ; use f> for functional component
      :input-name [ui/input-name-view state]
      :export [ui/export-view state]
      [ui/main-view state])))
```

## vscode 관련
`.vscode` 참고

- `settings.json` : krell repl 관련 설정
- `tasks.json` : krell 빌드 관련 설정

## 기타 사용법
- npm 으로 추가된 모듈이 있으면 build 
--  krel / repl / metro / run-android (npx react-native run-android) 다 끄고 다 다시 시작하는게 좋음

- calva repl 을 키면 cljs connect 될때까지 기다려야 한다. 뭔가 `npx react-native start` 에서 `r` 눌러서 reload 하면 하고 에물레이터가서 뭐라도 해야 connect 된다. 이유는 모름..

- 무슨 문제든 metro 로그는 `illegal operation on a directory, read` 어쩌고만 뜨는데, metro -> open DevTool 키고 js 로그 보자. 

- js/ts 라이브러리랑 맞추기 뒤지게 어렵다..., 특히 js 로 결국 처리되야 하는 특성상, cljs<->js  를 뒤지게 많이쓰고, js/ts 로 된 객체들을 엄청쓰는데, 동적언어라 잡히지 않아서 돌리리 전까지 모르겟다.. 서버 (clj <-> Java) 는 보통 clj 로 레핑된 라이브러리 쓰거나 아니여도 경계가 나름 명확하게 나눌수 있는데,
js/ts <-> cljs 는 내가 fe구조랑 js 를 몰라서 어케 처리해야되는지 몰것음;;

- 뭐 안되면 krell / node 다 clean 빌드하자
-- target 폴더 지우고 , node_modules 지우고 돌리면 어지간한 path 문제는 해결됨

REPL 은 신이다, GPT 는 무적이고 -> Cursor 는 신이자 무적이다. 이정도 Context 에서 커서는 신 그자체 같은 모델쓰는데 어쩜 이리..
