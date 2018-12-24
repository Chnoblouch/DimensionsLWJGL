package game.item.resources;

import java.awt.image.BufferedImage;

import engine.gfx.Sprites;
import engine.rendering.Screen;
import game.TextLoader;
import game.item.Item;

public class ItemNightHerb
extends Item {
	
	public ItemNightHerb()
	{
		setItemID(ID_NIGHT_HERB);
		setHand(BOTH_HANDS);
		setName(TextLoader.getText("item_night_herb"));
		setDescription(TextLoader.getText("item_descr_night_herb"));
	}
	
	@Override
	public void render(Screen screen, float x, float y, float size, boolean inCam, float rot, float alpha)
	{
		if(inCam) screen.render(Sprites.items.getSprite(64, SPRITES_RESOURCES_Y, 16, 16), x, y, size, size, rot, alpha);
		else screen.renderGUI(Sprites.items.getSprite(64, SPRITES_RESOURCES_Y, 16, 16), x, y, size, size, rot, alpha);
	}
	
//	@Override
//	public BufferedImage getShadow()
//	{
//		return SpriteFilter.getShadowStanding(SpriteSheet.items.getSprite(64, 0, 16, 16));
//	}

}
