package game.gui;

import java.util.Random;

import engine.gfx.Sprites;
import engine.rendering.Screen;
import game.Game;

public class HealthBar {
	
	private Game game;
	private float process = 100.0f;
	
	public boolean freeze = false;
	private float freezeAmount;
	
	public HealthBar(Game game)
	{
		this.game = game;
	}
	
	public void setFreeze(boolean freeze)
	{
		this.freeze = freeze;
	}
	
	public void render(Screen screen)
	{
		process = game.player.getHealth(); 
		
		int sy = game.player.invulnerableInvisible ? 9 : 0;
		
		for(int i = 0; i < 10; i ++)
		{
			int sx = 0;
			
			int x = i * 40 + 16;
			int y = game.player.invulnerableInvisible ? 16 - 4 + new Random().nextInt(9) : 16;
			
			if(process >= (i + 1) * 10) sx = 0;
			else if(process < (i + 1) * 10 && process >= ((i + 1) * 10) - 5) sx = 9;
			else if(process < ((i + 1) * 10) - 5) sx = 18;
			
			if(freezeAmount < 1.0f) screen.renderGUI(Sprites.bars.getSprite(sx, sy, 9, 9), x, y, 36, 36, 0, 1.0f - freezeAmount);
			if(freezeAmount > 0.0f) screen.renderGUI(Sprites.bars.getSprite(sx, sy + 18, 9, 9), x, y, 36, 36, 0, freezeAmount);
		}
	}
	
	public void update(float tpf)
	{
		float health = game.player.getHealth();
		
		if(health + 1.0 > process) process += 20.0f * tpf;
		if(health - 1.0f < process) process -= 20.0f * tpf;
		else process = health;
		
		if(freeze) 
		{
			if(freezeAmount < 1) freezeAmount += 0.01f * tpf;
			if(freezeAmount >= 0.9f) freezeAmount = 1.0f;
		}
		if(!freeze) 
		{
			if(freezeAmount > 0) freezeAmount -= 0.01f * tpf;
			if(freezeAmount <= 0.1f) freezeAmount = 0.0f;
		}
	}

}
