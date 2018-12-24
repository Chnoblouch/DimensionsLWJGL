package game.item.helmets;

import engine.gfx.Sprites;
import engine.rendering.Screen;
import game.TextLoader;
import game.creature.Player;
import game.item.Item;

public class ItemLobireMask
extends Item {
	
	public ItemLobireMask()
	{
		setItemID(ID_LOBIRE_MASK);
		setHand(BOTH_HANDS);
		setName(TextLoader.getText("item_lobire_mask"));
		setDescription(TextLoader.getText("item_descr_lobire_mask"));
	}
	
	
	@Override
	public void render(Screen screen, float x, float y, float size, boolean inCam, float rot, float alpha)
	{
		if(inCam) screen.render(Sprites.items.getSprite(64, SPRITES_HELMETS_Y, 16, 16), x, y, size, size, rot, alpha);
		else screen.renderGUI(Sprites.items.getSprite(64, SPRITES_HELMETS_Y, 16, 16), x, y, size, size, rot, alpha);
	}

}
