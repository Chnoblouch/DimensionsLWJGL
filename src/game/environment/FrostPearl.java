package game.environment;

import java.util.Random;

import engine.gfx.Sprites;
import engine.rendering.Screen;
import game.GameObject;
import game.creature.Player;
import game.item.resources.ItemFrostPearl;
import game.obj.Drop;
import utils.Block;
import utils.Hitbox;
import utils.HitboxFactory;

public class FrostPearl
extends GameObject {
	
	private boolean empty = false;
	
	@Override
	public int getZIndex()
	{
		return (int) (getY());
	}
	
	@Override
	public boolean doUpdate()
	{
		return false;
	}
	
	@Override
	public void render(Screen screen)
	{
		if(screen.isInside(getX(), getY(), Block.SIZE * 2, Block.SIZE)) 
		{			
			screen.render(Sprites.iceworld.getSprite(empty ? 48 : 24, 0, 24, 24), getX(), getY(), Block.SIZE, Block.SIZE, 0, 1);
		}
	}
	
	@Override
	public Hitbox getClickHitbox()
	{
		return HitboxFactory.create(getX(), getY(), Block.SIZE, Block.SIZE);
	}
	
	@Override
	public boolean doBlock(GameObject o)
	{
		return true;
	}
	
	@Override
	public void useItemOn(Player player, boolean mouseOn)
	{		
		if(empty || !mouseOn) return;
		
		empty = true;
		
		Drop drop = new Drop(new ItemFrostPearl(), 1);
		drop.setPosition(getX() + (new Random().nextInt((int) (Block.SIZE))), 
						 getY() + (new Random().nextInt((int) (Block.SIZE))));
		level.addObject(drop);
	}
	
	@Override
	public boolean doSave()
	{
		return true;
	}

}
