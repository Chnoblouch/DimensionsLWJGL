package game.creature.monster;

import java.util.Random;

import org.lwjgl.util.vector.Vector2f;

import engine.gfx.Font;
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

public class MonsterSlime 
extends Monster {
	
	private TimeCounter standTimer;
	private boolean jump = false;
	private float jumpY, jumpMotion = 0;
	private float speed = 12.0f;
	
	private Player target;
	
	private float angle = 0;
	
	public MonsterSlime()
	{
		setMaxHealth(40);
		setHealth(40);
		setInvulnerableTime(500);
		
		standTimer = new TimeCounter(1000, () -> 
		{
			jump();
			standTimer.reset();
		});
	}
	
	private void jump()
	{
		jump = true;
		jumpMotion = -24.0f;
		jumpY = jumpMotion;
		angle = Angles.getAngle(getHitbox().center(), target.getHitbox().center());
		standTimer.reset();
	}
		
	public void giantJump(float angle)
	{
		this.angle = angle;
		jump = true;
		jumpMotion = -64;
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
		if(screen.isInside(getX(), getY(), 192, 192))
		{									
//			screen.render(SpriteFilter.getShadowStanding(sprite), getX() - jumpY, getY() + jumpY, 192, 96, 0, 0.25); // add back in
			screen.render(Sprites.slime.getSprite(jump ? 24 : 0, 0, 24, 24), getX(), getY() + jumpY, 96, 96, 0, invulnerableInvisible ? 0.5f : 1);
			
			renderHealthbar(screen, getX(), getY() + jumpY);
		}
	}
	
	@Override
	public void renderGUI(Screen screen)
	{
		if(isMouseOn())
		{
			String name = TextLoader.getText("monster_slime");
			screen.renderFont(name, getX() + 48 - (Font.getTextWidth(name, 32) / 2), getY() + jumpY - 32, 32, Font.COLOR_WHITE, true);
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
		
		for(int i = 0; i < 16; i++)
		{
			ParticleDestroying particle = new ParticleDestroying();
			particle.setSprite(0, 0);
			particle.setPosition(getHitbox().center().x, getHitbox().center().y);
			level.addObject(particle);
		}
		
		for(int i = 0; i < 3 + new Random().nextInt(3); i++)
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
				target.damage(15, this);
				target.knockback(getAngle(target), 48.0f);
			}
			
			if(jump)
			{
				Vector2f moveDir = Angles.getMoveDirection(angle);
				move(moveDir.x * speed * tpf, moveDir.y * speed * tpf);
				
				jumpY += jumpMotion * tpf;
				jumpMotion += 3.0f * tpf;
								
				if(jumpY >= 0.0f)
				{
					jumpY = 0;
					jumpMotion = 0;
					jump = false;
				}
				
			} else standTimer.count(tpf);
		}
	}
	
	@Override
	public boolean doBlock(GameObject o)
	{
		return o instanceof MonsterSlime;
	}

	@Override
	public String save()
	{
		return ""+getHealth();
	}
	
	@Override
	public void load(String data)
	{
		setHealth(Float.parseFloat(data));
	}
	
	@Override
	public boolean doSave()
	{
		return true;
	}
}