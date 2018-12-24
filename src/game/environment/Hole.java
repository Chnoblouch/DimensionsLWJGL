package game.environment;

import engine.gfx.Sprites;
import engine.rendering.Screen;
import game.GameObject;
import utils.Block;
import utils.Hitbox;
import utils.HitboxFactory;
import utils.TimeCounter;

public class Hole
extends GameObject {
		
	public Hole right, left, top, bottom;
	private boolean water = false;
	
	private TimeCounter flowTimer;
	
	public Hole()
	{
		flowTimer = new TimeCounter(1000, () -> 
		{
			if(right != null && !right.isWater()) right.setWater(true);
			if(left != null && !left.isWater()) left.setWater(true);
			if(top != null && !top.isWater()) top.setWater(true);
			if(bottom != null && !bottom.isWater()) bottom.setWater(true);
			
			flowTimer.reset();
		});
	}
	
	@Override
	public int getZIndex()
	{
		return 1;
	}
	
	@Override
	public void render(Screen screen)
	{
		if(screen.isInside(getX(), getY(), Block.SIZE, Block.SIZE)) 
		{
			screen.render(Sprites.overworld.getSprite(0, 0, 24, 24), getX(), getY(), Block.SIZE, Block.SIZE, 0, 1);
			
			int sx = 0;
			int sy = 0;
			
			if(right != null && left == null) sx = 24;
			if(right == null && left != null) sx = 48;
			if(right != null && left != null) sx = 72;
			
			if(top != null && bottom == null) sy = 24;
			if(top == null && bottom != null) sy = 48;
			if(top != null && bottom != null) sy = 72;
			
			screen.render(Sprites.hole.getSprite(sx, sy, 24, 24), getX(), getY(), Block.SIZE, Block.SIZE, 0, 1);
			if(water) screen.render(Sprites.water4.getSprite(sx, sy, 24, 24), getX(), getY(), Block.SIZE, Block.SIZE, 0, 1);
		}
	}
	
	@Override
	public boolean inCollisionSpace()
	{
		return true;
	}
	
	@Override
	public boolean doBlock(GameObject o)
	{
		return true;
	}
	
	@Override
	public Hitbox getHitbox()
	{
		return HitboxFactory.create(getX(), getY(), Block.SIZE, Block.SIZE);
	}
	
	@Override
	public boolean doSave()
	{
		return true;
	}
	
	@Override
	public void update(float tpf)
	{
		if(water) flowTimer.count(tpf);
	}
	
	public void findNeighbors()
	{
		for(int i = 0; i < level.objects.size(); i++)
		{
			GameObject o = level.objects.get(i);
			
			if(o instanceof Hole)
			{
				if(o.getX() == getX() + Block.SIZE && o.getY() == getY()) 
				{
					right = (Hole) o;
					right.left = this;
				}
				
				else if(o.getX() == getX() - Block.SIZE && o.getY() == getY()) 
				{
					left = (Hole) o;
					left.right = this;
				}
				
				else if(o.getX() == getX() && o.getY() == getY() + Block.SIZE) 
				{
					bottom = (Hole) o;
					bottom.top = this;
				}
				
				else if(o.getX() == getX() && o.getY() == getY() - Block.SIZE) 
				{
					top = (Hole) o;
					top.bottom = this;
				}
			}
		}
	}
	
	public void setWater(boolean water)
	{
		this.water = water;
	}
	
	public boolean isWater()
	{
		return water;
	}
}
