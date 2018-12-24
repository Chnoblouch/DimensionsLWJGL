package game.item.pickaxes;

import java.awt.image.BufferedImage;

import engine.gfx.Sprites;
import engine.rendering.Screen;
import game.TextLoader;
import game.item.Item;

public class ItemIronPickaxe
extends Item {
	
	public ItemIronPickaxe()
	{
		setItemID(ID_IRON_PICKAXE);
		setHand(MAIN_HAND);
		setStoneDamage(4.75f);
		setName(TextLoader.getText("item_iron_pickaxe"));
		setDescription(TextLoader.getText("stone_damage") + ": " + getStoneDamage());
	}
	
	@Override
	public void render(Screen screen, float x, float y, float size, boolean inCam, float rot, float alpha)
	{
		if(inCam) screen.render(Sprites.items.getSprite(32, SPRITES_PICKAXES_Y, 16, 16), x, y, size, size, rot, alpha);
		else screen.renderGUI(Sprites.items.getSprite(32, SPRITES_PICKAXES_Y, 16, 16), x, y, size, size, rot, alpha);
	}
	
//	@Override
//	public BufferedImage getShadow()
//	{
//		return SpriteFilter.getShadowStanding(Sprites.items.getSprite(32, 80, 16, 16));
//	}

}
