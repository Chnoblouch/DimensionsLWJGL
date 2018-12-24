package engine.rendering;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;

public class GameDisplay {
	
	private int width = 1500;
	private int height = 850;
	private int fpsCap = 60;
	
	private boolean fullscreen = false;
	
	public GameDisplay()
	{
		
	}
	
	public void setWidth(int width)
	{
		this.width = width;
	}
	
	public void setHeight(int height)
	{
		this.height = height;
	}
	
	public void setFullscreen(boolean fullscreen)
	{
		this.fullscreen = fullscreen;
	}
	
	public void setFPSCap(int fps)
	{
		this.fpsCap = fps;
	}
	
	public void create()
	{
		ContextAttribs attributes = new ContextAttribs(3, 2).withForwardCompatible(true).withProfileCore(true);
		
		try 
		{
			if(!fullscreen) Display.setDisplayMode(new DisplayMode(width, height));
			else Display.setFullscreen(true);
			
			Display.setVSyncEnabled(true);
			Display.create(new PixelFormat(), attributes);
			Display.setTitle("Test");
						
		} catch (LWJGLException e)
		{
			e.printStackTrace();
		}
	}
	
	public void update()
	{
		Display.sync(fpsCap);
		Display.update();
	}
	
	public void close()
	{
		Display.destroy();
	}
}
