package game.gui.inventory;

import java.util.ArrayList;

import org.lwjgl.opengl.Display;

import engine.gfx.Font;
import engine.gfx.Sprites;
import engine.rendering.Screen;
import game.Game;
import game.gui.inventory.menus.AlchemyCraftingMenu;
import game.gui.inventory.menus.AnvilCraftingMenu;
import game.gui.inventory.menus.ChestMenu;
import game.gui.inventory.menus.CookingPotCraftingMenu;
import game.gui.inventory.menus.HandCraftingMenu;
import game.gui.inventory.menus.InventoryMenu;
import game.gui.inventory.menus.OvenCraftingMenu;
import game.gui.inventory.menus.WorkbenchCraftingMenu;
import game.item.Item;
import game.item.arrows.ItemArrow;
import game.item.axes.ItemIronAxe;
import game.item.axes.ItemWoodAxe;
import game.item.boots.ItemIronBoots;
import game.item.bows.ItemBow;
import game.item.capes.ItemCapeBlank;
import game.item.chestplates.ItemIronChestplate;
import game.item.chestplates.ItemWizardRobe;
import game.item.chestplates.ItemWoolPullover;
import game.item.craftingstations.ItemAlchemyTable;
import game.item.craftingstations.ItemAnvil;
import game.item.craftingstations.ItemChest;
import game.item.craftingstations.ItemCookingPot;
import game.item.craftingstations.ItemOven;
import game.item.craftingstations.ItemWorkbench;
import game.item.food.ItemApple;
import game.item.food.ItemMeat;
import game.item.helmets.ItemIronHelmet;
import game.item.helmets.ItemWizardHat;
import game.item.hoes.ItemIronHoe;
import game.item.leggings.ItemIronLeggings;
import game.item.magicweapons.ItemCrystalStaff;
import game.item.magicweapons.ItemTaykolos;
import game.item.ores.ItemGoldOre;
import game.item.ores.ItemIronOre;
import game.item.pickaxes.ItemIronPickaxe;
import game.item.potions.ItemSpeedPotion;
import game.item.resources.ItemStone;
import game.item.resources.ItemWood;
import game.item.shields.ItemIronShield;
import game.item.shovels.ItemIronShovel;
import game.item.special.ItemShimmeringSlime;
import game.item.swords.ItemIronSword;
import game.item.tools.ItemSpyglass;
import game.saving.SaveFile;
import utils.SafeArrayList;

public class Inventory {
	
	public boolean visible = false;
	public ArrayList<InventorySlotRenderable> defaultSlots = new SafeArrayList<>();
	public ArrayList<InventorySlotRenderable> slots = new SafeArrayList<>();
	
	public Game game;
	
	private InventoryMenu[] menus = new InventoryMenu[7];
	private int currentMenuID = 0;
	private InventoryMenu currentMenu;
	public HandCraftingMenu handCrafting;
	public WorkbenchCraftingMenu workbenchCrafting;
	public OvenCraftingMenu ovenCrafting;
	public AnvilCraftingMenu anvilCrafting;
	public AlchemyCraftingMenu alchemyCrafting;
	public CookingPotCraftingMenu cookingPotCrafting;
	public ChestMenu chestMenu;
	
	private InventorySelectionSlot mainHandItem;
	private InventorySelectionSlot offHandItem;
	
	private InventorySlotArmor armorHelmet;
	private InventorySlotArmor armorChestplate;
	private InventorySlotArmor armorLeggings;
	private InventorySlotArmor armorBoots;
	private InventorySlotArmor armorBack;
	
	private SaveButton saveButton;
	private LoadButton loadButton;
	
	public int x, y;
	public int firstSlotX, firstSlotY;
	
	public Item itemOnMouse;
	public int itemOnMouseCount;
	
