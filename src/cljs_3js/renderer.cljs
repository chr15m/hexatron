(ns cljs-3js.renderer
  (:require
    [figwheel.client :as fw]
    [cljs-3js.raf :as raf]
    [cljs.core.async :refer [put! chan <! >! alts! timeout close!]]
  )(:require-macros [cljs.core.async.macros :refer [go]]))

(enable-console-print!)

(defn renderer-loop [renderer scene camera]
  (println "Starting renderer.")
  (go
    (loop [x 0]
    (<! (raf/next-frame))
    (.render renderer scene camera)
    (recur (inc x))
    )
    ))

(println (if js/webglAvailable "Using WebGL renderer." "Using 2d canvas renderer."))

(def width (.-innerWidth js/window))
(def height (.-innerHeight js/window))

(defonce renderer (if (js/webglAvailable) (js/THREE.WebGLRenderer.) (js/THREE.CanvasRenderer.)))
(defonce scene (js/THREE.Scene.))
(defonce camera (js/THREE.PerspectiveCamera. 75 (/ width height) 0.1 1000))

(.setSize renderer width height)
(.position.set camera 3 3 3)
(.lookAt camera (.-position scene))

(defonce setup-stuff (do 
    (println "Setting up scene, renderer, camera.")
    (.appendChild js/document.body (.-domElement renderer) )
    (renderer-loop renderer scene camera)
    )
)

(defn get-scene [] scene)
