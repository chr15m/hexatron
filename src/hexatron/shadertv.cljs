(ns hexatron.shadertv
  (:require-macros [hexatron.utils :refer [load-text-file]]))

(def shader
  (clj->js {
    :uniforms {
      :tDiffuse { :type "t" :value nil }
      :opacity { :type "f" :value 1.0}
    }
    :vertexShader (load-text-file "src/hexatron/vertex-shader-tv.glsl")
    :fragmentShader (load-text-file "src/hexatron/fragment-shader-tv.glsl")
  })
)
