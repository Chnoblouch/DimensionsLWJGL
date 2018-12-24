package game.item;

import java.awt.image.BufferedImage;

import engine.rendering.Screen;
import game.Game;
import game.creature.Player;
import game.gui.inventory.InventorySlotRenderable;
import game.levels.Level;

public class Item {
	
	private static int lastID = 0;
	
	private int id;
	public static final int ID_WOOD = lastID ++;
	public static final int ID_STONE = lastID ++;
	public static final int ID_SLIME = lastID ++;
	public static final int ID_APPLE = lastID ++;
	public static final int ID_WOOD_AXE = lastID ++;
	public static final int ID_WOOD_PICKAXE = lastID ++;
	public static final int ID_WOOD_SHOVEL = lastID ++;
	public static final int ID_WOOD_SWORD = lastID ++;
	public static final int ID_STONE_AXE = lastID ++;
	public static final int ID_STONE_PICKAXE = lastID ++;
	public static final int ID_STONE_SHOVEL = lastID ++;
	public static final int ID_STONE_SWORD = lastID ++;
	public static final int ID_MEAT = lastID ++;
	public static final int ID_WORKBENCH = lastID ++;
	public static final int ID_OVEN = lastID ++;
	public static final int ID_ANVIL = lastID ++;
	public static final int ID_IRON_ORE = lastID ++;
	public static final int ID_IRON_INGOT = lastID ++;
	public static final int ID_IRON_AXE = lastID ++;
	public static final int ID_IRON_PICKAXE = lastID ++;
	public static final int ID_IRON_SHOVEL = lastID ++;
	public static final int ID_IRON_SWORD = lastID ++;
	public static final int ID_IRON_HOE = lastID ++;
	public static final int ID_IRON_HAMMER = lastID ++;
	public static final int ID_IRON_HELMET = lastID ++;
	public static final int ID_IRON_CHESTPLATE = lastID ++;
	public static final int ID_IRON_LEGGINGS = lastID ++;
	public static final int ID_IRON_BOOTS = lastID ++;
	public static final int ID_IRON_SHIELD = lastID ++;
	public static final int ID_FEATHER = lastID ++;
	public static final int ID_EYE_PIECE = lastID ++;
	public static final int ID_BOW = lastID ++;
	public static final int ID_ARROW = lastID ++;
	public static final int ID_TAYKOLOS = lastID ++;
	public static final int ID_FROST_PEARL = lastID ++;
	public static final int ID_ALCHEMY_TABLE = lastID ++;
	public static final int ID_HEALTH_POTION = lastID ++;
	public static final int ID_NIGHT_HERB = lastID ++;
	public static final int ID_SPEED_POTION = lastID ++;
	public static final int ID_MUSHROOM = lastID ++;
	public static final int ID_RED_STONE = lastID ++;
	public static final int ID_BLUE_STONE = lastID ++;
	public static final int ID_SHIMMERING_SLIME = lastID ++;
	public static final int ID_COOKED_MEAT = lastID ++;
	public static final int ID_GOLD_ORE = lastID ++;
	public static final int ID_GOLD_INGOT = lastID ++;
	public static final int ID_GOLD_AXE = lastID ++;
	public static final int ID_GOLD_PICKAXE = lastID ++;
	public static final int ID_GOLD_SHOVEL = lastID ++;
	public static final int ID_GOLD_SWORD = lastID ++;
	public static final int ID_GOLD_HOE = lastID ++;
	public static final int ID_GOLD_HAMMER = lastID ++;
	public static final int ID_GOLD_HELMET = lastID ++;
	public static final int ID_GOLD_CHESTPLATE = lastID ++;
	public static final int ID_GOLD_LEGGINGS = lastID ++;
	public static final int ID_GOLD_BOOTS = lastID ++;
	public static final int ID_GOLD_SHIELD = lastID ++;
	public static final int ID_WIZARD_HAT = lastID ++;
	public static final int ID_WIZARD_ROBE = lastID ++;
	public static final int ID_WIZARD_TROUSERS = lastID ++;
	public static final int ID_WIZARD_BOOTS = lastID ++;
	public static final int ID_CRYSTAL_STAFF = lastID ++;
	public static final int ID_WOOL = lastID ++;
	public static final int ID_CAPE_BLANK = lastID ++;
	public static final int ID_WOOL_PULLOVER = lastID ++;
	public static final int ID_LOBIRE_MASK = lastID ++;
	public static final int ID_COOKING_POT = lastID ++;
	public static final int ID_BAKED_APPLE = lastID ++;
	public static final int ID_ACORN = lastID ++;
	public static final int ID_SPYGLASS = lastID ++;
	public static final int ID_CHEST = lastID ++;
	
