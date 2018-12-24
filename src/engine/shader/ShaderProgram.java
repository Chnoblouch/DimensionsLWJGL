package engine.shader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import engine.shaders.uniforms.Uniform;

public class ShaderProgram {
	
	private int id;
	private int vertexShader;
	private int fragmentShader;
	
	public void create(String vertex, String fragment)
	{
		vertexShader = createShader(vertex, GL20.GL_VERTEX_SHADER);
		fragmentShader = createShader(fragment, GL20.GL_FRAGMENT_SHADER);
		
		id = GL20.glCreateProgram();
		GL20.glAttachShader(id, vertexShader);
		GL20.glAttachShader(id, fragmentShader);
		GL20.glBindAttribLocation(id, 0, "position");
		GL20.glLinkProgram(id);
		GL20.glValidateProgram(id);
	}
	
	private int createShader(String file, int type)
	{
		StringBuilder shaderSource = new StringBuilder();
		
		try 
		{
			InputStream in = ShaderProgram.class.getResourceAsStream(file);
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			
			String line = null;
			
			while((line = reader.readLine()) != null)
				shaderSource.append(line).append("\n");
			
			reader.close();
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		int shaderID = GL20.glCreateShader(type);
		GL20.glShaderSource(shaderID, shaderSource);
		GL20.glCompileShader(shaderID);
		
		if(GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE)
		{
			System.err.println("Couldn't compile shader: " + file);
			System.err.println(GL20.glGetShaderInfoLog(shaderID, 500));
		}
		
		return shaderID;
	}
	
	public void findUniformLocations(Uniform... uniforms)
	{
		for(Uniform u : uniforms) u.findLocation(id);
		GL20.glValidateProgram(id);
	}
	
	public void start()
	{
		GL20.glUseProgram(id);
	}
	
	public void stop()
	{
		GL20.glUseProgram(0);
	}
	
	public void delete()
	{
		stop();
		
		GL20.glDetachShader(id, vertexShader);
		GL20.glDetachShader(id, fragmentShader);
		
		GL20.glDeleteShader(vertexShader);
		GL20.glDeleteShader(fragmentShader);
		
		GL20.glDeleteProgram(id);
	}

}
