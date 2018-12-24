package game.creature.npc.ai;

import game.creature.Creature;

public class NPCRelation {
	
	private Creature other;
	
	private int type;
	public static final int TYPE_NEUTRAL = 0;
	public static final int TYPE_FRIENDLY = 1;
	public static final int TYPE_HOSTILE = 2;

}
