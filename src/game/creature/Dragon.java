package game.creature;

import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;

import engine.gfx.Font;
import engine.gfx.Sprite;
import engine.gfx.Sprites;
import engine.rendering.Screen;
import game.GameObject;
import game.TextLoader;
import game.environment.GrassBlock;
import game.environment.GrassGround;
import game.environment.Stone;
import game.environment.Tree;
import game.item.Item;
import game.levels.Level;
import game.particle.ParticleFire;
import game.projectiles.DragonFireball;
import utils.Angles;
import utils.Block;
import utils.Hitbox;
import utils.HitboxFactory;
import utils.TimeCounter;

public class Dragon 
extends Creature {
	
	private float speed = 12;
	public float angle = new Random().nextInt(360);
	private float turnSpeed = 0;
	public float newAngle = 0;
	private TimeCounter changeAngleTimer;
	
	private boolean followPlayer;
	
	public boolean mounted = false;
	public Creature rider;
	
	private float rotDistPos = 0;
	private float rotDistNeg = 0;
	
	private int flyStage = 0;
	private boolean wingsUp = true;
	private TimeCounter flyTimer;
	
	private int beatOfWingsCounter = 0;
	private boolean beatOfWings = true;
	private TimeCounter beatOfWingsTimer;
	
	private boolean disappointed = false;
	private TimeCounter getDisappointedTimer;
	private TimeCounter disappointedTimer;
	
	public boolean tryStopMoving = false;
	
	private boolean angry = false;
	private TimeCounter noMoreAngryTimer;
	private boolean shoot = false;
	private int shootCounter = 3;
	private TimeCounter shootTimer;
	private TimeCounter flyAroundPlayerTimer;
	
	private Vector2f home = new Vector2f();
	private boolean returnHome = false;
	private TimeCounter returnHomeTimer;
	
	private Sprite look = Sprites.dragon;
	private String name = null;
	
	private boolean flying = true;
	
	private boolean tamed = false;
	private Creature owner;
	
	public Dragon()
	{
		setMaxHealth(500);
		setHealth(500);
		setInvulnerableTime(500);
		
		home = new Vector2f(Level.CENTER, Level.CENTER);
		
		changeAngleTimer = new TimeCounter(2000, () ->
		{
			if(!followPlayer) newAngle = new Random().nextInt(360);
			changeAngleTimer.reset();
		});
		
		flyTimer = new TimeCounter(35, () -> 
		{
			flyStage += wingsUp ? 1 : -1;
			
			if(flyStage > 7) wingsUp = false;
			if(flyStage < 1) 
			{
				wingsUp = true;
				beatOfWingsCounter --;
				
				if(beatOfWingsCounter <= 0) 
				{
					flyStage = 0;
					beatOfWings = false;
				}
			}
			
			flyTimer.reset();
		});
		
		beatOfWingsTimer = new TimeCounter(3000, () ->
		{
			beatOfWings = true;
			beatOfWingsCounter = 2 + new Random().nextInt(4);
			
			beatOfWingsTimer.reset();
		});
		
		getDisappointedTimer = new TimeCounter(12000, () -> 
		{
			disappointed = true;
			if(!flying) startFlying();
			getDisappointedTimer.reset();
		});
		
		disappointedTimer = new TimeCounter(7000, () -> 
		{
			disappointed = false;
			disappointedTimer.reset();
		});
		
		shootTimer = new TimeCounter(1000, () ->
		{
			shootCounter --;
			if(shootCounter < 0)
			{
				shoot = false;
				tryStopMoving = true;
			} else shootFireball();
			
			shootTimer.reset();
		});
		
		flyAroundPlayerTimer = new TimeCounter(4000, () -> 
		{
			shoot = true;
			shootCounter = 3 + new Random().nextInt(6);
			tryStopMoving = false;
			
			flyAroundPlayerTimer.reset();
		});
		
		returnHomeTimer = new TimeCounter(20000, () ->
		{
			returnHome = true;
			followPlayer = false;
			
			returnHomeTimer.reset();
		});
		
		noMoreAngryTimer = new TimeCounter(25000, () -> 
		{
			angry = false;
			shoot = false;
			shootCounter = 3;
			shootTimer.reset();
			flyAroundPlayerTimer.reset();
			noMoreAngryTimer.reset();
		});
	}
	
	@Override
	public boolean doSave()
	{
		return true;
	}
	
	public void shootFireball()
	{
		DragonFireball fireball = new DragonFireball();
		fireball.setPosition(getX() + 384 - (Block.SIZE / 2) + (Angles.getMoveDirection(angle).x * 256), 
							 getY() + 384 - (Block.SIZE / 2) + (Angles.getMoveDirection(angle).y * 256));
		fireball.setAngle(angle);
		fireball.setDragon(this);
		level.addObject(fireball);
		
		for(int i = 0; i < 32; i++)
		{
			ParticleFire particle = new ParticleFire();
			particle.setPosition(fireball.getX() + (Block.SIZE / 2), fireball.getY() + (Block.SIZE / 2));
			particle.setAngle(angle - 45 + new Random().nextInt(91));
			level.addObject(particle);
		}
	}
	
	public void tame(Creature creature)
	{
		this.tamed = true;
		this.owner = creature;
	}
	
	public void angry()
	{
		if((tamed && owner == level.player) || mounted) return;
				
		if(!flying) startFlying();
		
		angry = true;
		followPlayer = false;
		noMoreAngryTimer.reset();
	}
	
	public boolean isAngry()
	{
		return angry;
	}
	
	public boolean canLand()
	{
		if(!flying) return false;
		
		boolean canLand = false;
		
		for(int i = 0; i < level.objects.size(); i++)
		{
			GameObject o = level.objects.get(i);
			if(isInRange(o) && getFeet().intersects(o.getHitbox())) 
			{
				if(o instanceof GrassGround)
				{
					canLand = true;
					break;
				}
				
				else if(o instanceof GrassBlock || o instanceof Tree || o instanceof Stone) return false;
			}
		}
		
		return canLand;
	}
	
	private void land()
	{
		flying = false;
		
//		followPlayer = false;
		angle = 0;
		newAngle = 0;
		turnSpeed = 0;
		returnHome = false;
		flyStage = 0;
		wingsUp = true;
		beatOfWings = false;
		beatOfWingsCounter = 0;
		shoot = false;
		shootCounter = 0;
//		disappointed = false;
		
		changeAngleTimer.reset();
		flyTimer.reset();
		beatOfWingsTimer.reset();
		returnHomeTimer.reset();
		shootTimer.reset();
//		getDisappointedTimer.reset();
//		disappointedTimer.reset();
	}
	
	public void startFlying()
	{
		flying = true;
		angle = new Random().nextInt(360);
	}
	
	public void setLook(Sprite look)
	{
		this.look = look;
	}
	
	public Sprite getLook()
	{
		return look;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return name;
	}
	
	@Override
	public int getZIndex()
	{
		return flying ? 100000 : (int) getY() + 512;
	}
	
	@Override
	public boolean updateOutside()
	{
		return true;
	}
	
	@Override
	public Hitbox getHitbox()
	{
		return HitboxFactory.create(getX() + 192, getY() + 192, 384, 384);
	}
	
	@Override
	public Hitbox getUpdateHitbox()
	{
		float s = 3072;
		return HitboxFactory.create(getX() + (768 / 2) - (s / 2), getY() + (768 / 2) - (s / 2), s, s);
	}
	
	@Override
	public Hitbox getClickHitbox()
	{
		return HitboxFactory.create(getX(), getY(), 768, 768);
	}
	
	public Hitbox getFeet()
	{
		return HitboxFactory.create(getX() + 384 - 64, getY() + 384 + 64, 128, 64);
	}
	
	@Override
	public void render(Screen screen)
	{
		if(screen.isInside(getX(), getY(), 768, 768))
		{						
//			screen.renderFont("Angle:                    "+angle, 100, 100, 32, Font.COLOR_WHITE, false);
//			screen.renderFont("New Angle:                "+newAngle, 100, 150, 32, Font.COLOR_WHITE, false);
//			screen.renderFont("Rotate Distance Positive: "+rotDistPos, 100, 200, 32, Font.COLOR_WHITE, false);
//			screen.renderFont("Rotate Distance Negative: "+rotDistNeg, 100, 250, 32, Font.COLOR_WHITE, false);
//			
//			screen.render(SpriteFilter.getShadow(sprite), getX(), jump ? getY() + 16 : getY(), 192, 96, 0, 0.25);
						
			int sx = 0, sy = 0;
			
			if(flying) sx = flyStage * 192;
			sy = flying ? 0 : 192;
			
//			if(!flying) screen.render(Sprites.getShadowStanding(sprite), getX() - 240, getY(), 1536, 768, 0, 0.25); // add back in
			screen.render(look.getSprite(sx, sy, 192, 192), getX(), getY(), 768, 768, angle, invulnerableInvisible ? 0.5f : 1);
//			
//			screen.renderFont("Health: "+getHealth(), getX(), getY() - 32, 32, Font.COLOR_WHITE, true);
		}
	}
	
	@Override
	public void renderGUI(Screen screen)
	{
//		screen.renderFont("Curious: "+followPlayer, 100, 150, 32, Font.COLOR_WHITE, false);
//		screen.renderFont("Disappointed: "+disappointed, 100, 200, 32, Font.COLOR_WHITE, false);
//		screen.renderFont("Angry: "+angry, 100, 250, 32, Font.COLOR_WHITE, false);
//		screen.renderFont("Shoot: "+shoot, 100, 300, 32, Font.COLOR_WHITE, false);
//		screen.renderFont("Speed: "+speed, 100, 350, 32, Font.COLOR_WHITE, false);
//		screen.renderFont("Return to Home: "+returnHome, 100, 400, 32, Font.COLOR_WHITE, false);
		
		if(isMouseOn())
		{
			String name = this.name == null ? TextLoader.getText("creature_dragon") : this.name;
			screen.renderFont(name, getX() + 384 - (Font.getTextWidth(name, 32) / 2), getY() + 192, 32, Font.COLOR_WHITE, true);
		}
	}
	
	@Override
	public void useItemOn(Player player, boolean mouseOn)
	{
		if(!mouseOn) return;
		
//		damage(player.getAttackDamage());
		if(flying)
		{
			if(player.inventory.getMainHandItem().getItemID() == Item.ID_WOOD_SWORD ||
			   player.inventory.getMainHandItem().getItemID() == Item.ID_STONE_SWORD || 
			   player.inventory.getMainHandItem().getItemID() == Item.ID_IRON_SWORD) 
			{
				if(!mounted) angry();
			}
		} else
		{
			if((followPlayer && !angry) || (tamed && owner == player))
			{
				if(!tamed || (tamed && owner == player))
				{
					player.tameDragon(this);
					player.rideOnDragon(this);
					if(!flying) startFlying();
				}
			}
		}
	}
	
	@Override
	public void getDamage(GameObject attacker)
	{
		if(angry) return;
		
		if(attacker instanceof Player) angry();
		else if(attacker instanceof Dragon && ((Dragon) attacker).mounted) angry();
	}
	
	@Override
	public void die()
	{
		level.removeObject(this);
	}
	
	@Override
	public void update(float tpf)
	{
		super.update(tpf);
		
		if(flying)
		{
			if(newAngle > 360) newAngle -= 360;
			else if(newAngle < 0) newAngle += 360;
			if(angle > 360) angle -= 360;
			else if(angle < 0) angle += 360;
			
			rotDistPos = angle - newAngle;
			rotDistNeg = newAngle - angle;
			
			if(angle < 180 && newAngle > 180 && (rotDistPos > 180 || rotDistPos < -180)) rotDistPos = -rotDistPos * 2;
			if(angle > 180 && newAngle < 180 && (rotDistNeg < -180 || rotDistNeg > 180)) rotDistNeg = -rotDistNeg * 2;
		
			if(rotDistPos <= rotDistNeg && turnSpeed < (mounted ? 3 : 2)) turnSpeed += 0.1 * tpf;
			else if(rotDistPos > rotDistNeg && turnSpeed > (mounted ? -3 : -2)) turnSpeed -= 0.1 * tpf;
			
			angle += turnSpeed * tpf;
			
			if(!angry)
			{
				if(!mounted)
				{
					if(!returnHome)
					{
						if(!disappointed)
						{
							if(level.game.inventory.getMainHandItem() != null && level.game.inventory.getMainHandItem().getItemID() == Item.ID_MEAT) 
							{
								followPlayer = true;
								getDisappointedTimer.count(tpf);
							}
							else followPlayer = false;
						} else 
						{
							followPlayer = false;
							disappointedTimer.count(tpf);
						}
						
						if(!followPlayer) changeAngleTimer.count(tpf);
						else
						{
							newAngle = getAngle(level.player);
							if(canLand()) land();
						}
						
						returnHomeTimer.count(tpf);
					} else
					{
						newAngle = Angles.getAngle(getHitbox().center(), home);
						if(getHitbox().contains(home)) 
						{
							returnHome = false;
							newAngle = new Random().nextInt(360);
						}
					}
				} else
				{
					newAngle = Angles.getAngle(Display.getWidth() / 2, Display.getHeight() / 2, 
											   level.game.getInput().getMouseX(), level.game.getInput().getMouseY());
				}
			} else
			{
				noMoreAngryTimer.count(tpf);
				
				newAngle = getAngle(level.player);
				
				if(shoot) shootTimer.count(tpf);
				else flyAroundPlayerTimer.count(tpf);
			}
			
			if(!angry)
			{
				if(!tryStopMoving)
				{
					if(!beatOfWings) speed = 12;
					else speed = 18;
				} else {
					if(speed > 1) speed -= 0.1 * tpf;
					else speed = 0;
				}
			} else {
				if(!tryStopMoving)
				{
					if(speed > 1) speed -= 0.1 * tpf;
					else speed = 0;	
				} else 
				{
					if(speed < 18) speed += 0.5 * tpf;
					else speed = 18;
				}
			}
					
			moveAlongAngle(angle, speed * tpf);
			
			if(tryStopMoving)
			{
				beatOfWings = true;
				beatOfWingsCounter = 1;
			}
			
			if(beatOfWings) flyTimer.count(tpf);
			else beatOfWingsTimer.count(tpf);
			
//			if(new Random().nextInt(1000) == 0) land();
		} else 
		{
			angle = 0;
			newAngle = 0;
			
			if(level.game.inventory.getMainHandItem() != null && level.game.inventory.getMainHandItem().getItemID() == Item.ID_MEAT) 
			{
				followPlayer = true;
				getDisappointedTimer.count(tpf);
			}
			else followPlayer = false;
		}
		
		if(mounted) rider.dragonUpdated();
	}
	
//	@Override
//	public boolean doBlock(GameObject o)
//	{
//		if(o instanceof Player) return true;
//		else return false;
//	}

}