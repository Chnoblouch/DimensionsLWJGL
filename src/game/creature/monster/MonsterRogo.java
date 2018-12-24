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
import game.item.creaturedrops.ItemSlime;
import game.obj.Drop;
import game.particle.ParticleDestroying;
import utils.Angles;
import utils.Block;
import utils.Hitbox;
import utils.HitboxFactory;
import utils.TimeCounter;

public class MonsterRogo 
extends Monster {
	
	private float speed = 8;
	private Player target;	
	
	private boolean rolling = false;
	private TimeCounter startRollingTimer;
	private int rollStage = 0;
	private TimeCounter rollTimer;
	private int rollDir = 0;
	
	private float angle = 0;
	
	public MonsterRogo()
	{
		setMaxHealth(50);
		setHealth(50);
		setInvulnerableTime(500);
		
		startRollingTimer = new TimeCounter(100, () -> 
		{
			rolling = true;
			startRollingTimer.reset();
		});
		
		rollTimer = new TimeCounter(100, () -> {
			rollStage ++;
			
			if(rollStage >= 4)
			{
				rollStage = 0;
				rolling = false;
				Sounds.rogoRoll.play(0, false, getX() + (Block.SIZE / 2), getY() + (Block.SIZE / 2), 
									 level.game.getCamera().getX(), level.game.getCamera().getY());
			}
			
			rollTimer.reset();
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
		if(screen.isInside(getX(), getY(), 256, 128))
		{			
			Sprite sprite = Sprites.rogo.getSprite(rolling ? 32 + (rollStage * 32) : 0, rolling ? rollDir * 32 : 0, 32, 32);
						
//			screen.render(SpriteFilter.getShadowStanding(sprite), getX(), getY(), 256, 128, 0, 0.25); // add back in
			screen.render(sprite, getX(), getY(), 128, 128, 0, invulnerableInvisible ? 0.5f : 1);
			
			renderHealthbar(screen, getX() + 64 - 48, getY() - 16);
		}
	}
	
	@Override
	public void renderGUI(Screen screen)
	{
		if(isMouseOn())
		{
			String name = TextLoader.getText("monster_rogo");
			screen.renderFont(name, getX() + 64 - (Font.getTextWidth(name, 32) / 2), getY() - 48, 32, Font.COLOR_WHITE, true);
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
		Sounds.rogoHurt.play(4, false, getX() + (Block.SIZE / 2), getY() + (Block.SIZE / 2), 
							 level.game.getCamera().getX(), level.game.getCamera().getY());
		
		for(int i = 0; i < 4; i++)
		{
			ParticleDestroying particle = new ParticleDestroying();
			particle.setSprite(24, 0);
			particle.setPosition(getHitbox().center().x, getHitbox().center().y);
			level.addObject(particle);
		}
	}
	
	@Override
	public void die()
	{
		level.removeObject(this);
		level.monsterCounter --;
		
		Sounds.rogoDie.play(0, false, getX() + (Block.SIZE / 2), getY() + (Block.SIZE / 2), 
							level.game.getCamera().getX(), level.game.getCamera().getY());
		
		for(int i = 0; i < 16; i++)
		{
			ParticleDestroying particle = new ParticleDestroying();
			particle.setSprite(24, 0);
			particle.setPosition(getHitbox().center().x, getHitbox().center().y);
			level.addObject(particle);
		}
		
		for(int i = 0; i < 1 + new Random().nextInt(3); i++)
		{
			Drop drop = new Drop(new ItemSlime(), 1);
			drop.setPosition(getX() + (new Random().nextInt(Block.SIZE)), getY() + (new Random().nextInt(Block.SIZE)));
			level.addObject(drop);
		}
	}
	
	@Override
	public void update(float tpf)
	{
		super.update(tpf);
		
		target = level.player;
	
		if(target != null)
		{
			if(!target.rideOnDragon && target.isInRange(this) && collides(target)) 
			{
				target.damage(18, this);
				target.knockback(getAngle(target), 48);
			}
						
			if(rolling)
			{
				angle = getAngle(target);
				
				if(angle <= 180) rollDir = 0;
				else if(angle > 180) rollDir = 1;
				
//				if((angle > 315 && angle <= 360) || (angle > 0 && angle <= 45)) rollDir = 2;
//				else if(angle > 45 && angle <= 135) rollDir = 1;
//				else if(angle > 135 && angle <= 225) rollDir = 0;
//				else if(angle > 225 && angle <= 315) rollDir = 3;
				
				Vector2f moveDir = Angles.getMoveDirection(angle);
				move(moveDir.x * speed * tpf, moveDir.y * speed * tpf);
				
				rollTimer.count(tpf);
			} else startRollingTimer.count(tpf);
		}
	}
	
	@Override
	public boolean doBlock(GameObject o)
	{
		return o instanceof MonsterRogo;
	}

}