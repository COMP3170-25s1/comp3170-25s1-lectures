#version 410

in vec4 a_position;
in vec3 a_colour;
void main()
{
	gl_Position = a_position;
}