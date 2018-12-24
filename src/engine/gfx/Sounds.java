package engine.gfx;

import utils.Time;

public class Sounds {
	
	public static Sound wood = new Sound("/snd/sfx/wood.wav");
	public static Sound woodDestroyed = new Sound("/snd/sfx/wood_destroyed.wav");
	
	public static Sound stone = new Sound("/snd/sfx/stone.wav");
	public static Sound stoneDestroyed = new Sound("/snd/sfx/stone_destroyed.wav");
	
	public static Sound plant = new Sound("/snd/sfx/plant.wav");
	public static Sound plantDestroyed = new Sound("/snd/sfx/plant_destroyed.wav");
	
	public static Sound portal = new Sound("/snd/sfx/portal.wav");
	
	public static Sound pickup = new Sound("/snd/sfx/pickup.wav");
	public static Sound eat = new Sound("/snd/sfx/creatures/player/eat.wav");
	public static Sound hurt0 = new Sound("/snd/sfx/creatures/player/hurt0.wav");
	public static Sound hurt1 = new Sound("/snd/sfx/creatures/player/hurt1.wav");
	public static Sound hurt2 = new Sound("/snd/sfx/creatures/player/hurt2.wav");
	public static Sound shoot = new Sound("/snd/sfx/creatures/player/shoot.wav");
	
	public static Sound lobireHurt = new Sound("/snd/sfx/creatures/lobire/hurt.wav");
	public static Sound lobireDie = new Sound("/snd/sfx/creatures/lobire/die.wav");
	
	public static Sound rogoRoll = new Sound("/snd/sfx/creatures/rogo/roll.wav");
	public static Sound rogoHurt = new Sound("/snd/sfx/creatures/rogo/hurt.wav");
	public static Sound rogoDie = new Sound("/snd/sfx/creatures/rogo/die.wav");
	
	public static Sound taykolos = new Sound("/snd/sfx/items/taykolos.wav");
	public static Sound crystal = new Sound("/snd/sfx/items/crystal.wav");
	public static Sound crystalHit = new Sound("/snd/sfx/items/crystal_hit.wav");
	
	public static Sound wind = new Sound("/snd/sfx/wind.wav");
	
	public static Sound youDied = new Sound("/snd/music/youdied.wav");
	
	public static Sound overworldMain = new Sound("/snd/music/overworld.wav");
	public static Music overworld = new Music(overworldMain);
	
	public static Sound darkWorldMain = new Sound("/snd/music/darkworld.wav");
	public static Music darkWorld = new Music(darkWorldMain);
	
	public static Sound skyIslandsMain = new Sound("/snd/music/skyislands.wav");
	public static Music skyIslands = new Music(skyIslandsMain);
	
	public static Sound iceWorldMain = new Sound("/snd/music/iceworld.wav");
	public static Music iceWorld = new Music(iceWorldMain);
	
	public static Sound battleIntro = new Sound("/snd/music/battleintro.wav");
	public static Sound battleMain = new Sound("/snd/music/battle.wav");
	public static Music battle = new Music(battleIntro, battleMain, 1435);
	
	public static Sound bossBattleIntro = new Sound("/snd/music/bossbattleintro.wav");
	public static Sound bossBattleMain = new Sound("/snd/music/bossbattle.wav");
	public static Music bossBattle = new Music(bossBattleIntro, bossBattleMain, 6300);
}
