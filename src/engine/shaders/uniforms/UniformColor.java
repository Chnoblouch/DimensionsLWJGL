package engine.shaders.uniforms;

import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

public class UniformColor 
extends Uniform {
	
	private float r, g, b, a;
	private boolean used = false;
	
	public UniformColor(String name)
	{
		super(name);
	}
	
	public void load(Vector4f color)
	{
		load(color.x, color.y, color.z, color.w);
	}
	
	public void load(float r, float g, float b, float a)
	{
		if(!used || r != this.r || g != this.g || b != this.b || a != this.a)
		{
			GL20.glUniform4f(getLocation(), r, g, b, a);
			
			used = true;
			this.r = r;
			this.g = g;
			this.b = b;
			this.a = a;
		}
	}

}
