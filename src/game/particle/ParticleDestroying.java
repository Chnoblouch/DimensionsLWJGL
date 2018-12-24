package game.particle;

import java.util.Random;

import engine.gfx.Sprites;
import engine.rendering.Screen;

public class ParticleDestroying 
extends Particle {
	
	private float size = 16 + (new Random().nextFloat() * 8);
	private float dirX = -4.0f + (new Random().nextFloat() * 8.0f), dirY = -1.0f - (new Random().nextFloat() * 6.0f);
	
	private float alpha = 1;
	
	private int sx, sy;
		
	public void setSprite(int sx, int sy)
	{
		this.sx = sx;
		this.sy = sy;
	}
	
	@Override
	public int getZIndex()
	{
		return (int) (getY() + size + 32);
	}
	
	@Override
	public void render(Screen screen)
	{
		screen.render(Sprites.particles.getSprite(sx, sy, 4, 4), getX(), getY(), size, size, 0, alpha);
	}
	
	@Override
	public void update(float tpf)
	{
		dirY += 0.2f * tpf;
		
		if(alpha > 0.015f) alpha -= 0.015f * tpf;
		else level.removeObject(this);
		
		move(dirX * tpf, dirY * tpf);
	}

}
