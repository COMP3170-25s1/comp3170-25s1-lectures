#version 410

in vec4 a_position;	// MODEL
in vec4 a_normal;	// MODEL
in vec2 a_texcoord;	// UV 

uniform mat4 u_mvpMatrix;			// MODEL -> NDC
uniform mat4 u_normalMatrix;		// MODEL -> WORLD (NORMAL)

out vec4 v_normal;		// WORLD 
out vec2 v_texcoord;	// UV 

void main() {
	v_normal = u_normalMatrix * a_normal;
	v_texcoord = a_texcoord;
    gl_Position = u_mvpMatrix * a_position;
}

