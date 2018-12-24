package game.item.magicweapons;

import java.awt.image.BufferedImage;

import engine.gfx.Sprites;
import engine.rendering.Screen;
import game.TextLoader;
import game.creature.Player;
import game.gui.inventory.InventorySlotRenderable;
import game.item.Item;
import game.levels.Level;
import game.projectiles.FlyingTaykolos;

public class ItemTaykolos
extends Item {
	
	public ItemTaykolos()
	{
		setItemID(ID_TAYKOLOS);
		setHand(MAIN_HAND);
		setName(TextLoader.getText("item_taykolos"));
		setDescription(TextLoader.getText("item_descr_taykolos"), 
					   TextLoader.getText("ranged_damage") + ": 15.0",
					   TextLoader.getText("uses_x_mana").split("/in")[0] + "14.0" + TextLoader.getText("uses_x_mana").split("/in")[1]);
	}
	
	@Override
	public void renderInHand(Screen screen, Player player, int hand)
	{
		if(player.thrownSomething) return;
		
		screen.render(Sprites.taykolosInHand.getSprite(player.sx, player.sy, 64, 64), player.getX(), player.getY(), 256, 256, 0, 1);
	}
	
	@Override
	public void use(float x, float y, Level level, Player player, InventorySlotRenderable slot)
	{		
		if(player.thrownSomething || player.getMana() < 14) return;
		
		player.thrownSomething = true;
		player.removeMana(14);
		
		FlyingTaykolos taykolos = new FlyingTaykolos();
		taykolos.setPosition(player.getX() + 128 - 32, player.getY() + 128 - 32);
		taykolos.setAngle(player.getAngleToMouse());
		level.addObject(taykolos);
	}
	
	@Override
	public void render(Screen screen, float x, float y, float size, boolean inCam, float rot, float alpha)
	{
		if(inCam) screen.render(Sprites.items.getSprite(0, SPRITES_MAGIC_WEAPONS_Y, 16, 16), x, y, size, size, rot, alpha);
		else screen.renderGUI(Sprites.items.getSprite(0, SPRITES_MAGIC_WEAPONS_Y, 16, 16), x, y, size, size, rot, alpha);
	}
	
//	@Override
//	public BufferedImage getShadow()
//	{
//		return SpriteFilter.getShadowStanding(Sprites.items.getSprite(0, 208, 16, 16));
//	}

}
