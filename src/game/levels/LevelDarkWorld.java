package game.levels;

import java.util.ArrayList;
import java.util.Random;

import game.Game;
import game.creature.Player;
import game.creature.monster.Monster;
import game.creature.monster.MonsterWalkingEye;
import game.creature.npc.NPCAxar;
import game.creature.npc.NPCBruno;
import game.environment.DarkGround;
import game.obj.Portal;
import utils.Block;
import utils.SafeArrayList;
import utils.TimeCounter;

public class LevelDarkWorld 
extends Level {
	
	private Player player;
	
	private TimeCounter monsterTimer;

	public LevelDarkWorld(Game game)
	{
		super(game, 1, "dim_darkworld");
		
		game.getMaps()[1].onLevel(this);
		
		if(!game.exists())
		{
			Portal portal = new Portal();
			portal.setPosition(CENTER, CENTER + (Block.SIZE * 12));
			portal.setLevel(0);
			addObject(portal);
			
			NPCAxar axar = new NPCAxar();
			axar.setPosition(CENTER - (Block.SIZE * 4), CENTER + (Block.SIZE * 8));
			addObject(axar);
		}
		
		monsterTimer = new TimeCounter(5000, () ->
		{
			ArrayList<DarkGround> grounds = new SafeArrayList<>();
			
			for(int i = 0; i < objects.size(); i++)
			{
				if(objects.get(i) instanceof DarkGround) grounds.add((DarkGround) objects.get(i));
			}
			
			DarkGround place = grounds.get(new Random().nextInt(grounds.size()));
			
			Monster m = new MonsterWalkingEye();
			m.setPosition(place.getX(), place.getY() - m.getHitbox().h);
			addObject(m);
			
			monsterCounter ++;
			
			monsterTimer.reset();
		});
	}
	
	@Override
	public void update(float tpf)
	{
		super.update(tpf);
		
		if(monsterCounter < 15) monsterTimer.count(tpf);
	}
	
}
