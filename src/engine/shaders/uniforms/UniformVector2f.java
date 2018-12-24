package engine.shaders.uniforms;

import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Vector2f;

public class UniformVector2f 
extends Uniform {
	
	private float x, y;
	private boolean used = false;
	
	public UniformVector2f(String name)
	{
		super(name);
	}
	
	public void load(Vector2f vec)
	{
		load(vec.x, vec.y);
	}
	
	public void load(float x, float y)
	{
		if(!used || x != this.x || y != this.y)
		{
			GL20.glUniform2f(getLocation(), x, y);
			
			used = true;
			this.x = x;
			this.y = y;
		}
	}

}
