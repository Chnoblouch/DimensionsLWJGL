package engine.shader;

import engine.shaders.uniforms.UniformColor;
import engine.shaders.uniforms.UniformFloat;
import engine.shaders.uniforms.UniformMatrix4f;
import engine.shaders.uniforms.UniformVector2f;

public class GUIShader 
extends ShaderProgram {
	
	public UniformVector2f texCoordsLeftTop;
	public UniformVector2f texCoordsRightBottom;
	public UniformMatrix4f transformationMatrix;
	public UniformMatrix4f screenMatrix;
	public UniformFloat textureAlpha;
	public UniformColor colorOverlay;
	
	public GUIShader()
	{
		create("/engine/shader/gui_vertex.glsl", "/engine/shader/gui_fragment.glsl");
		
		texCoordsLeftTop = new UniformVector2f("texCoordsLeftTop");
		texCoordsRightBottom = new UniformVector2f("texCoordsRightBottom");
		transformationMatrix = new UniformMatrix4f("transformationMatrix");
		screenMatrix = new UniformMatrix4f("screenMatrix");
		textureAlpha = new UniformFloat("textureAlpha");
		colorOverlay = new UniformColor("colorOverlay");
		
		findUniformLocations(texCoordsLeftTop, texCoordsRightBottom, transformationMatrix, screenMatrix, textureAlpha, colorOverlay);
	}
	
}
