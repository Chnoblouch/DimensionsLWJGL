package game.environment;

import engine.gfx.Sprites;
import engine.rendering.Screen;
import game.GameObject;
import utils.Block;

public class Farmland
extends GameObject {
		
	public Farmland right, left, top, bottom;
	
	@Override
	public int getZIndex()
	{
		return 1;
	}

	@Override
	public boolean doUpdate()
	{
		return false;
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
			
			screen.render(Sprites.farmland.getSprite(sx, sy, 24, 24), getX(), getY(), Block.SIZE, Block.SIZE, 0, 1);
		}
	}
	
	@Override
	public boolean doBlock(GameObject o)
	{
		return false;
	}
	
	@Override
	public boolean doSave()
	{
		return true;
	}

	public void findNeighbors()
	{
		for(int i = 0; i < level.objects.size(); i++)
		{
			GameObject o = level.objects.get(i);
			
			if(o instanceof Farmland)
			{
				if(o.getX() == getX() + Block.SIZE && o.getY() == getY()) 
				{
					right = (Farmland) o;
					right.left = this;
				}
				
				else if(o.getX() == getX() - Block.SIZE && o.getY() == getY()) 
				{
					left = (Farmland) o;
					left.right = this;
				}
				
				else if(o.getX() == getX() && o.getY() == getY() + Block.SIZE) 
				{
					bottom = (Farmland) o;
					bottom.top = this;
				}
				
				else if(o.getX() == getX() && o.getY() == getY() - Block.SIZE) 
				{
					top = (Farmland) o;
					top.bottom = this;
				}
			}
		}
	}
}
