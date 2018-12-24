package game.creature;

import java.util.Random;

import engine.gfx.Font;
import engine.gfx.Sprites;
import engine.rendering.Screen;
import game.GameObject;
import game.TextLoader;
import game.item.Item;
import utils.Hitbox;
import utils.HitboxFactory;
import utils.TimeCounter;

public class Horse 
extends Creature {
	
	public int lookDir;
	
	public boolean right, left, up, down, running;
	
	private TimeCounter changeRunningTimer;
	
	public boolean mounted = false;
	public Creature rider;
	
	private float speed = 16;
		
	public Horse()
	{
		setMaxHealth(40);
		setHealth(40);
		setInvulnerableTime(500);
		
		changeRunningTimer = new TimeCounter(3000, () -> 
		{
			if(!running)
			{
				right = false;
				left = false;
				up = false;
				down = false;
				
				switch (new Random().nextInt(4)) 
				{
					case 0: right = true; break;
					case 1: left = true; break;
					case 2: up = true; break;
					case 3: down = true; break;
				}
			} else
			{
				right = false;
				left = false;
				up = false;
				down = false;
			}
			
			changeRunningTimer.reset();
		});
	}
	
	@Override
	public int getZIndex()
	{
		return (int) (getY() + 384 - 1);
	}
	
	@Override
	public boolean inCollisionSpace()
	{
		return true;
	}
	
	@Override
	public Hitbox getHitbox()
	{
		return HitboxFactory.create(getX() + 256 - 64, getY() + 256, 128, 128);
	}
	
	@Override
	public Hitbox getClickHitbox()
	{
		return HitboxFactory.create(getX(), getY(), 512, 512);
	}
	
	@Override
	public void render(Screen screen)
	{
		if(screen.isInside(getX(), getY(), 128, 128))
		{	
			int sy = lookDir * 128;
			screen.render(Sprites.horse.getSprite(0, sy, 128, 128), getX(), getY(), 512, 512, 0, 1);
		}
	}
	
	@Override
	public void renderGUI(Screen screen)
	{
		if(isMouseOn())
		{
			String name = TextLoader.getText("creature_horse");
			screen.renderFont(name, getX() + 256 - (Font.getTextWidth(name, 32) / 2), getY() + 32, 32, Font.COLOR_WHITE, true);
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
		
	}
	
	@Override
	public void die()
	{
		level.removeObject(this);
		level.monsterCounter --;
	}
	
	@Override
	public void interactWith(Player player, boolean mouseOn)
	{
		if(!mouseOn) return;
		
		player.rideOnHorse(this);
	}
	
	@Override
	public void update(float tpf)
	{
		super.update(tpf);
				
		if(right) 
		{
			move(speed * tpf, 0);
			lookDir = 1;
		}
		else if(left)
		{
			move(-speed * tpf, 0);
			lookDir = 3;
		}
		
		if(down) 
		{
			move(0, speed * tpf);
			lookDir = 0;
		}
		else if(up)
		{
			move(0, -speed * tpf);
			lookDir = 2;
		}
		
		running = left || right || up || down;
		
		if(mounted) rider.horseUpdated();
		else changeRunningTimer.count(tpf);
	}
	
	@Override
	public boolean doBlock(GameObject o)
	{
		return o instanceof Horse;
	}

}