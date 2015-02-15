(ns hexatron.shaders
  (:require-macros [hexatron.utils :refer [load-text-file]]))

(enable-console-print!)

(def tv
  (clj->js {
    :uniforms {
      :tDiffuse { :type "t" :value nil }
      :time { :type "f", :value 0.0 },
      }
    :vertexShader (load-text-file "src/hexatron/vertex-shader-tv.glsl")
    :fragmentShader (load-text-file "src/hexatron/fragment-shader-tv.glsl")
    })
)

