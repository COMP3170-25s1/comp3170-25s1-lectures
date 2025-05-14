#version 410

uniform sampler2D u_texture;
uniform float u_gamma;
uniform vec3 u_ambientIntensity;	// RGB - INTENSITY
uniform vec3 u_lightIntensity;	// RGB - INTENSITY

in vec4 v_normal;   // WORLD
in vec2 v_texcoord;	// UV 

layout(location = 0) out vec4 o_colour;

void main() {
	// make the normal have length 1 to eliminate any scaling due to interpolation
	vec4 n = normalize(v_normal);

    vec3 diffuseMaterial = texture(u_texture, v_texcoord).rgb;	// BRIGHTNESS
    diffuseMaterial = pow(diffuseMaterial, vec3(u_gamma));	// INTENSITY
        
    // lighting physics in intensity space (linear)
    
    vec3 ambient = u_ambientIntensity * diffuseMaterial;
    vec3 diffuse = vec3(0);
    vec3 specular = vec3(0);    
    
    vec3 intensity = ambient + diffuse + specular;
    
    // return value in brightness space
    vec3 brightness = pow(intensity, vec3(1./u_gamma));		// B = I ^ (1/gamma)        
    
    o_colour = vec4(brightness,1);
}

