(ns hexatron.core
  (:require
    [figwheel.client :as fw]
    [hexatron.tile :as tile]
    [hexatron.ui :as ui]
    [hexatron.game-map :as game-map]
    [hexatron.renderer :as renderer]
    [cljs.core.async :refer [put! chan <! >! alts! timeout close!]]
  )(:require-macros [cljs.core.async.macros :refer [go]]))

(enable-console-print!)

;; define your app data so that it doesn't get over-written on reload
;; (def game-state (atom (:entities [])))

(defonce launch (let [
    [engine scene camera stats] (renderer/init)
    ]
  (renderer/start-loop engine scene camera stats)
  (ui/set-text "major-info" "hexatron")
  (game-map/generate scene 50 50)
  ))

(fw/start {
  :on-jsload (fn []
    ;; (stop-and-start-my app)
    ; (println "js-reload")
    ; (teardown)
    ; (setup)
    )
  }          
)
