package game.levels;

import java.util.ArrayList;
import java.util.Random;

import game.Game;
import game.creature.Player;
import game.creature.monster.Monster;
import game.creature.monster.MonsterLobire;
import game.creature.monster.MonsterSlime;
import game.creature.npc.NPCAnyrava;
import game.creature.npc.NPCBruno;
import game.creature.npc.NPCJessica;
import game.environment.GrassGround;
import game.environment.Tree;
import game.item.arrows.ItemArrow;
import game.item.axes.ItemGoldAxe;
import game.item.axes.ItemIronAxe;
import game.item.axes.ItemStoneAxe;
import game.item.axes.ItemWoodAxe;
import game.item.boots.ItemGoldBoots;
import game.item.boots.ItemIronBoots;
import game.item.bows.ItemBow;
import game.item.chestplates.ItemGoldChestplate;
import game.item.chestplates.ItemIronChestplate;
import game.item.chestplates.ItemWizardRobe;
import game.item.chestplates.ItemWoolPullover;
import game.item.helmets.ItemGoldHelmet;
import game.item.helmets.ItemIronHelmet;
import game.item.helmets.ItemWizardHat;
import game.item.hoes.ItemIronHoe;
import game.item.leggings.ItemGoldLeggings;
import game.item.leggings.ItemIronLeggings;
import game.item.magicweapons.ItemCrystalStaff;
import game.item.magicweapons.ItemTaykolos;
import game.item.pickaxes.ItemGoldPickaxe;
import game.item.pickaxes.ItemIronPickaxe;
import game.item.pickaxes.ItemStonePickaxe;
import game.item.pickaxes.ItemWoodPickaxe;
import game.item.shields.ItemIronShield;
import game.item.shovels.ItemIronShovel;
import game.item.swords.ItemGoldSword;
import game.item.swords.ItemIronSword;
import game.item.swords.ItemStoneSword;
import game.item.swords.ItemWoodSword;
import game.item.tools.ItemSpyglass;
import game.obj.Chest;
import game.obj.Portal;
import utils.Block;
import utils.SafeArrayList;
import utils.TimeCounter;

