package game.item.shovels;

import engine.gfx.Sprites;
import engine.rendering.Screen;
import game.TextLoader;
import game.item.Item;

public class ItemWoodShovel
extends Item {
	
	public ItemWoodShovel()
	{
		setItemID(Item.ID_WOOD_SHOVEL);
		setHand(MAIN_HAND);
		setName(TextLoader.getText("item_wood_shovel"));
	}
	
	@Override
	public void render(Screen screen, float x, float y, float size, boolean inCam, float rot, float alpha)
	{
		if(inCam) screen.render(Sprites.items.getSprite(0, SPRITES_SHOVELS_Y, 16, 16), x, y, size, size, rot, alpha);
		else screen.renderGUI(Sprites.items.getSprite(0, SPRITES_SHOVELS_Y, 16, 16), x, y, size, size, rot, alpha);
	}
	
//	@Override
//	public BufferedImage getShadow()
//	{
//		return SpriteFilter.getShadowStanding(Sprites.items.getSprite(0, 96, 16, 16));
//	}

}
