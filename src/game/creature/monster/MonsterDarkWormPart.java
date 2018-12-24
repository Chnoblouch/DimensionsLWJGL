package game.creature.monster;

import java.util.Random;

import engine.gfx.Font;
import engine.gfx.Sprite;
import engine.gfx.Sprites;
import engine.rendering.Screen;
import game.GameObject;
import game.TextLoader;
import game.creature.Player;
import game.item.creaturedrops.ItemSlime;
import game.obj.Drop;
import game.particle.ParticleDestroying;
import utils.Block;
import utils.Hitbox;
import utils.HitboxFactory;
import utils.TimeCounter;

public class MonsterDarkWormPart 
extends Monster {
	
	private MonsterDarkWormPart parent;
	private MonsterDarkWormPart child;
	
	private float speed = 8.0f;
	
	public float angle = 0.0f;
	
	private TimeCounter timer;
	private float nextAngle;
	
	public MonsterDarkWormPart()
	{
		setMaxHealth(40);
		setHealth(40);
		setInvulnerableTime(500);
		
		timer = new TimeCounter(60, () ->
		{
			if(parent != null)
			{
				angle = nextAngle;
				nextAngle = getAngle(parent);
			} else
			{
				 angle = getAngle(level.player);
			}
			
			timer.reset();
		});
	}
	
	public void setParent(MonsterDarkWormPart parent)
	{
		this.parent = parent;
	}
	
	public void setChild(MonsterDarkWormPart child)
	{
		this.child = child;
	}
	
	@Override
	public int getZIndex()
	{
		return (int) (getY() + 64 - 1);
	}
	
	@Override
	public boolean inCollisionSpace()
	{
		return parent == null;
	}
	
	@Override
	public Hitbox getHitbox()
	{
		return HitboxFactory.create(getX(), getY(), 64, 64);
	}
	
	@Override
	public Hitbox getClickHitbox()
	{
		return HitboxFactory.create(getX(), getY(), 64, 64);
	}
	
	@Override
	public void render(Screen screen)
	{		
		Sprite sprite = Sprites.darkWorm.getSprite(0, 16, 16, 16);
		if(parent == null) sprite = Sprites.darkWorm.getSprite(0, 0, 16, 16);
		else if(child == null) sprite = Sprites.darkWorm.getSprite(0, 32, 16, 16);
		
		screen.render(sprite, getX(), getY(), 64, 64, angle, invulnerableInvisible ? 0.5f : 1.0f);
	}
	
	@Override
	public void renderGUI(Screen screen)
	{
		if(isMouseOn())
		{
			String name = TextLoader.getText("monster_slime");
			screen.renderFont(name, getX() + 48 - (Font.getTextWidth(name, 32) / 2), getY() - 32, 32, Font.COLOR_WHITE, true);
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
	public boolean updateOutside()
	{
		return true;
	}
	
	@Override
	public void update(float tpf)
	{
		super.update(tpf);
		
		timer.count(tpf);
			
		moveAlongAngle(angle, speed * tpf);
	}
}
