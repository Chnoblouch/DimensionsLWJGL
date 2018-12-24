package game.creature;

import java.util.Random;

import org.lwjgl.input.Keyboard;

import engine.gfx.Sounds;
import engine.gfx.Sprite;
import engine.gfx.Sprites;
import engine.rendering.Screen;
import game.Game;
import game.GameObject;
import game.creature.monster.MonsterDarkWorm;
import game.creature.monster.MonsterLobire;
import game.creature.monster.MonsterRogo;
import game.creature.monster.MonsterSlime;
import game.creature.monster.MonsterWalkingEye;
import game.effects.Effect;
import game.effects.EffectFreezing;
import game.effects.EffectHandling;
import game.gui.inventory.Inventory;
import game.gui.inventory.InventorySlotRenderable;
import game.gui.inventory.menus.InventoryMenu;
import game.item.Item;
import game.obj.Drop;
import utils.Angles;
import utils.Hitbox;
import utils.HitboxFactory;
import utils.TimeCounter;

public class Player
extends Creature {
	
	private boolean right, left, up, down, walk;
	private int lookDir = 0;
	private float speed = 12.0f;
	
	private TimeCounter walkTimer;
	private int walkStage;
	private int[] walkSprites = {64, 128, 64, 0, 192, 256, 192, 0};
	
	private boolean useItem = false;
	private int useItemStage;
	private Item usedItem;
	private TimeCounter useItemTimer;
	
	private boolean eating = false;
	private int eatStage;
	private int biteCounter = 0;
	private Item eatItem;
	private TimeCounter eatTimer;
	
	public boolean aiming = false;
	public TimeCounter aimTimer;
	public boolean shooting = false;
	public boolean canShoot = false;
	private TimeCounter shootTimer;
	
	private boolean protect = false;
	private Item protectItem;
	
	private TimeCounter freezeTimer;
	
	private float mana = 100.0f;
	
	private boolean impressed = false;
	private float exclamationMark1 = 0;
	private float exclamationMark2 = 0;
	
	public int deadTime = 0;
	
	private Game game;
	
	public Inventory inventory;
	
	public boolean thrownSomething = false;
	
	private EffectHandling effectHandling;
	
	public int sx, sy;

	public Player(Game game)
	{		
		this.game = game;
		this.inventory = game.inventory;
		
		setInvulnerableTime(1000);
		setMaxHealth(100);
		setHealth(100);
		
		effectHandling = new EffectHandling();
				
		walkTimer = new TimeCounter(75, () -> 
		{
			walkStage ++;
			if(walkStage >= walkSprites.length) walkStage = 0;
			
			walkTimer.reset();
		});
		
		useItemTimer = new TimeCounter(50, () -> 
		{
			useItemStage ++;
			if(useItemStage == 3) useItemTimer.setTime(150);
			if(useItemStage >= 4) 
			{
				useItemStage = 0;
				useItem = false;
			}
			
			useItemTimer.reset();
		});
		
		eatTimer = new TimeCounter(150, () -> 
		{
			if(eatStage == 0) eatStage = 1;
			else if(eatStage == 1) 
			{
				eatStage = 0;
				biteCounter ++;
				Sounds.eat.play(1, 0);
			}
			
			if(biteCounter >= 5)
			{
				eating = false;
				eatStage = 0;
				biteCounter = 0;
			}
			
			eatTimer.reset();
		});
		
		aimTimer = new TimeCounter(250, () -> 
		{
			canShoot = true;
			aimTimer.reset();
		});
		
		shootTimer = new TimeCounter(250, () -> 
		{
			shooting = false;
			shootTimer.reset();
		});
		
		freezeTimer = new TimeCounter(5000, () ->
		{
			instantDamage(20);
			freezeTimer.reset();
		});
				
//		effectHandling.addEffect(new EffectRegeneration(Time.secondsInTicks(10)));
	}
	
	@Override
	public boolean doSave()
	{
		return false;
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
	
	private int nextHurtSound = new Random().nextInt(3);
	
	@Override
	public void damage(float h, GameObject attacker)
	{		
		if(protect && attacker.getHitbox().intersects(getProtectionArea())) 
		{
			if(attacker instanceof Creature) ((Creature) attacker).knockback(getAngle(attacker), 30);
		}
		else super.damage(h, attacker);
	}
	
	@Override
	public void instantDamage(float h)
	{		
		super.instantDamage(h);
		damageSound();
	}
	
	@Override
	public void getDamage(GameObject attacker)
	{		
		damageSound();
	}
	
	private void damageSound()
	{
		switch (nextHurtSound) 
		{
			case 0: 
			{
				Sounds.hurt0.play(1, 0);
				nextHurtSound = new Random().nextBoolean() ? 1 : 2;
			} break;
			
			case 1:
			{
				Sounds.hurt1.play(1, 0);
				nextHurtSound = new Random().nextBoolean() ? 0 : 2;
			} break;
			
			case 2: 
			{
				Sounds.hurt2.play(1, 0);
				nextHurtSound = new Random().nextBoolean() ? 0 : 1;
			} break;
		}
	}
	
	@Override
	public void die()
	{
		if(isDeath()) return;
		
//		game.changeLevel(0);
//		setHealth(20);
		
		eating = false;
		eatStage = 0;		
		eatTimer.reset();
			
		aiming = false;
		shooting = false;
		shootTimer.reset();
		
		protect = false;
		
		Sounds.youDied.play(0, 0);
		level.game.showYouDiedScreen(); 
		level.game.inventory.setVisible(false);
		
		if(rideOnDragon) leaveDragon();
		if(rideOnHorse) leaveHorse();
		
		for(int i = 0; i < inventory.slots.size(); i++)
		{
			InventorySlotRenderable s = inventory.slots.get(i);
			
			if(!s.isEmpty())
			{
				Drop drop = new Drop(s.getItem(), s.getItemCount());
				drop.setPosition(getX() + 128 - 256 + new Random().nextInt(512 + 1), getY() + 128 - 256 + new Random().nextInt(512 + 1));
				level.addObject(drop);
				
				s.clear();
			}
		}
	}
	
	public Hitbox getInteractionArea()
	{		
		float size = 160;
		
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
	
	public Hitbox getProtectionArea()
	{
		float size = 64;
		
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
	
	private void resetImage()
	{		
		sy = lookDir * 64;
				
		if(!isDeath())
		{
			if(!rideOnDragon)
			{
				if(!rideOnHorse)
				{
					if(!aiming)
					{
						if(!shooting)
						{
							if(!protect)
							{
								if(!eating)
								{
									if(!useItem)
									{
										if(!walk) sx = 0;
										else sx = walkSprites[walkStage];
									} else sx = 320 + (useItemStage * 64);
								} else sx = 640 + (eatStage * 64);
							} else sx = 768;
						} else sx = 960;
					} else sx = 896;
				} else sx = 1024;
			} else sx = 576;
		} else sx = 832;
	}
	
	public void useItem(float x, float y, Item item)
	{
		if(useItem) return;
		
		useItem = true;
		useItemTimer.reset();
		useItemTimer.setTime(75);
		usedItem = item;
		
		for(int i = 0; i < level.objects.size(); i++)
		{
			GameObject o = level.objects.get(i);
			if(o.getHitbox().intersects(getInteractionArea()) && o != this) o.useItemOn(this, o.isMouseInside(x, y));
		} 
	}
	
	public void interact(float x, float y)
	{
		for(int i = 0; i < level.objects.size(); i++)
		{
			GameObject o = level.objects.get(i);		
			if(o.getHitbox().intersects(getInteractionArea()) && o != this) o.interactWith(this, o.isMouseInside(x, y));
		}
	}
	
	public void eat(Item item)
	{
		if(eating || protect) return;
		
		eating = true;
		eatItem = item;
		eatTimer.reset();
	}
	
	public boolean isEating()
	{
		return eating;
	}
	
	public void protect(Item item)
	{
		if(protect || eating) return;
		
		protect = true;
		protectItem = item;
	}
	
	public void stopProtecting()
	{
		protect = false;
	}
	
	public void setMana(float mana)
	{
		this.mana = mana;
		if(this.mana > 100) this.mana = 100;
	}
	
	public void addMana(float mana)
	{
		this.mana += mana;
		if(this.mana > 100) this.mana = 100;
	}
	
	public void removeMana(float mana)
	{
		this.mana -= mana;	
	}
	
	public float getMana()
	{
		return mana;
	}
	
	public void impressed()
	{
		impressed = true;
		exclamationMark1 = 1;
		exclamationMark2 = 0;
	}

	public void addEffect(Effect effect)
	{
		effectHandling.addEffect(effect);
	}
	
	public boolean hasEffect(int id)
	{
		return effectHandling.hasEffect(id);
	}
	
	public void removeEffect(Effect effect)
	{
		effectHandling.addEffect(effect);
	}
	
	public void drop(Item item, int count)
	{
		Drop drop = new Drop(item, count);
		drop.setPosition(getX() + 128 - 32, getY() + 256);
		level.addObject(drop);
	}
	
	public float getAttackDamage()
	{
		if(!inventory.getMainHandSlot().isEmpty()) return inventory.getMainHandItem().getAttackDamage();
		return 3;
	}

	public float getWoodDamage()
	{		
		if(!inventory.getMainHandSlot().isEmpty()) return inventory.getMainHandItem().getWoodDamage();
		return 1; 
	}
	
	public float getStoneDamage()
	{
		if(!inventory.getMainHandSlot().isEmpty()) return inventory.getMainHandItem().getStoneDamage();
		return 1;
	}
	
	@Override
	public float getProtection()
	{
		float protection = 0;
		if(!inventory.getHelmetSlot().isEmpty()) protection += inventory.getHelmet().getProtection();
		if(!inventory.getChestplateSlot().isEmpty()) protection += inventory.getChestplate().getProtection();
		if(!inventory.getLeggingsSlot().isEmpty()) protection += inventory.getLeggings().getProtection();
		if(!inventory.getBootsSlot().isEmpty()) protection += inventory.getBoots().getProtection();
		
		return protection;
	}
	
	public float getAngleToMouse()
	{
		return Angles.getAngle(getX() - game.getCamera().getX() + 128, 
							   getY() - game.getCamera().getY() + 128,
							   game.getInput().getMouseX(), 
							   game.getInput().getMouseY());
	}
	
	public boolean needsFreezingEffect()
	{
		return level.getID() == 3 && !(inventory.getChestplate() != null && inventory.getChestplate().getItemID() == Item.ID_WOOL_PULLOVER);
	}
	
	public EffectHandling getEffectHandling()
	{
		return effectHandling;
	}
	
	@Override
	public void render(Screen screen)
	{
//		if(!isDeath())
//			screen.render(SpriteFilter.getShadowStanding(SpriteSheet.player.getSprite(sx, sy, 64, 64)), getX() - 64, getY(), 512, 256, 0, 0.25);
//		else
//			screen.render(SpriteFilter.getShadowLaid(SpriteSheet.player.getSprite(sx, sy, 64, 64)), getX() + 16, getY() - 16, 256, 256, 0, 0.25);
//				
		if(effectHandling.hasEffect(Effect.ID_FREEZING))
		{
			Sprite base = Sprites.player.getSprite(sx, sy, 64, 64);
			Sprite sprite = base.clone().setOverlayColor(0.45f, 0.6f, 0.9f, 0.5f);
			screen.render(sprite, getX(), getY(), 256, 256, 0, invulnerableInvisible ? 0.5f : 1);
		} else screen.render(Sprites.player.getSprite(sx, sy, 64, 64), getX(), getY(), 256, 256, 0, invulnerableInvisible ? 0.5f : 1);
//		screen.renderHitbox(getHitbox());	
		
		if(impressed)
		{
			screen.render(Sprites.exclamationmarks.getSprite(0, 0, 16, 16), getX() + 168, getY() + 8, 64, 64, 20, exclamationMark1);
			screen.render(Sprites.exclamationmarks.getSprite(16, 0, 16, 16), getX() + 200, getY() + 24, 64, 64, 40, exclamationMark2);
		}
		
		if(lookDir == 0 && inventory.getBackItem() != null) inventory.getBackItem().renderOnBody(screen, this);
		if(inventory.getHelmet() != null) inventory.getHelmet().renderOnBody(screen, this);
		if(inventory.getLeggings() != null) inventory.getLeggings().renderOnBody(screen, this);
		if(inventory.getChestplate() != null) inventory.getChestplate().renderOnBody(screen, this);
		if(inventory.getBoots() != null) inventory.getBoots().renderOnBody(screen, this);
		
		if(lookDir == 3 && inventory.getBackItem() != null) inventory.getBackItem().renderOnBody(screen, this);	
		if(lookDir != 3 && inventory.getOffHandItem() != null) inventory.getOffHandItem().renderInHand(screen, this, Item.MAIN_HAND);
						
		if(!useItem && !eating && inventory.getMainHandItem() != null)
			inventory.getMainHandItem().renderInHand(screen, this, Item.MAIN_HAND);
		else if(useItem && usedItem != null)
			usedItem.renderInHand(screen, this, Item.MAIN_HAND);
		else if(eating && eatItem != null)
			eatItem.renderInHand(screen, this, Item.MAIN_HAND);
				
		if(lookDir == 3 && inventory.getOffHandItem() != null) inventory.getOffHandItem().renderInHand(screen, this, Item.MAIN_HAND);
		if((lookDir == 1 || lookDir == 2) && inventory.getBackItem() != null) inventory.getBackItem().renderOnBody(screen, this);
		
//		screen.renderHitbox(getInteractionArea());
	}
	
	@Override
	public int getZIndex()
	{
		if(rideOnDragon) return dragon.getZIndex() + 1;
		else if(rideOnHorse) return horse.getZIndex() + 1;
		else return (int) (getY() + 256 - 64 - 1);
	}
	
	private int creature = 0;
	
	public void keyPressed(int key)
	{				
		if(key == Keyboard.KEY_W) 
		{
			if(rideOnHorse) horse.up = true;
			else up = true;
		}
		
		if(key == Keyboard.KEY_A) 
		{
			if(rideOnHorse) horse.left = true;
			else left = true;
		}
		
		if(key == Keyboard.KEY_S)
		{
			if(rideOnHorse) horse.down = true;
			else down = true;
		}
		
		if(key == Keyboard.KEY_D) 
		{
			if(rideOnHorse) horse.right = true; 
			else right = true;
		}
		
		if(key == Keyboard.KEY_E && !isDeath()) inventory.changeVisibility(InventoryMenu.MENU_HAND_CRAFTING);
		if(key == Keyboard.KEY_LSHIFT && !inventory.visible)
		{
			if(rideOnDragon && canLeaveDragon()) leaveDragon();
			if(rideOnHorse) leaveHorse();
		}
		
		if(key == Keyboard.KEY_C) setPosition(level.getPlayerSpawn().x, level.getPlayerSpawn().y);
		
		if(key == Keyboard.KEY_RIGHT) creature ++;
		if(key == Keyboard.KEY_LEFT) creature --;
		
//		if(key == Keyboard.KEY_Q && !inventory.getMainHandSlot().isEmpty())
//		{
//			drop(inventory.getMainHandItem(), inventory.getMainHandSlot().getItemCount());
//			inventory.getMainHandSlot().clear();
//		}
		
		if(key == Keyboard.KEY_Q)
		{
			float xInCam = level.game.getInput().getMouseX() + level.game.getCamera().getX();
			float yInCam = level.game.getInput().getMouseY() + level.game.getCamera().getY();
			
			interact(xInCam, yInCam);
		}
		
		if(key == Keyboard.KEY_SPACE)
		{
			if(!rideOnDragon)
			{
				if(isDeath() && deadTime >= 8000) 
				{
					respawn();
					deadTime = 0;
					game.getCamera().setZoom(1);
					game.changeLevel(0);
					game.hideYouDiedScreen();
					Sounds.youDied.stop();
				}
				
				Creature c = null;
				
				switch (creature) 
				{
					case 0: c = new MonsterSlime(); break;
					case 1: c = new MonsterLobire(); break;
					case 2: c = new MonsterWalkingEye(); break;
					case 3: c = new MonsterRogo(); break;
					case 4: c = new Dragon(); break;
					case 5: c = new Fluffy(); break;
					case 6: c = new MonsterDarkWorm(); break;
					case 7: c = new Horse(); break;
				}
				
				c.setPosition(getX(), getY() + 200);
				level.addObject(c);
				
			} else dragon.shootFireball();
		}
	}
	
	public void keyReleased(int key)
	{
		if(key == Keyboard.KEY_W) 
		{
			if(rideOnHorse) horse.up = false;
			else up = false;
		}
		
		if(key == Keyboard.KEY_A) 
		{
			if(rideOnHorse) horse.left = false;
			else left = false;
		}
		
		if(key == Keyboard.KEY_S) 
		{
			if(rideOnHorse) horse.down = false;
			else down = false;
		}
		
		if(key == Keyboard.KEY_D) 
		{
			if(rideOnHorse) horse.right = false;
			else right = false;
		}
	}
	
	public void mousePressed(int button)
	{
		float xInCam = level.game.getInput().getMouseX() + level.game.getCamera().getX();
		float yInCam = level.game.getInput().getMouseY() + level.game.getCamera().getY();
		
		if(button == 0) 
		{
			if(!inventory.getMainHandSlot().isEmpty()) inventory.getMainHandItem().use(xInCam, yInCam, level, this, inventory.getMainHandSlot());
			else useItem(xInCam, yInCam, inventory.getMainHandItem());
		} else if(button == 1)
		{			
			if(!inventory.getOffHandSlot().isEmpty()) inventory.getOffHandItem().use(xInCam, yInCam, level, this, inventory.getOffHandSlot());
			else useItem(xInCam, yInCam, inventory.getOffHandItem());
		}
	}

	public void mouseReleased(int button)
	{
		float xInCam = level.game.getInput().getMouseX() + level.game.getCamera().getX();
		float yInCam = level.game.getInput().getMouseY() + level.game.getCamera().getY();
		
		if(button == 0) 
		{
			if(!inventory.getMainHandSlot().isEmpty())
				inventory.getMainHandItem().release(xInCam, yInCam, level, this, inventory.getMainHandSlot());
		} else if(button == 1)
		{			
			if(!inventory.getOffHandSlot().isEmpty()) 
				inventory.getOffHandItem().release(xInCam, yInCam, level, this, inventory.getOffHandSlot());
		}
	}
	
	@Override
	public void dragonUpdated()
	{
		setPosition(dragon.getX() + 384 - 128 + (getMoveDirection(dragon.angle).x * 32), 
				dragon.getY() + 384 - 128 + (getMoveDirection(dragon.angle).y * 32));
	
		if((dragon.angle > 315 && dragon.angle <= 360) || (dragon.angle > 0 && dragon.angle <= 45)) lookDir = 2;
		else if(dragon.angle > 45 && dragon.angle <= 135) lookDir = 1;
		else if(dragon.angle > 135 && dragon.angle <= 225) lookDir = 0;
		else if(dragon.angle > 225 && dragon.angle <= 315) lookDir = 3;
	}
	
	@Override
	public void horseUpdated()
	{
		setPosition(horse.getX() + 256 - 128, horse.getY() + 256 - 128);
		lookDir = horse.lookDir;
	}
	
	@Override
	public void update(float tpf)
	{		
		super.update(tpf);
				
		if(!isDeath())
		{
			speed = 12.0f;
			if(effectHandling.hasEffect(Effect.ID_SPEED)) speed = 18.0f;
			
			effectHandling.updateEffects(tpf);
			
			if(aiming) lookDir = Angles.getLookDir(getAngleToMouse());
			
			if(!rideOnDragon && !rideOnHorse && !eating && !aiming && !shooting)
			{
				if(right) 
				{
					if(!protect) move(speed * tpf, 0);
					lookDir = 1;
				}
				else if(left) 
				{
					if(!protect) move(-speed * tpf, 0);
					lookDir = 3;
				}
				
				if(up) 
				{
					if(!protect) move(0, -speed * tpf);
					lookDir = 2;
				}
				else if(down) 
				{
					if(!protect) move(0, speed * tpf);
					lookDir = 0;
				}
			}
									
			addMana(0.05f * tpf);
			
			if(impressed)
			{
				if(exclamationMark1 > 0) exclamationMark1 -= 0.015 * tpf;
				
				if(exclamationMark1 <= 0.95 && exclamationMark2 == 0) exclamationMark2 = 1.0f;
				if(exclamationMark1 <= 0.95 && exclamationMark2 > 0) exclamationMark2 -= 0.015 * tpf;
				
				if(exclamationMark1 <= 0.95 && exclamationMark2 <= 0) impressed = false;
			}
					
			walk = left || right || up || down;
			
			if(walk) walkTimer.count(tpf);
			if(useItem) useItemTimer.count(tpf);
			if(eating) eatTimer.count(tpf);
			if(aiming) aimTimer.count(tpf);
			if(shooting) shootTimer.count(tpf);
			
			if(needsFreezingEffect()) 
			{
				freezeTimer.count(tpf);
				addEffect(new EffectFreezing(Effect.DURATION_INFINITE));
			} else
			{
				if(effectHandling.hasEffect(Effect.ID_FREEZING)) effectHandling.removeEffect(Effect.ID_FREEZING);
			}
		} else 
		{			
			deadTime += 25 * tpf;
			
			if(game.getCamera().getZoom() > 0.4f) 
			{
				float start = 0.0f;
				float end = 8000.0f;
				float process = deadTime;
											
				process = (process - start) / (end - start);	
				if(process > 1.0) process = 1.0f;
				if(process < 0.0) process = 0.0f;
								
				process = process * process * (3.0f - 2.0f * process);
														
				game.getCamera().setZoom(1.0f - process * 0.4f);
			}
		}
		
//		level.cam.lookAt(center().x, center().y);
		
		resetImage();
	}
	
}
