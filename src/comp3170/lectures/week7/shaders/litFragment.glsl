#version 410

uniform sampler2D u_diffuseTexture;
uniform sampler2D u_specularTexture;
uniform float u_shininess;

uniform float u_gamma;
uniform vec3 u_ambientIntensity;	// RGB - INTENSITY
uniform vec3 u_lightIntensity;	// RGB - INTENSITY
uniform vec4 u_lightDirection;	// WORLD

uniform vec4 u_cameraDirection;	// WORLD

in vec4 v_normal;   // WORLD
in vec2 v_texcoord;	// UV 

layout(location = 0) out vec4 o_colour;

void main() {
	// make the normal have length 1 to eliminate any scaling due to interpolation
	vec4 n = normalize(v_normal);
	vec4 s = normalize(u_lightDirection);
	vec4 r = reflect(-s, n); 
	vec4 v = normalize(u_cameraDirection);

	// materials
    vec3 diffuseMaterial = texture(u_diffuseTexture, v_texcoord).rgb;	// BRIGHTNESS
    diffuseMaterial = pow(diffuseMaterial, vec3(u_gamma));	// INTENSITY
    vec3 specularMaterial = texture(u_specularTexture, v_texcoord).rgb;	// INTENSITY
        
    // lighting physics in intensity space (linear)
    
    vec3 ambient = u_ambientIntensity * diffuseMaterial;
    vec3 diffuse = u_lightIntensity * diffuseMaterial * max(0, dot(n, s));
    vec3 specular = vec3(0);

	float rDotV = 0;
    
    if (dot(s,n) > 0) {
		rDotV = dot(r, v);
    	specular = u_lightIntensity * specularMaterial * pow(max(0, rDotV), u_shininess); 
    }
    
    vec3 intensity = ambient + diffuse + specular;
    
    // return value in brightness space
    vec3 brightness = pow(intensity, vec3(1./u_gamma));		// B = I ^ (1/gamma)        
    
    o_colour = vec4(brightness,1);
}

