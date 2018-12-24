package game.item.tools;

import engine.gfx.Sprites;
import engine.rendering.Screen;
import game.TextLoader;
import game.creature.Player;
import game.gui.inventory.InventorySlotRenderable;
import game.item.Item;
import game.levels.Level;

public class ItemSpyglass
extends Item {
	
	public ItemSpyglass()
	{
		setItemID(ID_SPYGLASS);
		setHand(BOTH_HANDS);
		setName(TextLoader.getText("item_spyglass"));
	}
	
	@Override
	public void renderInHand(Screen screen, Player player, int hand)
	{
//		screen.render(Sprites.goldSwordInHand.getSprite(player.sx, player.sy, 64, 64), player.getX(), player.getY(), 256, 256, 0, 1);
	}
	
	@Override
	public void use(float x, float y, Level level, Player player, InventorySlotRenderable slot) 
	{
		player.level.game.useSpyglass();
	}
	
	@Override
	public void release(float x, float y, Level level, Player player, InventorySlotRenderable slot) 
	{
		player.level.game.releaseSpyglass();
	}
	
	@Override
	public void render(Screen screen, float x, float y, float size, boolean inCam, float rot, float alpha)
	{
		if(inCam) screen.render(Sprites.items.getSprite(16, SPRITES_TOOLS_Y, 16, 16), x, y, size, size, rot, alpha);
		else screen.renderGUI(Sprites.items.getSprite(16, SPRITES_TOOLS_Y, 16, 16), x, y, size, size, rot, alpha);
	}
	
//	@Override
//	public BufferedImage getShadow()
//	{
//		return SpriteFilter.getShadowStanding(SpriteSheet.items.getSprite(48, 112, 16, 16));
//	}

}
