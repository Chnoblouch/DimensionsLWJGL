package game.gui.inventory;

import engine.gfx.Font;
import engine.gfx.Sprites;
import engine.rendering.Screen;
import game.item.Item;

public class InventorySlotArmor 
extends InventorySlotRenderable {
	
	private int iconX = 0;
	private int armor;
	private int x, y;

	public InventorySlotArmor(int id, int x, int y, int armor) 
	{
		super(id);
		
		this.x = x;
		this.y = y;
		this.armor = armor;
	}
	
	public int getX()
	{
		return inventory.getX() + x;
	}
	
	public int getY()
	{
		return inventory.getY() + y;
	}
	
	@Override
	public boolean isMouseOn()
	{
		double mx = inventory.game.getInput().getMouseX();
		double my = inventory.game.getInput().getMouseY();
		return mx >= getX() && mx <= getX() + 80 && my >= getY() && my <= getY() + 80;
	} 
	
	@Override
	public void mousePressed()
	{
		iconX = 20;
	}
	
	@Override
	public void mouseReleased()
	{
		iconX = 0;
	}
	
	@Override
	public boolean canHold(Item item, int count)
	{
		return item.isArmor() && item.getArmorID() == armor;
	}
	
	@Override
	public void render(Screen screen)
	{
		screen.renderGUI(Sprites.inventory.getSprite(iconX, 192, 20, 20), getX(), getY(), 80, 80, 0, 1);
				
		if(!isEmpty())
		{
			getItem().render(screen, getX() + 8, getY() + 8, 64, false, 0, 1);
			if(getItemCount() > 1) 
				screen.renderFont(getItemCount()+"", 
								  getX() + 80 - 4 - (Font.getTextWidth(getItemCount() + "", 32)), 
								  getY() + 80 - 32, 32,
								  Font.COLOR_WHITE, 
								  false);
		} else 
		{
			screen.renderGUI(Sprites.inventory.getSprite(140 + (armor * 20), 192, 20, 20), getX(), iconX == 0 ? getY() : getY() + 4, 80, 80, 0, 1);
		}
	}
	
	@Override
	public boolean doRenderInfo()
	{
		return true;
	}
}
