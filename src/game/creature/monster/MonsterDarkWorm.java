package game.creature.monster;

import java.util.ArrayList;

import game.levels.Level;

public class MonsterDarkWorm 
extends Monster {
	
	private ArrayList<MonsterDarkWormPart> body = new ArrayList<>();
	
	public MonsterDarkWorm()
	{
		
	}
	
	@Override
	public void init(Level level)
	{
		super.init(level);
		
		for(int i = 0; i < 50; i++)
		{
			MonsterDarkWormPart part = new MonsterDarkWormPart();
			part.setPosition(getX(), getY() + i * 64);
			
			if(!body.isEmpty()) 
			{
				MonsterDarkWormPart lastPart = body.get(body.size() - 1);
				part.setParent(lastPart);
				lastPart.setChild(part);
			}
			
			level.addObject(part);
			body.add(part);
		}
	}

}
