#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP
#endif

varying vec2 v_texCoords; // Comes in from default.vert

uniform float u_indicator_dir;
uniform float u_indicator_x;
uniform float u_target_falloff_width;
uniform float u_target_width;
uniform float u_target_x;

const float c_indicator_tail = 0.25;

float GetDistanceFromIndicator() {
    if (v_texCoords.x == u_indicator_x) {
        return 0.;
    }
    bool towards = (
        (v_texCoords.x < u_indicator_x && u_indicator_dir < 0.)
        || (v_texCoords.x > u_indicator_x && u_indicator_dir > 0.)
    );
    if (towards) {
        float previous_end_x = (u_indicator_dir > 0.)
            ? 0.
            : 1.;
        return abs(previous_end_x - u_indicator_x) + abs(previous_end_x - v_texCoords.x);
    } else {
        return abs(u_indicator_x - v_texCoords.x);
    }
}

void main()
{

    // Default color
    vec4 color = vec4(1,1,1,0);


    // Falloff color first
    if (v_texCoords.x >= u_target_x - u_target_falloff_width
            && v_texCoords.x < u_target_x) {
        // Falloff left
        color = vec4(0,0,1,1);
    } else if (v_texCoords.x >= u_target_x
            && v_texCoords.x <= u_target_x + u_target_width) {
        // Target
        color = vec4(0,1,0,1);
    } else if (v_texCoords.x > u_target_x + u_target_width
            && v_texCoords.x <= u_target_x + u_target_width + u_target_falloff_width) {
        // Falloff right
        color = vec4(0,0,1,1);
    }

    float distance_from_indicator = GetDistanceFromIndicator();
    if (distance_from_indicator <= c_indicator_tail) {
        color = color + (vec4(1,1,1,1) * max(0.,(1. - (distance_from_indicator / c_indicator_tail))));
    }

    gl_FragColor = color;

}

