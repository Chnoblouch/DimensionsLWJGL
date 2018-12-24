package game.item.bows;

import java.util.Random;

import engine.gfx.Sounds;
import engine.gfx.Sprites;
import engine.rendering.Screen;
import game.TextLoader;
import game.creature.Player;
import game.gui.inventory.InventorySlotRenderable;
import game.item.Item;
import game.levels.Level;
import game.projectiles.FlyingArrow;

public class ItemBow
extends Item {
	
	public ItemBow()
	{
		setItemID(ID_BOW);
		setHand(MAIN_HAND);
		setName(TextLoader.getText("item_bow"));
		setDescription(TextLoader.getText("ranged_damage") + ": 12");
	}
	
	@Override
	public void renderInHand(Screen screen, Player player, int hand)
	{
		screen.render(Sprites.bowInHand.getSprite(player.sx, player.sy, 64, 64), player.getX(), player.getY(), 256, 256, 0, 1);
	}
	
	@Override
	public void use(float x, float y, Level level, Player player, InventorySlotRenderable slot)
	{
		if(!player.inventory.hasEnough(Item.ID_ARROW, 1) || player.shooting) return;
		
		player.aiming = true;
	}
	
	@Override
	public void release(float x, float y, Level level, Player player, InventorySlotRenderable slot)
	{
		if(!player.inventory.hasEnough(Item.ID_ARROW, 1) || player.shooting) return;
		
		if(!player.canShoot)
		{
			player.aiming = false;
			player.aimTimer.reset();
			return;
		}
		
		FlyingArrow arrow = new FlyingArrow();
		arrow.setPosition(player.getX() + 128 - 32, player.getY() + 128 - 32);
		arrow.setAngle(player.getAngleToMouse() - 6 + new Random().nextInt(13));
		level.addObject(arrow);
		
		Sounds.shoot.play(1, false, arrow.getX() + 32, arrow.getY() + 32, level.game.getCamera().getX(), level.game.getCamera().getY());
		
		player.aiming = false;
		player.canShoot = false;
		player.shooting = true;
		
		player.inventory.getSlotWithEnough(Item.ID_ARROW, 1).removeItem(1);
	}
	
	@Override
	public void render(Screen screen, float x, float y, float size, boolean inCam, float rot, float alpha)
	{
		if(inCam) screen.render(Sprites.items.getSprite(0, SPRITES_BOWS_Y, 16, 16), x, y, size, size, rot, alpha);
		else screen.renderGUI(Sprites.items.getSprite(0, SPRITES_BOWS_Y, 16, 16), x, y, size, size, rot, alpha);
	}
	
//	@Override
//	public BufferedImage getShadow()
//	{
//		return SpriteFilter.getShadowStanding(Sprites.items.getSprite(0, 176, 16, 16));
//	}

}
