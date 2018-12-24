package game;

import utils.SafeArrayList;

public class CollisionSpace {

	private SafeArrayList<GameObject> objects = new SafeArrayList<>();
	
	public void add(GameObject o)
	{
		objects.add(o);
	}
	
	public SafeArrayList<GameObject> getObjects()
	{
		return objects;
	}
	
	public void remove(GameObject o)
	{
		objects.remove(o);
	}
	
	public SafeArrayList<GameObject> getObjInRange(GameObject obj)
	{		
		SafeArrayList<GameObject> collideablesInRange = new SafeArrayList<>();
		
		for(int i = 0; i < objects.size(); i++)
		{
			GameObject o = objects.get(i);
			
			if(obj.isInRange(o) && o != obj) collideablesInRange.add(o);
			if(collideablesInRange.size() > 200) break;
		}
		
		return collideablesInRange;
	}
}
