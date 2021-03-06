package game.item.craftingstations;

import engine.gfx.Sprites;
import engine.rendering.Screen;
import game.TextLoader;
import game.creature.Player;
import game.gui.inventory.InventorySlotRenderable;
import game.item.Item;
import game.levels.Level;
import game.obj.AlchemyTable;

public class ItemAlchemyTable
extends Item {
	
	public ItemAlchemyTable()
	{
		setItemID(ID_ALCHEMY_TABLE);
		setHand(BOTH_HANDS);
		setName(TextLoader.getText("item_alchemy_table"));
	}
	
	@Override
	public void use(float x, float y, Level level, Player player, InventorySlotRenderable slot)
	{
		AlchemyTable alchemyTable = new AlchemyTable();
		alchemyTable.setPosition(x - 96, y - 96);
		level.addObject(alchemyTable);
		
		slot.removeItem(1);
		
//		for(float fy = Level.CENTER - (Level.SIZE / 2); fy < Level.CENTER + (Level.SIZE / 2); fy += Block.SIZE)
//		{
//			for(float fx = Level.CENTER - (Level.SIZE / 2); fx < Level.CENTER + (Level.SIZE / 2); fx += Block.SIZE)
//			{
//				if(x >= fx && x <= fx + Block.SIZE && y >= fy && y <= fy + Block.SIZE)
//				{
//					Workbench workbench = new Workbench();
//					workbench.setPosition(fx, fy);
//					level.addObject(workbench);
//					
//					slot.removeItem(1);
//					
//					break;
//				}
//			}
//		}
	}
	
	@Override
	public void render(Screen screen, float x, float y, float size, boolean inCam, float rot, float alpha)
	{
		if(inCam) screen.render(Sprites.items.getSprite(48, SPRITES_CRAFTING_STATIONS_Y, 16, 16), x, y, size, size, rot, alpha);
		else screen.renderGUI(Sprites.items.getSprite(48, SPRITES_CRAFTING_STATIONS_Y, 16, 16), x, y, size, size, rot, alpha);
	}
	
//	@Override
//	public BufferedImage getShadow()
//	{
//		return SpriteFilter.getShadowStanding(Sprites.items.getSprite(0, 304, 16, 16));
//	}

}
