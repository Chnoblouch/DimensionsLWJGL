package game.item.pickaxes;

import engine.gfx.Sprites;
import engine.rendering.Screen;
import game.TextLoader;
import game.item.Item;

public class ItemStonePickaxe
extends Item {
	
	public ItemStonePickaxe()
	{
		setItemID(ID_STONE_PICKAXE);
		setHand(MAIN_HAND);
		setStoneDamage(3.25f);
		setName(TextLoader.getText("item_stone_pickaxe"));
		setDescription(TextLoader.getText("stone_damage") + ": " + getStoneDamage());
	}
	
	@Override
	public void render(Screen screen, float x, float y, float size, boolean inCam, float rot, float alpha)
	{
		if(inCam) screen.render(Sprites.items.getSprite(16, SPRITES_PICKAXES_Y, 16, 16), x, y, size, size, rot, alpha);
		else screen.renderGUI(Sprites.items.getSprite(16, SPRITES_PICKAXES_Y, 16, 16), x, y, size, size, rot, alpha);
	}
	
//	@Override
//	public BufferedImage getShadow()
//	{
//		return SpriteFilter.getShadowStanding(Sprites.items.getSprite(16, 80, 16, 16));
//	}

}
