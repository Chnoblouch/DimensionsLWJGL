package game.projectiles;

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
import game.particle.ParticleDestroying;
import utils.Hitbox;
import utils.HitboxFactory;
import utils.TimeCounter;

public class FlyingCrystalSplinter 
extends GameObject {
	
	private float angle = 0;
	private TimeCounter disappearTimer;
	private TimeCounter particleTimer;
	
	public FlyingCrystalSplinter()
	{
		disappearTimer = new TimeCounter(10000, () -> level.removeObject(this));
		
		particleTimer = new TimeCounter(100, () -> 
		{
			for(int i = 0; i < 3; i++)
			{
				ParticleDestroying particle = new ParticleDestroying();
				particle.setSprite(36, 0);
				particle.setPosition(getHitbox().center().x + 16, getHitbox().center().y - 16);
				level.addObject(particle);
			}
			
			particleTimer.reset();
		});
	}
	
	public void setAngle(float angle)
	{
		this.angle = angle;
	}
	
	private void destroy()
	{		
		level.removeObject(this);
		Sounds.crystalHit.play(0, false, getX() + 24, getY() + 24, level.game.getCamera().getX(), level.game.getCamera().getY());
		
		for(int i = 0; i < 8; i++)
		{
			ParticleDestroying particle = new ParticleDestroying();
			particle.setSprite(36, 0);
			particle.setPosition(getHitbox().center().x, getHitbox().center().y);
			level.addObject(particle);
		}
	}
	
	@Override
	public Hitbox getHitbox()
	{
		return HitboxFactory.create(getX(), getY() + 24, 64, 64);
	}
	
	@Override
	public boolean updateOutside()
	{
		return true;
	}
	
	@Override
	public void update(float tpf)
	{
		if(!level.game.getScreen().isInside(getHitbox())) level.removeObject(this);
		
		disappearTimer.count(tpf);
		particleTimer.count(tpf);
		
		firstLoop: for(int i = 0; i < 24; i++)
		{
			moveAlongAngle(angle, 2.0f * tpf);
							
			for(int j = 0; j < level.objects.size(); j++)
			{
				GameObject o = level.objects.get(j);
				if(isInRange(o) && collides(o))
				{
					if(o instanceof Monster || o instanceof NPC)
					{		
						level.removeObject(this);
						Sounds.crystalHit.play(0, false, getX(), getY(), level.game.getCamera().getX(), level.game.getCamera().getY());
						destroy();
						((Creature) o).damage(11, level.player);
						((Creature) o).knockback(angle, 32);
						
						break firstLoop;
					} 
					else if(o instanceof Dragon)
					{
						level.removeObject(this);
						Sounds.crystalHit.play(0, false, getX(), getY(), level.game.getCamera().getX(), level.game.getCamera().getY());
						destroy();
						((Dragon) o).damage(11, level.player);
						
						break firstLoop;
					}
					else if((o instanceof GrassBlock && level.getID() != 2) || o instanceof Tree || o instanceof Stone || o
							instanceof IronOreStone || o instanceof RedStone || o instanceof BlueStone)
					{
						destroy();
						break firstLoop;
					}
				}
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
//		BufferedImage shadow = SpriteFilter.getShadowStanding(SpriteFilter.instantRotating(sprite, angle));
		
//		screen.render(shadow, getX() - ((shadow.getWidth() * 2) - 48) + 64, getY() + 64,
//					  shadow.getWidth() * 4, shadow.getHeight() * 4, 0, 0.25); // add back in	
		screen.render(Sprites.projectiles.getSprite(88, 0, 12, 12), getX(), getY(), 48, 48, angle, 1);
	}

}
