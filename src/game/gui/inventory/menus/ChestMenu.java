package game.gui.inventory.menus;

import org.lwjgl.opengl.Display;

import engine.gfx.Sprites;
import engine.rendering.Screen;
import game.gui.inventory.ChestInventory;
import game.gui.inventory.Inventory;
import game.gui.inventory.InventorySlotDefault;
import game.gui.inventory.InventorySlotRenderable;

public class ChestMenu 
extends InventoryMenu {
	
	private int x, y;
	
	private ChestInventory chestInventory;
			
	public ChestMenu(Inventory inventory)
	{
		super(inventory);
		
		setMenuID(MENU_CHEST);
		setInventoryPosition(128, Display.getHeight() / 2 - 384);
		
		x = Display.getWidth() - 768 - 128;
		y = Display.getHeight() / 2 - 384;
	}
	
	public void setChest(ChestInventory chestInventory)
	{
		this.chestInventory = chestInventory;
		
		for(int i = 0; i < chestInventory.getSlots().size(); i++)
		{
			InventorySlotDefault s = chestInventory.getSlots().get(i); 
			s.setInventory(inventory);
		}
	}
	
	@Override
	public void mousePressed(int button)
	{
		for(int i = 0; i < chestInventory.getSlots().size(); i++) 
		{
			InventorySlotDefault s = chestInventory.getSlots().get(i);
			if(s.isMouseOn()) s.mousePressed(button);
		}
	}
	
	@Override
	public void mouseReleased(int button)
	{
		for(int i = 0; i < chestInventory.getSlots().size(); i++) chestInventory.getSlots().get(i).mouseReleased(button);
	}
	
	@Override
	public void render(Screen screen)
	{
		screen.renderGUI(Sprites.inventory.getSprite(0, 0, 192, 192), x, y, 768, 768, 0, 1);
		for(int i = 0; i < chestInventory.getSlots().size(); i++) chestInventory.getSlots().get(i).render(screen);
	}
	
	@Override
	public void renderInfo(Screen screen)
	{
		for(int i = 0; i < chestInventory.getSlots().size(); i++)
		{
			InventorySlotDefault s = chestInventory.getSlots().get(i);
			if(!s.isEmpty() && inventory.itemOnMouse == null && s.isMouseOn() && s.doRenderInfo()) s.renderInfo(screen);
		}
	}
}
