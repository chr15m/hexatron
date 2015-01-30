(ns hexatron.renderer
  (:require
    [figwheel.client :as fw]
    [hexatron.raf :as raf]
    [cljs.core.async :refer [put! chan <! >! alts! timeout close!]]
  )(:require-macros [cljs.core.async.macros :refer [go]]))

(enable-console-print!)

(defn render-loop [engine t]
    (.render (:renderer engine) (:scene engine) (:camera engine))
    (.update (:stats engine))
  )

(defn init []
  (println (if (.-webgl js/Detector) "Using WebGL renderer." "Using 2d canvas renderer."))
  
  (let [
    width (.-innerWidth js/window)
    height (.-innerHeight js/window)
  
    three-renderer (if (.-webgl js/Detector) (js/THREE.WebGLRenderer. (clj->js {:alpha true :antialias true})) (js/THREE.CanvasRenderer.))
    scene (js/THREE.Scene.)
    camera (js/THREE.PerspectiveCamera. 90 (/ width height) 0.1 1000)
    directional-light (js/THREE.DirectionalLight. 0xffffff 1)
    directional-back-light (js/THREE.DirectionalLight. 0x222200)
    ambient-light (js/THREE.AmbientLight. 0x222222)
    controls (js/THREE.OrbitControls. camera)
    fog (js/THREE.FogExp2. 0x555555 0.002)
    stats (js/Stats.)
    ]

      (println "Setting up scene, render engine, camera.")

      (.setSize three-renderer width height)
      (.position.set camera 5 5 5)
      (.lookAt camera (.-position scene))
      
      (set! (.-damping controls) 0.2)
      (set! (.-fog scene) fog)
      (.setClearColor three-renderer (.-color (.-fog scene)))
      (.normalize (.set (.-position directional-light) 0.5 1 1.5))
      (.normalize (.set (.-position directional-back-light) -0.5 -1 -1.5))
      (.add scene directional-back-light)
      (.add scene directional-light)
      (.add scene ambient-light)
    
      (set! (.-position (.-style (.-domElement stats))) "absolute")
      (set! (.-top (.-style (.-domElement stats))) "0px")
      
      (.appendChild js/document.body (.-domElement stats))
      (.appendChild js/document.body (.-domElement three-renderer))

      {
        :renderer three-renderer
        :scene scene
        :camera camera
        :stats stats
        :animate render-loop
        }
    )
)
