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
uniform bool u_is_on_cooldown;
uniform float u_cooldown_percent_remaining;
uniform float hit_score;

const float c_indicator_tail = 0.2;

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

float GetDistanceFromTarget() {
    if (v_texCoords.x >= u_target_x && v_texCoords.x <= (u_target_x + u_target_width)) {
        return 0.;
    }
    if (v_texCoords.x < u_target_x) {
        return u_target_x - v_texCoords.x;
    } else {
        return v_texCoords.x - (u_target_x + u_target_width);
    }
}

void main()
{

    // Default color
    vec4 color = vec4(1,0,0,0);

    float distance_from_target = GetDistanceFromTarget();
    if (distance_from_target <= u_target_falloff_width) {
        if (distance_from_target == 0.) {
            color = vec4(0,1,0,1);
        } else {
            if (v_texCoords.y > 0.2 && v_texCoords.y < 0.8) {
                float a = 0.5 * (1. - (distance_from_target / u_target_falloff_width));
                color = vec4(0.,1.,0.,a);
            }
        }
    }

    float distance_from_indicator = GetDistanceFromIndicator();
    if (distance_from_indicator <= c_indicator_tail) {
        vec4 tailColor;
        if (u_is_on_cooldown) {
            // Shrink the tail down.
            float tail_length = c_indicator_tail * u_cooldown_percent_remaining;
            float fade = max(0.,(1. - (distance_from_indicator / tail_length)));
//            vec4 score_color = (hit_score == 1.)
//                ? vec4(1,1,1,1)
//                : vec4(1,0,0,1); // vec4(1. - hit_score, hit_score, hit_score, 1);
//            vec4 tail_color = score_color; //mix(score_color, vec4(1,1,1,1), pow(u_cooldown_percent_remaining, 2.));
            tailColor = (vec4(1,1,1,1) * fade);
        } else {
            // Draw a full on majestic tail
            tailColor = (vec4(1,1,1,1) * max(0.,(1. - (distance_from_indicator / c_indicator_tail))));
        }
        color = max(color, tailColor);
    }

    gl_FragColor = color;

}

