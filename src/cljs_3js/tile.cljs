(ns cljs-3js.tile
  (:require
    [cljs-3js.raf :as raf]
    [cljs.core.async :refer [put! chan <! >! alts! timeout close!]]
  )(:require-macros [cljs.core.async.macros :refer [go]]))

(defn update-tile [mesh t]
    ;(set! (.-x (.-rotation mesh)) (.sin js/Math (/ t 1000)))
    ;(set! (.-y (.-rotation mesh)) (.sin js/Math (/ t 807)))
    ;(set! (.-y (.-rotation mesh)) 0)
    (set! (.-y (.-position mesh)) (* 0.2 (.sin js/Math (/ t 143))))
    ;(set! (.-x (.-position mesh)) (* 0.2 (.sin js/Math (/ t 140))))
  )

(defn create [scene] 
      (let [
        geometry (js/THREE.BoxGeometry. 1 1 1)
        material (js/THREE.MeshBasicMaterial. (clj->js {:color 0x00ff00}))
        mesh (js/THREE.Mesh. geometry material)
      ]
      (set! (.-x (.-rotation mesh)) 0)
      (set! (.-y (.-rotation mesh)) 0)
      (set! (.-y (.-scale mesh)) 0.1)
      (println "Creating tile and adding it to the scene.")
      (.add scene mesh)
      (go (loop [f 0]
        (let [t (<! (raf/next-frame))]
          (update-tile mesh t)
          )
        (recur (inc f))
        ))
      ) )
