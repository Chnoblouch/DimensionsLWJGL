package game.particle;

import org.lwjgl.opengl.Display;

import engine.gfx.Font;
import engine.gfx.Sprites;
import engine.rendering.Screen;
import game.Game;
import game.TextLoader;

public class YouDiedScreen 
{	
	private boolean running = false;
	private boolean pulsing = false;
	
	private float alpha = 0;
	private float addAlphaSpeed = 0.025f;
	private float pulsingSize;
	private boolean pulsingBigger = false;
	
	private Game game;
		
	public YouDiedScreen(Game game)
	{
		this.game = game;
	}
			
	public void render(Screen screen)
	{		
		if(!running) return;
		
		float size = 2048 * (1.0f + (1.0f - alpha)) + pulsingSize;
		
		screen.renderGUI(Sprites.deathScreen, Display.getWidth() / 2 - (size / 2), Display.getHeight() / 2 - (size / 2), size, size, 0, alpha);
		
		String youDied = TextLoader.getText("you_died0");
		String respawn = TextLoader.getText("you_died1");
		
//		screen.renderFont(youDied, Display.getWidth() / 2 - (Font.getTextWidth(youDied, 64)) / 2, 
//						  Display.getHeight() / 2 - 384, 64, Font.COLOR_WHITE, false);
		
		if(game.player.deadTime >= 8000)
		{
			screen.renderFont(respawn, Display.getWidth() / 2 - (Font.getTextWidth(respawn, 48)) / 2, 
							  Display.getHeight() / 2 + 96 - 384, 48, Font.COLOR_WHITE, false);
		}
	}
	
	public void run()
	{
		running = true;
		alpha = 0;
		addAlphaSpeed = 0.025f;
		pulsing = false;
		pulsingBigger = false;
	}
	
	public void hide()
	{
		running = false;
	}
	
	public void update(float tpf)
	{
		if(running)
		{			
			if(alpha < 0.99) 
			{
				float process = game.player.deadTime / 8000.0f;
				alpha = process * process * (3.0f - 2.0f * process);
			}
			else 
			{
				alpha = 1.0f;
//				pulsing = true;
			}
			
			if(pulsing)
			{
				if(pulsingSize <= 0) pulsingBigger = true;
				else if(pulsingSize >= 256) pulsingBigger = false;
				
				pulsingSize += pulsingBigger ? 8 * tpf : -4 * tpf;
			}
		}
	}

}
