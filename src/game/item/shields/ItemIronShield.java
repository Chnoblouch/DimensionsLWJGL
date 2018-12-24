package game.item.shields;

import java.awt.image.BufferedImage;

import engine.gfx.Sprites;
import engine.rendering.Screen;
import game.TextLoader;
import game.creature.Player;
import game.gui.inventory.InventorySlotRenderable;
import game.item.Item;
import game.levels.Level;

public class ItemIronShield
extends Item {
	
	public ItemIronShield()
	{
		setItemID(ID_IRON_SHIELD);
		setHand(OFF_HAND);
		setName(TextLoader.getText("item_iron_shield"));
	}
		
	@Override
	public void renderInHand(Screen screen, Player player, int hand)
	{
		screen.render(Sprites.ironShieldInHand.getSprite(player.sx, player.sy, 64, 64), player.getX(), player.getY(), 256, 256, 0, 1);
	}
	
	@Override
	public void use(float x, float y, Level level, Player player, InventorySlotRenderable slot)
	{
		player.protect(this);
	}
	
	@Override
	public void release(float x, float y, Level level, Player player, InventorySlotRenderable slot)
	{
		player.stopProtecting();
	}
	
	@Override
	public void render(Screen screen, float x, float y, float size, boolean inCam, float rot, float alpha)
	{
		if(inCam) screen.render(Sprites.items.getSprite(32, SPRITES_SHIELDS_Y, 16, 16), x, y, size, size, rot, alpha);
		else screen.renderGUI(Sprites.items.getSprite(32, SPRITES_SHIELDS_Y, 16, 16), x, y, size, size, rot, alpha);
	}
	
//	@Override
//	public BufferedImage getShadow()
//	{
//		return SpriteFilter.getShadowStanding(SpriteSheet.items.getSprite(32, 160, 16, 16));
//	}

}
