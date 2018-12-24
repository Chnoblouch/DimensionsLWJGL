package game.gui.inventory.menus;

import java.util.ArrayList;

import engine.rendering.Screen;
import game.crafting.CraftingRecipes;
import game.gui.inventory.CraftingButton;
import game.gui.inventory.Inventory;
import utils.SafeArrayList;

public class AnvilCraftingMenu 
extends InventoryMenu {
	
	private ArrayList<CraftingButton> buttons = new SafeArrayList<>();
		
	public AnvilCraftingMenu(Inventory inventory)
	{
		super(inventory);
		setMenuID(MENU_ANVIL_CRAFTING);
		
		int x = -1;
		int y = 0;
		
		for(int i = 0; i < CraftingRecipes.getAnvilRecipes().size(); i++)
		{
			x ++;
			if(x >= 3) 
			{
				x = 0;
				y ++;
			}
			
			buttons.add(new CraftingButton(this, -300 + (x * 85), inventory.firstSlotY + (y * 85), 
										   CraftingRecipes.getAnvilRecipes().get(i)));
		}
	}
	
	@Override
	public void mousePressed(int button)
	{
		for(int i = 0; i < buttons.size(); i++) 
		{
			CraftingButton b = buttons.get(i);
			if(b.isMouseOn()) b.mousePressed(button);
		}
	}
	
	@Override
	public void mouseReleased(int button)
	{
		for(int i = 0; i < buttons.size(); i++) { buttons.get(i).mouseReleased(button); }
	}
	
	@Override
	public void render(Screen screen)
	{
		for(int i = 0; i < buttons.size(); i++) buttons.get(i).render(screen);
	}
	
	@Override
	public void renderInfo(Screen screen)
	{
		for(int i = 0; i < buttons.size(); i++)
		{
			CraftingButton b = buttons.get(i);
			if(b.isMouseOn()) b.renderInfo(screen);
		}
	}
}
