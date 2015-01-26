(defproject figwheel-test "0.1.0-SNAPSHOT"
  :description "Playing with Clojurescript and Three.js with figwheel in between"
  :url "https://github.com/chr15m/clojurescript-threejs-playground"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2411"]
                 [figwheel "0.1.7-SNAPSHOT"]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha"]
                 [sablono "0.2.22"]
                 [om "0.8.0-beta2"]]

  :plugins [[lein-cljsbuild "1.0.3"]
            [lein-figwheel "0.1.7-SNAPSHOT"]]

  :source-paths ["src"]
  
  :cljsbuild {
    :builds [{:id "dev"
              :source-paths ["src"]
              :compiler {:output-to "resources/public/js/compiled/hexatron.js"
                         :output-dir "resources/public/js/compiled/out"
                         :optimizations :none
                         :source-map true}}
             {:id "min"
              :source-paths ["src"]
              :compiler {:output-to "www/hexatron.min.js"
                         :optimizations :advanced
                         :pretty-print false
                         :preamble [""]
                         :externs [""]}}]}
  :figwheel {
             :http-server-root "public" ;; default and assumes "resources" 
             :server-port 3449 ;; default
             :css-dirs ["resources/public/css"] ;; watch and update CSS
             ;; :ring-handler figwheel-test.server/handler
             })
