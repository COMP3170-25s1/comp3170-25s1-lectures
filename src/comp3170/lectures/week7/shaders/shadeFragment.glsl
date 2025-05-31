#version 410

uniform float u_gamma;
uniform vec4 u_lightDirection;	// WORLD

in vec4 v_normal;   // WORLD
in vec2 v_texcoord;	// UV 

in vec4 v_vertexA;
in vec4 v_vertexB;
in vec4 v_vertexC;
in vec4 v_vertexD;

layout(location = 0) out vec4 o_colour;


float cross( in vec2 a, in vec2 b ) { return a.x*b.y - a.y*b.x; }

vec2 invBilinear( in vec2 p, in vec2 a, in vec2 b, in vec2 c, in vec2 d )
{
    vec2 res = vec2(-1.0);

    vec2 e = b-a;
    vec2 f = d-a;
    vec2 g = a-b+c-d;
    vec2 h = p-a;
        
    float k2 = cross( g, f );
    float k1 = cross( e, f ) + cross( h, g );
    float k0 = cross( h, e );
    
    // if edges are parallel, this is a linear equation
    if( abs(k2)<0.001 )
    {
        res = vec2( (h.x*k1+f.x*k0)/(e.x*k1-g.x*k0), -k0/k1 );
    }
    // otherwise, it's a quadratic
    else
    {
        float w = k1*k1 - 4.0*k0*k2;
        if( w<0.0 ) return vec2(-1.0);
        w = sqrt( w );

        float ik2 = 0.5/k2;
        float v = (-k1 - w)*ik2;
        float u = (h.x - f.x*v)/(e.x + g.x*v);
        
        if( u<0.0 || u>1.0 || v<0.0 || v>1.0 )
        {
           v = (-k1 + w)*ik2;
           u = (h.x - f.x*v)/(e.x + g.x*v);
        }
        res = vec2( u, v );
    }
    
    return res;
}

void main() {
	// make the normal have length 1 to eliminate any scaling due to interpolation
	vec4 n = normalize(v_normal);
	vec4 s = normalize(u_lightDirection);

    // lighting physics in intensity space (linear)
    
    float diffuse = max(0, dot(n, s));
    diffuse = mix(diffuse, 1., 0.1);
    
    vec3 intensity = vec3(diffuse);
    
    // return value in brightness space
    vec3 brightness = pow(intensity, vec3(1./u_gamma));		// B = I ^ (1/gamma)        
    
    vec2 uv = mod(v_texcoord, 1.);
    uv = round(uv * 10.) / 10.;
    
    o_colour = vec4(uv,0,1);
}

