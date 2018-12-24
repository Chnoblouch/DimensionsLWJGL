#version 400 core

in vec2 textureCoords;

uniform sampler2D spriteTexture;
uniform float textureAlpha;
uniform vec4 colorOverlay;

out vec4 outColor;

void main(void)
{
	outColor = texture(spriteTexture, textureCoords);
	outColor.a *= textureAlpha;
	
	outColor.xyz = mix(outColor, vec4(colorOverlay.r, colorOverlay.g, colorOverlay.b, 1.0), colorOverlay.a).xyz;
}