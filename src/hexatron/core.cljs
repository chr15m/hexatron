(ns hexatron.core
  (:require
    [figwheel.client :as fw]
    [hexatron.tile :as tile]
    [hexatron.renderer :as renderer]
    [cljs.core.async :refer [put! chan <! >! alts! timeout close!]]
  )(:require-macros [cljs.core.async.macros :refer [go]]))

(enable-console-print!)

;; define your app data so that it doesn't get over-written on reload
;; (def game-state (atom (:entities [])))

(defonce launch (let [[engine scene camera] (renderer/init)]
  (renderer/start-loop engine scene camera)
  (tile/create scene [1 0 1])  
  (tile/create scene [2 0 0])  
  (tile/create scene)))

(fw/start {
  :on-jsload (fn []
    ;; (stop-and-start-my app)
    ; (println "js-reload")
    ; (teardown)
    ; (setup)
    )
  }          
)
