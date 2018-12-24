package utils;

import java.util.HashMap;

public class HitboxFactory {
	
	private static HashMap<String, Hitbox> cache = new HashMap<>();
	
	public static Hitbox create(float x, float y, float w, float h)
	{	
		return cache.computeIfAbsent(x+","+y+","+w+","+h, key -> new Hitbox(x, y, w, h));
	}
	
	public static Hitbox create(float[] xp, float[] yp)
	{
		String name = "";
		
		for(int i = 0; i < xp.length; i++) { name = name + xp[i]+";"+yp[i]+","; }
				
		Hitbox hb = cache.get(name);
		if(hb != null) return hb;
		
		hb = new Hitbox(xp, yp);
		cache.put(name, hb);
				
		return hb;
	}
}
