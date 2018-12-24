package game.creature.npc;

import java.util.Random;

import engine.gfx.Font;
import engine.gfx.Sprites;
import engine.rendering.Screen;
import game.GameObject;
import game.TextLoader;
import game.creature.Player;
import game.gui.Speech;
import game.projectiles.FlyingArrow;
import game.projectiles.FlyingCrystalSplinter;
import game.projectiles.FlyingTaykolos;
import utils.Angles;
import utils.Block;
import utils.Hitbox;
import utils.HitboxFactory;
import utils.TimeCounter;

public class NPCJessica 
extends NPC {
	
	public int sx, sy;
	
	private TimeCounter changeWalkTimer;
	
	private Speech explanation;
	
	private boolean attack = false;
	private int attackStage = 0;
	private TimeCounter attackTimer;
	private TimeCounter startAttackingTimer;
	
	private boolean angry = false;
	
	public NPCJessica()
	{
		changeWalkTimer = new TimeCounter(2000, () ->
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
		
		attackTimer = new TimeCounter(100, () ->
		{
			attackStage ++;
			if(attackStage >= 4) 
			{
				attackStage = 0;
				attack = false;
			}
			
			attackTimer.reset();
		});
		
		startAttackingTimer = new TimeCounter(1000, () -> 
		{
			attack();
			startAttackingTimer.reset();
		});
		
		explanation = new Speech(TextLoader.getText("speech_jessica_explanation"));
	}
	
	private void attack()
	{
		if(attack) return;
		
		attack = true;
		attackStage = 0;
		changeWalkTimer.reset();
		attackTimer.reset();
		
		walk = false;
		startAttackingTimer.setTime(2000 + new Random().nextInt(3000 + 1));
		
		if(getInteractionArea().intersects(level.player.getHitbox())) 
		{
			level.player.damage(20, this);
			level.player.knockback(getAngle(level.player), 48.0f);
		}
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
			if(!attack)
			{
				if(!walk) sx = 0;
				else sx = walkSprites[walkStage];
			} else sx = 320 + (attackStage * 64);
//		} else sx = 576;
	}
	
	public Hitbox getInteractionArea()
	{		
		float size = 192;
		
		float x = getHitbox().x;
		float y = getHitbox().y;
		float w = getHitbox().w;
		float h = getHitbox().h;
		
		switch (lookDir) 
		{
			case 0: return HitboxFactory.create(x + (w / 2) - (size / 2), y + h, size, size);
			case 1: return HitboxFactory.create(x + w, y + (h / 2) - (size / 2), size, size);
			case 2: return HitboxFactory.create(x + (w / 2) - (size / 2), y - size, size, size);
			case 3: return HitboxFactory.create(x - size, y + (h / 2) - (size / 2), size, size);
			default: return null;
		}
	}
	
	@Override
	public void interactWith(Player player, boolean mouseOn)
	{
		if(!mouseOn) return;
		
		if(explanation.isOver()) level.game.getTextBox().show(TextLoader.getText("speech_jessica"+new Random().nextInt(3)));
		else level.game.getTextBox().show(explanation);
	}
	
	@Override
	public void useItemOn(Player player, boolean mouseOn)
	{
		damage(player.getAttackDamage(), player);
		knockback(player.getAngle(this), 48.0f);
	}
	
	@Override
	public void getDamage(GameObject attacker)
	{
		if(level.player == attacker) 
		{
			angry = true;
			
			attack = false;
			attackStage = 0;
			changeWalkTimer.reset();
			attackTimer.reset();
		}
	}
	
	@Override
	public void render(Screen screen)
	{
//		screen.render(SpriteFilter.getShadowStanding(SpriteSheet.jessica.getSprite(sx, sy, 64, 64)), getX() - 64, getY(), 512, 256, 0, 0.25);
		// add back in
		
		screen.render(Sprites.jessica.getSprite(sx, sy, 64, 64), getX(), getY(), 256, 256, 0, invulnerableInvisible ? 0.5f : 1);
		screen.render(Sprites.ironSwordInHand.getSprite(sx, sy, 64, 64), getX(), getY(), 256, 256, 0, 1);
		
		renderHealthbar(screen, getX() + 128 - 48, getY() - 32);
	}
	
	@Override
	public void renderGUI(Screen screen)
	{
		if(isMouseOn())
		{
			String name = TextLoader.getText("npc_jessica");
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
	public boolean doBlock(GameObject o)
	{
		return o instanceof Player;
	}
	
	@Override
	public void update(float tpf)
	{
		super.update(tpf);
		
		if(!angry)
		{
			changeWalkTimer.count(tpf);
			if(!walk && !attack) startAttackingTimer.count(tpf);
		} else
		{			
			if(getDistance(level.player) > Block.SIZE * 1.5f) walk = true;
			else 
			{
				walk = false;
				if(!attack) attack();
			}
			
			speed = 8;
			
			angle = getAngle(level.player);
			lookDir = Angles.getLookDir(angle);
						
			for(int i = 0; i < level.objects.size(); i++)
			{
				GameObject o = level.objects.get(i);
				
				if((o instanceof FlyingArrow || o instanceof FlyingTaykolos || o instanceof FlyingCrystalSplinter) && 
				   isInRange(o))
				{					
					if(((o instanceof FlyingArrow && ((FlyingArrow) o).isFlying()) || !(o instanceof FlyingArrow)) && getDistance(o) < Block.SIZE * 8)
					{
						angle = 180 - getAngle(o);
						speed = 12;
					}
				}
			}
		}
		
		if(attack) attackTimer.count(tpf);
		
		resetImage();
	}

}
