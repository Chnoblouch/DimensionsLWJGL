package engine.shaders.uniforms;

import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Vector2f;

public class UniformInt 
extends Uniform {
	
	private int value;
	private boolean used = false;
	
	public UniformInt(String name)
	{
		super(name);
	}
	
	public void load(int value)
	{
		if(!used || value != this.value)
		{
			GL20.glUniform1i(getLocation(), value);
			
			used = true;
			this.value = value;
		}
	}

}
