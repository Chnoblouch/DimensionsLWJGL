package game.item.food;

import engine.gfx.Sprites;
import engine.rendering.Screen;
import game.TextLoader;
import game.creature.Player;
import game.gui.inventory.InventorySlotRenderable;
import game.item.Item;
import game.levels.Level;

public class ItemBakedApple
extends Item {
	
	public ItemBakedApple()
	{
		setItemID(ID_BAKED_APPLE);
		setHand(BOTH_HANDS);
		setName(TextLoader.getText("item_baked_apple"));
	}
	
	@Override
	public void use(float x, float y, Level level, Player player, InventorySlotRenderable slot)
	{
		if(player.isEating()) return;
		
		player.eat(this);
		player.heal(17);
		slot.removeItem(1);
	}
	
	@Override
	public void render(Screen screen, float x, float y, float size, boolean inCam, float rot, float alpha)
	{
		if(inCam) screen.render(Sprites.items.getSprite(96, SPRITES_FOOD_Y, 16, 16), x, y, size, size, rot, alpha);
		else screen.renderGUI(Sprites.items.getSprite(96, SPRITES_FOOD_Y, 16, 16), x, y, size, size, rot, alpha);
	}
	
	@Override
	public void renderInHand(Screen screen, Player player, int hand)
	{
		screen.render(Sprites.bakedAppleInHand.getSprite(player.sx, player.sy, 64, 64), player.getX(), player.getY(), 256, 256, 0, 1);
	}
	
//	@Override
//	public BufferedImage getShadow()
//	{
//		return SpriteFilter.getShadowStanding(Sprites.items.getSprite(0, 48, 16, 16));
//	}

}
