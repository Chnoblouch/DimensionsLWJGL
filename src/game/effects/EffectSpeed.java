package game.effects;

import engine.gfx.Sprites;
import engine.rendering.Screen;
import game.TextLoader;

public class EffectSpeed
extends Effect {

	public EffectSpeed(float durationLeft) 
	{
		super(durationLeft);
		setEffectID(ID_SPEED);
		setName(TextLoader.getText("effect_speed"));
	}
	
	@Override
	public void render(Screen screen, float x, float mouseX, float mouseY)
	{
		screen.renderGUI(Sprites.effects.getSprite(0, 0, 24, 24), x, ICON_Y, 96, 96, 0, 1);
	}
	
}
