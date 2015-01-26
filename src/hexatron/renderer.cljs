(ns hexatron.renderer
  (:require
    [figwheel.client :as fw]
    [hexatron.raf :as raf]
    [cljs.core.async :refer [put! chan <! >! alts! timeout close!]]
  )(:require-macros [cljs.core.async.macros :refer [go]]))

(enable-console-print!)

(defn start-loop [engine scene camera]
  (println "Starting three.js renderer.")
  (go
    (loop [t 0]
    (<! (raf/next-frame))
    (.render engine scene camera)
    (recur (raf/now)))))

(defn init []
  (println (if (js/webglAvailable) "Using WebGL renderer." "Using 2d canvas renderer."))
  
  (let [
    width (.-innerWidth js/window)
    height (.-innerHeight js/window)
  
    engine (if (js/webglAvailable) (js/THREE.WebGLRenderer.) (js/THREE.CanvasRenderer.))
    scene (js/THREE.Scene.)
    camera (js/THREE.PerspectiveCamera. 75 (/ width height) 0.1 1000)]

      (.setSize engine width height)
      (.position.set camera 3 3 3)
      (.lookAt camera (.-position scene))
    
      (println "Setting up scene, render engine, camera.")
      (.appendChild js/document.body (.-domElement engine))
      [engine scene camera]
    )
)
