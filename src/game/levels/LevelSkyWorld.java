package game.levels;

import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.opengl.Display;

import engine.gfx.Sprites;
import engine.rendering.Screen;
import game.Game;
import game.creature.Dragon;
import game.creature.Player;
import game.creature.monster.Monster;
import game.creature.monster.MonsterLobire;
import game.creature.monster.MonsterSlime;
import game.environment.GrassGround;
import game.obj.Portal;
import utils.Block;
import utils.SafeArrayList;
import utils.TimeCounter;

public class LevelSkyWorld 
extends Level {
	
	private Player player;
	
	public Dragon dragon;
	public Dragon eldenir;

	private TimeCounter monsterTimer;
	
	public LevelSkyWorld(Game game)
	{
		super(game, 2, "dim_skyworld");
		
		setPlayerSpawn(Level.CENTER - (Block.SIZE * 1.5f), Level.CENTER - (Block.SIZE * 4.5f));
		
		game.getMaps()[2].onLevel(this);
		
		if(!game.exists())
		{
			Portal portal = new Portal();
			portal.setPosition(CENTER + (Block.SIZE * 11), CENTER);
			portal.setLevel(0);
			addObject(portal);
			
			dragon = new Dragon();
			dragon.setPosition(CENTER - (Block.SIZE * 4), CENTER - 384);
			addObject(dragon);
			
//			eldenir = new Dragon();
//			eldenir.setPosition(CENTER - (Block.SIZE * 4), CENTER - 384);
//			eldenir.setLook(Sprites.dragonEldenir);
//			eldenir.setName("Eldenir");
//			addObject(eldenir);
			
//			NPCAnyrava anyrava = new NPCAnyrava();
//			anyrava.setPosition(CENTER, CENTER);
//			anyrava.tameDragon(eldenir);
//			anyrava.rideOnDragon(eldenir);
//			addObject(anyrava);
		}
		
		monsterTimer = new TimeCounter(5000, () ->
		{
			ArrayList<GrassGround> grounds = new SafeArrayList<>();
			
			for(int i = 0; i < objects.size(); i++)
			{
				if(objects.get(i) instanceof GrassGround) grounds.add((GrassGround) objects.get(i));
			}
			
			GrassGround place = grounds.get(new Random().nextInt(grounds.size()));
			
			Monster m = new Random().nextBoolean() ? new MonsterSlime() : new MonsterLobire();
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
		
		if(monsterCounter < 5) monsterTimer.count(tpf);
	}
	
	@Override
	public void render(Screen screen)
	{
		screen.render(Sprites.skyworldbg.getSprite(0, 0, 128, 128), game.getCamera().getX(), game.getCamera().getY(), 
					  Display.getWidth(), Display.getHeight(), 0, 1);		
		super.render(screen);
	}
	
}
