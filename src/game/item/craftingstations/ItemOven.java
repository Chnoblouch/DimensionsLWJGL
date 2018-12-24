package game.item.craftingstations;

import engine.gfx.Sprites;
import engine.rendering.Screen;
import game.TextLoader;
import game.creature.Player;
import game.gui.inventory.InventorySlotRenderable;
import game.item.Item;
import game.levels.Level;
import game.obj.Oven;

public class ItemOven
extends Item {
	
	public ItemOven()
	{
		setItemID(ID_OVEN);
		setHand(BOTH_HANDS);
		setName(TextLoader.getText("item_oven"));
	}
	
	@Override
	public void use(float x, float y, Level level, Player player, InventorySlotRenderable slot)
	{
		Oven oven = new Oven();
		oven.setPosition(x - 96, y - 96);
		level.addObject(oven);
		
		slot.removeItem(1);
	}
	
	@Override
	public void render(Screen screen, float x, float y, float size, boolean inCam, float rot, float alpha)
	{
		if(inCam) screen.render(Sprites.items.getSprite(16, SPRITES_CRAFTING_STATIONS_Y, 16, 16), x, y, size, size, rot, alpha);
		else screen.renderGUI(Sprites.items.getSprite(16, SPRITES_CRAFTING_STATIONS_Y, 16, 16), x, y, size, size, rot, alpha);
	}
	
//	@Override
//	public BufferedImage getShadow()
//	{
//		return SpriteFilter.getShadowStanding(Sprites.items.getSprite(16, 304, 16, 16));
//	}

}
