package game.item.resources;

import java.awt.image.BufferedImage;

import engine.gfx.Sprites;
import engine.rendering.Screen;
import game.TextLoader;
import game.item.Item;

public class ItemBlueStone
extends Item {
	
	public ItemBlueStone()
	{
		setItemID(ID_BLUE_STONE);
		setHand(BOTH_HANDS);
		setName(TextLoader.getText("item_blue_stone"));
		setDescription(TextLoader.getText("item_descr_blue_stone"));
	}
	
	@Override
	public void render(Screen screen, float x, float y, float size, boolean inCam, float rot, float alpha)
	{
		if(inCam) screen.render(Sprites.items.getSprite(96, SPRITES_RESOURCES_Y, 16, 16), x, y, size, size, rot, alpha);
		else screen.renderGUI(Sprites.items.getSprite(96, SPRITES_RESOURCES_Y, 16, 16), x, y, size, size, rot, alpha);
	}
	
//	@Override
//	public BufferedImage getShadow()
//	{
//		return SpriteFilter.getShadowStanding(Sprites.items.getSprite(112, 0, 16, 16));
//	}

}
