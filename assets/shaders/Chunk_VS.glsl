#version 130

in vec3 position;
in vec2 textureCoordinates;
in vec3 normal;

out vec2 pass_textureCoordinates;
out float visibility;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 playerPosition;
uniform int renderDistance;

const float density = 0.5;
const float gradient = 1.5;

void main(void){
	vec4 worldPosition = transformationMatrix * vec4(position,1.0);
	gl_Position = projectionMatrix * viewMatrix * worldPosition;
	pass_textureCoordinates = textureCoordinates;

	float distance = sqrt(pow(worldPosition.x - playerPosition.x, 2)+ pow(worldPosition.z - playerPosition.z,2));
	visibility = 1;
	if(distance / 16 > renderDistance-2){
		visibility = 1-(distance - (16*(renderDistance-2)))/16.0;
	}
	visibility = clamp(visibility,0.0,1.0);
}
