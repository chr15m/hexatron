(ns posts
  (:gen-class)
  (:require
    [clojure.java.io :as io]
    [markdown.core :refer [md-to-html-string]]
    ))

(defn generate-posts [] 
  (let [posts (filter (fn [f] (.endsWith f ".md")) (.list (io/file "posts")))]
    (doseq [p posts] (println (md-to-html-string (slurp (str "posts/" p)))))))

(defn -main
  [& args]
  (println "Generating posts from 'posts' subdirectory.")
  (generate-posts)
  )

