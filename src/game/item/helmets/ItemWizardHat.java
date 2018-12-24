package game.item.helmets;

import engine.gfx.Sprites;
import engine.rendering.Screen;
import game.TextLoader;
import game.creature.Player;
import game.item.Item;

public class ItemWizardHat
extends Item {
	
	public ItemWizardHat()
	{
		setItemID(ID_WIZARD_HAT);
		setHand(BOTH_HANDS);
		setArmor(true);
		setArmorID(ARMOR_HELMET);
		setProtection(3);
		setName(TextLoader.getText("item_wizard_hat"));
		setDescription(TextLoader.getText("protection") + ": " + getProtection());
	}
	
	@Override
	public void renderOnBody(Screen screen, Player player)
	{
		screen.render(Sprites.wizardHatOnBody.getSprite(player.sx, player.sy, 64, 64), player.getX(), player.getY(), 256, 256, 0, 1);
	}
	
	@Override
	public void render(Screen screen, float x, float y, float size, boolean inCam, float rot, float alpha)
	{
		if(inCam) screen.render(Sprites.items.getSprite(32, SPRITES_HELMETS_Y, 16, 16), x, y, size, size, rot, alpha);
		else screen.renderGUI(Sprites.items.getSprite(32, SPRITES_HELMETS_Y, 16, 16), x, y, size, size, rot, alpha);
	}
	
//	@Override
//	public BufferedImage getShadow()
//	{
//		return SpriteFilter.getShadowStanding(Sprites.items.getSprite(32, 224, 16, 16));
//	}

}
