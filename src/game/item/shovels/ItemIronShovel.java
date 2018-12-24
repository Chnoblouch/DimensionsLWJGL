package game.item.shovels;

import engine.gfx.Sprites;
import engine.rendering.Screen;
import game.GameObject;
import game.TextLoader;
import game.creature.Player;
import game.environment.Farmland;
import game.environment.GrassGround;
import game.environment.Hole;
import game.gui.inventory.InventorySlotRenderable;
import game.item.Item;
import game.levels.Level;
import utils.Block;

public class ItemIronShovel
extends Item {
	
	public ItemIronShovel()
	{
		setItemID(Item.ID_IRON_SHOVEL);
		setHand(MAIN_HAND);
		setName(TextLoader.getText("item_iron_shovel"));
	}
	
	@Override
	public void render(Screen screen, float x, float y, float size, boolean inCam, float rot, float alpha)
	{
		if(inCam) screen.render(Sprites.items.getSprite(32, SPRITES_SHOVELS_Y, 16, 16), x, y, size, size, rot, alpha);
		else screen.renderGUI(Sprites.items.getSprite(32, SPRITES_SHOVELS_Y, 16, 16), x, y, size, size, rot, alpha);
	}
	
	boolean firstUse = true;
	
	@Override
	public void use(float x, float y, Level level, Player player, InventorySlotRenderable slot)
	{
		for(int i = 0; i < level.objects.size(); i++)
		{
			GameObject o = level.objects.get(i);
			
			if(o instanceof GrassGround && o.getHitbox().intersects(player.getInteractionArea()) &&
			   x >= o.getX() && x <= o.getX() + Block.SIZE && y >= o.getY() && y <= o.getY() + Block.SIZE)
			{				
				Hole hole = new Hole();
				hole.setPosition(o.getX(), o.getY());
				level.addObject(hole);
				hole.findNeighbors();
				hole.setWater(firstUse);
				
				level.removeObject(o);
				
				firstUse = false;
			}
		}
	}
	
//	float
//	public BufferedImage getShadow()
//	{
//		return SpriteFilter.getShadowStanding(Sprites.items.getSprite(32, 96, 16, 16));
//	}

}
