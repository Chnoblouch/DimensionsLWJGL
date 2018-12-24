package game.item.hoes;

import java.awt.image.BufferedImage;

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

public class ItemIronHoe
extends Item {
	
	public ItemIronHoe()
	{
		setItemID(ID_IRON_HOE);
		setHand(MAIN_HAND);
		setName(TextLoader.getText("item_iron_hoe"));
	}
	
	@Override
	public void use(float x, float y, Level level, Player player, InventorySlotRenderable slot)
	{
		for(int i = 0; i < level.objects.size(); i++)
		{
			GameObject o = level.objects.get(i);
			
			if(o instanceof GrassGround && o.getHitbox().intersects(player.getInteractionArea()) &&
			   x >= o.getX() && x <= o.getX() + Block.SIZE && y >= o.getY() && y <= o.getY() + Block.SIZE)
			{				
				Farmland farmland = new Farmland();
				farmland.setPosition(o.getX(), o.getY());
				level.addObject(farmland);
				farmland.findNeighbors();
				
				level.removeObject(o);
			}
		}
	}
	
	@Override
	public void renderInHand(Screen screen, Player player, int hand)
	{
//		if(hand == MAIN_HAND)
//			screen.render(SpriteSheet.ironAxeInHand.getSprite(player.sx, player.sy, 64, 64), player.getX(), player.getY(), 256, 256, 0, 1);
//		else if(hand == OFF_HAND)
//			screen.render(SpriteSheet.ironAxeInHand.getSprite(player.sx, player.sy, 64, 64), player.getX(), player.getY(), 256, 256, 0, 1);
	}
	
	@Override
	public void render(Screen screen, float x, float y, float size, boolean inCam, float rot, float alpha)
	{
		if(inCam) screen.render(Sprites.items.getSprite(32, SPRITES_HOES_Y, 16, 16), x, y, size, size, rot, alpha);
		else screen.renderGUI(Sprites.items.getSprite(32, SPRITES_HOES_Y, 16, 16), x, y, size, size, rot, alpha);
	}
	
//	@Override
//	public BufferedImage getShadow()
//	{
//		return SpriteFilter.getShadowStanding(Sprites.items.getSprite(32, 128, 16, 16));
//	}

}
