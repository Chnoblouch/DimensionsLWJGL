package game.gui.inventory;

import game.item.Item;

public class InventorySlot {
	
	private int id;
	private Item item;
	private int count;
	
	public InventorySlot(int id)
	{
		this.id = id;
	}
	
	public int getID()
	{
		return id;
	}
	
	public void setItem(Item item, int count)
	{
		this.item = item;
		this.count = count;
	}
	
	public Item getItem()
	{
		return item;
	}
	
	public void setItemCount(int count)
	{
		this.count = count;
	}
	
	public void addItem(int count)
	{
		this.count += count;
	}
	
	public void removeItem(int count)
	{
		this.count -= count;
		if(count <= 0) clear();
	}
	
	public int getItemCount()
	{
		return count;
	}
	
	public void clear()
	{
		item = null;
		count = 0;
	}
	
	public boolean isEmpty()
	{
		return item == null || count < 1;
	}

}
