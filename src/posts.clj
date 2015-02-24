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
    (concat (doseq [p posts] 
      (println p)
      (let [
            ; process the filename to extract the post title
            title (string/join " " (get (split-at 3 (string/split (get (string/split p #"\.") 0) #"-")) 1))
            ; get the filename
            filename (str "posts/" p)
            ; use markdown to convert to html
            body (md-to-html-string (slurp filename))
            ; when the file was last modified
            touched (.lastModified (java.io.File. filename))
            ]
        (println (.format (java.text.SimpleDateFormat. "yyyy-MM-dd") touched))
        (println title)
        (println body)
        )))))

(defn -main
  [& args]
  (println "Generating posts from 'posts' subdirectory.")
  (generate-posts)
  )

