package game.item.resources;

import java.awt.image.BufferedImage;

import engine.gfx.Sprites;
import engine.rendering.Screen;
import game.TextLoader;
import game.item.Item;

public class ItemFrostPearl
extends Item {
	
	public ItemFrostPearl()
	{
		setItemID(ID_FROST_PEARL);
		setHand(BOTH_HANDS);
		setName(TextLoader.getText("item_frost_pearl"));
		setDescription(TextLoader.getText("item_descr_frost_pearl"));
	}
	
	@Override
	public void render(Screen screen, float x, float y, float size, boolean inCam, float rot, float alpha)
	{
		if(inCam) screen.render(Sprites.items.getSprite(48, SPRITES_RESOURCES_Y, 16, 16), x, y, size, size, rot, alpha);
		else screen.renderGUI(Sprites.items.getSprite(48, SPRITES_RESOURCES_Y, 16, 16), x, y, size, size, rot, alpha);
	}
	
//	@Override
//	public BufferedImage getShadow()
//	{
//		return SpriteFilter.getShadowStanding(SpriteSheet.items.getSprite(48, 0, 16, 16));
//	}

}
