package game.gui.inventory;

import java.util.ArrayList;

import org.lwjgl.opengl.Display;

import game.item.Item;

public class ChestInventory {
	
	private ArrayList<InventorySlotDefault> slots = new ArrayList<>();
	
	public ChestInventory()
	{
		int firstSlotX = Display.getWidth() - 768 - 256 + 384 - ((6 * 100) / 2) + 10;
		int firstSlotY = 384 - ((6 * 100) / 2) + 10;
		
		int id = 199;
		
		for(int y = 0; y < 6; y++)
		{
			for(int x = 0; x < 6; x++)
			{		
				id ++;
				
				InventorySlotDefault s = new InventorySlotDefault(id, (x * 100) + firstSlotX, (y * 100) + firstSlotY);
				slots.add(s);
			}
		}
	}
	
	public void fillNextFreeSlot(Item item, int count)
	{
		for(int i = 0; i < slots.size(); i++)
		{
			InventorySlotDefault s = slots.get(i);
			if(!s.isEmpty() && s.getItem().getItemID() == item.getItemID()) 
			{
				s.addItem(count);
				return;
			}
		}
		
		for(int i = 0; i < slots.size(); i++)
		{
			InventorySlotDefault s = slots.get(i);
			if(s.isEmpty()) 
			{
				s.setItem(item, count);
				return;
			}
		}
	}
	
	public InventorySlotRenderable getSlotForID(int id)
	{
		for(int i = 0; i < slots.size(); i++)
		{
			InventorySlotRenderable s = slots.get(i);
			if(s.getID() == id) return s;
		}
		
		return null;
	}
	
	public ArrayList<InventorySlotDefault> getSlots()
	{
		return slots;
	}

}
