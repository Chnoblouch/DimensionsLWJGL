package game.saving;

import game.Game;
import game.GameObject;
import game.levels.Level;
import utils.SafeArrayList;

public class SaveInformation {
	
	private SafeArrayList<SaveFile> files = new SafeArrayList<>();
	
	public void put(SaveFile file)
	{
		files.add(file);
	}
	
	public SafeArrayList<SaveFile> getFiles()
	{
		return files;	
	}
	
	public static SaveInformation convertGame(Game game)
	{
		SaveInformation information = new SaveInformation();
		
		for(int i = 0; i < game.getLevels().length; i++)
		{
			Level level = game.getLevels()[i];
			
			StringBuilder string = new StringBuilder();
			
			string.append(level.getID()+System.lineSeparator());
			
			for(int j = 0; j < level.objects.size(); j++)
			{
				GameObject o = level.objects.get(j);
				if(o.doSave()) string.append(o.getClass().getName()+";"+o.getX()+";"+o.getY()+";"+o.save()+System.lineSeparator());
			}
			
			information.put(new SaveFile(level.getName(), string.toString()));
		}
		
		String playerData = game.getLevel().getID()+";"+game.player.getX()+";"+game.player.getY()+";"+game.player.getHealth();
		
		information.put(new SaveFile("player", playerData));
		information.put(game.inventory.convertToSaveFile());
		
		return information;
	}
}
