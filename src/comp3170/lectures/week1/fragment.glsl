#version 410

uniform vec3 u_colour; // colour as a 3D vector (r,g,b)
uniform vec2 u_screenSize; // screen dimensions in pixels

layout(location = 0) out vec4 o_colour; // (r,g,b,a) (0-1,0-1,0-1,0-1)

void main()
{
	vec2 p = gl_FragCoord.xy / u_screenSize; // scale pixel location into a range of (0,0) to (1,1)
	vec2 offset = vec2(0.5,0.5f);

	float greenIntensity = 2.0f;
	float redChannel = p.y;
	float blueChannel = 1.0f-p.y;
	float greenChannel = distance(offset,p) * greenIntensity;
	
	vec3 finalColour = vec3(redChannel,greenChannel,blueChannel);
	o_colour = vec4(u_colour.rgb + finalColour.rgb, 1.0f);
}