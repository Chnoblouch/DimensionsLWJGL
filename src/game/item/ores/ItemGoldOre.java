package game.item.ores;

import java.awt.image.BufferedImage;

import engine.gfx.Sprites;
import engine.rendering.Screen;
import game.TextLoader;
import game.item.Item;

public class ItemGoldOre
extends Item {
	
	public ItemGoldOre()
	{
		setItemID(ID_GOLD_ORE);
		setHand(BOTH_HANDS);
		setName(TextLoader.getText("item_gold_ore"));
	}
	
	@Override
	public void render(Screen screen, float x, float y, float size, boolean inCam, float rot, float alpha)
	{
		if(inCam) screen.render(Sprites.items.getSprite(16, SPRITES_ORES_Y, 16, 16), x, y, size, size, rot, alpha);
		else screen.renderGUI(Sprites.items.getSprite(16, SPRITES_ORES_Y, 16, 16), x, y, size, size, rot, alpha);
	}
	
//	@Override
//	public BufferedImage getShadow()
//	{
//		return SpriteFilter.getShadowStanding(Sprites.items.getSprite(16, 16, 16, 16));
//	}

}
