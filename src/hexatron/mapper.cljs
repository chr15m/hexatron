(ns hexatron.mapper
  (:require
    [hexatron.tile :as tile]))

(defn generator [w h]
  (js/ROT.Map.Rogue. w h))

; generate a map of tile positions
(defn generate [rot-map-generator]
  (let [new-map (clj->js [])]
      (.create rot-map-generator
        (fn [x y t]
          (when (= t 0) (.push new-map [x y]))))
      (js->clj new-map)))

