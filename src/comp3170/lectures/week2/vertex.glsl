#version 410

in vec4 a_position; // vertex position - (X,Y,Z,W)
in vec3 a_colour; // vertex colour (R,G,B)
out vec3 v_colour; // RGB to the fragment shader

void main() {
	gl_Position = a_position;
	v_colour = a_colour;	// Pass the colour to the fragment shader
}