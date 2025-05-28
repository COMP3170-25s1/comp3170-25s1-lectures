#version 410

uniform sampler2D u_texture;
uniform vec2 u_resolution;	// width x height in pixels

in vec2 v_texcoord;	// UV 

layout(location = 0) out vec4 o_colour;

vec3 rescale(vec3 n) {
	return (n * 2) - 1;
}

void main() {
	
	// read neighbouring pixels 
	
	vec2 delta = 1. / u_resolution; // 1/w, 1/h 

	vec4 p = texture(u_texture, v_texcoord);
	vec4 pLeft  = texture(u_texture, v_texcoord + vec2(-delta.x,0));
	vec4 pRight = texture(u_texture, v_texcoord + vec2(+delta.x,0));
	vec4 pAbove = texture(u_texture, v_texcoord + vec2(0,+delta.y));
	vec4 pBelow = texture(u_texture, v_texcoord + vec2(0,-delta.y));

	// process normals
	
	vec3 n = rescale(p.rgb);
	vec3 nLeft   = rescale(pLeft.rgb);
	vec3 nRight  = rescale(pRight.rgb);
	vec3 nAbove  = rescale(pAbove.rgb);
	vec3 nBelow  = rescale(pBelow.rgb);
	
	float dpLeft  = dot(n, nLeft);
	float dpRight = dot(n, nRight);
	float dpAbove = dot(n, nAbove);
	float dpBelow = dot(n, nBelow);
	
	float dp = max(0, min(min(dpLeft, dpRight), min(dpAbove, dpBelow)));
	
	// process depths
	
	float d = p.a;
	float dLeft  = pLeft.a;
	float dRight = pRight.a;
	float dAbove = pAbove.a;
	float dBelow = pBelow.a;

	float ddLeft  = abs(d - dLeft);
	float ddRight = abs(d - dRight);
	float ddAbove = abs(d - dAbove);
	float ddBelow = abs(d - dBelow);
	
	float dd = 1-max(max(ddLeft, ddRight),max(ddAbove, ddBelow));
	
	float c = dd * dp;
	
	o_colour = vec4(c,c,c,1);
}

