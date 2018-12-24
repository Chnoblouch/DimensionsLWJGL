package game.saving;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import game.Game;
import game.GameObject;
import game.item.Item;
import game.item.craftingstations.ItemAlchemyTable;
import game.item.craftingstations.ItemAnvil;
import game.item.craftingstations.ItemCookingPot;
import game.item.craftingstations.ItemOven;
import game.item.craftingstations.ItemWorkbench;

public class SaveManager {
	
	public static void save(SaveInformation information)
	{
		try {
			for(int i = 0; i < information.getFiles().size(); i++)
			{
				SaveFile saveFile = information.getFiles().get(i);
								
				File file = new File("saves/" + saveFile.getName() + ".dimsav");
				if(!file.exists())
				{
					file.getParentFile().mkdirs();
					file.createNewFile();
				}
				
				PrintWriter writer = new PrintWriter(file);
				writer.write(saveFile.getData());
				writer.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void loadMaps(Game game)
	{				
		try {
			File[] files = new File("saves").listFiles();
			
			for(int i = 0; i < files.length; i++)
			{
				File file = files[i];
				
				BufferedReader br = new BufferedReader(new FileReader(file));
				
				String line;
				int lineNumber = -1;
				
				int levelID = 0;
				
			    while ((line = br.readLine()) != null)
			    {			    	
			    	lineNumber ++;

			    	if(file.getName().startsWith("dim"))
			    	{
			    		if(lineNumber == 0)
				    	{
				    		levelID = Integer.parseInt(line);
				    	} else 
				    	{
				    		if(line.contains(";"))
				    		{
				    			String name = line.split(";")[0];
						    	float x = Float.parseFloat(line.split(";")[1]);
						    	float y = Float.parseFloat(line.split(";")[2]);
						    	String data = line.split(";")[3];
						    	
						    	GameObject obj = (GameObject) Class.forName(name).newInstance();
						    	obj.setPosition(x, y);
						    	obj.load(data);
						    	game.getMaps()[levelID].addObject(obj);
				    		}
				    	}
			    	}
			    }
			    
			    br.close();
			}
		} catch(IOException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static void loadGameState(Game game)
	{	
		game.inventory.clear();
		
		try {
			File[] files = new File("saves").listFiles();
			
			for(int i = 0; i < files.length; i++)
			{
				File file = files[i];
				
				BufferedReader br = new BufferedReader(new FileReader(file));
				
				String line;
								
			    while ((line = br.readLine()) != null)
			    {			    	
			    	if(file.getName().equals("inventory.dimsav"))
			    	{
			    		int id = Integer.parseInt(line.split(";")[0]);
			    		String name = line.split(";")[1];
			    		int count = Integer.parseInt(line.split(";")[2]);
			    		
			    		if(!name.equals("empty"))
			    			game.inventory.getSlotForID(id).setItem((Item) Class.forName(name).newInstance(), count);
			    	} else if(file.getName().equals("player.dimsav"))
			    	{
			    		int dimension = Integer.parseInt(line.split(";")[0]);
			    		float x = Float.parseFloat(line.split(";")[1]);
			    		float y = Float.parseFloat(line.split(";")[2]);
			    		float health = Float.parseFloat(line.split(";")[3]);
			    		
			    		game.changeLevel(dimension);
			    		game.player.setPosition(x, y);
			    		game.player.setHealth(health);
			    	}
			    }
			    
			    br.close();
			}
		} catch(IOException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
