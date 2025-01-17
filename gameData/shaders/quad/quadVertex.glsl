#version 430 core

layout(location = 0) in vec2 position;

out vec2 texCoord;

uniform mat4 transformationMatrix;
uniform float z;

void main(void) {
	gl_Position = transformationMatrix * vec4(position, 0.0, 1.0);
	gl_Position.z = z;
	gl_Position.w = 1.0;
	texCoord = position / 2.0 + 0.5;
}
