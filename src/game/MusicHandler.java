package game;

import engine.gfx.Music;
import engine.gfx.Sounds;
import game.creature.monster.Monster;
import game.creature.monster.MonsterGiantSlime;
import utils.Block;

public class MusicHandler {
	
	private Music music;
	private Music newMusic;
	private Game game;
	
	private boolean fadeOver = false;
		
	public MusicHandler(Game game)
	{
		this.game = game;
	}
	
	public void changeMusic(Music music)
	{
		if(music == this.music || (music == newMusic && newMusic != null)) return;
		
		if(this.music != null) 
		{
			this.music.fadeOut();
			this.newMusic = music;
			fadeOver = true;
		}
		else
		{
			this.music = music;
			this.newMusic = null;
			this.music.play();
		}
	}
	
	public void update(float tpf)
	{		
		if(music != null && music.isPlaying()) music.update(tpf);
				
		if(fadeOver && (music == null || !music.isPlaying()))
		{			
			music = newMusic;
			newMusic = null;
			fadeOver = false;
			
			if(music != null) music.play();
		}
		
		boolean battle = false;
		boolean bossBattle = false;
		
		for(int i = 0; i < game.getLevel().objects.size(); i++)
		{
			GameObject o = game.getLevel().objects.get(i);
			
			if(o instanceof MonsterGiantSlime) 
				bossBattle = true;
			
			if(o instanceof Monster && !(o instanceof MonsterGiantSlime) && o.getDistance(game.getLevel().player) < Block.SIZE * 8) 
				battle = true;
		}
					
		if(battle && !bossBattle) changeMusic(Sounds.battle);
		else if(bossBattle) changeMusic(Sounds.bossBattle);
		else if(!battle && !bossBattle) 
		{
			if(game.getLevel().getID() == 0) changeMusic(Sounds.overworld);
			else if(game.getLevel().getID() == 1) changeMusic(Sounds.darkWorld);
			else if(game.getLevel().getID() == 2) changeMusic(Sounds.skyIslands);
			else if(game.getLevel().getID() == 3) changeMusic(Sounds.iceWorld);
			else changeMusic(null);
		}
	}

}
