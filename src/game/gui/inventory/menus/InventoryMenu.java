package game.gui.inventory.menus;

import org.lwjgl.opengl.Display;

import engine.rendering.Screen;
import game.gui.inventory.Inventory;

public class InventoryMenu {
	
	public Inventory inventory;
	
	private int id;
	public static final int MENU_HAND_CRAFTING = 0;
	public static final int MENU_WORKBENCH_CRAFTING = 1;
	public static final int MENU_OVEN_CRAFTING = 2;
	public static final int MENU_ANVIL_CRAFTING = 3;
	public static final int MENU_ALCHEMY_CRAFTING = 4;
	public static final int MENU_COOKING_POT_CRAFTING = 5;
	public static final int MENU_CHEST = 6;
	
	private int inventoryX = Display.getWidth() / 2 - 384, inventoryY = Display.getHeight() / 2 - 384;
	
	public InventoryMenu(Inventory inventory)
	{
		this.inventory = inventory;
	}
	
	public Inventory getInventory()
	{
		return inventory;
	}
	
	public void setMenuID(int id)
	{
		this.id = id;
	}
	
	public int getMenuID()
	{
		return id;
	}
	
	public void setInventoryPosition(int x, int y)
	{
		this.inventoryX = x;
		this.inventoryY = y;
	}
	
	public int getInventoryX()
	{
		return inventoryX;
	}
	
	public int getInventoryY()
	{
		return inventoryY;
	}
	
	public void mousePressed(int button) {}
	public void mouseReleased(int button) {}
	public void render(Screen screen) {}
	public void renderInfo(Screen screen) {}
}
