package engine.shader;

import engine.shaders.uniforms.UniformColor;
import engine.shaders.uniforms.UniformFloat;
import engine.shaders.uniforms.UniformInt;
import engine.shaders.uniforms.UniformMatrix4f;
import engine.shaders.uniforms.UniformVector2f;

public class GameObjectShader 
extends ShaderProgram {
	
	public UniformVector2f texCoordsLeftTop;
	public UniformVector2f texCoordsRightBottom;
	public UniformMatrix4f transformationMatrix;
	public UniformMatrix4f screenMatrix;
	public UniformMatrix4f viewMatrix;
	public UniformInt spriteTexture;
	public UniformInt shadowMap;
	public UniformFloat textureAlpha;
	public UniformColor colorOverlay;
	
	public GameObjectShader()
	{
		create("/engine/shader/obj_vertex.glsl", "/engine/shader/obj_fragment.glsl");
		
		texCoordsLeftTop = new UniformVector2f("texCoordsLeftTop");
		texCoordsRightBottom = new UniformVector2f("texCoordsRightBottom");
		transformationMatrix = new UniformMatrix4f("transformationMatrix");
		screenMatrix = new UniformMatrix4f("screenMatrix");
		viewMatrix = new UniformMatrix4f("viewMatrix");
		spriteTexture = new UniformInt("spriteTexture");
		shadowMap = new UniformInt("shadowMap");
		textureAlpha = new UniformFloat("textureAlpha");
		colorOverlay = new UniformColor("colorOverlay");
		
		findUniformLocations(texCoordsLeftTop, texCoordsRightBottom, transformationMatrix, screenMatrix, viewMatrix, spriteTexture, 
							 shadowMap, textureAlpha, colorOverlay);
	}
	
}
