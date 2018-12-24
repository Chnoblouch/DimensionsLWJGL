package game.creature.npc.ai;

import game.creature.npc.NPC;

public class NPCAI {
	
	private NPC npc;
	
	public NPCAI(NPC npc)
	{
		this.npc = npc;
	}
	
	public NPC getNPC()
	{
		return npc;
	}
	
	public void update(float tpf) {}
}
