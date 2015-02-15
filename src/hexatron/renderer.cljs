(ns hexatron.renderer
  (:require
    [hexatron.colorscheme :as color]
    [hexatron.shaders :as shaders]
    [figwheel.client :as fw]
    [cljs.core.async :refer [put! chan <! >! alts! timeout close!]]
  )(:require-macros [cljs.core.async.macros :refer [go]]))

(enable-console-print!)

(defn render-loop [engine t]
    ;(.render (:renderer engine) (:scene engine) (:camera engine))
    (.render (:composer engine))
    (.update (:stats engine))
    (set! (.-value (:screen-shader-time engine)) (+ (.-value (:screen-shader-time engine)) 1.0)) 
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
    directional-back-light (js/THREE.DirectionalLight. 0x222228)
    ambient-light (js/THREE.AmbientLight. 0x333333)
    controls (js/THREE.OrbitControls. camera)
    fog (js/THREE.FogExp2. (:black color/scheme) 0.005)
    stats (js/Stats.)
    ]

      (println "Setting up scene, render engine, camera.")
      
      (.setSize three-renderer width height)
      (.position.set camera 5 5 5)
      (.lookAt camera (.-position scene))
      
      (set! (.-damping controls) 0.2)
      (set! (.-fog scene) fog)
      (.setClearColor three-renderer (.-color (.-fog scene)))
      (.set (.-position directional-light) 25 50 75)
      (.normalize (.set (.-position directional-back-light) -0.5 0.5 -1.5))
      (set! (.-castShadow directional-light) true)
      (set! (.-shadowDarkness directional-light) 0.25)
      ; (set! (.-shadowCameraVisible directional-light) true)
      (set! (.-shadowCameraFar directional-light) 125)
      (set! (.-shadowCameraLeft directional-light) -30)
      (set! (.-shadowCameraRight directional-light) 30)
      (set! (.-shadowCameraTop directional-light) 30)
      (set! (.-shadowCameraBottom directional-light) -30)
      (set! (.-shadowMapWidth directional-light) 2048)
      (set! (.-shadowMapHeight directional-light) 2048)
      (.add scene directional-back-light)
      (.add scene directional-light)
      (.add scene ambient-light)
    
      (set! (.-position (.-style (.-domElement stats))) "absolute")
      (set! (.-top (.-style (.-domElement stats))) "0px")
      
      (set! (.-autoClear three-renderer) false) 
      (set! (.-shadowMapEnabled three-renderer) true)
      ;(set! (.-shadowMapType three-renderer) js/THREE.PCFSoftShadowMap)
      ; (set! (.-shadowMapSoft three-renderer) true)

      (.appendChild js/document.body (.-domElement stats))
      (.appendChild js/document.body (.-domElement three-renderer))

      (let [
        composer (js/THREE.EffectComposer. three-renderer)
        render-pass (js/THREE.RenderPass. scene camera)
        screen-shader (js/THREE.ShaderPass. shaders/tv)
        bloom-shader (js/THREE.BloomPass. 0.75)
        copy-shader (js/THREE.ShaderPass. js/THREE.CopyShader)
        ]
        
        (.addPass composer render-pass)
        (set! (.-renderToScreen render-pass) true)
        (.addPass composer bloom-shader)
        (.addPass composer screen-shader)
        (set! (.-renderToScreen screen-shader) true)
        
        ;(.addPass composer copy-shader)
        ;(set! (.-renderToScreen copy-shader) true)
        {
          :renderer three-renderer
          :composer composer
          :scene scene
          :camera camera
          :stats stats
          :screen-shader-time (.-time (.-uniforms screen-shader))
          :animate (fn [] render-loop)
          }
        
        )
    )
)
