#version 410

uniform vec3 u_intensity;

layout(location = 0) out vec4 o_colour;

void main() {
    o_colour = vec4(u_intensity, 1);
}

