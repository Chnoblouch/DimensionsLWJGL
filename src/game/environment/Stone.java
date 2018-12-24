package game.environment;

import java.util.Random;

import engine.gfx.Sounds;
import engine.gfx.Sprite;
import engine.gfx.Sprites;
import engine.rendering.Screen;
import game.GameObject;
import game.creature.Player;
import game.item.resources.ItemStone;
import game.obj.Drop;
import game.particle.ParticleDestroying;
import utils.Block;
import utils.Hitbox;
import utils.HitboxFactory;

public class Stone
extends GameObject {
		
	private double health = 30;
	
	@Override
	public int getZIndex()
	{
		return (int) (getY() + Block.SIZE);
	}
	
	@Override
	public boolean doUpdate()
	{
		return false;
	}
	
	@Override
	public void render(Screen screen)
	{
		if(screen.isInside(getX(), getY(), Block.SIZE * 3, Block.SIZE * 2)) 
		{			
//			screen.render(SpriteFilter.getShadowStanding(SpriteSheet.overworld.getSprite(0, 72, 32, 24)), 
//					      getX(), getY(), Block.SIZE * 3, Block.SIZE, 0, 0.25); // add back in
//			screen.render(Sprites.overworld.getSprite(216, 72, 32, 24), getX(), getY(), Block.SIZE * 1.5f, Block.SIZE, 0, 1);
			
			boolean pickaxe = !level.player.inventory.getMainHandSlot().isEmpty() && level.player.inventory.getMainHandItem().getStoneDamage() > 1;
			boolean highlight = pickaxe && isMouseOn() && level.player.getInteractionArea().intersects(getHitbox());
			Sprite base = Sprites.overworld.getSprite(216, 72, 32, 24);
			Sprite sprite = highlight ? base.clone().setOverlayColor(1, 1, 1, 0.125f) : base;
			screen.render(sprite, getX(), getY(), Block.SIZE * 1.5f, Block.SIZE, 0, 1);
		}
	}
	
	@Override
	public boolean inCollisionSpace()
	{
		return true;
	}
	
	@Override
	public Hitbox getHitbox()
	{
		return HitboxFactory.create(getX(), getY() + (Block.SIZE / 2), Block.SIZE * 1.5f, Block.SIZE / 2);
	}
	
	@Override
	public Hitbox getClickHitbox()
	{
		return HitboxFactory.create(getX(), getY(), Block.SIZE * 1.5f, Block.SIZE);
	}
	
	@Override
	public boolean doBlock(GameObject o)
	{
		return true;
	}
	
	@Override
	public void useItemOn(Player player, boolean mouseOn)
	{
		if(!mouseOn) return;
		
		health -= player.getStoneDamage();
		
		if(health <= 0) destroy();
		else 
		{
			for(int i = 0; i < 6; i++)
			{
				ParticleDestroying particle = new ParticleDestroying();
				particle.setSprite(8, 0);
				particle.setPosition(getHitbox().center().x, getHitbox().center().y);
				level.addObject(particle);
			}
			
			Sounds.stone.play(0, false, getX() + (Block.SIZE * 0.75), getY() + (Block.SIZE / 2), 
							  level.game.getCamera().getX(), level.game.getCamera().getY());
		}
	}
	
	public void destroy()
	{
		for(int i = 0; i < 4 + new Random().nextInt(5); i++)
		{
			Drop drop = new Drop(new ItemStone(), 1);
			drop.setPosition(getX() + (new Random().nextInt((int) (Block.SIZE * 1.5f))), 
							 getY() + (new Random().nextInt((int) (Block.SIZE * 1.5f))));
			level.addObject(drop);
		}
		
		for(int i = 0; i < 24; i++)
		{
			ParticleDestroying particle = new ParticleDestroying();
			particle.setSprite(8, 0);
			particle.setPosition(getHitbox().center().x, getHitbox().center().y);
			level.addObject(particle);
		}
		
		level.removeObject(this);
		level.collisionSpace.remove(this);
		
		Sounds.stoneDestroyed.play(0, false, getX() + (Block.SIZE * 0.75), getY() + (Block.SIZE / 2), 
								   level.game.getCamera().getX(), level.game.getCamera().getY());
	}
	
	@Override
	public boolean doSave()
	{
		return true;
	}

}
