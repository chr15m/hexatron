(ns hexatron.events
  (:require
    [cljs.core.async :refer [put! chan <! >! alts! timeout close!]]
  )(:require-macros [cljs.core.async.macros :refer [go]]))

(defn now []
  (.getTime (new js/Date)))

(defn next-frame []
  (let [c (chan)]
    (js/requestAnimationFrame (fn [] (put! c (now))))
    c))

(defn mouse-move []
  (let [c (chan)]
  (.addEventListener js/document "mousemove" (fn [ev]
        (.preventDefault ev)
        (put! c [(.-clientX ev) (.-clientY ev)])
      ) false)
    c))

; http://threejs.org/examples/canvas_interactive_cubes.html
(defn set-picker-listener [engine interactive-entities listener]
  (let [
    raycaster (js/THREE.Raycaster.)
    mouse (js/THREE.Vector2.)
    renderer (:renderer engine)
    camera (:camera engine)
    entities (clj->js (map (fn [t] (set! (.-cljo (:mesh t)) t) (:mesh t)) interactive-entities))
    mouse-move-chan (mouse-move)
        ]
    (go (loop [old-intersected-objects []]
        (let [[x y] (<! mouse-move-chan)]
          (set! (.-x mouse) (- (* (/ x (.-width (.-domElement renderer))) 2) 1))
          (set! (.-y mouse) (+ (* (* (/ y (.-height (.-domElement renderer))) 2) -1) 1))
          (.setFromCamera raycaster mouse camera)
          (let [intersected-meshes (.intersectObjects raycaster entities)
                intersected-objects (map (fn [m] (.-cljo (.-object m))) intersected-meshes)
                ]
            (dorun (map (fn [o] ((:unpick o) o)) old-intersected-objects))
            (dorun (map (fn [o] ((:pick o) o)) intersected-objects))
            (recur intersected-objects)
          ))
        ))
    )
  )
