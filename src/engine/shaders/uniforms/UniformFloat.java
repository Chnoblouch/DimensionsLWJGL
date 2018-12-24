package engine.shaders.uniforms;

import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Vector2f;

public class UniformFloat 
extends Uniform {
	
	private float value;
	private boolean used = false;
	
	public UniformFloat(String name)
	{
		super(name);
	}
	
	public void load(float value)
	{
		if(!used || value != this.value)
		{
			GL20.glUniform1f(getLocation(), value);
			
			used = true;
			this.value = value;
		}
	}

}
