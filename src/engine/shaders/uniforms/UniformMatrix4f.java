package engine.shaders.uniforms;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;

public class UniformMatrix4f 
extends Uniform {
	
	private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
	
	public UniformMatrix4f(String name)
	{
		super(name);
	}
	
	public void load(Matrix4f matrix)
	{
		matrix.store(matrixBuffer);
		matrixBuffer.flip();
		
		GL20.glUniformMatrix4(getLocation(), false, matrixBuffer);
	}

}
