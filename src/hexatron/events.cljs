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

; http://threejs.org/examples/canvas_interactive_cubes.html
(defn set-picker-listener [engine interactive-entities listener]
  (let [
    raycaster (js/THREE.Raycaster.)
    mouse (js/THREE.Vector2.)
    renderer (:renderer engine)
    camera (:camera engine)
    entities (clj->js (map (fn [t] (set! (.-cljo (:mesh t)) t) (:mesh t)) interactive-entities))
        ]
    ; (println (.-cljo  (nth entities 0)))
    ; TODO: use a channel loop to listen for mouse event & only unpick old objects
    (.addEventListener js/document "mousemove" (fn [ev]
        (.preventDefault ev)
        (set! (.-x mouse) (- (* (/ (.-clientX ev) (.-width (.-domElement renderer))) 2) 1))
        (set! (.-y mouse) (+ (* (* (/ (.-clientY ev) (.-height (.-domElement renderer))) 2) -1) 1))
        ; (println "mouse" (.-x mouse) (.-y mouse))
        (.setFromCamera raycaster mouse camera)
        (let [intersects (.intersectObjects raycaster entities)]
          ; (println "intersects" intersects)
          (when (count intersects) 
            ;(dorun (map (fn [e] (put! (:channel e) "unpicked")) interactive-entities))
            (dorun (map (fn [e] ((:unpick e) e)) interactive-entities))
            )
          ; (dorun (map (fn [o] (put! (:channel (.-cljo (.-object o))) "picked")) intersects))
          (dorun (map (fn [o]
            (let [e (.-cljo (.-object o))] ( (:pick e) e))
            ) intersects))
          )
      ) false)
    )
  )