	private int hand = 2;
	public static final int MAIN_HAND = 0;
	public static final int OFF_HAND = 1;
	public static final int BOTH_HANDS = 2;

	private String name;
	private String[] description;
	
	private float attackDmg = 1;
	private float woodDmg = 1;
	private float stoneDmg = 1;
	private float protection = 0;
	
	private boolean armor = false;
	private int armorID = 0;
	public static final int ARMOR_HELMET = 0;
	public static final int ARMOR_CHESTPLATE = 1;
	public static final int ARMOR_LEGGINGS = 2;
	public static final int ARMOR_BOOTS = 3;
	public static final int ARMOR_BACK = 4;
	
	private static int lastResourcesY = -16;
	protected static final int SPRITES_RESOURCES_Y = lastResourcesY += 16;
	protected static final int SPRITES_ORES_Y = lastResourcesY += 16;
	protected static final int SPRITES_CREATURE_DROPS_Y = lastResourcesY += 16;
	protected static final int SPRITES_FOOD_Y = lastResourcesY += 16;
	protected static final int SPRITES_AXES_Y = lastResourcesY += 16;
	protected static final int SPRITES_PICKAXES_Y = lastResourcesY += 16;
	protected static final int SPRITES_SHOVELS_Y = lastResourcesY += 16;
	protected static final int SPRITES_SWORDS_Y = lastResourcesY += 16;
	protected static final int SPRITES_HOES_Y = lastResourcesY += 16;
	protected static final int SPRITES_HAMMERS_Y = lastResourcesY += 16;
	protected static final int SPRITES_TOOLS_Y = lastResourcesY += 16;
	protected static final int SPRITES_SHIELDS_Y = lastResourcesY += 16;
	protected static final int SPRITES_BOWS_Y = lastResourcesY += 16;
	protected static final int SPRITES_ARROWS_Y = lastResourcesY += 16;
	protected static final int SPRITES_MAGIC_WEAPONS_Y = lastResourcesY += 16;
	protected static final int SPRITES_HELMETS_Y = lastResourcesY += 16;
	protected static final int SPRITES_CHESTPLATES_Y = lastResourcesY += 16;
	protected static final int SPRITES_LEGGINGS_Y = lastResourcesY += 16;
	protected static final int SPRITES_BOOTS_Y = lastResourcesY += 16;
	protected static final int SPRITES_BACK_ITEMS_Y = lastResourcesY += 16;
	protected static final int SPRITES_INGOTS_Y = lastResourcesY += 16;
	protected static final int SPRITES_CRAFTING_STATIONS_Y = lastResourcesY += 16;
	protected static final int SPRITES_POTIONS_Y = lastResourcesY += 16;
	protected static final int SPRITES_SPECIAL_Y = lastResourcesY += 16;
	
	public void setItemID(int id) {	this.id = id; }
	public int getItemID() { return id; }
	
	public void setHand(int hand) { this.hand = hand; }
	public int getHand() { return hand;	}
	
	public void setName(String name) { this.name = name; }
	public String getName() { return name; }
	public void setDescription(String... description) { this.description = description; }
	public String[] getDescription() { return description; }
	
	public void setAttackDamage(float dmg) { this.attackDmg = dmg; }
	public float getAttackDamage() { return attackDmg; }
	public void setWoodDamage(float dmg) { this.woodDmg = dmg; }
	public float getWoodDamage() { return woodDmg; }
	public void setStoneDamage(float dmg) { this.stoneDmg = dmg; }
	public float getStoneDamage() { return stoneDmg; }
	public void setProtection(float protection) { this.protection = protection; }
	public float getProtection() { return protection; }
	
	public void setArmor(boolean armor) { this.armor = armor; }
	public boolean isArmor() { return armor; }
	public void setArmorID(int id) { this.armorID = id; }
	public int getArmorID() { return armorID; }
	
	public void use(float x, float y, Level level, Player player, InventorySlotRenderable slot) 
	{
		player.useItem(x, y, this);
	}
	
	public void release(float x, float y, Level level, Player player, InventorySlotRenderable slot) {}
	public void inHand(Player player) {}
	public void renderInHand(Screen screen, Player player, int hand) {}
	public void renderOnBody(Screen screen, Player player) {}
	public void render(Screen screen, float x, float y, float size, boolean inCam, float alpha, float rot) {}
	public BufferedImage getShadow() { return null; }
	
	public void resetLanguage(String language) {}
}
