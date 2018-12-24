package game.creature;

import java.util.Random;

import engine.gfx.Font;
import engine.gfx.Sprite;
import engine.gfx.Sprites;
import engine.rendering.Screen;
import game.GameObject;
import game.TextLoader;
import game.creature.monster.Monster;
import game.item.creaturedrops.ItemWool;
import game.obj.Drop;
import game.particle.ParticleDestroying;
import utils.Block;
import utils.Hitbox;
import utils.HitboxFactory;
import utils.TimeCounter;

public class Fluffy 
extends Creature {
	
	private float jumpY, jumpMotion = 0;
	private float speed = 12.0f;
	
	private boolean shorn = false;
	
	private float rotation = 0;
	
	private TimeCounter shakeTimer;
	private int shakeStage = 0;
	
	private float angle;
	
	private boolean sleeping = false;
	private int sleepStage = 0;
	private TimeCounter sleepTimer;
	
	private TimeCounter changeStateTimer;
			
	public Fluffy()
	{
		setMaxHealth(40);
		setHealth(40);
		setInvulnerableTime(500);
		
		shakeTimer = new TimeCounter(150, () -> 
		{
			shakeStage = shakeStage == 0 ? 1 : 0;
			shakeTimer.reset();
		});
		
		sleepTimer = new TimeCounter(2000, () -> 
		{
			sleepStage = sleepStage == 0 ? 1 : 0;
			sleepTimer.reset();
			sleepTimer.setTime(sleepStage == 0 ? 2000 : 1500);
		});
		
		changeStateTimer = new TimeCounter(5000, () -> 
		{
			if(sleeping) wakeUp();
			else startSleeping();
			
			changeStateTimer.reset();
		});
	}
	
	private void startSleeping()
	{
		sleeping = true;
		rotation = 0;
	}
	
	private void wakeUp()
	{
		sleeping = false;
		sleepStage = 0;
		sleepTimer.reset();
	}
	
	@Override
	public int getZIndex()
	{
		return (int) (getY() + 96 - 1);
	}
	
	@Override
	public boolean inCollisionSpace()
	{
		return true;
	}
	
	@Override
	public Hitbox getHitbox()
	{
		return HitboxFactory.create(getX(), getY() + 48, 96, 48);
	}
	
	@Override
	public Hitbox getClickHitbox()
	{
		return HitboxFactory.create(getX(), getY() + 48, 96, 48);
	}
	
	@Override
	public void render(Screen screen)
	{
		if(screen.isInside(getX(), getY(), 128, 128))
		{	
			int sx = 0;
			int sy = 0;
			
			if(!sleeping) sx = 0;
			else sx = 24 + sleepStage * 24;
			
			if(!shorn) sy = 0;
			else sy = 24 + shakeStage * 24;
						
//			screen.render(Sprites.getShadowStanding(sprite), getX() - jumpY, getY() + jumpY, 182, 96, 0, 0.25); // add back in
			screen.render(Sprites.fluffy.getSprite(sx, sy, 24, 24), getX(), getY() + jumpY, 96, 96, rotation, invulnerableInvisible ? 0.5f : 1);
		}
	}
	
	@Override
	public void renderGUI(Screen screen)
	{
		if(isMouseOn())
		{
			String name = TextLoader.getText("creature_fluffy");
			screen.renderFont(name, getX() + 48 - (Font.getTextWidth(name, 32) / 2), getY() - 32, 32, Font.COLOR_WHITE, true);
		}
	}
	
	@Override
	public void useItemOn(Player player, boolean mouseOn)
	{
		damage(player.getAttackDamage(), player);
		knockback(player.getAngle(this), 48);
	}
	
	@Override
	public void getDamage(GameObject attacker)
	{
		if(!shorn)
		{
			for(int i = 0; i < 40; i++)
			{
				ParticleDestroying particle = new ParticleDestroying();
				particle.setSprite(40, 0);
				particle.setPosition(getHitbox().center().x, getHitbox().center().y);
				level.addObject(particle);
			}
			
			for(int i = 0; i < 1 + new Random().nextInt(3); i++)
			{
				Drop drop = new Drop(new ItemWool(), 1);
				drop.setPosition(getX() + (new Random().nextInt(Block.SIZE)), getY() + (new Random().nextInt(Block.SIZE)));
				level.addObject(drop);
			}
			
			shorn = true;
			rotation = 0;
			wakeUp();
		}
	}
	
	@Override
	public void die()
	{
		level.removeObject(this);
		level.monsterCounter --;
	}
	
	@Override
	public void update(float tpf)
	{
		super.update(tpf);
		
		if(!shorn && !sleeping) 
		{
			rotation += 5.0f * tpf;			
			moveAlongAngle(angle, speed * tpf);
		}
		
		if(shorn) shakeTimer.count(tpf);
		if(sleeping) sleepTimer.count(tpf);
		
		jumpY += jumpMotion * tpf;
		jumpMotion += 2.0f * tpf;
		
		if(jumpY >= 0)
		{
			jumpY = 0;
			if(!shorn && !sleeping)
			{
				angle = 180 - getAngle(level.player) - 45 + new Random().nextInt(91);
				jumpMotion = -24.0f;
			}
		}
		
		changeStateTimer.count(tpf);
	}
	
	@Override
	public boolean doBlock(GameObject o)
	{
		return o instanceof Fluffy;
	}

}