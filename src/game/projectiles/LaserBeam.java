package game.projectiles;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

import org.lwjgl.util.vector.Vector2f;
import org.w3c.dom.css.Rect;

import engine.gfx.Sprites;
import engine.rendering.Screen;
import game.GameObject;
import game.creature.Player;
import utils.Angles;
import utils.Block;
import utils.Hitbox;
import utils.TimeCounter;

public class LaserBeam 
extends GameObject {
	
	private float angle = 0;
	private boolean flying = true;
	private TimeCounter disappearTimer;
	
	public float length;
	
	public LaserBeam()
	{
		disappearTimer = new TimeCounter(10000, () -> level.removeObject(this));
	}
	
	public void setTarget(Player player)
	{
		angle = Angles.getAngle(getX() + 32, getY() + 32, player.getHitbox().center().x, player.getHitbox().center().y);
//		length = getDistance(player) + Block.SIZE * 3;
	}
	
	@Override
	public Hitbox getHitbox()
	{
		Rectangle rect = new Rectangle((int) getX(), (int) getY(), 32, (int) length);
		AffineTransform transform = new AffineTransform();
		transform.rotate(Math.toRadians(angle + 180), getX() + 32, getY() + 32);
				
		return new Hitbox(transform.createTransformedShape(rect));
//		return new Hitbox(rect);
	}
	
	@Override
	public boolean updateOutside()
	{
		return true;
	}
	
	public float getAngle()
	{
		return angle;
	}
	
	public boolean isFlying()
	{
		return flying;
	}
	
	@Override
	public void update(float tpf)
	{
		if(!level.game.getScreen().isInside(getHitbox())) level.removeObject(this);
		
		for(int i = 0; i < 24; i++)
		{								
			if(collides(level.player))
			{
				level.player.damage(10, this);
				level.player.knockback(getAngle(level.player), 48);
			}
		}
		
		if(length < Block.SIZE * 24) length += 16 * tpf;
		else level.removeObject(this);
		
		disappearTimer.count(tpf);
	}
	
	@Override
	public int getZIndex()
	{
		return (int) (getY() + 64 + 24);
	}
	
	@Override
	public void render(Screen screen)
	{
//		BufferedImage shadow = SpriteFilter.getShadowStanding(SpriteFilter.instantRotating(sprite, angle));
		
//		screen.render(shadow, getX() - ((shadow.getWidth() * 2) - 64) + 24, getY() + 24,
//					  shadow.getWidth() * 4, shadow.getHeight() * 4, 0, 0.25); // add back in	
		
		for(float i = 0; i < length - 1; i += 64)
		{
			Vector2f moveDir = Angles.getMoveDirection(angle);
			float x = getX() + moveDir.x * i;
			float y = getY() + moveDir.y * i;
			screen.render(Sprites.projectiles.getSprite(100, 0, 16, 16), x, y, 64, 64, angle, 1);
		}
		
		Vector2f moveDir = Angles.getMoveDirection(angle);
		float x = getX() + moveDir.x * length;
		float y = getY() + moveDir.y * length;
		screen.render(Sprites.projectiles.getSprite(100, 0, 16, 16), x, y, 64, 64, angle, 1);
		
//		Rectangle rect = getHitbox().getRectangle();
//		screen.render(Sprites.hitbox, rect.x, rect.y, rect.width, rect.height, 0, 0.5f);
	}

}
