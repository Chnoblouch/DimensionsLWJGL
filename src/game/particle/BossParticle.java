package game.particle;

import java.util.Random;

import engine.gfx.Sprites;
import engine.rendering.Screen;
import game.creature.monster.MonsterGiantSlime;
import utils.Angles;
import utils.TimeCounter;

public class BossParticle 
extends Particle {
	
	private float size = 16 + (new Random().nextFloat() * 8);
	private MonsterGiantSlime target;
	private float dirX, dirY;
	
	private TimeCounter disappearTimer;
	
	public BossParticle()
	{
		disappearTimer = new TimeCounter(5000, () -> level.removeObject(this));
	}

	@Override
	public int getZIndex()
	{
		return (int) (getY() + size + 32);
	}
	
	@Override
	public boolean updateOutside()
	{
		return true;
	}
	
	public void setTarget(MonsterGiantSlime target)
	{
		this.target = target;
		
		float angle = new Random().nextInt(360);
		dirX = Angles.getMoveDirection(angle).x;
		dirY = Angles.getMoveDirection(angle).y;
		
		setPosition(target.getX() + 128, target.getY() + 256);
		move(-dirX * 1500, -dirY * 1500);
	}
	
	@Override
	public void render(Screen screen)
	{
		screen.render(Sprites.particles.getSprite(12, 4, 6, 6), getX(), getY(), size, size, 0, 1);
	}
	
	@Override
	public void update(float tpf)
	{
		disappearTimer.count(tpf);
		
		moveAlongAngle(getAngle(target), 20 * tpf);
		
		if(getX() > target.getX() - 16 && getX() < target.getX() + 256 + 16 && 
		   getY() > target.getY() - 16 && getY() < target.getY() + 256 + 16)
			level.removeObject(this);
	}

}
