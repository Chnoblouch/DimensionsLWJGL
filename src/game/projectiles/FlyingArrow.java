package game.projectiles;

import engine.gfx.Sprites;
import engine.rendering.Screen;
import game.GameObject;
import game.creature.Creature;
import game.creature.Dragon;
import game.creature.monster.Monster;
import game.creature.npc.NPC;
import game.environment.BlueStone;
import game.environment.GrassBlock;
import game.environment.IronOreStone;
import game.environment.RedStone;
import game.environment.Stone;
import game.environment.Tree;
import game.item.Item;
import game.item.arrows.ItemArrow;
import utils.Hitbox;
import utils.HitboxFactory;
import utils.TimeCounter;

public class FlyingArrow 
extends GameObject {
	
	private float angle = 0;
	private boolean flying = true;
	private TimeCounter disappearTimer;
	
	public FlyingArrow()
	{
		disappearTimer = new TimeCounter(10000, () -> level.removeObject(this));
	}
	
	public void setAngle(float angle)
	{
		this.angle = angle;
	}
	
	@Override
	public Hitbox getHitbox()
	{
		return HitboxFactory.create(getX(), getY() + 24, 64, 64);
	}
	
	@Override
	public boolean updateOutside()
	{
		return true;
	}
	
	public float getAngle()
	{
		return angle;
	}
	
	public boolean isFlying()
	{
		return flying;
	}
	
	@Override
	public void update(float tpf)
	{
		if(!level.game.getScreen().isInside(getHitbox())) level.removeObject(this);
		
		if(flying)
		{		
			for(int i = 0; i < 24; i++)
			{
				moveAlongAngle(angle, 2 * tpf);
								
				for(int j = 0; j < level.objects.size(); j++)
				{
					GameObject o = level.objects.get(j);
					if(isInRange(o) && collides(o))
					{
						if(o instanceof Monster || o instanceof NPC)
						{		
							level.removeObject(this);
							((Creature) o).damage(12, level.player);
							((Creature) o).knockback(angle, 32);
							
							break;
						}
						else if(o instanceof Dragon)
						{
							level.removeObject(this);
							((Dragon) o).damage(12, level.player);
						}
						else if((o instanceof GrassBlock && level.getID() != 2) || o instanceof Tree || o instanceof Stone || o
								instanceof IronOreStone || o instanceof RedStone || o instanceof BlueStone)
						{
							flying = false;
							break;
						}
					}
				}
				
				if(!flying) break;
			}
		} else 
		{
			if(level.player.inventory.canHold(Item.ID_ARROW, 1) && isInRange(level.player) && collides(level.player))
			{
				level.removeObject(this);
				level.player.inventory.fillNextFreeSlot(new ItemArrow(), 1);
			}
			
			disappearTimer.count(tpf);
		}
	}
	
	@Override
	public int getZIndex()
	{
		return (int) (getY() + 64 + 24);
	}
	
	@Override
	public void render(Screen screen)
	{
//		BufferedImage shadow = SpriteFilter.getShadowStanding(SpriteFilter.instantRotating(sprite, angle));
		
//		screen.render(shadow, getX() - ((shadow.getWidth() * 2) - 64) + 24, getY() + 24,
//					  shadow.getWidth() * 4, shadow.getHeight() * 4, 0, 0.25); // add back in		
		screen.render(Sprites.projectiles.getSprite(0, 0, 16, 16), getX(), getY(), 64, 64, angle, 1);
	}

}
