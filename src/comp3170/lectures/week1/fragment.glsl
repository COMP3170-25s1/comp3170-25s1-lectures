#version 410

uniform vec3 u_colour; // colour as a 3D vector (r,g,b)
uniform vec2 u_screenSize; // screen dimensions in pixels

layout(location = 0) out vec4 o_colour; // (r,g,b,a)

void main()
{
	vec2 p = gl_FragCoord.xy / u_screenSize; // scale pixel location into a range of (0,0) to (1,1)
	o_colour = vec4(u_colour.rgb, 1.0f);
}
	