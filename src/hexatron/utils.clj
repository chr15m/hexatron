(ns hexatron.utils)

(defmacro load-text-file [relative-uri-filename] (slurp relative-uri-filename))
