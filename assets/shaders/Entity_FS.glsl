#version 130

in vec2 pass_textureCoordinates;

out vec4 out_Color;

uniform sampler2D modelTexture;
uniform vec3 tint;

void main(void){
	vec4 textureColour = texture(modelTexture,pass_textureCoordinates);
	if(textureColour.a<0.5) discard;
	out_Color = textureColour;
	out_Color.rgb += tint.xyz;
}
