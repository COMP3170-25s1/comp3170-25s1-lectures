#version 410

in vec4 a_position;	// MODEL
in vec4 a_normal;	// MODEL
in vec2 a_texcoord;	// UV 

in vec4 a_vertexA;	// MODEL
in vec4 a_vertexB;	// MODEL
in vec4 a_vertexC;	// MODEL
in vec4 a_vertexD;	// MODEL

uniform mat4 u_mvpMatrix;			// MODEL -> NDC
uniform mat4 u_modelMatrix;			// MODEL -> WORLD
uniform mat4 u_normalMatrix;		// MODEL -> WORLD (NORMAL)

out vec4 v_normal;		// WORLD 
out vec2 v_texcoord;	// UV 

out vec4 v_position;
out vec4 v_vertexA;
out vec4 v_vertexB;
out vec4 v_vertexC;
out vec4 v_vertexD;

void main() {
	
	v_position = u_modelMatrix * a_position;
	v_vertexA = u_modelMatrix * a_vertexA;
	v_vertexB = u_modelMatrix * a_vertexB;
	v_vertexC = u_modelMatrix * a_vertexC;
	v_vertexD = u_modelMatrix * a_vertexD;

	v_normal = u_normalMatrix * a_normal;
	v_texcoord = a_texcoord;
    gl_Position = u_mvpMatrix * a_position;
}

