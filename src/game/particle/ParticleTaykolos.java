package game.particle;

import java.util.Random;

import engine.gfx.Sprites;
import engine.rendering.Screen;

public class ParticleTaykolos 
extends Particle {
	
	private float size = 24 + (new Random().nextFloat() * 8);
	
	private float angle = new Random().nextInt(360);
	private float speed = 2.0f + new Random().nextInt(6);
	
	private float alpha = 1;
		
	public void setAngle(float angle)
	{
		this.angle = angle;
	}
	
	@Override
	public int getZIndex()
	{
		return 1000000;
	}
	
	@Override
	public void render(Screen screen)
	{
		if(screen.isInside(getX(), getY(), size, size))
			screen.render(Sprites.particles.getSprite(6, 4, 6, 6), getX(), getY(), size, size, 0, alpha);
	}
	
	@Override
	public void update(float tpf)
	{		
		if(alpha > 0.025f) alpha -= 0.025 * tpf;
		else level.removeObject(this);
		
		moveAlongAngle(angle, speed * tpf);
	}

}
