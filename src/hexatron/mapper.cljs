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
          (when (= t 0) (.push new-map [(- x (/ (.-_width rot-map-generator) 2)) (- y (/ (.-_height rot-map-generator) 2))]))))
      (js->clj new-map)))

(defn find-path [map-structure from to]
  (let [
    path (clj->js [])
    [xf yf] from
    [xt yt] to]
    ; run the path finding computation
    (.compute
      ; create a new AStar pathfinding object
      (ROT.Path.AStar.
        ; from position
        xf yf
        ; callback which tells the pathfinder valid points
        (fn [x y]
          (let [c (count (filter (fn [i] (= i [x y])) map-structure))])
          (println "path-test" x y c)
          c)
        (clj->js {:topology 4}))
      ; to position
      xt yt
      ; callback with points that should go into the path
      (fn [x y]
        (println "hello" x y)
        (.push path [x y])
        )
    )
    (println "path" path)
    path))

