package game.item.ores;

import java.awt.image.BufferedImage;

import engine.gfx.Sprites;
import engine.rendering.Screen;
import game.TextLoader;
import game.item.Item;

public class ItemIronOre
extends Item {
	
	public ItemIronOre()
	{
		setItemID(ID_IRON_ORE);
		setHand(BOTH_HANDS);
		setName(TextLoader.getText("item_iron_ore"));
	}
	
	@Override
	public void render(Screen screen, float x, float y, float size, boolean inCam, float rot, float alpha)
	{
		if(inCam) screen.render(Sprites.items.getSprite(0, SPRITES_ORES_Y, 16, 16), x, y, size, size, rot, alpha);
		else screen.renderGUI(Sprites.items.getSprite(0, SPRITES_ORES_Y, 16, 16), x, y, size, size, rot, alpha);
	}
	
//	@Override
//	public BufferedImage getShadow()
//	{
//		return SpriteFilter.getShadowStanding(Sprites.items.getSprite(0, 16, 16, 16));
//	}

}
