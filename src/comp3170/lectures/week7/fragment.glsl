#version 410

in vec3 v_colour;

layout(location = 0) out vec4 o_colour;	// RGBA

const float NLINES = 10.0;

void main() {
	vec3 c = v_colour;
	
    o_colour = vec4(c, 1);
}

