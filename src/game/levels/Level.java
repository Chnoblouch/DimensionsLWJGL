package game.levels;

import java.util.Comparator;

import org.lwjgl.util.vector.Vector2f;

import engine.rendering.Screen;
import game.CollisionSpace;
import game.Game;
import game.GameObject;
import game.creature.Player;
import utils.Block;
import utils.SafeArrayList;

public class Level
{
	public Game game;
		
	private int id = 0;
	private String name;
	
	public Player player;
	public SafeArrayList<GameObject> objects = new SafeArrayList<>();
	public SafeArrayList<GameObject> updateables = new SafeArrayList<>();
	public CollisionSpace collisionSpace = new CollisionSpace();
	
	public static float SIZE = Block.SIZE * 128;
	public static float CENTER = Block.SIZE * 64;
	
	private Vector2f playerSpawn = new Vector2f(Level.CENTER, Level.CENTER + (Block.SIZE * 4));
	
	public int monsterCounter = 0;
	
	public Level(Game game, int id, String name)
	{
		this.game = game;
		this.id = id;
		this.name = name;
	}
	
	public void setPlayerSpawn(float x, float y)
	{
		playerSpawn.x = x;
		playerSpawn.y = y;
	}
	
	public Vector2f getPlayerSpawn()
	{
		return playerSpawn;
	}
	
	public void addObject(GameObject obj)
	{
		if(obj instanceof Player) player = (Player) obj;
		
		objects.add(obj);
		if(obj.doUpdate()) updateables.add(obj);
		if(obj.inCollisionSpace()) collisionSpace.add(obj);
		obj.init(this);
	}
	
	public void removeObject(GameObject obj)
	{
		objects.remove(obj);
		if(obj.doUpdate()) updateables.remove(obj);
		if(obj.inCollisionSpace()) collisionSpace.remove(obj);
	}
	
	public void update(float tpf)
	{
		updateables.forEach(o -> 
		{ 
			if(game.getScreen().isInside(o.getUpdateHitbox()) || o.updateOutside())
				o.update(tpf);
		});
	}
	
	public int getID()
	{
		return id;
	}
	
	public String getName()
	{
		return name;
	}
	
	private void sortObjectsByZIndex()
	{
		objects.sort(new Comparator<GameObject>()
		{
			@Override
			public int compare(GameObject r1, GameObject r2) 
			{
				return r1.getZIndex() - r2.getZIndex();
			}
		});
	}
	
	public void render(Screen screen)
	{
		sortObjectsByZIndex();
		objects.forEach(o -> o.render(screen));
	}
	
	public void renderGUI(Screen screen)
	{
		objects.forEach(o -> o.renderGUI(screen));
	}
	
	public void renderShadows(Screen screen)
	{
		sortObjectsByZIndex();
		objects.forEach(r -> r.renderShadow(screen));
	}

}
