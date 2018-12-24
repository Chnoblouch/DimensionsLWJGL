package game.item.creaturedrops;

import java.awt.image.BufferedImage;

import engine.gfx.Sprites;
import engine.rendering.Screen;
import game.TextLoader;
import game.item.Item;

public class ItemSlime
extends Item {
	
	public ItemSlime()
	{
		setItemID(ID_SLIME);
		setHand(BOTH_HANDS);
		setName(TextLoader.getText("item_slime"));
		setDescription(TextLoader.getText("item_descr_slime"));
	}
	
	@Override
	public void render(Screen screen, float x, float y, float size, boolean inCam, float rot, float alpha)
	{
		if(inCam) screen.render(Sprites.items.getSprite(0, SPRITES_CREATURE_DROPS_Y, 16, 16), x, y, size, size, rot, alpha);
		else screen.renderGUI(Sprites.items.getSprite(0, SPRITES_CREATURE_DROPS_Y, 16, 16), x, y, size, size, rot, alpha);
	}
	
//	@Override
//	public BufferedImage getShadow()
//	{
//		return SpriteFilter.getShadowStanding(Sprites.items.getSprite(0, 32, 16, 16));
//	}

}
