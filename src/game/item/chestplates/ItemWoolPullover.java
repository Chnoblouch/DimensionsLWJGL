package game.item.chestplates;

import engine.gfx.Sprites;
import engine.rendering.Screen;
import game.TextLoader;
import game.creature.Player;
import game.item.Item;

public class ItemWoolPullover
extends Item {
	
	public ItemWoolPullover()
	{
		setItemID(ID_WOOL_PULLOVER);
		setHand(BOTH_HANDS);
		setArmor(true);
		setArmorID(ARMOR_CHESTPLATE);
		setProtection(3);
		setName(TextLoader.getText("item_wool_pullover"));
		setDescription(TextLoader.getText("protection") + ": " + getProtection(), TextLoader.getText("item_descr_wool_pullover"));
	}
	
	@Override
	public void renderOnBody(Screen screen, Player player)
	{
		screen.render(Sprites.woolPulloverOnBody.getSprite(player.sx, player.sy, 64, 64), player.getX(), player.getY(), 256, 256, 0, 1);
	}
	
	@Override
	public void render(Screen screen, float x, float y, float size, boolean inCam, float rot, float alpha)
	{
		if(inCam) screen.render(Sprites.items.getSprite(48, SPRITES_CHESTPLATES_Y, 16, 16), x, y, size, size, rot, alpha);
		else screen.renderGUI(Sprites.items.getSprite(48, SPRITES_CHESTPLATES_Y, 16, 16), x, y, size, size, rot, alpha);
	}
	
//	@Override
//	public BufferedImage getShadow()
//	{
//		return SpriteFilter.getShadowStanding(Sprites.items.getSprite(32, 240, 16, 16));
//	}

}