	public Inventory(Game game)
	{
		this.game = game;
		
		x = Display.getWidth() / 2 - 384;
		y = Display.getHeight() / 2 - 384;
		
		firstSlotX = 384 - ((6 * 100) / 2) + 10;
		firstSlotY = 384 - ((6 * 100) / 2) + 10;
		
		handCrafting = new HandCraftingMenu(this);
		workbenchCrafting = new WorkbenchCraftingMenu(this);
		ovenCrafting = new OvenCraftingMenu(this);
		anvilCrafting = new AnvilCraftingMenu(this);
		alchemyCrafting = new AlchemyCraftingMenu(this);
		cookingPotCrafting = new CookingPotCraftingMenu(this);
		chestMenu = new ChestMenu(this);
		
		menus[0] = handCrafting;
		menus[1] = workbenchCrafting;
		menus[2] = ovenCrafting;
		menus[3] = anvilCrafting;
		menus[4] = alchemyCrafting;
		menus[5] = cookingPotCrafting;
		menus[6] = chestMenu;
		
		currentMenuID = 0;
		currentMenu = menus[currentMenuID];
		
		mainHandItem = new InventorySelectionSlot(50, Display.getWidth() - 120 - 40, 40, Item.MAIN_HAND);
		offHandItem = new InventorySelectionSlot(51, Display.getWidth() - 120 - 40, 180, Item.OFF_HAND);
		
		mainHandItem.setInventory(this);
		offHandItem.setInventory(this);
		
		slots.add(mainHandItem);
		slots.add(offHandItem);
		
		int id = -1;
		
		for(int y = 0; y < 6; y++)
		{
			for(int x = 0; x < 6; x++)
			{		
				id ++;
				
				InventorySlotDefault s = new InventorySlotDefault(id, (x * 100) + firstSlotX, (y * 100) + firstSlotY);
				s.setInventory(this);
				defaultSlots.add(s);
				slots.add(s);
			}
		}
		
		armorHelmet = new InventorySlotArmor(100, 768 + 20, firstSlotY, Item.ARMOR_HELMET);
		armorChestplate = new InventorySlotArmor(101, 768 + 20, firstSlotY + 100, Item.ARMOR_CHESTPLATE);
		armorLeggings = new InventorySlotArmor(102, 768 + 20, firstSlotY + 200, Item.ARMOR_LEGGINGS);
		armorBoots = new InventorySlotArmor(103, 768 + 20, firstSlotY + 300, Item.ARMOR_BOOTS);
		armorBack = new InventorySlotArmor(104, 768 + 20, firstSlotY + 400, Item.ARMOR_BACK);
		
		armorHelmet.setInventory(this);
		armorChestplate.setInventory(this);
		armorLeggings.setInventory(this);
		armorHelmet.setInventory(this);
		armorBoots.setInventory(this);
		armorBack.setInventory(this);
		
		slots.add(armorHelmet);
		slots.add(armorChestplate);
		slots.add(armorLeggings);
		slots.add(armorBoots);
		slots.add(armorBack);
		
		saveButton = new SaveButton(this, Display.getWidth() - 128, Display.getHeight() - 128);
		loadButton = new LoadButton(this, Display.getWidth() - 128 - 80 - 32, Display.getHeight() - 128);
		
		armorHelmet.setItem(new ItemIronHelmet(), 1);
		armorChestplate.setItem(new ItemIronChestplate(), 1);
		armorBoots.setItem(new ItemIronBoots(), 1);
		armorLeggings.setItem(new ItemIronLeggings(), 1);
		armorBack.setItem(new ItemCapeBlank(), 1);
		
//		armorChestplate.setItem(new ItemWizardRobe(), 1);
//		armorHelmet.setItem(new ItemWizardHat(), 1);
		
		fillNextFreeSlot(new ItemWood(), 500);
		fillNextFreeSlot(new ItemStone(), 500);
		fillNextFreeSlot(new ItemIronOre(), 100);
		fillNextFreeSlot(new ItemMeat(), 50);
		fillNextFreeSlot(new ItemApple(), 50);
		
		fillNextFreeSlot(new ItemIronSword(), 1);
		fillNextFreeSlot(new ItemIronShield(), 1);
		fillNextFreeSlot(new ItemIronPickaxe(), 1);
		fillNextFreeSlot(new ItemIronAxe(), 1);
		
		fillNextFreeSlot(new ItemBow(), 1);
		fillNextFreeSlot(new ItemArrow(), 100);
		
		fillNextFreeSlot(new ItemCrystalStaff(), 1);
		
		fillNextFreeSlot(new ItemWorkbench(), 1);
		fillNextFreeSlot(new ItemOven(), 1);
		fillNextFreeSlot(new ItemAnvil(), 1);
		fillNextFreeSlot(new ItemAlchemyTable(), 1);
		fillNextFreeSlot(new ItemCookingPot(), 1);
		fillNextFreeSlot(new ItemChest(), 10);
		
		fillNextFreeSlot(new ItemShimmeringSlime(), 1);
		
		fillNextFreeSlot(new ItemTaykolos(), 1);
		
		fillNextFreeSlot(new ItemWizardHat(), 1);
		fillNextFreeSlot(new ItemWizardRobe(), 1);
		
		fillNextFreeSlot(new ItemWoolPullover(), 1);
		
		fillNextFreeSlot(new ItemSpeedPotion(), 10);
		
		fillNextFreeSlot(new ItemIronHoe(), 1);
		
		getMainHandSlot().setItem(new ItemSpyglass(), 1);
		
//		getMainHandSlot().setItem(new ItemIronShovel(), 1);
		
//		clear();
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
	
	public void changeVisibility(int menu)
	{
		visible = !visible;
		currentMenuID = menu;
		currentMenu = menus[currentMenuID];
		
		if(!visible && isItemOnMouse())
		{
			game.player.drop(itemOnMouse, itemOnMouseCount);
			itemOnMouse = null;
			itemOnMouseCount = 0;
		}
		
		if(visible)
		{
			x = currentMenu.getInventoryX();
			y = currentMenu.getInventoryY();
		}
	}
	
	public void setVisible(boolean visible)
	{
		this.visible = visible;
		
		if(!visible && isItemOnMouse())
		{
			game.player.drop(itemOnMouse, itemOnMouseCount);
			itemOnMouse = null;
			itemOnMouseCount = 0;
		}
	}
	
	public void fillNextFreeSlot(Item item, int count)
	{
		for(int i = 0; i < defaultSlots.size(); i++)
		{
			InventorySlotRenderable s = defaultSlots.get(i);
			if(!s.isEmpty() && s.getItem().getItemID() == item.getItemID()) 
			{
				s.addItem(count);
				return;
			}
		}
		
		for(int i = 0; i < defaultSlots.size(); i++)
		{
			InventorySlotRenderable s = defaultSlots.get(i);
			if(s.isEmpty()) 
			{
				s.setItem(item, count);
				return;
			}
		}
	}
	
	public boolean hasEnough(int itemID, int count)
	{
		return getSlotWithEnough(itemID, count) != null;
	}
	
	public InventorySlotRenderable getSlotWithEnough(int itemID, int count)
	{
		for(int i = 0; i < defaultSlots.size(); i++)
		{
			InventorySlotRenderable s = defaultSlots.get(i);
			
			if(!s.isEmpty() && s.getItem().getItemID() == itemID && s.getItemCount() >= count)
				return s;
		}
		
		return null;
	}
	
	public boolean canHold(int itemID, int count)
	{
		for(int i = 0; i < defaultSlots.size(); i++)
		{
			InventorySlotRenderable s = defaultSlots.get(i);
			
			if(s.isEmpty() || s.getItem().getItemID() == itemID) return true;
		}
		
		return false;
	}
	
	public void clear()
	{
		for(int i = 0; i < slots.size(); i++) slots.get(i).clear();
	}
	
	public ArrayList<InventorySlotRenderable> getSlots()
	{
		return slots;
	}
	
	public InventorySlotRenderable getSlotForID(int id)
	{
		for(int i = 0; i < slots.size(); i++)
		{
			InventorySlotRenderable s = slots.get(i);
			if(s.getID() == id) return s;
		}
		
		return null;
	}
	
	public void mousePressed(int button)
	{
		for(int i = 0; i < slots.size(); i++)
		{
			InventorySlotRenderable s = slots.get(i);
			if(s.isMouseOn()) s.mousePressed(button);
		}
		
		currentMenu.mousePressed(button);
		
		saveButton.mousePressed(button);
		loadButton.mousePressed(button);
		
//		if(isItemOnMouse() && (pos.x < x - 300 || pos.x > x + 512 + 20 + 80 || pos.y < y || pos.y > y + 512))
//		{
//			game.player.drop(itemOnMouse, itemOnMouseCount);
//			itemOnMouse = null;
//			itemOnMouseCount = 0;
//		}
	}

	public void mouseReleased(int button)
	{		
		for(int i = 0; i < slots.size(); i++) slots.get(i).mouseReleased(button);
		currentMenu.mouseReleased(button);
		saveButton.mouseReleased(button);
		loadButton.mouseReleased(button);
	}
	
	public void render(Screen screen)
	{
		if(visible)
		{			
			screen.renderGUI(Sprites.inventory.getSprite(0, 0, 192, 192), x, y, 768, 768, 0, 1);
			currentMenu.render(screen);
			
			for(int i = 0; i < defaultSlots.size(); i++)
			{
				InventorySlotRenderable s = defaultSlots.get(i);
				s.render(screen);
			}
			
			armorHelmet.render(screen);
			armorChestplate.render(screen);
			armorLeggings.render(screen);
			armorBoots.render(screen);
			armorBack.render(screen);
			
			saveButton.render(screen);
			loadButton.render(screen);
			
			for(int i = 0; i < slots.size(); i++)
			{
				InventorySlotRenderable s = slots.get(i);
				if(!s.isEmpty() && itemOnMouse == null && s.isMouseOn() && s.doRenderInfo()) s.renderInfo(screen);
			}
			
			currentMenu.renderInfo(screen);
		}
		
		mainHandItem.render(screen);
		offHandItem.render(screen);
		
		if(visible)
		{
			int x = (int) game.getInput().getMouseX();
			int y = (int) game.getInput().getMouseY();
			
			if(isItemOnMouse())
			{				
				itemOnMouse.render(screen, x+8, y+8, 64, false, 0, 1);
				if(itemOnMouseCount > 1) 
					screen.renderFont(itemOnMouseCount+"", 
									  x+80-4-(Font.getTextWidth(itemOnMouseCount+"", 32)), 
									  y+80-32, 32, 
									  Font.COLOR_WHITE, 
									  false);
			}
		}
	}
	
	public SaveFile convertToSaveFile()
	{
		StringBuilder builder = new StringBuilder();
		
		for(int i = 0; i < slots.size(); i++)
		{
			InventorySlotRenderable s = slots.get(i);
			if(!s.isEmpty()) builder.append(s.getID()+";"+s.getItem().getClass().getName()+";"+s.getItemCount()+System.lineSeparator());
			else builder.append(s.getID()+";empty;0"+System.lineSeparator());
		}
		return new SaveFile("inventory", builder.toString());
	}
	
	public InventorySlotRenderable getMainHandSlot() { return mainHandItem;}
	public Item getMainHandItem() {	return getMainHandSlot().getItem(); }
	public InventorySlotRenderable getOffHandSlot() { return offHandItem; }
	public Item getOffHandItem() { return getOffHandSlot().getItem(); }
	
	public InventorySlotRenderable getHelmetSlot() { return armorHelmet; }
	public Item getHelmet() { return armorHelmet.getItem(); }
	public InventorySlotRenderable getChestplateSlot() { return armorChestplate; }
	public Item getChestplate() { return armorChestplate.getItem(); }
	public InventorySlotRenderable getLeggingsSlot() { return armorLeggings; }
	public Item getLeggings() { return armorLeggings.getItem(); }
	public InventorySlotRenderable getBootsSlot() { return armorBoots; }
	public Item getBoots() { return armorBoots.getItem(); }
	public InventorySlotRenderable getBackSlot() { return armorBack; }
	public Item getBackItem() { return armorBack.getItem(); }
	
	public boolean isItemOnMouse()
	{
		return itemOnMouse != null && itemOnMouseCount != 0;
	}
}
