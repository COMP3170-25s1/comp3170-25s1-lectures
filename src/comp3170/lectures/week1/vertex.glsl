#version 410

in vec4 a_position;
in vec3 a_colour;
out vec3 vertexColour;

void main()
{
	gl_Position = a_position;
	vertexColour = a_colour;
}