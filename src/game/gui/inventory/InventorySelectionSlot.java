package game.gui.inventory;

import engine.gfx.Font;
import engine.gfx.Sprites;
import engine.rendering.Screen;
import game.item.Item;

public class InventorySelectionSlot 
extends InventorySlotRenderable {
	
	private int hand;
	private int iconX = 40;
	private int x, y;

	public InventorySelectionSlot(int id, int x, int y, int hand)
	{
		super(id);
		
		this.x = x;
		this.y = y;
		this.hand = hand;
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
	
	@Override
	public boolean isMouseOn()
	{
		double mx = inventory.game.getInput().getMouseX();
		double my = inventory.game.getInput().getMouseY();
		return mx >= getX() && mx <= getX() + 120 && my >= getY() && my <= getY() + 120;
	}
	
	@Override
	public boolean canHold(Item item, int count)
	{
		return item != null && (item.getHand() == Item.BOTH_HANDS || item.getHand() == hand);
	}
	
	@Override
	public void mousePressed()
	{
		iconX = 70;
	}
	
	@Override
	public void mouseReleased()
	{
		iconX = 40;
	}
	
	@Override
	public void render(Screen screen)
	{
		screen.renderGUI(Sprites.inventory.getSprite(iconX, 192, 30, 30), getX(), getY(), 120, 120, 0, 1);
				
		if(!isEmpty())
		{
			getItem().render(screen, getX() + 12, getY() + 12, 96, false, 0, 1);
			if(getItemCount() > 1) 
				screen.renderFont(getItemCount() + "", 
								  getX() + 120 - 16 -(Font.getTextWidth(getItemCount() + "", 32)), 
								  getY() + 120 - 48, 32,
								  Font.COLOR_WHITE, 
								  false);
		}
	}

}
