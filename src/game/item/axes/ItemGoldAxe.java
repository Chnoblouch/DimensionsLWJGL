package game.item.axes;

import engine.gfx.Sprites;
import engine.rendering.Screen;
import game.TextLoader;
import game.creature.Player;
import game.item.Item;

public class ItemGoldAxe
extends Item {
	
	public ItemGoldAxe()
	{
		setItemID(ID_GOLD_AXE);
		setHand(MAIN_HAND);
		setWoodDamage(5);
		setName(TextLoader.getText("item_gold_axe"));
		setDescription(TextLoader.getText("wood_damage") + ": " + getWoodDamage());
	}
	
	@Override
	public void renderInHand(Screen screen, Player player, int hand)
	{
		screen.render(Sprites.ironAxeInHand.getSprite(player.sx, player.sy, 64, 64), player.getX(), player.getY(), 256, 256, 0, 1);
	}
	
	@Override
	public void render(Screen screen, float x, float y, float size, boolean inCam, float rot, float alpha)
	{
		if(inCam) screen.render(Sprites.items.getSprite(48, SPRITES_AXES_Y, 16, 16), x, y, size, size, rot, alpha);
		else screen.renderGUI(Sprites.items.getSprite(48, SPRITES_AXES_Y, 16, 16), x, y, size, size, rot, alpha);
	}
	
//	@Override
//	public BufferedImage getShadow()
//	{
//		return SpriteFilter.getShadowStanding(Sprites.items.getSprite(48, 64, 16, 16));
//	}

}
