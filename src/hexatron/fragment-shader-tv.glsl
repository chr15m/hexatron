// fuzzy tv with lines shader
precision mediump float;

uniform float time;
uniform sampler2D tDiffuse;
varying vec2 vUv;
varying vec3 vNormal;

float rand(vec2 co) {
  return fract(sin(dot(co.xy ,vec2(12.9898,78.233))) * 43758.5453);
}

void main() {
  vec2 co = vec2(gl_FragCoord.x * time / 10000.0, gl_FragCoord.y * time / 10000.0);
  float line = 0.0;
  
  if (int(mod(gl_FragCoord.y / 2.0, 2.0)) == 0) {
    line = 0.02;
  }

  float a = rand(co) * 0.02 + line;

  // float intensity = pow( 0.7 - dot( vNormal, vec3( 0.0, 0.0, 1.0 ) ), 4.0 );
  
  vec4 texel = texture2D( tDiffuse, vUv );
  gl_FragColor = vec4(texel.r + a, texel.g + a, texel.b + a, texel.a);
}
