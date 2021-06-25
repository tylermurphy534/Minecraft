#version 140

in vec2 textureCoords;

out vec4 out_Color;

uniform vec4 color;

void main(void){
	out_Color = color;
}
