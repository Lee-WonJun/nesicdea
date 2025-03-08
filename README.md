# 트러블슈팅 기록

### krell tcp socket 버전 문제
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


### npm modules 사용법

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

### vscode 관련
`.vscode` 참고

- `settings.json` : krell repl 관련 설정
- `tasks.json` : krell 빌드 관련 설정

### 기타 사용법
- npm 으로 추가된 모듈이 있으면 build 
- calva repl 을 키면 cljs connect 될때까지 기다려야 한다. 뭔가 `npx react-native start` 에서 `r` 눌러서 reload 하면 하고 에물레이터가서 뭐라도 해야 connect 된다. 이유는 모름..

REPL 은 신이다, GPT 는 무적이고