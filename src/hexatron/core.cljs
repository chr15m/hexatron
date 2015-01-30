(ns hexatron.core
  (:require
    [figwheel.client :as fw]
    [hexatron.tile :as tile]
    [hexatron.ui :as ui]
    [hexatron.raf :as raf]
    [hexatron.game-map :as game-map]
    [hexatron.renderer :as renderer]
    [cljs.core.async :refer [put! chan <! >! alts! timeout close!]]
  )(:require-macros [cljs.core.async.macros :refer [go]]))

(enable-console-print!)

;; define your app data so that it doesn't get over-written on reload
;; (def game-state (atom (:entities [])))

(defonce launch (let [
    engine (renderer/init)
    ]
  (renderer/start-loop engine)
  (ui/set-text "major-info" "hexatron")
  (let [tiles (game-map/generate (:scene engine) 50 50)]
    (println "tiles" (count tiles))
    (go (loop [t 0]
        (let [t (<! (raf/next-frame))]
          (dorun (map (fn [tile] (tile/animate tile t)) tiles))
          )
        (recur (raf/now))
        )))
  ))

(fw/start {
  :on-jsload (fn []
    ;; (stop-and-start-my app)
    ; (println "js-reload")
    ; (teardown)
    ; (setup)
    )
  }          
)
