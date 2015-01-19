(ns cljs-3js.raf
  (:require
    [cljs.core.async :refer [put! chan <! >! alts! timeout close!]]
  )(:require-macros [cljs.core.async.macros :refer [go]]))


(defn next-frame []
  (let [c (chan)]
    (js/requestAnimationFrame (fn [] (put! c (.getTime (new js/Date)))))
    c))


