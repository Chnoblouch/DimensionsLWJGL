package game.particle;

import java.util.Random;

import engine.gfx.Sprites;
import engine.rendering.Screen;

public class ParticleFeather 
extends Particle {
	
	private float size = 24 + (new Random().nextFloat() * 12);
	private float dirX = -4.0f + (new Random().nextFloat() * 8.0f), dirY = -1 - (new Random().nextFloat() * 6.0f);
	private float rot = - 90 + new Random().nextInt(90);
	
	private float alpha = 1;
		
	@Override
	public int getZIndex()
	{
		return (int) (getY() + size);
	}
	
	@Override
	public void render(Screen screen)
	{
		screen.render(Sprites.particles.getSprite(0, 10, 8, 8), getX(), getY(), size, size, rot, alpha);
	}
	
	@Override
	public void update(float tpf)
	{
		dirY += 0.2f * tpf;
		
		if(alpha > 0.015) alpha -= 0.015 * tpf;
		else level.removeObject(this);
		
		move(dirX * tpf, dirY * tpf);
	}

}
