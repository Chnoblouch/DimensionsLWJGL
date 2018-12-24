package game.item.special;

import engine.gfx.Sprites;
import engine.rendering.Screen;
import game.TextLoader;
import game.creature.Player;
import game.creature.monster.MonsterGiantSlime;
import game.gui.inventory.InventorySlotRenderable;
import game.item.Item;
import game.levels.Level;
import game.levels.LevelSkyWorld;

public class ItemShimmeringSlime
extends Item {
	
	public ItemShimmeringSlime()
	{
		setItemID(ID_SHIMMERING_SLIME);
		setHand(BOTH_HANDS);
		setName(TextLoader.getText("item_shimmering_slime"));
		setDescription(TextLoader.getText("item_descr_shimmering_slime"));
	}
	
	@Override
	public void use(float x, float y, Level level, Player player, InventorySlotRenderable slot)
	{
		if(level instanceof LevelSkyWorld) return;
		
		MonsterGiantSlime giantSlime = new MonsterGiantSlime();
		giantSlime.setPosition(player.getX(), player.getY() + 1024);
		level.addObject(giantSlime);
		
		player.impressed();
		
		slot.removeItem(1);
	}
	
	@Override
	public void render(Screen screen, float x, float y, float size, boolean inCam, float rot, float alpha)
	{
		if(inCam) screen.render(Sprites.items.getSprite(0, SPRITES_SPECIAL_Y, 16, 16), x, y, size, size, rot, alpha);
		else screen.renderGUI(Sprites.items.getSprite(0, SPRITES_SPECIAL_Y, 16, 16), x, y, size, size, rot, alpha);
	}
	
//	@Override
//	public BufferedImage getShadow()
//	{
//		return SpriteFilter.getShadowStanding(Sprites.items.getSprite(0, 336, 16, 16));
//	}

}
