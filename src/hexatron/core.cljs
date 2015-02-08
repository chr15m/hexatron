(ns hexatron.core
  (:require
    [figwheel.client :as fw]
    [hexatron.tile :as tile]
    [hexatron.player :as player]
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
  (let [animators (filter (fn [e] (:animate e)) entities)]
    (go (loop [t 0]
        (let [t (<! (events/next-frame))]
          (dorun (map (fn [e] (((:animate e)) e t)) animators)))
        (recur (events/now))
        ))))

(defonce launch (let [
    quest-seed (rng/init)
    engine (renderer/init)
    tiles (mapper/generate (:scene engine) 50 50)
    player (player/create (:scene engine) :pos (:pos (nth tiles 27)))
    entities (concat [engine player] tiles)
    ]
  (ui/set-text "major-info" "hexatron")
  (render-loop entities)
  (let [picker-chan (events/entity-picker engine tiles)]
    (go (loop []
      (let [[intersected old-intersected] (<! picker-chan)]
        ; do something interesting with intersected objects here - path find
        (println intersected)
        )
      (recur))))
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
