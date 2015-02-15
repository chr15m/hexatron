(ns hexatron.player
  (:require
    [hexatron.colorscheme :as colors]
    [cljs.core.async :refer [put! chan <! >! alts! timeout close!]]
  )(:require-macros [cljs.core.async.macros :refer [go]]))

(defn animate [being t]
  (let [mesh (:mesh being)
        [x y z] (:pos being)
        ]
    ;(set! (.-x (.-rotation mesh)) 0)
    ;(set! (.-y (.-rotation mesh)) (.sin js/Math (/ t 1000)))
    (set! (.-y (.-rotation mesh)) (/ t 2000))
    ; (set! (.-y (.-rotation mesh)) (.sin js/Math (/ t 807)))
    ;(set! (.-y (.-rotation mesh)) 0)
    (set! (.-y (.-position mesh)) (+ (* 0.1 (.sin js/Math (/ t 143))) (+ y 1)))
    ;(set! (.-x (.-position mesh)) (+ (* 6 (.sin js/Math (/ t 843))) (+ x)))
    ;(set! (.-z (.-position mesh)) (+ (* 6 (.cos js/Math (/ t 973))) (+ z)))
    )
  )

(defn init [being]
  (let [mesh (:mesh being)
        pos (:pos being)
        [x y z] pos
        ]
    ; (println "Creating tile at" [x y z] "and adding it to the scene.") 
    (set! (.-x (.-position mesh)) x)
    (set! (.-y (.-position mesh)) y)
    (set! (.-z (.-position mesh)) z)
    ; (set! (.-y (.-scale mesh)) 0.1)
    ; reference back from the mesh to the clojure object so we can grab it
    )
  )

(defn create [scene & {:keys [pos] :or {pos [0 0 0]}}] 
      (let [
        geometry (js/THREE.SphereGeometry. 0.375 7 7)
        material (js/THREE.MeshLambertMaterial. (clj->js {:ambient 0xffffff :color (:blue colors/scheme) :shading js/THREE.FlatShading}))
        mesh (js/THREE.Mesh. geometry material)
        player {
              :mesh mesh
              :geometry geometry
              :material material
              :pos pos
              :channel (chan)
              :animate (fn [] animate)
              }
      ]
      (set! (.-castShadow mesh) true)
      (init player)
      (.add scene mesh)
      player
      ))
