package game.creature.npc;

import java.util.Random;

import engine.gfx.Font;
import engine.gfx.Sprites;
import engine.rendering.Screen;
import game.GameObject;
import game.TextLoader;
import game.creature.Player;
import game.creature.npc.ai.NPCAnyravaAI;
import game.gui.Speech;
import utils.Hitbox;
import utils.HitboxFactory;
import utils.TimeCounter;

public class NPCAnyrava 
extends NPC {
	
	public int sx, sy;
	
	private TimeCounter changeWalkTimer;
	
	private boolean seePlayerFirstTime = true;
	
	private Speech classic;
	private Speech greetings;
	private Speech speechWarning;
	private Speech speechAngry;
	private Speech explanation;
	
	private float warningDragonHealth;
	
	private boolean warning = false;
	private boolean angry = false;
	
	public NPCAnyrava()
	{
		setAI(new NPCAnyravaAI(this));
				
		changeWalkTimer = new TimeCounter(2500, () ->
		{
			if(walk) walk = false;
			else
			{
				walk = true;
				
				lookDir = new Random().nextInt(4);
								
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
		
		speechWarning = new Speech(TextLoader.getText("speech_anyrava_warning"));
		speechAngry = new Speech(TextLoader.getText("speech_anyrava_angry"));
		explanation = new Speech(TextLoader.getText("speech_anyrava_explanation"));
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
//				if(!walk) sx = 0;
//				else sx = walkSprites[walkStage];
//			} else sx = 320 + (interactStage * 64);
//		} else sx = 576;
		
		if(!rideOnDragon)
		{
			if(!walk) sx = 0;
			else sx = walkSprites[walkStage];
		} else sx = 576;
	}
	
	@Override
	public void interactWith(Player player, boolean mouseOn)
	{
		if(!mouseOn) return;
				
		classic = new Speech(TextLoader.getText("speech_anyrava"+new Random().nextInt(3)));
		level.game.getTextBox().show(classic);
	}
	
	@Override
	public void render(Screen screen)
	{
//		screen.render(SpriteFilter.getShadowStanding(SpriteSheet.anyrava.getSprite(sx, sy, 64, 64)), getX() - 64, getY(), 512, 256, 0, 0.25);
		// add back in
		screen.render(Sprites.anyrava.getSprite(sx, sy, 64, 64), getX(), getY(), 256, 256, 0, invulnerableInvisible ? 0.5f : 1);
	}
	
	@Override
	public void renderGUI(Screen screen)
	{
		if(isMouseOn())
		{
			String name = TextLoader.getText("npc_anyrava");
			String part1 = name.split("/nl")[0];
			String part2 = name.split("/nl")[1];
			
			screen.renderFont(part1, getX() + 128 - (Font.getTextWidth(part1, 32) / 2), getY() - 16, 32, Font.COLOR_WHITE, true);
			screen.renderFont(part2, getX() + 128 - (Font.getTextWidth(part2, 32) / 2), getY() + 16, 32, Font.COLOR_WHITE, true);
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
	public boolean updateOutside()
	{
		return true;
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
		
		if(level.getID() == 2)
		{
			if(!warning && dragon.getHealth() != dragon.getMaxHealth())
			{
				warning = true;
				warningDragonHealth = dragon.getHealth();
				level.game.getTextBox().show(speechWarning);
			}
			
			if(warning && !angry && dragon.getHealth() != warningDragonHealth)
			{
				angry = true;
				level.game.getTextBox().show(speechAngry);
			}
			
			if(!angry)
			{
				if(seePlayerFirstTime)
				{
					seePlayerFirstTime = false;
					
					greetings = new Speech(TextLoader.getText("speech_anyrava_greetings"));
					level.game.getTextBox().show(greetings);
								
				} else if(!seePlayerFirstTime && level.game.getScreen().isInside(getClickHitbox()) && rideOnDragon)
				{
					dragon.tryStopMoving = true;
					dragon.newAngle = getAngle(level.player);
					
					if(greetings.isOver())
					{
						dragon.tryStopMoving = false;
						if(canLeaveDragon()) leaveDragon();
						
						if(!rideOnDragon) level.game.getTextBox().show(explanation);
					}
				}
			}
		}
	}

}
