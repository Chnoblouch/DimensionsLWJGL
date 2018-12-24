#version 400 core

in vec2 textureCoords;

uniform sampler2D spriteTexture;
uniform float textureAlpha;

out vec4 outColor;

void main(void)
{
	outColor = texture(spriteTexture, textureCoords);
	outColor.a *= textureAlpha;
	
	outColor.r = 0.0;
	outColor.g = 0.0;
	outColor.b = 0.0;
}