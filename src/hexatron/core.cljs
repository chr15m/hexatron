(ns hexatron.core
  (:require
    [figwheel.client :as fw]
    [hexatron.tile :as tile]
    [hexatron.ui :as ui]
    [hexatron.renderer :as renderer]
    [cljs.core.async :refer [put! chan <! >! alts! timeout close!]]
  )(:require-macros [cljs.core.async.macros :refer [go]]))

(enable-console-print!)

;; define your app data so that it doesn't get over-written on reload
;; (def game-state (atom (:entities [])))

(defonce launch (let [
    [engine scene camera controls] (renderer/init)
    ]
  (renderer/start-loop engine scene camera controls)
  (ui/set-text "major-info" "hexatron")
  (let [map-generator (js/ROT.Map.Rogue. 50 50)]
    (.create map-generator
      (fn [x y tile]
        (when (= tile 0) (tile/create scene :pos [(- x (/ (.-_width map-generator) 2)) 0 (- y (/ (.-_height map-generator) 2))]))
  )))))

(fw/start {
  :on-jsload (fn []
    ;; (stop-and-start-my app)
    ; (println "js-reload")
    ; (teardown)
    ; (setup)
    )
  }          
)
