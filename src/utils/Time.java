package utils;

public class Time {

	public static float secondsInTicks(float sec)
	{
		return sec * 40;
	}
	
	public static float ticksInSeconds(float ticks)
	{
		return ticks / 40;
	}
	
}
