#version 410

in vec4 v_normal;	// WORLD SPACE

layout(location = 0) out vec4 o_colour;

void main() {
	// make the normal have length 1 to eliminate any scaling due to interpolation
	vec4 n = normalize(v_normal);
	
	// colour the fragments based on the normal
    o_colour = vec4(n.xyz,1);
}

