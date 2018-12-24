package game.obj;

import engine.gfx.Sprites;
import engine.rendering.Screen;
import game.GameObject;
import game.creature.Player;
import game.gui.inventory.ChestInventory;
import game.gui.inventory.menus.InventoryMenu;
import utils.Block;
import utils.Hitbox;
import utils.HitboxFactory;

public class Chest 
extends GameObject {
	
	private ChestInventory inventory;
	
	public Chest()
	{
		inventory = new ChestInventory();
	}
	
	public ChestInventory getInventory()
	{
		return inventory;
	}
	
	@Override
	public void interactWith(Player player, boolean mouseOn)
	{
		if(!mouseOn) return;
		
		player.inventory.changeVisibility(InventoryMenu.MENU_CHEST);
		player.inventory.chestMenu.setChest(inventory);
	}
	
	@Override
	public int getZIndex()
	{
		return (int) (getY() + (Block.SIZE * 2) - 24);
	}
	
	@Override
	public void render(Screen screen)
	{
		if(screen.isInside(getX(), getY(), Block.SIZE * 4, Block.SIZE * 4))
		{						
//			screen.render(SpriteFilter.getShadowStanding(sprite), getX() - 16, getY(), Block.SIZE * 4, Block.SIZE * 2, 0, 0.25); // add back in
			screen.render(Sprites.craftingstations.getSprite(48, 48, 48, 48), getX(), getY(), Block.SIZE * 2, Block.SIZE * 2, 0, 1);
		}
	}
	
	@Override
	public boolean inCollisionSpace()
	{
		return true;
	}
	
	@Override
	public boolean doBlock(GameObject o)
	{
		return true;
	}
	
	@Override
	public Hitbox getHitbox()
	{
		return HitboxFactory.create(getX() + (Block.SIZE / 2) - 24, getY() + Block.SIZE, Block.SIZE + 24, Block.SIZE / 2);
	}
	
	@Override
	public Hitbox getClickHitbox()
	{
		return HitboxFactory.create(getX(), getY(), Block.SIZE * 2, Block.SIZE * 2);
	}

}
