package game.gui.inventory;

import engine.gfx.Sprites;
import engine.rendering.Screen;
import game.saving.SaveInformation;
import game.saving.SaveManager;

public class SaveButton {
	
	private Inventory inventory;
	private int x, y;
	
	private boolean pressed = false;
	private boolean saved = false;
	
	public SaveButton(Inventory inventory, int x, int y)
	{
		this.inventory = inventory;
		this.x = x;
		this.y = y;
	}
	
	public boolean isMouseOn()
	{
		double mx = inventory.game.getInput().getMouseX();
		double my = inventory.game.getInput().getMouseY();
		return mx >= x && mx <= x + 96 && my >= y && my <= y + 96;
	}
	
	public void mousePressed(int button)
	{
		if(isMouseOn()) pressed = true;
	}
	
	public void mouseReleased(int button)
	{
		if(isMouseOn())
		{
			SaveManager.save(SaveInformation.convertGame(inventory.game));
			saved = true;
		}
		
		pressed = false;
	}
	
	public void render(Screen screen)
	{
		screen.renderGUI(Sprites.buttons.getSprite(pressed ? 24 : 0, saved ? 24 : 0, 24, 24), x, y, 96, 96, 0, 1);
	}

}
