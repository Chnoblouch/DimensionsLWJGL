package game.creature.npc;

import game.creature.Creature;
import game.creature.npc.ai.NPCAI;
import utils.TimeCounter;

public class NPC 
extends Creature {
	
	public boolean walk;
	public float angle = 0;
	public int lookDir = 0;
	public float speed = 4;
	
	public TimeCounter walkTimer;
	public int walkStage;
	public int[] walkSprites = {64, 128, 64, 0, 192, 256, 192, 0};
	
	private NPCAI ai;

	public NPC()
	{
		walkTimer = new TimeCounter(75, () -> 
		{
			walkStage ++;
			if(walkStage >= walkSprites.length) walkStage = 0;
			
			walkTimer.reset();
		});
		
		setHealth(500);
		setMaxHealth(500);
	}
	
	public void setAI(NPCAI ai)
	{
		this.ai = ai;
	}
	
	public NPCAI getAI()
	{
		return ai;
	}
	
	@Override
	public boolean doSave()
	{
		return true;
	}
	
	@Override
	public boolean updateOutside()
	{
		return true;
	}
	
	@Override
	public void update(float tpf)
	{
		super.update(tpf);
		
		if(ai != null) ai.update(tpf);
		
		if(!rideOnDragon)
		{
//			if(right) 
//			{
//				move(speed * tpf, 0);
//				lookDir = 1;
//			}
//			else if(left) 
//			{
//				move(-speed * tpf, 0);
//				lookDir = 3;
//			}
//			
//			if(up) 
//			{
//				move(0, -speed * tpf);
//				lookDir = 2;
//			}
//			else if(down) 
//			{
//				move(0, speed * tpf);
//				lookDir = 0;
//			}
			
			if(walk) moveAlongAngle(angle, speed * tpf);
		} else
		{
			setPosition(dragon.getX() + 384 - 128 + (getMoveDirection(dragon.angle).x * 32), 
						dragon.getY() + 384 - 128 + (getMoveDirection(dragon.angle).y * 32));
			
			if((dragon.angle > 315 && dragon.angle <= 360) || (dragon.angle > 0 && dragon.angle <= 45)) lookDir = 2;
			else if(dragon.angle > 45 && dragon.angle <= 135) lookDir = 1;
			else if(dragon.angle > 135 && dragon.angle <= 225) lookDir = 0;
			else if(dragon.angle > 225 && dragon.angle <= 315) lookDir = 3;
		}
		
		if(walk) walkTimer.count(tpf);
	}

}
