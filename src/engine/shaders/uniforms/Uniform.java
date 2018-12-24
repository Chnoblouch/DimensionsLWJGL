package engine.shaders.uniforms;

import org.lwjgl.opengl.GL20;

public class Uniform {
	
	private String name;
	private int location;
	
	public Uniform(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void findLocation(int programID)
	{
		location = GL20.glGetUniformLocation(programID, name);
	}
	
	public int getLocation()
	{
		return location;
	}
}
