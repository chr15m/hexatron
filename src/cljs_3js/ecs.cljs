; entity-component-system
(ns cljs-3js.ecs)

(defn run-system [n entities] 
  ; run through every entity in game-state
  ; filter out entities which don't have the key we are looking for
  ; those that do, run the key
  (doseq [e (filter
              (fn [e] (n e))
              entities)          
          ] ((n e)))
  )

