package engine.gfx;

import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class SpriteLoader {
	
	private static ArrayList<Integer> sprites = new ArrayList<>();
	
	public static Sprite load(String file)
	{
		Texture texture = null;
		
		try
		{
			texture = TextureLoader.getTexture("PNG", SpriteLoader.class.getResourceAsStream(file));
			
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
			
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		int id = texture.getTextureID();
		int width = (int) texture.getImageWidth();
		int height = (int) texture.getImageHeight();
		sprites.add(id);
								
		return new Sprite(id, width, height, 0, 0, width, height);
	}
	
	public static void clear()
	{
		for(int i = 0; i < sprites.size(); i++) GL11.glDeleteTextures(sprites.get(i));
	}

}
