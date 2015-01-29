(ns hexatron.ui)

(defn set-text [element text]
  (set! (.-innerText (.getElementById js/document element)) text) 
  )
