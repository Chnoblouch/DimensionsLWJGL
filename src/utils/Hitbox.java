package utils;

import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Area;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.w3c.dom.css.Rect;

public class Hitbox {
	
	public float x, y, w, h;
	public Shape shape;
	
	public Rectangle left, right, top, bottom;
	
	public Hitbox() {}
	public Hitbox(float x, float y, float w, float h)
	{
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		shape = new Rectangle((int) x, (int) y, (int) w, (int) h);
		
		createSides();
	}
	
	public Hitbox(float[] xp, float[] yp)
	{
		int[] xpi = new int[xp.length];
		int[] ypi = new int[yp.length];
		
		for(int i = 0; i < xp.length; i++) { xpi[i] = (int) xp[i]; }
		for(int i = 0; i < yp.length; i++) { ypi[i] = (int) yp[i]; }
		
		shape = new Polygon(xpi, ypi, xpi.length);
		
		this.x = (float) shape.getBounds().getX();
		this.y = (float) shape.getBounds().getY();
		this.w = (float) shape.getBounds().getWidth();
		this.h = (float) shape.getBounds().getHeight();
		
		createSides();
	}
	
	public Hitbox(Shape shape)
	{
		this.shape = shape;
		this.x = (float) shape.getBounds().getX();
		this.y = (float) shape.getBounds().getY();
		this.w = (float) shape.getBounds().getWidth();
		this.h = (float) shape.getBounds().getHeight();
		
		createSides();
	}
	
	private void createSides()
	{
		int cr = 8;
		
		int x = (int) this.x;
		int y = (int) this.y;
		int w = (int) this.w;
		int h = (int) this.h;
		
		left = new Rectangle(x, y + cr, cr, h - cr * 2);
		right = new Rectangle(x + w - cr, y + cr, cr, h - cr * 2);
		top = new Rectangle(x + cr, y, w - cr * 2, cr);
		bottom = new Rectangle(x + cr, y + h - cr, w - cr * 2, cr);
	}
	
	public Shape getShape()
	{
		return shape;
	}
	
	public boolean isRectangle()
	{
		return getShape() instanceof Rectangle;
	}
	
	public Rectangle getRectangle()
	{
//		return new Rectangle((int) x, (int) y, (int) w, (int) h);
		return shape.getBounds();
	}
	
	public boolean intersects(Hitbox other)
	{
		if(getShape() instanceof Rectangle && other.getShape() instanceof Rectangle)
			return x + w >= other.x && x <= other.x + other.w && y + h >= other.y && y <= other.y + other.h;
		else 
		{
			Area shape = new Area(getShape());
			shape.intersect(new Area(other.getShape()));
			
			return !shape.isEmpty();
		}
	}
	
	public boolean intersects(Rectangle rect)
	{
		if(shape instanceof Rectangle) return x + w >= rect.x && x <= rect.x + rect.width && y + h >= rect.y && y <= rect.y + rect.height;
		else return shape.intersects(rect);
	}
	
	public boolean contains(Vector2f p)
	{
		if(shape instanceof Rectangle) return p.x >= x && p.x <= x + w && p.y >= y && p.y <= y + h;
		else return shape.contains((int) p.x, (int) p.y);
	}
	
	public boolean contains(float x, float y)
	{
		if(shape instanceof Rectangle) return x >= x && x <= x + w && y >= y && y <= y + h;
		else return shape.contains((int) x, (int) y);
	}
	
	public Vector2f center()
	{
//		return new DoublePoint(x + (w / 2), y + (h / 2));
		return new Vector2f(getRectangle().x + (getRectangle().width / 2), getRectangle().y + (getRectangle().height / 2));
	}
}
