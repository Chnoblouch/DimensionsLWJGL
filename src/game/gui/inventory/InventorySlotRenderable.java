package game.gui.inventory;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import engine.gfx.Font;
import engine.gfx.Sprites;
import engine.rendering.Screen;
import game.item.Item;

public class InventorySlotRenderable
extends InventorySlot {
	
	public Inventory inventory;
			
	public InventorySlotRenderable(int id)
	{
		super(id);
	}
	
	public void setInventory(Inventory inventory)
	{
		this.inventory = inventory;
	}
	
	public Inventory getInventory()
	{
		return inventory;
	}
	
	public boolean canHold(Item item, int count)
	{
		return true;
	}
	
	public boolean isMouseOn()
	{
		return false;
	}
	
	public void mousePressed(int button)
	{			
		if(!inventory.visible) return;
			
		mousePressed();
		
		if(!(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && this instanceof InventorySlotDefault))
		{
			if(!inventory.isItemOnMouse())
			{
				inventory.itemOnMouse = getItem();
				inventory.itemOnMouseCount = getItemCount();
				clear();
								
				return;
			} else 
			{
				if(!canHold(inventory.itemOnMouse, inventory.itemOnMouseCount)) return;
				
				if(isEmpty())
				{
					setItem(inventory.itemOnMouse, inventory.itemOnMouseCount);
					
					inventory.itemOnMouse = null;
					inventory.itemOnMouseCount = 0;
				} else 
				{
					if(getItem().getItemID() == inventory.itemOnMouse.getItemID())
					{
						addItem(inventory.itemOnMouseCount);						
						inventory.itemOnMouse = null;
						inventory.itemOnMouseCount = 0;
					} else 
					{
						Item oldItem = getItem();
						int oldCount = getItemCount();
						
						setItem(inventory.itemOnMouse, inventory.itemOnMouseCount);
						
						inventory.itemOnMouse = oldItem;
						inventory.itemOnMouseCount = oldCount;
					}
				}
				
				return;
			}	
		} else 
		{
			InventorySlotRenderable target = null;
			if(button == 0) target = inventory.getMainHandSlot();
			else if(button == 1) target = inventory.getOffHandSlot();
			
			if(target.isEmpty())
			{
				if(target.canHold(getItem(), getItemCount())) 
				{
					target.setItem(getItem(), getItemCount());
					clear();
				}
			} else
			{
				if(target.canHold(getItem(), getItemCount()) && canHold(target.getItem(), target.getItemCount()))
				{
					Item oldItem = getItem();
					int oldCount = getItemCount();
					
					setItem(target.getItem(), target.getItemCount());
					target.setItem(oldItem, oldCount);
				}
			}
		}
	}

	public void mouseReleased(int button)
	{
		if(!inventory.visible) return;
		mouseReleased();
	}
	
	public void renderInfo(Screen screen)
	{
		int mx = (int) inventory.game.getInput().getMouseX();
		int my = (int) inventory.game.getInput().getMouseY();
		
		int dmx = 48; // x distance to mouse
		int dmy = -48; // y distance to mouse
		int fontSize = 32;
				
		String longestPart = getItem().getName();

		if(getItem().getDescription() != null) 
		{
			int longestPartLength = longestPart.length();
			for(int i = 0; i < getItem().getDescription().length; i++) 
			{
				String text = getItem().getDescription()[i];
				if(text.length() > longestPartLength)
				{
					longestPartLength = text.length();
					longestPart = text;
				}
			}
		}
		
		int bx = mx + dmx; // border x;
		int by = my + dmy; // border y;
		int bw = (int) Math.ceil(Font.getTextWidth(longestPart, fontSize)); // border with
		int bh = getItem().getDescription() == null ? 32 : 32 + getItem().getDescription().length * 32; // border height
						
		if(bx + bw >= Display.getWidth()) bx -= bw + dmx * 2;
		
		screen.renderGUI(Sprites.itemInfo.getSprite(0, 0, 8, 8), bx, by, 32, 32, 0, 1);		
		screen.renderGUI(Sprites.itemInfo.getSprite(16, 0, 8, 8), bx + bw, by, 32, 32, 0, 1);
		screen.renderGUI(Sprites.itemInfo.getSprite(0, 16, 8, 8), bx, by + bh, 32, 32, 0, 1);		
		screen.renderGUI(Sprites.itemInfo.getSprite(16, 16, 8, 8), bx + bw, by + bh, 32, 32, 0, 1);
		
		for(int i = 0; i < bw - 32; i+= 32) 
		{ 
			screen.renderGUI(Sprites.itemInfo.getSprite(8, 0, 8, 8), bx + 32 + i, by, 32, 32, 0, 1); 
			screen.renderGUI(Sprites.itemInfo.getSprite(8, 16, 8, 8), bx + 32 + i, by + bh, 32, 32, 0, 1); 
		}
		
		for(int i = 0; i < bh - 32; i+= 32) 
		{ 
			screen.renderGUI(Sprites.itemInfo.getSprite(0, 8, 8, 8), bx, by + i + 32, 32, 32, 0, 1); 
			screen.renderGUI(Sprites.itemInfo.getSprite(16, 8, 8, 8), bx + bw, by + 32 + i, 32, 32, 0, 1); 
		}
		
		for(int fy = by + 32; fy < by + bh; fy+= 32)
		{
			for(int fx = bx + 32; fx < bx + bw; fx+= 32)
			{
				screen.renderGUI(Sprites.itemInfo.getSprite(8, 8, 8, 8), fx, fy, 32, 32, 0, 1); 
			}
		}
		
		screen.renderFont(getItem().getName(), bx + 16, by + 16, fontSize, Font.COLOR_WHITE, false);
		
		if(getItem().getDescription() != null)
		{
			for(int i = 0; i < getItem().getDescription().length; i++)
			{
				screen.renderFont(getItem().getDescription()[i], bx + 16, by + 48 + i * 32, fontSize, Font.COLOR_WHITE, false);
			}
		}
	}
	
	public void render(Screen screen) {}
	public boolean doRenderInfo() { return false; }
	public void mousePressed() {}
	public void mouseReleased() {}

}
