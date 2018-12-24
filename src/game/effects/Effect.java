package game.effects;

import org.lwjgl.opengl.Display;

import engine.gfx.Font;
import engine.gfx.Sprites;
import engine.rendering.Screen;
import game.TextLoader;
import utils.Time;

public class Effect {

	private static int lastEffectID = 0;
	
	private int effectID = 0;
	public static final int ID_SPEED = lastEffectID ++;
	public static final int ID_REGENERATION = lastEffectID ++;
	public static final int ID_FREEZING = lastEffectID ++;
	
	private String name;
	
	private float durationLeft;
	private boolean over = false;
	public static final int DURATION_INFINITE = -1;
			
	protected static final int ICON_Y = 128;
	
	public Effect(float durationLeft)
	{
		this.durationLeft = durationLeft;
	}
	
	public void update(float tpf)
	{
		if(over || durationLeft == DURATION_INFINITE) return;
		
		durationLeft -= tpf;
		if(durationLeft <= 0) over = true;
	}
	
	public void render(Screen screen, float x, float mouseX, float mouseY) {}
	
	public void renderInfo(Screen screen, float x, float mouseX, float mouseY)
	{
		if(mouseX >= x && mouseX <= x + 96 && mouseY >= ICON_Y && mouseY <= ICON_Y + 96)
		{
			int dmx = 48; // x distance to mouse
			int dmy = -48; // y distance to mouse
			int fontSize = 32;
			
			boolean infinite = getDurationLeft() == Effect.DURATION_INFINITE;
					
			String name = getName();
			String durationRaw = "" + Time.ticksInSeconds(getDurationLeft());
			String duration = durationRaw.substring(0, durationRaw.indexOf(".") + 2) + " " + TextLoader.getText("second_short");
			
			String longestPart = name.length() > duration.length() ? name : duration;
			if(infinite) longestPart = name;
			
			int bx = (int) mouseX + dmx; // border x;
			int by = (int) mouseY + dmy; // border y;
			int bw = (int) Math.ceil(Font.getTextWidth(longestPart, fontSize)); // border with
			int bh = infinite ? 32 : 64; // border height
							
			if(bx + bw >= Display.getWidth()) bx -= bw + dmx * 2;
			
			screen.renderGUI(Sprites.itemInfo.getSprite(0, 0, 8, 8), bx, by, 32, 32, 0, 1);		
			screen.renderGUI(Sprites.itemInfo.getSprite(16, 0, 8, 8), bx + bw, by, 32, 32, 0, 1);
			screen.renderGUI(Sprites.itemInfo.getSprite(0, 16, 8, 8), bx, by + bh, 32, 32, 0, 1);		
			screen.renderGUI(Sprites.itemInfo.getSprite(16, 16, 8, 8), bx + bw, by + bh, 32, 32, 0, 1);
			
			for(int i = 0; i < bw - 32; i+= 32) 
			{ 
				screen.renderGUI(Sprites.itemInfo.getSprite(8, 0, 8, 8), bx + 32 + i, by, 32, 32, 0, 1); 
				screen.renderGUI(Sprites.itemInfo.getSprite(8, 16, 8, 8), bx + 32 + i, by + bh, 32, 32, 0, 1); 
			}
			
			for(int i = 0; i < bh - 32; i+= 32) 
			{ 
				screen.renderGUI(Sprites.itemInfo.getSprite(0, 8, 8, 8), bx, by + i + 32, 32, 32, 0, 1); 
				screen.renderGUI(Sprites.itemInfo.getSprite(16, 8, 8, 8), bx + bw, by + 32 + i, 32, 32, 0, 1); 
			}
			
			for(int fy = by + 32; fy < by + bh; fy+= 32)
			{
				for(int fx = bx + 32; fx < bx + bw; fx+= 32)
				{
					screen.renderGUI(Sprites.itemInfo.getSprite(8, 8, 8, 8), fx, fy, 32, 32, 0, 1); 
				}
			}
			
			screen.renderFont(name, bx + 16, by + 16, fontSize, Font.COLOR_WHITE, false);
			if(!infinite) screen.renderFont(duration, bx + 16, by + 48, fontSize, Font.COLOR_WHITE, false);
		}
	}
	
	public void setEffectID(int id)
	{
		this.effectID = id;
	}
	
	public int getEffectID()
	{
		return effectID;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return name;
	}
	
	public float getDurationLeft()
	{
		return durationLeft;
	}
	
	public boolean isOver()
	{
		return over;
	}
}
