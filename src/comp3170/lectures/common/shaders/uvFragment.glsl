#version 410

in vec2 v_texcoord;	// UV 

layout(location = 0) out vec4 o_colour;

void main() {
	vec2 uv = mod(v_texcoord, 1.);
	
    o_colour = vec4(uv,0,1);
}

