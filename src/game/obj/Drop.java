package game.obj;

import engine.gfx.Sounds;
import engine.rendering.Screen;
import game.GameObject;
import game.item.Item;
import utils.Angles;

public class Drop 
extends GameObject {
	
	private Item item;
	private int count;
	
//	private float flyY = 0;
//	private float flyUpSpeed = 0;
//	private boolean flyUp = true;
	
	private boolean pickedUp = false;
	private float size = 64;
	
	public Drop(Item item, int count)
	{
		this.item = item;
		this.count = count;
	}
	
	@Override
	public int getZIndex()
	{
		return (int) (getY() + 64 - 1);
	}
	
	@Override
	public void update(float tpf)
	{
//		if(flyUp) flyUpSpeed += 0.1 * tpf;
//		else flyUpSpeed -= 0.1 * tpf;
//		
//		if(flyUpSpeed >= 0.75) flyUp = false;
//		if(flyUpSpeed <= -0.75) flyUp = true;
//		
//		flyY += flyUpSpeed;
				
		if(!pickedUp)
		{
			if(!level.player.isDeath() && level.player.inventory.canHold(item.getItemID(), count) && isInRange(level.player) && collides(level.player)) 
			{
				level.player.inventory.fillNextFreeSlot(item, count);
				pickedUp = true;
				Sounds.pickup.play(0, false, getX() + 32, getY() + 32, level.game.getCamera().getX(), level.game.getCamera().getY());
			}
		} else 
		{
			moveAlongAngle(Angles.getAngle(getX() + (size / 2), getY() + (size / 2), 
										   level.player.getX() + 128, level.player.getY() + 128), 8.0f * tpf);
			size -= 4.0f * tpf;
			
			if(size < 16) level.removeObject(this);
		}
	}
	
	@Override
	public void render(Screen screen)
	{
//		float ss = 64 - (flyY / 2);
		
//		screen.render(SpriteSheet.shadows.getSprite(0, 0, 16, 16), getX() + 32 - (ss / 2), getY() + 64 - (ss / 2), ss, ss, 0, 0.75 - (flyY / 96));
//		screen.render(item.getShadow(), getX(), getY(), size * 2, size, 0, 0.25f); // add back in
		item.render(screen, getX(), getY(), size, true, 0, 1);
	}
	

}
