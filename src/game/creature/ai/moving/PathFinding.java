package game.creature.ai.moving;

import java.util.ArrayList;
import java.util.Comparator;

import org.lwjgl.util.vector.Vector2f;

import game.GameObject;
import game.levels.Level;
import utils.Block;
import utils.Hitbox;

public class PathFinding {
	
	public static Path find(Level level, GameObject a, GameObject b)
	{
		Path p = new Path();
		Vector2f start = a.getHitbox().center();
		Vector2f lastVector = start;
		
		Hitbox tb = b.getHitbox();
		
		for(int i = 0; i < 200; i++)
		{
			ArrayList<Vector2f> directions = new ArrayList<>();
			directions.add(new Vector2f(lastVector.x + Block.SIZE, lastVector.y));
			directions.add(new Vector2f(lastVector.x - Block.SIZE, lastVector.y));
			directions.add(new Vector2f(lastVector.x, lastVector.y + Block.SIZE));
			directions.add(new Vector2f(lastVector.x, lastVector.y - Block.SIZE));
			
			for(int j = 0; j < level.collisionSpace.getObjects().size(); j++)
			{
				GameObject o = level.collisionSpace.getObjects().get(j);
				Hitbox h = o.getHitbox();
				
				if(o.doBlock(a))
				{
					for(int di = 0; di < directions.size(); di++)
					{
						Vector2f d = directions.get(di);
						if(d.x >= h.x - 32 && d.x <= h.x + h.w + 32 && d.y >= h.y - 32 && d.y <= h.y + h.h + 32)
							directions.remove(d);
					}
				}
			}
			
			directions.sort(new Comparator<Vector2f>() 
			{
				@Override
				public int compare(Vector2f va, Vector2f vb)
				{
					return (int) (Vector2f.sub(b.getHitbox().center(), va, null).length() - 
								  Vector2f.sub(b.getHitbox().center(), vb, null).length());
				}
			});
			
			if(directions.isEmpty()) break;
			
			for(int c = 0; c < directions.size(); c++)
			{
				for(int j = 0; j < p.getSteps().size(); j++)
				{
					Vector2f s = p.getSteps().get(j);
					if(s.x == directions.get(0).x && s.y == directions.get(0).y) 
					{
						directions.remove(0);
						break;
					}
				}
			}
			
			if(directions.isEmpty()) break;
			
			p.addStep(directions.get(0));
			lastVector = directions.get(0);
			
			if(lastVector.x >= tb.x - 96 && lastVector.x <= tb.x + tb.w + 96 && lastVector.y >= tb.y - 96 && lastVector.y <= tb.y + tb.h + 96)
				break;
		}
		
		p.setAngle(a.getAngle(b));
		
		return p;
	}
	
	private static float getDistance(float x1, float y1, float x2, float y2)
	{
		return (float) Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
	}
}
