#version 130

in vec2 pass_textureCoordinates;
in float visibility;

out vec4 out_Color;

uniform sampler2D modelTexture;
uniform vec3 tint;
uniform vec3 skyColor;

void main(void){
	vec4 textureColour = texture(modelTexture,pass_textureCoordinates);
	if(textureColour.a<0.5) discard;
	out_Color = textureColour;
	out_Color.rgb += tint.xyz;
	out_Color = mix(vec4(skyColor.x,skyColor.y,skyColor.z,1.0),out_Color, visibility);
}
