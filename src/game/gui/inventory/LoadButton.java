package game.gui.inventory;

import engine.gfx.Sprites;
import engine.rendering.Screen;
import game.Game;
import game.saving.SaveInformation;
import game.saving.SaveManager;

public class LoadButton {
	
	private Inventory inventory;
	private int x, y;
	
	private boolean pressed = false;
	
	public LoadButton(Inventory inventory, int x, int y)
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
		if(isMouseOn()) inventory.game.load();
		pressed = false;
	}
	
	public void render(Screen screen)
	{
		screen.renderGUI(Sprites.buttons.getSprite(pressed ? 72 : 48, 0, 24, 24), x, y, 96, 96, 0, 1);
	}

}
