(ns hexatron.game-map
  (:require
    [hexatron.tile :as tile]
    )
  )

(defn generate [scene w h]
  (let [rot-map-generator (js/ROT.Map.Rogue. w h)]
      (.create rot-map-generator
        (fn [x y t]
          (when (= t 0) (tile/create scene :pos [(- x (/ (.-_width rot-map-generator) 2)) 0 (- y (/ (.-_height rot-map-generator) 2))]))
    ))))

