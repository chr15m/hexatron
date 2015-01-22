(ns nesia
  (:require
    [cljs.reader :as edn]
    )
  )

(defn remember! [itemName values] (
  .setItem js/window.localStorage (pr-str itemName) (pr-str values)))

(defn recall! [itemName]
  (let [
    result (.getItem js/window.localStorage (pr-str itemName))
    ]
    (if (nil? result) nil (edn/read-string result))))

(defn forget! [itemName]
  .removeItem js/window.localStorage (pr-str itemName))

