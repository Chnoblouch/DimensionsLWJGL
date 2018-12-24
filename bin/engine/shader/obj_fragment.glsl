#version 400 core

in vec2 textureCoords;
in float distance;

uniform sampler2D spriteTexture;
uniform sampler2D shadowMap;
uniform float textureAlpha;
uniform vec4 colorOverlay;

out vec4 outColor;

const float screenWidth = 1920.0;
const float screenHeight = 1080.0;

void main(void)
{
	outColor = texture(spriteTexture, textureCoords);
	outColor.a *= textureAlpha;
	
	outColor.xyz = mix(outColor, vec4(colorOverlay.r, colorOverlay.g, colorOverlay.b, 1.0), colorOverlay.a).xyz;

	//vec4 shadowPixel = texture(shadowMap, vec2(gl_FragCoord.x / screenWidth, gl_FragCoord.y / screenHeight));
	//if(outColor.a > 0 && shadowPixel.r == 0) outColor.rgb /= 2;
	
	//if(inScreenWidth <= 1.0 && inScreenHeight <= 1.0 && shadowMapColor.a > 0.5)
		//outColor.xyz /= 4;
		
	//outColor.r *= 1 - distance * 1.25;
	//outColor.g *= 1 - distance * 1.25;
	//outColor.b *= 1 - distance * 1.25;
	
	//outColor.xyz = 1 - outColor.xyz;
	
	//float average = (outColor.x + outColor.y + outColor.z) / 3.0;
	//outColor.x = average;
	//outColor.y = average;
	//outColor.z = average;
	
	//outColor.x = pow(outColor.x, 4);
	//outColor.y = pow(outColor.y, 4);
	//outColor.z = pow(outColor.z, 4);
}