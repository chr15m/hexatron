(ns posts
  (:gen-class)
  (:require
    [clojure.java.io :as io]
    [clojure.string :as string]
    [markdown.core :refer [md-to-html-string]]
    ))

(defn generate-posts [] 
  ; get the .md files from the posts directory
  (let [posts (filter (fn [f] (.endsWith f ".md")) (.list (io/file "posts")))]
    (doseq [p posts] 
      (println p)
      ; process the filename to extract the post title
      (let [
            title (string/join " " (get (split-at 3 (string/split (get (string/split p #"\.") 0) #"-")) 1))
            body (md-to-html-string (slurp (str "posts/" p)))]
        (println title)
        ; use markdown to convert to html
        (println body)))))

(defn -main
  [& args]
  (println "Generating posts from 'posts' subdirectory.")
  (generate-posts)
  )

