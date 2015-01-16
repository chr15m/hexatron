(ns figwheel-test.core
  (:require
    [figwheel.client :as fw]
    [cljs.core.async :refer [put! chan <! >! alts! timeout close!]]
  )(:require-macros [cljs.core.async.macros :refer [go]]))

(enable-console-print!)

;; define your app data so that it doesn't get over-written on reload
;; (defonce app-data (atom {}))

(defn next-frame-channel [] (let [c (chan)]
                          (js/requestAnimationFrame (fn [] (put! c 1)))
                          c
                          ))

(defn ^:export example []
  (let [scene (js/THREE.Scene.)
        width (.-innerWidth js/window)
        height (.-innerHeight js/window)
        camera (js/THREE.PerspectiveCamera. 75 (/ width height) 0.1 1000 )
        renderer (js/THREE.WebGLRenderer.)
        ]
    (.log js/console "Hello.")
    (.setSize renderer width height)
    (.appendChild js/document.body (.-domElement renderer) )
    (set! (.-z (.-position camera))  5)
    ; (render)
    (go 
      (let [
        geometry (js/THREE.BoxGeometry. 1 1 1)
        material (js/THREE.MeshBasicMaterial. (clj->js {:color 0x00ff00}))
        cube (js/THREE.Mesh. geometry material)
      ]
      (.add scene cube)
      (loop [x 0]
        (<! (next-frame-channel))
        (set! (.-x (.-rotation cube)) (.sin js/Math (/ x 100)))
        (set! (.-y (.-rotation cube)) (.sin js/Math (/ x 80)))
        (.render renderer scene camera)
        (recur (inc x))
        )
      ))
    )
  )

(fw/start {
  :on-jsload (fn []
               ;; (stop-and-start-my app)
               (println "Edits to this text should show up in your developer console. Ok.")
               (example)
               )})

