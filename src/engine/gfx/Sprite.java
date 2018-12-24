package engine.gfx;

import java.util.HashMap;

import org.lwjgl.util.Color;

public class Sprite {
	
	private int id;
	private int fullWidth;
	private int fullHeight;
	private int x;
	private int y;
	private int width;
	private int height;
	private float r, g, b, a;
	
	private HashMap<String, Sprite> spriteCache = new HashMap<>();
	
	public Sprite(int id, int fullWidth, int fullHeight, int x, int y, int width, int height)
	{
		this.id = id;
		this.fullWidth = fullWidth;
		this.fullHeight = fullHeight;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public Sprite clone()
	{
		return new Sprite(id, fullWidth, fullHeight, x, y, width, height).setOverlayColor(r, g, b, a);
	}
	
	public int getID()
	{
		return id;
	}
	
	public int getFullWidth()
	{
		return fullWidth;
	}
	
	public int getFullHeight()
	{
		return fullHeight;
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
	
	public int getWidth()
	{
		return width;
	}
	
	public int getHeight()
	{
		return height;
	}
	
	public Sprite getSprite(int x, int y, int width, int height)
	{
		return spriteCache.computeIfAbsent(x + "," + y + "," + width + "," + height,
										   key -> new Sprite(id, fullWidth, fullHeight, x, y, width, height));
	}

	public Sprite setOverlayColor(float r, float g, float b, float a)
	{
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
		
		return this;
	}
	
	public float getRedOverlay()
	{
		return r;
	}
	
	public float getGreenOverlay()
	{
		return g;
	}
	
	public float getBlueOverlay()
	{
		return b;
	}
	
	public float getOverlayAlpha()
	{
		return a;
	}
}
