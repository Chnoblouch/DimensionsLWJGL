package engine.shader;

import engine.shaders.uniforms.UniformFloat;
import engine.shaders.uniforms.UniformMatrix4f;
import engine.shaders.uniforms.UniformVector2f;

public class ShadowShader 
extends ShaderProgram {
	
	public UniformVector2f texCoordsLeftTop;
	public UniformVector2f texCoordsRightBottom;
	public UniformMatrix4f transformationMatrix;
	public UniformMatrix4f viewMatrix;
	public UniformFloat textureAlpha;
	
	public ShadowShader()
	{
		create("/engine/shader/shadow_vertex.glsl", "/engine/shader/shadow_fragment.glsl");
		
		texCoordsLeftTop = new UniformVector2f("texCoordsLeftTop");
		texCoordsRightBottom = new UniformVector2f("texCoordsRightBottom");
		transformationMatrix = new UniformMatrix4f("transformationMatrix");
		viewMatrix = new UniformMatrix4f("viewMatrix");
		textureAlpha = new UniformFloat("textureAlpha");
		
		findUniformLocations(texCoordsLeftTop, texCoordsRightBottom, transformationMatrix, viewMatrix, textureAlpha);
	}
	
}
