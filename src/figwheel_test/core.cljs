(ns figwheel-test.core
  (:require
    [figwheel.client :as fw]))

(enable-console-print!)

;; define your app data so that it doesn't get over-written on reload
;; (defonce app-data (atom {}))

(defn ^:export example []
  (let [scene (js/THREE.Scene.)
        width (.-innerWidth js/window)
        height (.-innerHeight js/window)
        camera (js/THREE.PerspectiveCamera. 75 (/ width height) 0.1 1000 )
        renderer (js/THREE.WebGLRenderer.)
        geometry (js/THREE.BoxGeometry. 1 1 1)
        material (js/THREE.MeshBasicMaterial. (clj->js {:color 0x00ff00}))
        cube (js/THREE.Mesh. geometry material)
        render (fn cb []
                   (js/requestAnimationFrame cb) 
                   (set! (.-x (.-rotation cube))  (+ 0.1 (.-x (.-rotation cube))) )
                   (set! (.-y (.-rotation cube))  (+ 0.1 (.-y (.-rotation cube))) )
                   (.render renderer scene camera)
                 ) 
        ]
    (.log js/console "Hello.")
    (.setSize renderer width height)
    (.appendChild js/document.body (.-domElement renderer) )
    (.add scene cube)
    (set! (.-z (.-position camera))  5)
    (render)
    )
  )

(fw/start {
  :on-jsload (fn []
               ;; (stop-and-start-my app)
               (println "Edits to this text should show up in your developer console. Ok.")
               (example)
               )})

