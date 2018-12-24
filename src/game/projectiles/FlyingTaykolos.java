package game.projectiles;

import java.util.Random;

import engine.gfx.Sounds;
import engine.gfx.Sprites;
import engine.rendering.Screen;
import game.GameObject;
import game.creature.Creature;
import game.creature.Dragon;
import game.creature.monster.Monster;
import game.creature.npc.NPC;
import game.environment.BlueStone;
import game.environment.GrassBlock;
import game.environment.IronOreStone;
import game.environment.RedStone;
import game.environment.Stone;
import game.environment.Tree;
import game.particle.ParticleTaykolos;
import utils.Block;
import utils.Hitbox;
import utils.HitboxFactory;
import utils.TimeCounter;

public class FlyingTaykolos 
extends GameObject {
	
	private float angle = 0;
	private boolean returnToPlayer = false;
	private float rot = 0;
	private float speed = 48.0f;
	
	private TimeCounter particleTimer;
	
	private TimeCounter soundTimer;
		
	public FlyingTaykolos()
	{		
		particleTimer = new TimeCounter(50, () -> 
		{
			for(int i = 0; i < 4; i++)
			{
				ParticleTaykolos particle = new ParticleTaykolos();
				particle.setPosition(getX() + (Block.SIZE / 2), getY() + (Block.SIZE / 2));
				particle.setAngle(-angle - 45 + new Random().nextInt(91));
				level.addObject(particle);
			}
			
			particleTimer.reset();
		});
		
		soundTimer = new TimeCounter(150, () -> {
			playSound();
			soundTimer.reset();
		});
	}
	
	public void setAngle(float angle)
	{
		this.angle = angle;
	}
	
	public void playSound()
	{
		Sounds.taykolos.play(15, false, getX() + 48, getY() + 48, level.game.getCamera().getX(), level.game.getCamera().getY());
	}
	
	@Override
	public Hitbox getHitbox()
	{
		return HitboxFactory.create(getX(), getY() + 24, 96, 96);
	}
	
	@Override
	public boolean updateOutside()
	{
		return true;
	}
	
	@Override
	public void update(float tpf)
	{				
		rot += returnToPlayer ? -speed / 2.0f * tpf: speed / 2.0f * tpf;
		
		particleTimer.count(tpf);
		soundTimer.count(tpf);
		
		for(int j = 0; j < level.objects.size(); j++)
		{
			GameObject o = level.objects.get(j);
			if(isInRange(o) && collides(o))
			{
				if(o instanceof Monster || o instanceof NPC)
				{		
//					returnToPlayer = true;
					((Creature) o).damage(15, level.player);
					((Creature) o).knockback(angle, 32.0f);
					
					break;
				} 
				else if(o instanceof Dragon)
				{
//					returnToPlayer = true;
					((Dragon) o).damage(15, level.player);
				}
				else if((o instanceof GrassBlock && level.getID() != 2) || o instanceof Tree || o instanceof Stone ||
						o instanceof IronOreStone || o instanceof RedStone || o instanceof BlueStone)
				{
					returnToPlayer = true;
					break;
				}
			}
		}
		
		if(!returnToPlayer)
		{		
			moveAlongAngle(angle, speed * tpf);
			
			if(speed >= 0) speed -= 1.5f * tpf;
			else returnToPlayer = true;
		} else 
		{
			if(speed < 32.0f) speed += 1.5f * tpf;
			moveAlongAngle(getAngle(level.player), speed * tpf);
			
			if(isInRange(level.player) && collides(level.player))
			{
				level.removeObject(this);
				level.player.thrownSomething = false;
			}
		}
	}
	
	@Override
	public int getZIndex()
	{
		return (int) (getY() + 64 + 24);
	}
	
	@Override
	public void render(Screen screen)
	{
//		BufferedImage shadow = SpriteFilter.getShadowStanding(SpriteFilter.instantRotating(sprite, rot));
		
//		screen.render(shadow, getX() - ((shadow.getWidth() * 2) - 80) + 24, getY() + 24,
//					  shadow.getWidth() * 4, shadow.getHeight() * 4, 0, 0.25);	// add back in		
		screen.render(Sprites.projectiles.getSprite(16, 0, 20, 20), getX(), getY(), 80, 80, rot, 1);
	}
	
	@Override
	public void renderShadow(Screen screen)
	{
		screen.renderShadow(Sprites.projectiles.getSprite(16, 0, 20, 20), getX(), getY(), 80, 80, 0, 1);
	}

}
