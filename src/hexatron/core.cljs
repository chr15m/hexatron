(ns hexatron.core
  (:require
    [figwheel.client :as fw]
    [hexatron.tile :as tile]
    [hexatron.ui :as ui]
    [hexatron.events :as events]
    [hexatron.rng :as rng]
    [hexatron.mapper :as mapper]
    [hexatron.renderer :as renderer]
    [cljs.core.async :refer [put! chan <! >! alts! timeout close!]]
  )(:require-macros [cljs.core.async.macros :refer [go]]))

(enable-console-print!)

;; define your app data so that it doesn't get over-written on reload
;; (def game-state (atom (:entities [])))

(defn render-loop [entities]
  (go (loop [t 0]
      (let [t (<! (events/next-frame))]
        (dorun (map (fn [e] (((:animate e)) e t)) entities)))
      (recur (events/now))
      ))
  )

(defonce launch (let [
    quest-seed (rng/init)
    engine (renderer/init)
    tiles (mapper/generate (:scene engine) 50 50)
    entities (concat [engine] tiles)
    ]
  (ui/set-text "major-info" "hexatron")
  (render-loop entities)
  (events/set-picker-listener engine tiles (fn []))
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
