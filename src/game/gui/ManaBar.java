package game.gui;

import java.util.Random;

import engine.gfx.Sprites;
import engine.rendering.Screen;
import game.Game;

public class ManaBar {
	
	private Game game;
	private float process = 100.0f;
	
	public ManaBar(Game game)
	{
		this.game = game;
	}
	
	public void render(Screen screen)
	{		
		process = game.player.getMana();
		
		for(int i = 0; i < 10; i ++)
		{			
			if(process >= (i + 1) * 10)
				screen.renderGUI(Sprites.bars.getSprite(27, 0, 9, 9), i * 40 + 16, 64, 36, 36, 0, 1);
			else if(process < (i + 1) * 10 && process >= ((i + 1) * 10) - 5)
				screen.renderGUI(Sprites.bars.getSprite(36, 0, 9, 9), i * 40 + 16, 64, 36, 36, 0, 1);
			else if(process < ((i + 1) * 10) - 5)
				screen.renderGUI(Sprites.bars.getSprite(45, 0, 9, 9), i * 40 + 16, 64, 36, 36, 0, 1);
		}
	}
	
	public void update(float tpf)
	{
		float mana = game.player.getMana();
		
		if(mana + 1.0f > process) process += 20.0f * tpf;
		if(mana - 1.0f < process) process -= 20.0f * tpf;
		else process = mana;
	}

}
