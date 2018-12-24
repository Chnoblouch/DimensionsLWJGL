package game.creature.npc;

import java.util.Random;

import engine.gfx.Font;
import engine.gfx.Sprites;
import engine.rendering.Screen;
import game.GameObject;
import game.TextLoader;
import game.creature.Player;
import game.gui.Speech;
import utils.Hitbox;
import utils.HitboxFactory;
import utils.TimeCounter;

public class NPCAxar 
extends NPC {
	
	public int sx, sy;
	
	private TimeCounter changeWalkTimer;
	
	private Speech explanation;
	
	public NPCAxar()
	{
		changeWalkTimer = new TimeCounter(2000, () ->
		{
			if(walk) walk = false;
			else
			{
				walk = true;
				
				lookDir = new Random().nextInt(4);
				lookDir = 0;
								
				switch(lookDir)
				{
					case 0: { angle = 180.0f; } break;
					case 1: { angle = 90.0f; } break;
					case 2: { angle = 0.0f; } break;
					case 3: { angle = 270.0f; } break;
				}
			}
						
			changeWalkTimer.reset();
		});
		
		explanation = new Speech(TextLoader.getText("speech_bruno_explanation"));
	}
	
	@Override
	public int getZIndex()
	{
		if(rideOnDragon) return dragon.getZIndex() + 1;
		else return (int) (getY() + 256 - 64 - 1);
	}
	
	private void resetImage()
	{		
		sy = lookDir * 64;
				
//		if(!rideOnDragon)
//		{
//			if(!interact)
//			{
				if(!walk) sx = 0;
				else sx = walkSprites[walkStage];
//			} else sx = 320 + (interactStage * 64);
//		} else sx = 576;
	}
	
	@Override
	public void interactWith(Player player, boolean mouseOn)
	{
		if(!mouseOn) return;
		
		if(explanation.isOver()) level.game.getTextBox().show(TextLoader.getText("speech_bruno"+new Random().nextInt(3)));
		else level.game.getTextBox().show(explanation);
	}
	
	@Override
	public void render(Screen screen)
	{
//		screen.render(SpriteFilter.getShadowStanding(SpriteSheet.bruno.getSprite(sx, sy, 64, 64)), getX() - 64, getY(), 512, 256, 0, 0.25);
		// add back in
		screen.render(Sprites.axar.getSprite(sx, sy, 64, 64), getX(), getY(), 256, 256, 0, invulnerableInvisible ? 0.5f : 1);
	}
	
	@Override
	public void renderGUI(Screen screen)
	{
		if(isMouseOn())
		{
			String name = TextLoader.getText("npc_axar");
			screen.renderFont(name, getX() + 128 - (Font.getTextWidth(name, 32) / 2), getY() + 16, 32, Font.COLOR_WHITE, true);
		}
	}
	
	@Override
	public boolean inCollisionSpace()
	{
		return true;
	}
	
	@Override
	public Hitbox getHitbox()
	{
		return HitboxFactory.create(getX() + 128 - 40, getY() + 128 + 32, 80, 32);
	}
	
	@Override
	public Hitbox getClickHitbox()
	{
		return HitboxFactory.create(getX() + 64, getY() + 64, 128, 128);
	}
	
	@Override
	public boolean doBlock(GameObject o)
	{
		return o instanceof Player;
	}
	
	@Override
	public void update(float tpf)
	{
		super.update(tpf);
		
		changeWalkTimer.count(tpf);
		resetImage();
	}

}
