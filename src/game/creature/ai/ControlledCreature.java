package game.creature.ai;

import game.creature.Creature;

public class ControlledCreature 
extends Creature {
	
	private AI ai;
	
	public void setAI(AI ai)
	{
		this.ai = ai;
	}
	
	public AI getAI()
	{
		return ai;
	}

	@Override
	public void update(float tpf)
	{
		super.update(tpf);
		if(ai != null) ai.update(tpf);
	}
}
