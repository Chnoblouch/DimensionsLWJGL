package game.item.capes;

import engine.gfx.Sprites;
import engine.rendering.Screen;
import game.TextLoader;
import game.creature.Player;
import game.item.Item;

public class ItemCapeBlank
extends Item {
	
	public ItemCapeBlank()
	{
		setItemID(ID_CAPE_BLANK);
		setHand(BOTH_HANDS);
		setArmor(true);
		setArmorID(ARMOR_BACK);
		setName(TextLoader.getText("item_cape_blank"));
		setDescription(TextLoader.getText("item_descr_cape_blank"));
	}
	
	@Override
	public void renderOnBody(Screen screen, Player player)
	{
		screen.render(Sprites.capeBlankOnBoy.getSprite(player.sx, player.sy, 64, 64), player.getX(), player.getY(), 256, 256, 0, 1);
	}
	
	@Override
	public void render(Screen screen, float x, float y, float size, boolean inCam, float rot, float alpha)
	{
		if(inCam) screen.render(Sprites.items.getSprite(0, SPRITES_BACK_ITEMS_Y, 16, 16), x, y, size, size, rot, alpha);
		else screen.renderGUI(Sprites.items.getSprite(0, SPRITES_BACK_ITEMS_Y, 16, 16), x, y, size, size, rot, alpha);
	}
	
//	@Override
//	public BufferedImage getShadow()
//	{
//		return SpriteFilter.getShadowStanding(Sprites.items.getSprite(16, 272, 16, 16));
//	}

}
