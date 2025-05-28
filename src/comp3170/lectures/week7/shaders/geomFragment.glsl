#version 410

in vec4 v_normal;	// WORLD SPACE

layout(location = 0) out vec4 o_colour;

void main() {
	// make the normal have length 1 to eliminate any scaling due to interpolation
	vec4 n = normalize(v_normal);	
	n = (n + 1) / 2;
	
	// store the normal and the depth
    o_colour = vec4(n.xyz, gl_FragCoord.z);
}

