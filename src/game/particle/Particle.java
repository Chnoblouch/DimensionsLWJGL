package game.particle;

import game.GameObject;

public class Particle
extends GameObject {
	
	@Override
	public boolean updateOutside()
	{
		return true;
	}

}
