package game.item.food;

import engine.gfx.Sprites;
import engine.rendering.Screen;
import game.TextLoader;
import game.creature.Player;
import game.gui.inventory.InventorySlotRenderable;
import game.item.Item;
import game.levels.Level;

public class ItemMeat
extends Item {
	
	public ItemMeat()
	{
		setItemID(ID_MEAT);
		setHand(BOTH_HANDS);
		setName(TextLoader.getText("item_meat"));
	}
	
	@Override
	public void use(float x, float y, Level level, Player player, InventorySlotRenderable slot)
	{
		if(player.isEating()) return;

		player.eat(this);
		player.heal(12);
		slot.removeItem(1);
	}
	
	@Override
	public void render(Screen screen, float x, float y, float size, boolean inCam, float rot, float alpha)
	{
		if(inCam) screen.render(Sprites.items.getSprite(32, SPRITES_FOOD_Y, 16, 16), x, y, size, size, rot, alpha);
		else screen.renderGUI(Sprites.items.getSprite(32, SPRITES_FOOD_Y, 16, 16), x, y, size, size, rot, alpha);
	}
	
	@Override
	public void renderInHand(Screen screen, Player player, int hand)
	{
		screen.render(Sprites.meatInHand.getSprite(player.sx, player.sy, 64, 64), player.getX(), player.getY(), 256, 256, 0, 1);
	}
	
//	@Override
//	public BufferedImage getShadow()
//	{
//		return SpriteFilter.getShadowStanding(Sprites.items.getSprite(32, 48, 16, 16));
//	}

}
