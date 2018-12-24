package game.creature.ai.moving;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector2f;

public class Path {
	
	private ArrayList<Vector2f> steps = new ArrayList<>();
	
	public void addStep(Vector2f step)
	{
		steps.add(step);
	}
	
	public ArrayList<Vector2f> getSteps()
	{
		return steps;
	}
	
	
	
	
	private float angle = 0;
	
	public void setAngle(float angle)
	{
		this.angle = angle;
	}
	
	public float getAngle()
	{
		return angle;
	}

}
