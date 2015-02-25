(ns posts
  (:gen-class)
  (:require
    [clojure.java.io :as io]
    [clojure.string :as string]
    [markdown.core :refer [md-to-html-string]]
    [hiccup.core :refer [html]]
    [net.cgrand.enlive-html :as enlive]))

(enlive/deftemplate index-html (java.io.File. "./index.html") [posts]
  [:#posts] (enlive/html-content (html
    (for [p posts]
      [:div.post
        [:h2.date (.format (java.text.SimpleDateFormat. "yyyy-MM-dd") (:touched p))]
        [:h1 (:title p)]
        [:div (:body p)]]))))

(defn list-posts []
  (reverse (sort (filter (fn [f] (.endsWith f ".md")) (.list (io/file "posts"))))))

(defn generate-posts [] 
  ; get the .md files from the posts directory
  (let [posts (list-posts)]
    (for [p posts]
      (let [
          ; process the filename to extract the post title
          title (string/join " " (get (split-at 3 (string/split (get (string/split p #"\.") 0) #"-")) 1))
          ; get the filename
          filename (str "posts/" p)
          ; use markdown to convert to html
          body (md-to-html-string (slurp filename))
          ; when the file was last modified
          touched (.lastModified (java.io.File. filename))]
        ; return the datastructure holding what we've generated
        {:title title :body body :touched touched}))))

(defn -main
  [& args]
  (println "Generating posts from 'posts' subdirectory.")
  (spit "index.html" (apply str (index-html (generate-posts)))))
