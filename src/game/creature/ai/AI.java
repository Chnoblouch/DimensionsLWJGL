package game.creature.ai;

import game.creature.ai.moving.Path;

public class AI {
	
	private ControlledCreature creature;
	
	private boolean moving;
	public Path path;
	private float speed;
//	private int cantChangePathTimer = 0;
	
	public AI(ControlledCreature creature)
	{
		this.creature = creature;
	}
	
	public ControlledCreature getCreature()
	{
		return creature;
	}
	
	public void move(Path path, float speed)
	{
//		if(cantChangePathTimer > 0) return;
		
		this.path = path;
		this.speed = speed;
		moving = true;
//		cantChangePathTimer = 500;
	}
	
	public void update(float tpf) 
	{
		if(moving)
		{
//			cantChangePathTimer -= 25 * tpf;
//			if(cantChangePathTimer <= 0) cantChangePathTimer = 0;
			
			creature.moveAlongAngle(path.getAngle(), speed * tpf);
//			creature.moveAlongAngle(creature.getAngle(path.getSteps().get(0)), speed * tpf);
		}
	}

}
