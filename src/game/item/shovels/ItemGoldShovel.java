package game.item.shovels;

import java.awt.image.BufferedImage;

import engine.gfx.Sprites;
import engine.rendering.Screen;
import game.TextLoader;
import game.item.Item;

public class ItemGoldShovel
extends Item {
	
	public ItemGoldShovel()
	{
		setItemID(Item.ID_GOLD_SHOVEL);
		setHand(MAIN_HAND);
		setName(TextLoader.getText("item_gold_shovel"));
	}
	
	@Override
	public void render(Screen screen, float x, float y, float size, boolean inCam, float rot, float alpha)
	{
		if(inCam) screen.render(Sprites.items.getSprite(48, SPRITES_SHOVELS_Y, 16, 16), x, y, size, size, rot, alpha);
		else screen.renderGUI(Sprites.items.getSprite(48, SPRITES_SHOVELS_Y, 16, 16), x, y, size, size, rot, alpha);
	}
	
//	@Override
//	public BufferedImage getShadow()
//	{
//		return SpriteFilter.getShadowStanding(Sprites.items.getSprite(48, 96, 16, 16));
//	}

}
