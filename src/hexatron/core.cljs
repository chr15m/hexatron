(ns hexatron.core
  (:require
    [figwheel.client :as fw]
    [hexatron.tile :as tile]
    [hexatron.ui :as ui]
    [hexatron.raf :as raf]
    [hexatron.game-map :as game-map]
    [hexatron.renderer :as renderer]
    [cljs.core.async :refer [put! chan <! >! alts! timeout close!]]
  )(:require-macros [cljs.core.async.macros :refer [go]]))

(enable-console-print!)

;; define your app data so that it doesn't get over-written on reload
;; (def game-state (atom (:entities [])))

(defn render-loop [entities]
  (go (loop [t 0]
      (let [t (<! (raf/next-frame))]
        (dorun (map (fn [e] ((:animate e) e t)) entities))
        )
      (recur (raf/now))
      ))
  )

(defonce launch (let [
    engine (renderer/init)
    tiles (game-map/generate (:scene engine) 50 50)
    entities (concat [engine] tiles)
    ]
  (ui/set-text "major-info" "hexatron")
  (render-loop entities)
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
