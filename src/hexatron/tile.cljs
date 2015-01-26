(ns hexatron.tile
  (:require
    [hexatron.raf :as raf]
    [cljs.core.async :refer [put! chan <! >! alts! timeout close!]]
  )(:require-macros [cljs.core.async.macros :refer [go]]))

(defn animate [mesh t]
    ;(set! (.-x (.-rotation mesh)) (.sin js/Math (/ t 1000)))
    ; (set! (.-y (.-rotation mesh)) (.sin js/Math (/ t 807)))
    ;(set! (.-y (.-rotation mesh)) 0)
    (set! (.-y (.-position mesh)) (* 0.2 (.sin js/Math (/ t 143))))
    ;(set! (.-x (.-position mesh)) (* 0.2 (.sin js/Math (/ t 140))))
  )

(defn create [scene & [[x y z]]] 
      (let [
        geometry (js/THREE.BoxGeometry. 1 1 1)
        material (js/THREE.MeshLambertMaterial. (clj->js {:ambient 0xffffff :color 0x00ff00}))
        mesh (js/THREE.Mesh. geometry material)
      ]
      (set! (.-x (.-position mesh)) (or x 0))
      (set! (.-y (.-position mesh)) (or y 0))
      (set! (.-z (.-position mesh)) (or z 0))
      (set! (.-y (.-scale mesh)) 0.1)
      (println "Creating tile at" [(or x 0) y z] "and adding it to the scene.")
      (.add scene mesh)
      (go (loop [t 0]
        (let [t (<! (raf/next-frame))]
          (animate mesh t)
          )
        (recur (raf/now))
        ))
      ) )
