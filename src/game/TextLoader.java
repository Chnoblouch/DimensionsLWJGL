package game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class TextLoader {
	
	private static HashMap<String, HashMap<String, String>> languages = new HashMap<>();
	
	static
	{		
		languages.put("english", loadText("english"));
		languages.put("german", loadText("german"));
	}
	
	private static HashMap<String, String> loadText(String fileName)
	{
		HashMap<String, String> map = new HashMap<>();
		
		InputStream in = TextLoader.class.getResourceAsStream("/txt/"+fileName+".txt");
		
		try 
		{						
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			
			String line;			
			
		    while ((line = br.readLine()) != null)
		    {			
		    	if(!line.contains("=")) continue;
		    			    	
		    	String name = line.split("=")[0];
		    	String text = line.split("=")[1];
		    	map.put(name, text);
		    }
		    
		    br.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		return map;
	}
	
	public static String getText(String text)
	{ 	
		return languages.get("german").get(text);
	}
	
	public static String getText(String language, String text)
	{
		return languages.get(language).get(text);
	}
}
