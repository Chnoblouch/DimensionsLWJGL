package game.creature.monster;

import java.util.Random;

import org.lwjgl.util.vector.Vector2f;

import engine.gfx.Font;
import engine.gfx.Sprite;
import engine.gfx.Sprites;
import engine.rendering.Screen;
import game.GameObject;
import game.TextLoader;
import game.creature.Player;
import game.environment.BlueStone;
import game.environment.DarkSpike;
import game.environment.RedStone;
import game.environment.Stone;
import game.environment.Tree;
import game.item.creaturedrops.ItemSlime;
import game.obj.Drop;
import game.particle.BossParticle;
import game.particle.ParticleDestroying;
import game.projectiles.Slimeball;
import utils.Angles;
import utils.Block;
import utils.Hitbox;
import utils.HitboxFactory;
import utils.TimeCounter;

public class MonsterGiantSlime 
extends Monster {
	
	private TimeCounter standTimer;
	private boolean jump = false;
	private float jumpY, jumpMotion = 0;
	private float speed = 24;
	
	private boolean shootInCircle;
	private float circleAngle;
	private TimeCounter shootInCircleTimer;
	
	private Player target;
	
	private float angle = 0;
	
	private TimeCounter shootTimer;
	private TimeCounter smallSlimeTimer;
	private TimeCounter startShootingInCircleTimer;
	
	private TimeCounter particleTimer;
	
	public MonsterGiantSlime()
	{
		setMaxHealth(500);
		setHealth(500);
		setInvulnerableTime(1500);
		
		standTimer = new TimeCounter(1000, () -> 
		{
			jump = true;
			jumpMotion = -24;
			jumpY = jumpMotion;
			angle = Angles.getAngle(getHitbox().center(), target.getHitbox().center());
			standTimer.reset();
		});
		
		shootTimer = new TimeCounter(2000, () -> 
		{
			shootSlimeball();
			shootTimer.reset();
		});
		
		smallSlimeTimer = new TimeCounter(5000, () -> 
		{
			MonsterSlime slime = new MonsterSlime();
			slime.setPosition(getHitbox().center().x + new Random().nextInt(256 + 1), getHitbox().center().y + new Random().nextInt(256 + 1));
			slime.giantJump(angle);
			level.addObject(slime);
			
			smallSlimeTimer.reset();
		});
		
		startShootingInCircleTimer = new TimeCounter(7500, () ->
		{
			shootInCircle = true;
			startShootingInCircleTimer.reset();
		});
		
		shootInCircleTimer = new TimeCounter(75, () ->
		{
			shootSlimeball(circleAngle);
			circleAngle += 20;
			
			if(circleAngle > 360)
			{
				shootInCircle = false;
				circleAngle = 0;
				startShootingInCircleTimer.reset();
			}
			
			shootInCircleTimer.reset();
		});
		
		particleTimer = new TimeCounter(500, () -> 
		{
			for(int i = 0; i < 10; i++)
			{
				BossParticle particle = new BossParticle();
				particle.setTarget(this);
				level.addObject(particle);
			}
			
			particleTimer.reset();
		});
	}
	
	@Override
	public int getZIndex()
	{
		return (int) (getY() + 512 - 1);
	}
	
	@Override
	public boolean inCollisionSpace()
	{
		return false;
	}
	
	@Override
	public boolean updateOutside()
	{
		return true;
	}
	
	@Override
	public Hitbox getHitbox()
	{
		return HitboxFactory.create(getX(), getY() + 256, 512, 256);
	}
	
	@Override
	public Hitbox getClickHitbox()
	{
		return HitboxFactory.create(getX(), getY() + 256, 512, 256);
	}
	
	@Override
	public void render(Screen screen)
	{
		if(screen.isInside(getX(), getY(), 1024, 512))
		{
			float ss = jump ? 56 : 64;
			
			Sprite sprite = Sprites.giantSlime.getSprite(jump ? 128 : 0, 0, 128, 128);
						
//			screen.render(SpriteSheet.shadows.getSprite(16, 0, 32, 16), getX() + 48 - ss, getY() + 128 - ss, ss * 2, ss, 0, jump ? 0.5 : 0.75);
//			screen.render(SpriteFilter.getShadowStanding(sprite), getX() - jumpY, getY() + jumpY, 1024, 512, 0, 0.25); // add back in
			screen.render(sprite, getX(), getY() + jumpY, 512, 512, 0, invulnerableInvisible ? 0.5f : 1);
		}
	}
	
	@Override
	public void renderGUI(Screen screen)
	{
		if(isMouseOn())
		{
			String name = TextLoader.getText("monster_giant_slime");
			screen.renderFont(name, getX() + 256 - (Font.getTextWidth(name, 32) / 2), getY() + jumpY, 32, Font.COLOR_WHITE, true);
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
		for(int i = 0; i < 4; i++)
		{
			ParticleDestroying particle = new ParticleDestroying();
			particle.setSprite(0, 0);
			particle.setPosition(getHitbox().center().x, getHitbox().center().y);
			level.addObject(particle);
		}
	}
	
	@Override
	public void die()
	{
		level.removeObject(this);
		level.monsterCounter --;
		
		for(int i = 0; i < 500; i++)
		{
			ParticleDestroying particle = new ParticleDestroying();
			particle.setSprite(0, 0);
			particle.setPosition(getHitbox().center().x + new Random().nextInt(256 + 1), getHitbox().center().y + new Random().nextInt(256 + 1));
			level.addObject(particle);
		}
		
		for(int i = 0; i < 10; i++)
		{
			MonsterSlime slime = new MonsterSlime();
			slime.setPosition(getHitbox().center().x + new Random().nextInt(256 + 1), getHitbox().center().y + new Random().nextInt(256 + 1));
			slime.giantJump(new Random().nextInt(360));
			level.addObject(slime);
		}
		
		for(int i = 0; i < 1 + new Random().nextInt(3); i++)
		{
			Drop drop = new Drop(new ItemSlime(), 1);
			drop.setPosition(getX() + (new Random().nextInt(Block.SIZE)), getY() + (new Random().nextInt(Block.SIZE)));
			level.addObject(drop);
		}
	}
	
	public void shootSlimeball()
	{
		Slimeball slimeball = new Slimeball();
		slimeball.setPosition(getX() + 128 - 64, getY() + 256 - 64);
		slimeball.setAngle(slimeball.getAngle(target));
		level.addObject(slimeball);
	}
	
	public void shootSlimeball(float angle)
	{
		Slimeball slimeball = new Slimeball();
		slimeball.setPosition(getX() + 128 + 64, getY() + 256 + 64);
		slimeball.setAngle(angle);
		level.addObject(slimeball);
	}
	
	@Override
	public void update(float tpf)
	{
		super.update(tpf);
		
		target = level.player;
		
		if(!shootInCircle)
		{
			shootTimer.count(tpf);
			startShootingInCircleTimer.count(tpf);
			smallSlimeTimer.count(tpf);
			particleTimer.count(tpf);
		} else shootInCircleTimer.count(tpf);
	
		if(target != null)
		{
			if(!target.rideOnDragon && target.isInRange(this) && collides(target)) 
			{
				target.damage(32, this);
				target.knockback(getAngle(target), 48);
			}
			
			for(int i = 0; i < level.objects.size(); i++)
			{
				GameObject o = level.objects.get(i);
				if(isInRange(o) && collides(o))
				{
					if(o instanceof Tree) ((Tree) o).destroy();
					else if(o instanceof Stone) ((Stone) o).destroy();
					else if(o instanceof RedStone) ((RedStone) o).destroy();
					else if(o instanceof BlueStone) ((BlueStone) o).destroy();
					else if(o instanceof DarkSpike) ((DarkSpike) o).destroy();
				}
			}
			
			if(jump)
			{
				Vector2f moveDir = Angles.getMoveDirection(angle);
				move(moveDir.x * speed * tpf, moveDir.y * speed * tpf);
				
				jumpY += jumpMotion * tpf;
				
				jumpMotion += 3 * tpf;
				if(jumpY >= 0)
				{
					jumpY = 0;
					jumpMotion = 0;
					jump = false;
				}
				
			} else 
			{
				if(!shootInCircle) standTimer.count(tpf);
			}
		}
	}
	
	@Override
	public boolean doBlock(GameObject o)
	{
		return o instanceof MonsterGiantSlime;
	}

}