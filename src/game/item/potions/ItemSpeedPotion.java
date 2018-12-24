package game.item.potions;

import engine.gfx.Sprites;
import engine.rendering.Screen;
import game.TextLoader;
import game.creature.Player;
import game.effects.EffectSpeed;
import game.gui.inventory.InventorySlotRenderable;
import game.item.Item;
import game.levels.Level;
import utils.Time;

public class ItemSpeedPotion
extends Item {
	
	public ItemSpeedPotion()
	{
		setItemID(ID_SPEED_POTION);
		setHand(MAIN_HAND);
		setName(TextLoader.getText("item_speed_potion"));
		setDescription(TextLoader.getText("item_descr_speed_potion"));
	}
	
	@Override
	public void use(float x, float y, Level level, Player player, InventorySlotRenderable slot)
	{
		player.addEffect(new EffectSpeed(Time.secondsInTicks(20)));
		slot.removeItem(1);
	}
	
	@Override
	public void render(Screen screen, float x, float y, float size, boolean inCam, float rot, float alpha)
	{
		if(inCam) screen.render(Sprites.items.getSprite(32, SPRITES_POTIONS_Y, 16, 16), x, y, size, size, rot, alpha);
		else screen.renderGUI(Sprites.items.getSprite(32, SPRITES_POTIONS_Y, 16, 16), x, y, size, size, rot, alpha);
	}
	
//	@Override
//	public BufferedImage getShadow()
//	{
//		return SpriteFilter.getShadowStanding(SpriteSheet.items.getSprite(32, 320, 16, 16));
//	}

}
