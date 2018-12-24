package game.item.swords;

import java.awt.image.BufferedImage;

import engine.gfx.Sprites;
import engine.rendering.Screen;
import game.TextLoader;
import game.creature.Player;
import game.item.Item;

public class ItemWoodSword
extends Item {
	
	public ItemWoodSword()
	{
		setItemID(ID_WOOD_SWORD);
		setHand(MAIN_HAND);
		setAttackDamage(7);
		setName(TextLoader.getText("item_wood_sword"));
		setDescription(TextLoader.getText("melee_damage") + ": " + getAttackDamage());
	}
	
	@Override
	public void renderInHand(Screen screen, Player player, int hand)
	{
		screen.render(Sprites.woodSwordInHand.getSprite(player.sx, player.sy, 64, 64), player.getX(), player.getY(), 256, 256, 0, 1);
	}
	
	@Override
	public void render(Screen screen, float x, float y, float size, boolean inCam, float rot, float alpha)
	{
		if(inCam) screen.render(Sprites.items.getSprite(0, SPRITES_SWORDS_Y, 16, 16), x, y, size, size, rot, alpha);
		else screen.renderGUI(Sprites.items.getSprite(0, SPRITES_SWORDS_Y, 16, 16), x, y, size, size, rot, alpha);
	}
	
//	@Override
//	public BufferedImage getShadow()
//	{
//		return SpriteFilter.getShadowStanding(Sprites.items.getSprite(0, 112, 16, 16));
//	}

}
