package game.item.boots;

import engine.gfx.Sprites;
import engine.rendering.Screen;
import game.TextLoader;
import game.creature.Player;
import game.item.Item;

public class ItemGoldBoots
extends Item {
	
	public ItemGoldBoots()
	{
		setItemID(ID_GOLD_BOOTS);
		setHand(BOTH_HANDS);
		setArmor(true);
		setArmorID(ARMOR_BOOTS);
		setProtection(4);
		setName(TextLoader.getText("item_gold_boots"));
		setDescription(TextLoader.getText("protection") + ": " + getProtection());
	}
	
	@Override
	public void renderOnBody(Screen screen, Player player)
	{
		screen.render(Sprites.goldBootsOnBody.getSprite(player.sx, player.sy, 64, 64), player.getX(), player.getY(), 256, 256, 0, 1);
	}
	
	@Override
	public void render(Screen screen, float x, float y, float size, boolean inCam, float rot, float alpha)
	{
		if(inCam) screen.render(Sprites.items.getSprite(16, SPRITES_BOOTS_Y, 16, 16), x, y, size, size, rot, alpha);
		else screen.renderGUI(Sprites.items.getSprite(16, SPRITES_BOOTS_Y, 16, 16), x, y, size, size, rot, alpha);
	}
	
//	@Override
//	public BufferedImage getShadow()
//	{
//		return SpriteFilter.getShadowStanding(Sprites.items.getSprite(16, 272, 16, 16));
//	}

}
