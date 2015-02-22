(ns hexatron.store
  (:require [cljs.reader :as edn]))

(defn remember! [item-name values]
  (.setItem js/window.localStorage (pr-str item-name) (pr-str values)))

(defn recall [item-name]
  (if-let [result (.getItem js/window.localStorage (pr-str item-name))]
    (edn/read-string result)))

(defn forget! [item-name]
  (.removeItem js/window.localStorage (pr-str item-name)))

