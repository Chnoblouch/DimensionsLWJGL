package utils;

import org.lwjgl.util.vector.Vector2f;

public class Angles {
	
	public static float getAngle(Vector2f from, Vector2f to)
	{		
		return getAngle(from.x, from.y, to.x, to.y);
	}
	
	public static float getAngle(float  fx, float  fy, float  tx, float  ty)
	{		
	    float theta = (float) Math.atan2(ty - fy, tx - fx);
	    theta += Math.PI/2.0;
	    
	    float angle = (float) Math.toDegrees(theta);
	    if (angle < 0) angle += 360;
	    
	    return angle;
	}
	
	public static Vector2f getMoveDirection(float angle)
	{
		if(angle == 90) return new Vector2f(1.0f, 0.0f);
		else if(angle == 180) return new Vector2f(0.0f, 1.0f);
		else if(angle == 270) return new Vector2f(-1.0f, 0.0f);
				
		float x = (float) Math.sin(Math.toRadians(angle));
        float y = (float) -Math.cos(Math.toRadians(angle));
		
		return new Vector2f(x, y);
	}

	public static int getLookDir(float angle)
	{
		if((angle > 315 && angle <= 360) || (angle > 0 && angle <= 45)) return 2;
		else if(angle > 45 && angle <= 135) return 1;
		else if(angle > 135 && angle <= 225) return 0;
		else if(angle > 225 && angle <= 315) return 3;
		
		return 2;
	}
}
