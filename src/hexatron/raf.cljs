(ns hexatron.raf
  (:require
    [cljs.core.async :refer [put! chan <! >! alts! timeout close!]]
  )(:require-macros [cljs.core.async.macros :refer [go]]))

(defn now []
  (.getTime (new js/Date)))

(defn next-frame []
  (let [c (chan)]
    (js/requestAnimationFrame (fn [] (put! c (now))))
    c))

