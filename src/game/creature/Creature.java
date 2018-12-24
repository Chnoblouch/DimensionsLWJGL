package game.creature;

import java.util.Random;

import engine.gfx.Sprites;
import engine.rendering.Screen;
import game.GameObject;
import game.environment.GrassBlock;
import game.environment.GrassGround;
import game.environment.Stone;
import game.environment.Tree;
import utils.TimeCounter;

public class Creature
extends GameObject {
	
	private float health = 0;
	private float maxHealth = 0;
	private boolean death = false;
	private int invulnerableTime;
	private boolean invulnerable = false;
	private TimeCounter invulnerableTimer;
	
	private TimeCounter invulnerableFlashTimer;
	public boolean invulnerableInvisible = false;
	
	private float knockbackAngle = 0;
	private float knockbackSpeed = 0;
	
	public boolean rideOnDragon = false;
	public Dragon dragon;
	
	public boolean rideOnHorse = false;
	public Horse horse;
	
	public Creature()
	{
		setHealth(50);
		
		invulnerableTimer = new TimeCounter(1000, () -> {
			invulnerable = false;
			invulnerableInvisible = false;
			invulnerableFlashTimer.reset();
			invulnerableTimer.reset();
		});
		
		invulnerableFlashTimer = new TimeCounter(125, () -> {
			invulnerableInvisible = !invulnerableInvisible;
			invulnerableFlashTimer.reset();
		});
	}
	
	public void setHealth(float h)
	{
		health = h;
	}
	
	public void setMaxHealth(float mx)
	{
		maxHealth = mx;
	}
	
	public float getMaxHealth()
	{
		return maxHealth;
	}
	
	public void heal(float h)
	{
		health += h;
		if(health > maxHealth) health = maxHealth;
	}
	
	public void damage(float h, GameObject attacker)
	{
		if(invulnerable) return;
		
		float finalDamage = h / (1 + getProtection() / 20);
		
		health -= finalDamage;
		invulnerable = true;
		
		if(health <= 0) 
		{
			die();
			death = true;
		}
		else getDamage(attacker);
	}
	
	public void instantDamage(float h)
	{
		health -= h;
		
		if(health <= 0) 
		{
			die();
			death = true;
		}
	}
	
	public float getHealth()
	{
		return health;
	}
	
	public void respawn()
	{
		death = false;
		setHealth(maxHealth);
	}
	
	public boolean isDeath()
	{
		return death;
	}
	
	public void setInvulnerableTime(int time)
	{
		invulnerableTime = time;
		invulnerableTimer.setTime(time);
	}
	
	public int getInvulnerableTime()
	{
		return invulnerableTime;
	}
		
	public void knockback(float angle, float speed)
	{
		if(isKnockback()) return;
		
		knockbackAngle = angle - 12 + new Random().nextInt(25);
		knockbackSpeed = speed;
	}
	
	public boolean isKnockback()
	{
		return knockbackSpeed > 0;
	}
	
	public float getProtection() 
	{
		return 0;
	}
	
	public void tameDragon(Dragon dragon)
	{
		this.dragon = dragon;
		dragon.tame(this);
	}
	
	public void rideOnDragon(Dragon dragon)
	{
		if(dragon.mounted) return;
		
		rideOnDragon = true;
		this.dragon = dragon;
		dragon.mounted = true; 
		dragon.rider = this;
	}
	
	public boolean canLeaveDragon()
	{
		if(!rideOnDragon) return false;
		
		boolean canLeave = false;
		
		for(int i = 0; i < level.objects.size(); i++)
		{
			GameObject o = level.objects.get(i);
			if(isInRange(o) && collides(o)) 
			{
				if(o instanceof GrassGround)
				{
					canLeave = true;
					break;
				}
				
				else if(o instanceof GrassBlock || o instanceof Tree || o instanceof Stone) return false;
			}
		}
		
		return canLeave;
	}
	
	public void leaveDragon()
	{
		rideOnDragon = false;
		dragon.mounted = false;
		dragon.rider = null;
	}
	
	public void rideOnHorse(Horse horse)
	{
		rideOnHorse = true;
		this.horse = horse;
		horse.mounted = true;
		horse.rider = this;
	}
	
	public void leaveHorse()
	{
		rideOnHorse = false;
		horse.mounted = false;
		horse.rider = null;
	}
	
	protected void renderHealthbar(Screen screen, float x, float y)
	{
		if(health < maxHealth) 
		{
			float maxStages = 22;
			int stage = 0;
			
			stage = (int) Math.floor((maxStages - (maxStages / (maxHealth / health))));
						
			screen.render(Sprites.bars.getSprite(54, stage * 4, 24, 4), x, y, 96, 16, 0, 1);
		}
	}
	
	@Override
	public void update(float tpf)
	{
		if(invulnerable) 
		{
			invulnerableTimer.count(tpf);
			invulnerableFlashTimer.count(tpf);
		}
		
		if(knockbackSpeed != 0)
		{
			moveAlongAngle(knockbackAngle, knockbackSpeed * tpf);
			
			if(knockbackSpeed >= 1) knockbackSpeed -= 4.0f * tpf;
			else knockbackSpeed = 0;
		} 
	}
	
	public void die() {}
	public void getDamage(GameObject attacker) {}

	public void dragonUpdated() {}
	public void horseUpdated() {}
}
