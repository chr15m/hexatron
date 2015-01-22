(ns cljs-3js.core
  (:require
    [figwheel.client :as fw]
    [cljs-3js.ecs :as ecs]
    [cljs-3js.tile :as tile]
    [cljs-3js.raf :as raf]
    [cljs-3js.renderer :as renderer]
    [nesia :as nesia]
    [cljs.core.async :refer [put! chan <! >! alts! timeout close!]]
  )(:require-macros [cljs.core.async.macros :refer [go]]))


(enable-console-print!)

; (nesia/remember! "my-data" {:one ["small" "array" "of" "joy"] :other "Yes quite good" :extra 42})

; (println "extra:" (:extra (nesia/recall! "my-data")))
; (println "other:" (:other (nesia/recall! "my-data")))
; (println "one:" (:one (nesia/recall! "my-data")))
; (println "nah:" (:nah (nesia/recall! "my-data")))

; (println "non-existent key:" (nesia/recall! "poopstick"))

; (nesia/remember! {:cats "great"} {:ok 1 :cool 3})

; (println "cat's, great:" (nesia/recall! {:cats "great"}))

;; define your app data so that it doesn't get over-written on reload
;; (defonce app-data (atom {}))

(def otherthing {
                 :setup (fn [] (println "Otherthing setup"))
                 })

(def thing {
            :setup (fn [] (println "Thing setup"))
            :update (fn [] (println "Thing update"))  
            :teardown (fn [] (println "Thing teardown"))  
            })

; (def game-state (atom (:entities [thing])))
(def game-state {:entities [thing otherthing]})

(defonce tiley (tile/create (renderer/get-scene)))

; whenever setup happens run the 
(defn setup []
  ; (println "(setup)")
  (ecs/run-system :setup (:entities game-state))
  )

(defn teardown []
  ; (println "(teardown)")
  (ecs/run-system :teardown (:entities game-state))
  )

(fw/start {
  :on-jsload (fn []
    ;; (stop-and-start-my app)
    ; (println "js-reload")
    (teardown)
    (setup)
    )
  }          
)
