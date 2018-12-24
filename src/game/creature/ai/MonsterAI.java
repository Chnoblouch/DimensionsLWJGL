package game.creature.ai;

import game.creature.Player;
import game.creature.ai.moving.PathFinding;
import game.levels.Level;

public class MonsterAI
extends AI {
	
	private float speed;

	public MonsterAI(ControlledCreature creature) 
	{
		super(creature);
	}
	
	public void setSpeed(float speed)
	{
		this.speed = speed;
	}
	
	public float getSpeed()
	{
		return speed;
	}
	
	@Override
	public void update(float tpf)
	{
		super.update(tpf);
		
		Player target = getCreature().level.player;
		move(PathFinding.find(getCreature().level, getCreature(), target), speed);
	}
	
}
