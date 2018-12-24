#version 400 core

in vec2 position;

uniform vec2 texCoordsLeftTop;
uniform vec2 texCoordsRightBottom;
uniform mat4 transformationMatrix;
uniform mat4 screenMatrix;
uniform mat4 viewMatrix;

out vec2 textureCoords;
out float distance;

void main(void)
{
	vec4 worldPosition = transformationMatrix * vec4(position.x, position.y, 0.0, 1.0);
	vec4 finalPosition = viewMatrix * worldPosition * screenMatrix;
	gl_Position = finalPosition;
	
	distance = length(finalPosition.xy);
	
	// left bottom corner
	// right bottom corner
	// left top corner
	// right bottom corner
	// center
	
	if(position.x == -1 && position.y == -1) textureCoords = vec2(texCoordsLeftTop.x, texCoordsRightBottom.y); 				
	else if(position.x == 1 && position.y == -1) textureCoords = texCoordsRightBottom;	
	else if(position.x == -1 && position.y == 1) textureCoords = texCoordsLeftTop;				
	else if(position.x == 1 && position.y == 1) textureCoords = vec2(texCoordsRightBottom.x, texCoordsLeftTop.y);			
	else textureCoords = vec2((texCoordsLeftTop + texCoordsRightBottom) / 2);								
}