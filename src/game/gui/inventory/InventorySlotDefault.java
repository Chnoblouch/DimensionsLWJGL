package game.gui.inventory;

import engine.gfx.Font;
import engine.gfx.Sprites;
import engine.rendering.Screen;

public class InventorySlotDefault 
extends InventorySlotRenderable {
	
	private int iconX = 0;
	private int x, y;

	public InventorySlotDefault(int id, int x, int y) 
	{
		super(id);
		
		this.x = x;
		this.y = y;
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
		}
	}
	
	@Override
	public boolean doRenderInfo()
	{
		return true;
	}
}
