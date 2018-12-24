package game.item.magicweapons;

import java.awt.image.BufferedImage;

import engine.gfx.Sounds;
import engine.gfx.Sprites;
import engine.rendering.Screen;
import game.TextLoader;
import game.creature.Player;
import game.gui.inventory.InventorySlotRenderable;
import game.item.Item;
import game.levels.Level;
import game.projectiles.FlyingCrystalSplinter;
import utils.Angles;

public class ItemCrystalStaff
extends Item {
	
	public ItemCrystalStaff()
	{
		setItemID(ID_CRYSTAL_STAFF);
		setHand(MAIN_HAND);
		setName(TextLoader.getText("item_crystal_staff"));
		setDescription(TextLoader.getText("item_descr_crystal_staff"), 
					   TextLoader.getText("ranged_damage") + ": 11.0",
					   TextLoader.getText("uses_x_mana").split("/in")[0] + "7.0" + TextLoader.getText("uses_x_mana").split("/in")[1]);
	}
	
	@Override
	public void renderInHand(Screen screen, Player player, int hand)
	{
		if(player.thrownSomething) return;
		
		screen.render(Sprites.crystalStaffInHand.getSprite(player.sx, player.sy, 64, 64), player.getX(), player.getY(), 256, 256, 0, 1);
	}
	
	@Override
	public void use(float x, float y, Level level, Player player, InventorySlotRenderable slot)
	{		
		if(player.getMana() < 7) return;
				
		player.useItem(x, y, this);
		player.removeMana(7);
		
		FlyingCrystalSplinter splinter = new FlyingCrystalSplinter();
		splinter.setPosition(player.getX() + 128 - 32, player.getY() + 128 - 64);
		
		float angle = Angles.getAngle(splinter.getX() - level.game.getCamera().getX() + 32, 
									   splinter.getY() - level.game.getCamera().getY() + 32, 
									   level.game.getInput().getMouseX(), 
									   level.game.getInput().getMouseY());
		
		splinter.setAngle(angle);
		level.addObject(splinter);
		
		Sounds.crystal.play(0, false, splinter.getX() + 24, splinter.getY() + 24, level.game.getCamera().getX(), level.game.getCamera().getY());
	}
	
	@Override
	public void render(Screen screen, float x, float y, float size, boolean inCam, float rot, float alpha)
	{
		if(inCam) screen.render(Sprites.items.getSprite(16, SPRITES_MAGIC_WEAPONS_Y, 16, 16), x, y, size, size, rot, alpha);
		else screen.renderGUI(Sprites.items.getSprite(16, SPRITES_MAGIC_WEAPONS_Y, 16, 16), x, y, size, size, rot, alpha);
	}
	
//	@Override
//	public BufferedImage getShadow()
//	{
//		return SpriteFilter.getShadowStanding(Sprites.items.getSprite(16, 208, 16, 16));
//	}

}
