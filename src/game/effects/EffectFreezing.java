package game.effects;

import engine.gfx.Sprites;
import engine.rendering.Screen;
import game.TextLoader;

public class EffectFreezing
extends Effect {

	public EffectFreezing(float durationLeft) 
	{
		super(durationLeft);
		setEffectID(ID_FREEZING);
		setName(TextLoader.getText("effect_freezing"));
	}
	
	@Override
	public void render(Screen screen, float x, float mouseX, float mouseY)
	{
		screen.renderGUI(Sprites.effects.getSprite(0, 24, 24, 24), x, ICON_Y, 96, 96, 0, 1);
	}
	
}
