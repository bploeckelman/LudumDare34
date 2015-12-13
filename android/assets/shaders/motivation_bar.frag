#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP
#endif

varying LOWP vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform float u_time;



void main()
{
  vec2 offset = vec2(sin(u_time + 20.5 * v_texCoords.x), cos(u_time + 16.5 * v_texCoords.y))* .01;
  vec4 color = texture2D(u_texture, offset + v_texCoords) * v_color;

  color = vec4(v_texCoords.x, v_texCoords.y, 0, 1);

  gl_FragColor = color;
}