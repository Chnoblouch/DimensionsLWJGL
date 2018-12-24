package game.item.chestplates;

import engine.gfx.Sprites;
import engine.rendering.Screen;
import game.TextLoader;
import game.creature.Player;
import game.item.Item;

public class ItemWizardRobe
extends Item {
	
	public ItemWizardRobe()
	{
		setItemID(ID_WIZARD_ROBE);
		setHand(BOTH_HANDS);
		setArmor(true);
		setArmorID(ARMOR_CHESTPLATE);
		setProtection(6);
		setName(TextLoader.getText("item_wizard_robe"));
		setDescription(TextLoader.getText("protection") + ": " + getProtection());
	}
	
	@Override
	public void renderOnBody(Screen screen, Player player)
	{
		screen.render(Sprites.wizardRobeOnBody.getSprite(player.sx, player.sy, 64, 64), player.getX(), player.getY(), 256, 256, 0, 1);
	}
	
	@Override
	public void render(Screen screen, float x, float y, float size, boolean inCam, float rot, float alpha)
	{
		if(inCam) screen.render(Sprites.items.getSprite(32, SPRITES_CHESTPLATES_Y, 16, 16), x, y, size, size, rot, alpha);
		else screen.renderGUI(Sprites.items.getSprite(32, SPRITES_CHESTPLATES_Y, 16, 16), x, y, size, size, rot, alpha);
	}
	
//	@Override
//	public BufferedImage getShadow()
//	{
//		return SpriteFilter.getShadowStanding(Sprites.items.getSprite(32, 240, 16, 16));
//	}

}
