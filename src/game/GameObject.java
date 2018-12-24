package game;

import java.awt.geom.Point2D;

import org.lwjgl.util.vector.Vector2f;

import engine.rendering.Screen;
import game.creature.Player;
import game.creature.npc.NPCJessica;
import game.levels.Level;
import utils.Angles;
import utils.Hitbox;
import utils.HitboxFactory;

public class GameObject
{
	public Level level;
	private float x, y;
	public boolean mouseOn = false;
		
	public void init(Level level)
	{
		this.level = level;
		resetLanguage(level.game.getLanguage());
	}
	
	public void setPosition(float x, float y) 
	{
		this.x = x;
		this.y = y;
	}
		
	public void move(float x, float y)
	{
		if(x == 0 && y == 0) return;
		
		if(inCollisionSpace())
		{			
			float stepSize = 1.0f;
			
			if(x > 0) { for(float i = 0; i < x; i+= stepSize) { step(stepSize, 0); } }
			if(x < 0) { for(float i = 0; i > x; i-= stepSize) { step(-stepSize, 0); } }
			if(y > 0) { for(float i = 0; i < y; i+= stepSize) { step(0, stepSize); } }
			if(y < 0) { for(float i = 0; i > y; i-= stepSize) { step(0, -stepSize); } }
		} else 
		{
			this.x += x; 
			this.y += y;
		}
	}
	
	private void step(float x, float y)
	{
		for(int i = 0; i < level.collisionSpace.getObjects().size(); i++)
		{
			GameObject o = level.collisionSpace.getObjects().get(i);
						
			if(o != this && o.doBlock(this) && isInRange(o) && collides(o))
			{
				CollisionResult result = collidesSides(o);
								
				if(x > 0 && result.right) x = 0;
				if(x < 0 && result.left) x = 0;
				if(y > 0 && result.bottom) y = 0;
				if(y < 0 && result.top) y = 0;
			}
		}
				
		this.x += x;
		this.y += y;
	}
	
	public float getX() 
	{
		return x;
	}
	
	public float getY() 
	{
		return y; 
	}
	
	public boolean inCollisionSpace()
	{
		return false;
	}
		
	public boolean doBlock(GameObject other)
	{
		return false;
	}
	
	public Hitbox getHitbox()
	{
		return HitboxFactory.create(x, y, 128, 128);
	}
	
	public Hitbox getClickHitbox()
	{
		return HitboxFactory.create(x, y, 128, 128);
	}
	
	public Hitbox getUpdateHitbox()
	{
		return HitboxFactory.create(x, y, 128, 128);
	}
	
	public boolean collides(GameObject other) 
	{ 
		return getHitbox().intersects(other.getHitbox());
	}
	
	public class CollisionResult
	{
		public boolean left;
		public boolean right;
		public boolean top;
		public boolean bottom;
	}
	
	public CollisionResult collidesSides(GameObject other)
	{
		CollisionResult result = new CollisionResult();
		Hitbox hb = other.getHitbox();
		result.left = hb.intersects(getHitbox().left);
		result.top = hb.intersects(getHitbox().top);
		result.right = hb.intersects(getHitbox().right);
		result.bottom = hb.intersects(getHitbox().bottom);
		
		return result;
	}
	
	public boolean isInRange(GameObject other)
	{
		if(x == other.x && y == other.y) return true;
				
		float distanceX = x > other.x ? x - other.x : other.x - x;
		float distanceY = y > other.y ? y - other.y : other.y - y;
		
		if(distanceX < 500 && distanceY < 500) return true;
		
		return false;
	}
	
	public boolean isInside(float x, float y)
	{
		return getHitbox().getShape().contains((int) x, (int) y);
	}
	
	public boolean isMouseInside(float x, float y)
	{
		return getClickHitbox().getShape().contains((int) x, (int) y);
	}
	
	public boolean isMouseOn()
	{
		return isMouseInside(level.game.getInput().getMouseX() + level.game.getCamera().getX(), 
							 level.game.getInput().getMouseY() + level.game.getCamera().getY());
	}
	
	public float getAngle(GameObject other)
	{
		return Angles.getAngle(getHitbox().center(), other.getHitbox().center());
	}
	
	public float getAngle(Vector2f point)
	{
		return Angles.getAngle(getHitbox().center(), point);
	}
	
	public Vector2f getMoveDirection(float angle)
	{
		return Angles.getMoveDirection(angle);
	}
	
	public void moveAlongAngle(float angle, float speed)
	{
		Vector2f dir = getMoveDirection(angle);
		move(dir.x * speed, dir.y * speed);
	}
	
	public float getDistance(GameObject other)
	{
		float x = getHitbox().center().x;
		float y = getHitbox().center().y;
		float ox = other.getHitbox().center().x;
		float oy = other.getHitbox().center().y;
		float distance = (float) Point2D.distance(x, y, ox, oy);
		
		return distance;
	}
		
	public String save() { return null; }
	public void load(String data) {}
	public boolean doSave() { return false; }
	
	public void render(Screen screen) {}
	public void renderShadow(Screen screen) {}
	public void renderGUI(Screen screen) {}
	public int getZIndex() { return (int) (y + 128); }
	public void readData(int data) {}
	public void brightness(float brightness) {}
	public void useItemOn(Player player, boolean mouseOn) {}
	public void interactWith(Player player, boolean mouseOn) {}
	public void update(float tpf) {}
	public boolean doUpdate() { return true; }
	public boolean updateOutside() { return false; }
	
	public void resetLanguage(String language) {}
}
