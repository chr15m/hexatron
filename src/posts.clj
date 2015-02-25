(ns posts
  (:gen-class)
  (:require
    [clojure.java.io :as io]
    [clojure.string :as string]
    [markdown.core :refer [md-to-html-string]]
    [net.cgrand.enlive-html :as html]))

(html/deftemplate index-html (java.io.File. "./index.html") []
  [:.posts] (html/append "Hello"))

(defn generate-posts [] 
  ; get the .md files from the posts directory
  (let [posts (filter (fn [f] (.endsWith f ".md")) (.list (io/file "posts")))]
    (for [p posts] 
      ;(println p)
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
        ;(println (.format (java.text.SimpleDateFormat. "yyyy-MM-dd") touched))
        ;(println title)
        ;(println body)
        {:title title :body body :touched touched}
        ))))

(defn -main
  [& args]
  (println "Generating posts from 'posts' subdirectory.")
  (println (generate-posts))
  ;(println (str main-template))
  (println (apply str (index-html)))
  )

