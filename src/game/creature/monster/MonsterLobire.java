package game.creature.monster;

import java.util.Random;

import org.lwjgl.util.vector.Vector2f;

import engine.gfx.Font;
import engine.gfx.Sounds;
import engine.gfx.Sprite;
import engine.gfx.Sprites;
import engine.rendering.Screen;
import game.GameObject;
import game.TextLoader;
import game.creature.Player;
import game.creature.ai.MonsterAI;
import game.item.creaturedrops.ItemFeather;
import game.item.food.ItemMeat;
import game.item.helmets.ItemLobireMask;
import game.obj.Drop;
import game.particle.ParticleFeather;
import game.projectiles.LaserBeam;
import game.projectiles.Slimeball;
import utils.Angles;
import utils.Block;
import utils.Hitbox;
import utils.HitboxFactory;
import utils.TimeCounter;

public class MonsterLobire 
extends Monster {
	
	private int lookDir = 0;
	private float speed = 4.0f;
	
	private Player target;
	
	private float angle = 0;
	
	private TimeCounter walkTimer;
	private int walkStage;
	private int[] walkSprites = {32, 64, 32, 0, 96, 128, 96, 0};
	
	private TimeCounter shootLaserTimer;
	
	private boolean mask = new Random().nextInt(10) == 0;
	
	public MonsterLobire()
	{
//		mask = true;
		
		setMaxHealth(60);
		setHealth(60);
		setInvulnerableTime(500);
		
		MonsterAI ai = new MonsterAI(this);
		ai.setSpeed(speed);
//		setAI(ai);
		
		walkTimer = new TimeCounter(75, () -> 
		{
			walkStage ++;
			if(walkStage >= walkSprites.length) walkStage = 0;
			
			walkTimer.reset();
		});
		
		shootLaserTimer = new TimeCounter(5000, () -> 
		{
			LaserBeam laserBeam = new LaserBeam();
			laserBeam.setPosition(getX() + 64 - 32, getY());
			laserBeam.setTarget(target);
			level.addObject(laserBeam);
						
			shootLaserTimer.reset();
		});
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
		return HitboxFactory.create(getX(), getY() + 64, 128, 64);
	}
	
	@Override
	public Hitbox getClickHitbox()
	{
		return HitboxFactory.create(getX(), getY(), 128, 128);
	}
	
	@Override
	public void render(Screen screen)
	{
		if(screen.isInside(getX(), getY(), 256, 256))
		{
			int sx = 0, sy = 0;
			
			sy = lookDir * 32;
			sx = walkSprites[walkStage];
						
//			screen.render(SpriteFilter.getShadowStanding(sprite), getX(), getY(), 256, 128, 0, 0.25); // add back in
			screen.render(Sprites.lobire.getSprite(sx, sy, 32, 32), getX(), getY(), 128, 128, 0, invulnerableInvisible ? 0.5f : 1);
			if(mask) screen.render(Sprites.lobireMask.getSprite(sx, sy, 32, 32), getX(), getY(), 128, 128, 0, 1.0f);
						
			renderHealthbar(screen, getX() + 64 - 48, getY() - 32);
			
//			if(getAI().path != null)
//			{
//				for(int i = 0; i < getAI().path.getSteps().size(); i++)
//				{
//					Vector2f target = getAI().path.getSteps().get(i);
//					
//					if(i < getAI().path.getSteps().size() - 1)
//					{
//						Vector2f next = getAI().path.getSteps().get(i + 1);
//						float rot = 0;
//						
//						if(next.getX() == target.getX() + Block.SIZE && next.getY() == target.getY()) rot = 90;
//						else if(next.getX() == target.getX() && next.getY() == target.getY() + Block.SIZE) rot = 180;
//						else if(next.getX() == target.getX() - Block.SIZE && next.getY() == target.getY()) rot = 270;
//						
//						screen.render(Sprites.hitbox.getSprite(0, 0, 24, 24), target.x - 48, target.y - 48, 96, 96, rot, 1);
//					} else
//						screen.render(Sprites.hitbox.getSprite(0, 0, 24, 24), target.x - 48, target.y - 48, 96, 96, 0, 1);
//				}
//			}
		}
	}
	
	@Override
	public void renderGUI(Screen screen)
	{
		if(isMouseOn())
		{
			String name = TextLoader.getText("monster_lobire");
			screen.renderFont(name, getX() + 64 - (Font.getTextWidth(name, 32) / 2), getY() - 64, 32, Font.COLOR_WHITE, true);
		}
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
		Sounds.lobireHurt.play(0, false, getX() + 64, getY() + 64, level.game.getCamera().getX(), level.game.getCamera().getY());
		
		for(int i = 0; i < 4; i++)
		{
			ParticleFeather particle = new ParticleFeather();
			particle.setPosition(getHitbox().center().x, getHitbox().center().y);
			level.addObject(particle);
		}
	}
	
	@Override
	public void die()
	{
		level.removeObject(this);
		level.monsterCounter --;
		
		Sounds.lobireDie.play(0, false, getX() + 64, getY() + 64, level.game.getCamera().getX(), level.game.getCamera().getY());
		
		for(int i = 0; i < 16; i++)
		{
			ParticleFeather particle = new ParticleFeather();
			particle.setPosition(getHitbox().center().x, getHitbox().center().y);
			level.addObject(particle);
		}
		
		for(int i = 0; i < 1 + new Random().nextInt(3); i++)
		{
			Drop drop = new Drop(new ItemFeather(), 1);
			drop.setPosition(getX() + (new Random().nextInt(128)), getY() + (new Random().nextInt(128)));
			level.addObject(drop);
		}
		
		for(int i = 0; i < 1 + new Random().nextInt(2); i++)
		{
			Drop drop = new Drop(new ItemMeat(), 1);
			drop.setPosition(getX() + (new Random().nextInt(128)), getY() + (new Random().nextInt(128)));
			level.addObject(drop);
		}
		
		if(mask)
		{
			Drop drop = new Drop(new ItemLobireMask(), 1);
			drop.setPosition(getX() + (new Random().nextInt(128)), getY() + (new Random().nextInt(128)));
			level.addObject(drop);
		}
	}
	
	@Override
	public float getProtection()
	{
		return mask ? 75.0f : 0.0f;
	}
	
	@Override
	public void update(float tpf)
	{
		super.update(tpf);
		
		target = level.player;
	
		if(target != null)
		{
			walkTimer.count(tpf);
			
			if(!target.rideOnDragon && target.isInRange(this) && collides(target)) 
			{
				target.damage(22, this);
				target.knockback(getAngle(target), 64.0f);
			}
						
			angle = getAngle(target);
			lookDir = Angles.getLookDir(angle);
			
			moveAlongAngle(getAngle(target), speed * tpf);
			
//			if(mask) shootLaserTimer.count(tpf);
		}
	}
	
	@Override
	public boolean doBlock(GameObject o)
	{
		return o instanceof MonsterLobire;
	}

}