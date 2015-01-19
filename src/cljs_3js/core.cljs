(ns cljs-3js.core
  (:require
    [figwheel.client :as fw]
    [cljs.core.async :refer [put! chan <! >! alts! timeout close!]]
  )(:require-macros [cljs.core.async.macros :refer [go]]))


(enable-console-print!)

;; define your app data so that it doesn't get over-written on reload
;; (defonce app-data (atom {}))

(def otherthing {
                 :setup (fn [] (println "Otherthing setup"))
                 })

(def thing {
            :setup (fn [] (println "Thing setup"))
            :update (fn [] (println "Thing update"))  
            :teardown (fn [] (println "Thing teardown"))  
            })

; (def game-state (atom (:entities [thing])))
(def game-state {:entities [thing otherthing]})


;(defn setup-system [state]
;  (map #(when (satisfies? Moveable %) (c/move % state)) 
;        (:entities state)))

; (println game-state)

; ((:setup thing))

(defn next-frame []
  (let [c (chan)]
    (js/requestAnimationFrame (fn [] (put! c (.getTime (new js/Date)))))
    c))

(defn update-cube [cube t]
    ;(set! (.-x (.-rotation cube)) (.sin js/Math (/ t 1000)))
    (set! (.-y (.-rotation cube)) (.sin js/Math (/ t 807)))
    ;(set! (.-y (.-rotation cube)) 0)
    (set! (.-y (.-position cube)) (* 0.2 (.sin js/Math (/ t 143))))
    ;(set! (.-x (.-position cube)) (* 0.2 (.sin js/Math (/ t 140))))
  )

(defn create-cube [scene] 
      (let [
        geometry (js/THREE.BoxGeometry. 1 1 1)
        material (js/THREE.MeshBasicMaterial. (clj->js {:color 0x00ff00}))
        cube (js/THREE.Mesh. geometry material)
      ]
      (set! (.-x (.-rotation cube)) 0)
      (set! (.-y (.-rotation cube)) 0)
      (set! (.-y (.-scale cube)) 0.1)
      (println "Creating cube and adding it to the scene.")
      (.add scene cube)
      (go (loop [f 0]
        (let [t (<! (next-frame))]
          (update-cube cube t)
          )
        (recur (inc f))
        ))
      ) )

(defn renderer-loop [renderer scene camera]
  (println "Starting renderer.")
  (go
    (loop [x 0]
    (<! (next-frame))
    (.render renderer scene camera)
    (recur (inc x))
    )
    ))

(println (if js/webglAvailable "Using WebGL renderer." "Using 2d canvas renderer."))

(defonce width (.-innerWidth js/window))
(defonce height (.-innerHeight js/window))
(defonce renderer (if (js/webglAvailable) (js/THREE.WebGLRenderer.) (js/THREE.CanvasRenderer.)))
; (defonce renderer (js/THREE.WebGLRenderer.))
; (defonce renderer (js/THREE.CanvasRenderer.))
(defonce scene (js/THREE.Scene.))
(defonce camera (js/THREE.PerspectiveCamera. 75 (/ width height) 0.1 1000))
(.position.set camera 3 3 3)
(.lookAt camera (.-position scene))
(defonce setup-stuff (do 
    (println "Setting up scene, renderer, camera.")
    (.setSize renderer width height)
    (.appendChild js/document.body (.-domElement renderer) )
    (set! (.-z (.-position camera))  5)
    (create-cube scene)
    (renderer-loop renderer scene camera)
    )
)

(defn run-game-state-fn [n] 
  ; run through every entity in game-state
  ; filter out entities which don't have the key we are looking for
  ; those that do, run the key
  (doseq [e (filter
              (fn [e] (n e))
              (:entities game-state))
          
          ] ((n e)))
  )

; whenever setup happens run the 
(defn setup []
  (println "(setup)")
  (run-game-state-fn :setup)
  )

(defn teardown []
  (println "(teardown)")
  (run-game-state-fn :teardown)
  )

(fw/start {
  :on-jsload (fn []
    ;; (stop-and-start-my app)
    (println "js-reload")
    (teardown)
    (setup)
    )
  
  }          
)
