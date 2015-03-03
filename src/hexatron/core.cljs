(ns hexatron.core
  (:require
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
    map-generator (mapper/generator 50 50)
    floor-map (mapper/generate map-generator)
    tiles (for [[x y] floor-map] (tile/create (:scene engine) :pos [(- x (/ (.-_width map-generator) 2)) 0 (- y (/ (.-_height map-generator) 2))]))
    player (player/create (:scene engine) :pos (:pos (nth tiles 27)))
    entities (concat [engine player] tiles)
    ]
  (ui/set-text "major-info" "hexatron")
  (render-loop entities)
  (renderer/resize-watch engine)
  (let [picker-chan (events/entity-picker engine tiles)]
    (go (loop []
      (let [[intersected old-intersected] (<! picker-chan)]
        ; do something interesting with intersected objects here - path find
        ;(println intersected)
        (when (not (= intersected old-intersected))
          ; (mapper/find-path (:pos (nth intersected 0)))
          (println intersected)))
      (recur))))))

