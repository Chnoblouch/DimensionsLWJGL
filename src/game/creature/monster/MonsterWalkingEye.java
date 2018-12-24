package game.creature.monster;

import java.util.Random;

import engine.gfx.Font;
import engine.gfx.Sprite;
import engine.gfx.Sprites;
import engine.rendering.Screen;
import game.GameObject;
import game.TextLoader;
import game.creature.Player;
import game.item.creaturedrops.ItemEyePiece;
import game.obj.Drop;
import game.particle.ParticleDestroying;
import utils.Angles;
import utils.Hitbox;
import utils.HitboxFactory;
import utils.TimeCounter;

public class MonsterWalkingEye 
extends Monster {
	
	private int lookDir = 0;
	private float speed = 4.0f;
	
	private Player target;
	
	private float angle = 0;
	
	private TimeCounter walkTimer;
	private int walkStage;
	private int[] walkSprites = {32, 64, 32, 0, 96, 128, 96, 0};
	
	public MonsterWalkingEye()
	{
		setMaxHealth(70);
		setHealth(70);
		setInvulnerableTime(500);
		
		walkTimer = new TimeCounter(75, () -> 
		{
			walkStage ++;
			if(walkStage >= walkSprites.length) walkStage = 0;
			
			walkTimer.reset();
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
			
			Sprite sprite = Sprites.walkingEye.getSprite(sx, sy, 32, 32);
						
//			screen.render(SpriteFilter.getShadowStanding(sprite), getX(), getY(), 256, 128, 0, 0.25); // add back in
			screen.render(sprite, getX(), getY(), 128, 128, 0, invulnerableInvisible ? 0.5f : 1);
			
			renderHealthbar(screen, getX() + 64 - 48, getY() - 32);
		}
	}
	
	@Override
	public void renderGUI(Screen screen)
	{
		if(isMouseOn())
		{
			String name = TextLoader.getText("monster_walking_eye");
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
	public void getDamage(GameObject creature)
	{
		for(int i = 0; i < 4; i++)
		{
			ParticleDestroying particle = new ParticleDestroying();
			particle.setSprite(12, 0);
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
			particle.setSprite(12, 0);
			particle.setPosition(getHitbox().center().x, getHitbox().center().y);
			level.addObject(particle);
		}
		
		for(int i = 0; i < 1 + new Random().nextInt(2); i++)
		{
			Drop drop = new Drop(new ItemEyePiece(), 1);
			drop.setPosition(getX() + (new Random().nextInt(128)), getY() + (new Random().nextInt(128)));
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
			walkTimer.count(tpf);
			
			if(!target.rideOnDragon && target.isInRange(this) && collides(target))
			{
				target.damage(28, this);
				target.knockback(getAngle(target), 48.0f);
			}
			
			angle = getAngle(target);
			lookDir = Angles.getLookDir(angle);
			moveAlongAngle(angle, speed * tpf);
		}
	}
	
	@Override
	public boolean doBlock(GameObject o)
	{
		return o instanceof MonsterWalkingEye;
	}

}