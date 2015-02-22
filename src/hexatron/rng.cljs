(ns hexatron.rng
  (:require [hexatron.store :as store]))

(defn seed-from-url []
  (when
    (not (= (.indexOf js/document.location.href "?") -1))
    (.pop (.split js/document.location.href "?")))
  )

(defn fresh-seed []
  (.getSeed js/ROT.RNG))

(defn seed-to-url [seed]
  (.replaceState js/history {:quest-seed seed} (str "Quest #" seed) (str "?" seed))
  )

(defn init []
  (let [quest-seed (or (seed-from-url) (store/recall :quest-seed) (fresh-seed))]
    (store/remember! :quest-seed quest-seed)
    (seed-to-url quest-seed)
    (println "quest-seed" quest-seed)
    (.setSeed js/ROT.RNG quest-seed)
    quest-seed
    ))

