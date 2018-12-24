package game.item.pickaxes;

import engine.gfx.Sprites;
import engine.rendering.Screen;
import game.TextLoader;
import game.item.Item;

public class ItemWoodPickaxe
extends Item {
	
	public ItemWoodPickaxe()
	{
		setItemID(ID_WOOD_PICKAXE);
		setHand(MAIN_HAND);
		setStoneDamage(2);
		setName(TextLoader.getText("item_wood_pickaxe"));
		setDescription(TextLoader.getText("stone_damage") + ": " + getStoneDamage());
	}
	
	@Override
	public void render(Screen screen, float x, float y, float size, boolean inCam, float rot, float alpha)
	{
		if(inCam) screen.render(Sprites.items.getSprite(0, SPRITES_PICKAXES_Y, 16, 16), x, y, size, size, rot, alpha);
		else screen.renderGUI(Sprites.items.getSprite(0, SPRITES_PICKAXES_Y, 16, 16), x, y, size, size, rot, alpha);
	}
	
//	@Override
//	public BufferedImage getShadow()
//	{
//		return SpriteFilter.getShadowStanding(Sprites.items.getSprite(0, 80, 16, 16));
//	}

}
