package game.effects;

import engine.gfx.Sprites;
import engine.rendering.Screen;
import game.TextLoader;

public class EffectRegeneration
extends Effect {

	public EffectRegeneration(float durationLeft) 
	{
		super(durationLeft);
		setEffectID(ID_REGENERATION);
		setName(TextLoader.getText("effect_regeneration"));
	}
	
	@Override
	public void render(Screen screen, float x, float mouseX, float mouseY)
	{
		screen.renderGUI(Sprites.effects.getSprite(24, 0, 24, 24), x, ICON_Y, 96, 96, 0, 1);
	}
	
}
