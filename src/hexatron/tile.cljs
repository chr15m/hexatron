(ns hexatron.tile
  (:require
    [hexatron.colorscheme :as colors]
    [cljs.core.async :refer [put! chan <! >! alts! timeout close!]]
  )(:require-macros [cljs.core.async.macros :refer [go]]))

(defn animate [tile t]
  (let [mesh (:mesh tile)]
    ;(set! (.-x (.-rotation mesh)) (.sin js/Math (/ t 1000)))
    ; (set! (.-y (.-rotation mesh)) (.sin js/Math (/ t 807)))
    ;(set! (.-y (.-rotation mesh)) 0)
    ;(set! (.-y (.-position mesh)) (* 0.2 (.sin js/Math (/ t 143))))
    ; lol wave board
    ; (set! (.-y (.-position mesh)) (* 0.2 (.sin js/Math (/ (+ t (*  (js/Math.sqrt (+ (js/Math.pow (.-x (.-position mesh)) 2) (js/Math.pow (.-z (.-position mesh)) 2))) 100)) 243))))
    ;(set! (.-x (.-position mesh)) (* 0.2 (.sin js/Math (/ t 140))))
    )
  )

(defn init [tile]
  (let [mesh (:mesh tile)
        pos (:pos tile)
        [x y z] pos
        ]
    ; (println "Creating tile at" [x y z] "and adding it to the scene.") 
    (set! (.-x (.-position mesh)) x)
    (set! (.-y (.-position mesh)) y)
    (set! (.-z (.-position mesh)) z)
    ; (set! (.-y (.-scale mesh)) 0.1)
    ; reference back from the mesh to the clojure object so we can grab it
    (set! (.-cljo mesh) tile)
    )
  (event-listener tile)
  )

(defn pick [tile]
    (.setHex (.-color (:material tile)) (:pink colors/scheme))
  )

(defn unpick [tile]
    (.setHex (.-color (:material tile)) (:grey-4-bright colors/scheme))
  )

(defn event-listener [tile]
  (go (loop []
    (let [ev (<! (:channel tile))]
      ; (println "EVENT!" ev)
      (when (= ev "picked") (pick tile))
      (when (= ev "unpicked") (unpick tile))
      (recur)
      ))
    )
  )

(defn create [scene & {:keys [pos] :or {pos [0 0 0]}}] 
      (let [
        geometry (js/THREE.BoxGeometry. 1 0.1 1)
        material (js/THREE.MeshLambertMaterial. (clj->js {:ambient 0xffffff :color (:grey-4-bright colors/scheme) :shading js/THREE.FlatShading}))
        mesh (js/THREE.Mesh. geometry material)
        tile {
              :mesh mesh
              :geometry geometry
              :material material
              :pos pos
              :channel (chan)
              ; :animate (fn [] animate)
              :pick pick
              :unpick unpick
              }
      ]
      (init tile)
      (.add scene mesh)
      tile
      ))
