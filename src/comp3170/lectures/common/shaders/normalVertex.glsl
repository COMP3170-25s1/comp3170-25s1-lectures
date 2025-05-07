#version 410

in vec4 a_position;	// MODEL 
in vec4 a_normal;	// MODEL 

out vec4 v_normal;	// WORLD

uniform mat4 u_mvpMatrix;			// MODEL -> NDC
uniform mat4 u_normalMatrix;		// MODEL -> WORLD

void main() {
	// convert the normal to world space and pass to fragment shader
	v_normal = u_normalMatrix * a_normal;	// MODEL -> WORLD
    gl_Position = u_mvpMatrix * a_position;	// MODEL -> NDC
}