public class LevelOverworld 
extends Level {
	
	private Player player;
	
	private TimeCounter monsterTimer;
	
	public Tree t;

	public LevelOverworld(Game game)
	{
		super(game, 0, "dim_overworld");
		
		game.getMaps()[0].onLevel(this);
				
		if(!game.exists())
		{		
			t = new Tree();
			t.setPosition(CENTER, CENTER + Block.SIZE * 8);
			addObject(t);
			
			Portal darkworld = new Portal();
			darkworld.setPosition(CENTER, CENTER + (Block.SIZE * 12));
			darkworld.setLevel(1);
			addObject(darkworld);
			
			Portal skyworld = new Portal();
			skyworld.setPosition(CENTER + (Block.SIZE * 8), CENTER - (Block.SIZE * 16));
			skyworld.setLevel(2);
			addObject(skyworld);
			
			Portal frostworld = new Portal();
			frostworld.setPosition(CENTER + (Block.SIZE * 19), CENTER - (Block.SIZE * 3));
			frostworld.setLevel(3);
			addObject(frostworld);
			
			NPCBruno bruno = new NPCBruno();
			bruno.setPosition(CENTER - (Block.SIZE * 4), CENTER + (Block.SIZE * 8));
			addObject(bruno);
			
			NPCAnyrava anyrava = new NPCAnyrava();
			anyrava.setPosition(CENTER, CENTER + (Block.SIZE * 8));
			addObject(anyrava);
			
			NPCJessica jessica = new NPCJessica();
			jessica.setPosition(CENTER + (Block.SIZE * 4), CENTER + (Block.SIZE * 8));
			addObject(jessica);
			
			addToolChest();
			addArmorChest();
		}
		
		monsterTimer = new TimeCounter(5000, () ->
		{
			ArrayList<GrassGround> grounds = new SafeArrayList<>();
			
			for(int i = 0; i < objects.size(); i++)
			{
				if(objects.get(i) instanceof GrassGround) grounds.add((GrassGround) objects.get(i));
			}
			
			GrassGround place = grounds.get(new Random().nextInt(grounds.size()));
			
			Monster m = new Random().nextBoolean() ? new MonsterSlime() : new MonsterLobire();
			m.setPosition(place.getX(), place.getY() - m.getHitbox().h);
			addObject(m);
			
			monsterCounter ++;
			
			monsterTimer.reset();
		});
	}
	
	private void addToolChest()
	{
		Chest chest = new Chest();
		chest.setPosition(CENTER + Block.SIZE * 2, CENTER + Block.SIZE * 2);
		addObject(chest);
		
		chest.getInventory().getSlotForID(200).setItem(new ItemWoodAxe(), 1);
		chest.getInventory().getSlotForID(201).setItem(new ItemStoneAxe(), 1);
		chest.getInventory().getSlotForID(202).setItem(new ItemIronAxe(), 1);
		chest.getInventory().getSlotForID(203).setItem(new ItemGoldAxe(), 1);
		
		chest.getInventory().getSlotForID(206).setItem(new ItemWoodPickaxe(), 1);
		chest.getInventory().getSlotForID(207).setItem(new ItemStonePickaxe(), 1);
		chest.getInventory().getSlotForID(208).setItem(new ItemIronPickaxe(), 1);
		chest.getInventory().getSlotForID(209).setItem(new ItemGoldPickaxe(), 1);
		
		chest.getInventory().getSlotForID(212).setItem(new ItemIronShovel(), 1);
		chest.getInventory().getSlotForID(213).setItem(new ItemIronHoe(), 1);
		
		chest.getInventory().getSlotForID(218).setItem(new ItemWoodSword(), 1);
		chest.getInventory().getSlotForID(219).setItem(new ItemStoneSword(), 1);
		chest.getInventory().getSlotForID(220).setItem(new ItemIronSword(), 1);
		chest.getInventory().getSlotForID(221).setItem(new ItemGoldSword(), 1);
		
		chest.getInventory().getSlotForID(224).setItem(new ItemBow(), 1);
		chest.getInventory().getSlotForID(225).setItem(new ItemArrow(), 100);
		chest.getInventory().getSlotForID(226).setItem(new ItemIronShield(), 1);
		
		chest.getInventory().getSlotForID(230).setItem(new ItemSpyglass(), 1);
		chest.getInventory().getSlotForID(231).setItem(new ItemTaykolos(), 1);
		chest.getInventory().getSlotForID(232).setItem(new ItemCrystalStaff(), 1);
	}
	
	private void addArmorChest()
	{
		Chest chest = new Chest();
		chest.setPosition(CENTER + Block.SIZE * 4, CENTER + Block.SIZE * 2);
		addObject(chest);
		
		chest.getInventory().getSlotForID(200).setItem(new ItemIronHelmet(), 1);
		chest.getInventory().getSlotForID(206).setItem(new ItemIronChestplate(), 1);
		chest.getInventory().getSlotForID(212).setItem(new ItemIronLeggings(), 1);
		chest.getInventory().getSlotForID(218).setItem(new ItemIronBoots(), 1);
		chest.getInventory().getSlotForID(201).setItem(new ItemGoldHelmet(), 1);
		chest.getInventory().getSlotForID(207).setItem(new ItemGoldChestplate(), 1);
		chest.getInventory().getSlotForID(213).setItem(new ItemGoldLeggings(), 1);
		chest.getInventory().getSlotForID(219).setItem(new ItemGoldBoots(), 1);
		chest.getInventory().getSlotForID(202).setItem(new ItemWizardHat(), 1);
		chest.getInventory().getSlotForID(208).setItem(new ItemWizardRobe(), 1);
		chest.getInventory().getSlotForID(209).setItem(new ItemWoolPullover(), 1);
	}
	
	@Override
	public void update(float tpf)
	{
		super.update(tpf);
		
		if(monsterCounter < 15) monsterTimer.count(tpf);
	}
	
}
