package game.item.resources;

import engine.gfx.Sprites;
import engine.rendering.Screen;
import game.TextLoader;
import game.item.Item;

public class ItemWood
extends Item {
	
	public ItemWood()
	{
		setItemID(ID_WOOD);
		setHand(BOTH_HANDS);
		setName(TextLoader.getText("item_wood"));
	}
	
	@Override
	public void render(Screen screen, float x, float y, float size, boolean inCam, float rot, float alpha)
	{		
		if(inCam) screen.render(Sprites.items.getSprite(0, SPRITES_RESOURCES_Y, 16, 16), x, y, size, size, rot, alpha);
		else screen.renderGUI(Sprites.items.getSprite(0, SPRITES_RESOURCES_Y, 16, 16), x, y, size, size, rot, alpha);
	}
	
//	@Override
//	public BufferedImage getShadow()
//	{
//		return SpriteFilter.getShadowStanding(SpriteSheet.items.getSprite(0, 0, 16, 16));
//	}

}
