package game.projectiles;

import engine.gfx.Sprites;
import engine.rendering.Screen;
import game.GameObject;
import game.creature.Creature;
import game.creature.monster.MonsterGiantSlime;
import game.creature.monster.MonsterSlime;
import utils.TimeCounter;

public class Slimeball 
extends GameObject {
	
	private float angle = 0;
	
	private TimeCounter disappearTimer;
			
	public Slimeball()
	{
		disappearTimer = new TimeCounter(3000, () -> level.removeObject(this));
	}
	
	public void setAngle(float angle)
	{
		this.angle = angle;
	}
	
	@Override
	public void render(Screen screen)
	{
		if(screen.isInside(getX(), getY(), 128, 128))
		{
			screen.render(Sprites.projectiles.getSprite(56, 0, 32, 32), getX(), getY(), 128, 128, angle, 0.9f);
		}
	}
	
	@Override
	public boolean updateOutside()
	{
		return true;
	}
	
	@Override
	public void update(float tpf)
	{	
		moveAlongAngle(angle, 24 * tpf);
		
		for(int i = 0; i < level.objects.size(); i++)
		{
			GameObject o = level.objects.get(i);
			
			if(o.isInRange(this) && o instanceof Creature && collides(o) && !(o instanceof MonsterGiantSlime) && !(o instanceof MonsterSlime)) 
			{
				((Creature) o).damage(20, this);
				level.removeObject(this);
				
				break;
			}
		}
		
		disappearTimer.count(tpf);
		
		if(!level.game.getScreen().isInside(getX(), getY(), 128, 128)) level.removeObject(this);
	}

}
