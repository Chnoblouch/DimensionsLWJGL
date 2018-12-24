package game;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.Random;

import game.environment.GrassBlock;
import game.environment.GrassGround;
import game.levels.Level;
import utils.Block;

public class MapGeneratorRandomShaped {
	
	public static Map generateOverworld()
	{
		Map map = new Map();
		
		for(float x = 0; x < Level.CENTER * 2; x += Block.SIZE)
		{
			for(float y = 0; y < Level.CENTER * 2; y += Block.SIZE)
			{
				GrassGround ground = new GrassGround();
				ground.setPosition(x, y);
				map.addObject(ground);
			}
		}
		
		generateHill(map, Level.CENTER, Level.CENTER - Block.SIZE * 4);
		
		return map;
	}

	private static void generateHill(Map map, float x, float y)
	{		
		int height = 2 + new Random().nextInt(4);
		int length = 7 + new Random().nextInt(4);
		int depth = 3 + new Random().nextInt(3);
		
		int[] xPoints = {0, new Random().nextInt(10), 0};
		int[] yPoints = {0, new Random().nextInt(10), new Random().nextInt(10)};
				
		Shape shape = createRandomPolygon();
						
		for(int by = shape.getBounds().x; by <= shape.getBounds().height; by++)
		{
			for(int bx = shape.getBounds().y; bx <= shape.getBounds().width; bx++)
			{
				if(shape.contains(bx, by))
				{
					GrassBlock ground = new GrassBlock();
					ground.setPosition(x + bx * Block.SIZE, y + by * Block.SIZE);
					ground.readData(1);
					map.addObject(ground);
				}
			}
		}
	}
	
	private static Polygon createRandomPolygon()
	{
		final int angleCount = 10;
		
		ArrayList<Integer> angles = new ArrayList<>();
		int lastAngle = 0;
		
		for(int i = 0; i < angleCount; i++)
		{
			int angle = lastAngle + 360 / angleCount; 
			angles.add(angle);
			
			lastAngle = angle;
		}
		
		ArrayList<Point> points = new ArrayList<>();
		
		for(int i = 0; i < angles.size(); i++)
		{
			int angle = angles.get(i);
			
	        float x = (float) Math.cos(Math.toRadians(angle - 90)) * (4.0f + new Random().nextInt(4));
			float y = (float) Math.sin(Math.toRadians(angle - 90)) * (4.0f + new Random().nextInt(4));
			
			points.add(new Point((int) x, (int) y));
		}
		
		int[] xPoints = new int[angleCount];
		int[] yPoints = new int[angleCount];
		
		for(int i = 0; i < points.size(); i++)
		{			
			xPoints[i] = points.get(i).x;
			yPoints[i] = points.get(i).y;
		}
		
		return new Polygon(xPoints, yPoints, angleCount);
	}
}
