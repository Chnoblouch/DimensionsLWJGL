package game.particle;

import org.lwjgl.opengl.Display;

import engine.gfx.Sprites;
import engine.rendering.Screen;

public class ChangeLevelScreen 
{	
	private boolean running = false;
	public boolean finish = false;
	
	private float s1 = 0;
	private float a1 = 0, a2 = 0;
		
	public void render(Screen screen)
	{
		if(!running && !finish) return;
		
		screen.renderGUI(Sprites.changelevelParticle.getSprite(0, 0, 128, 128), 
						(Display.getWidth() / 2) - (s1 / 2), 
						(Display.getHeight() / 2) - (s1 / 2),
						s1, s1, 0, a1);
		
		screen.renderGUI(Sprites.changelevelParticle.getSprite(128, 0, 128, 128), 0, 0, Display.getWidth(), Display.getHeight(), 0, a2);
	}
	
	public void run()
	{
		running = true;
		finish = false;
		s1 = 0;
		a1 = 0;
		a2 = 0;
	}
	
	public void update(float tpf)
	{
		if(running)
		{
			s1 += 30 * tpf;
			if(a1 < 0.95) a1 += 0.05 * tpf;
			if(a2 < 0.98) a2 += 0.02 * tpf;
			else
			{
				running = false;
				finish = true;
				
				s1 = 0;
				a1 = 0;
			}
		}
		
		if(finish)
		{
			if(a2 > 0.02) a2 -= 0.02 * tpf;
			else 
			{
				finish = false;
				a2 = 0;
			}
		}
	}

}
