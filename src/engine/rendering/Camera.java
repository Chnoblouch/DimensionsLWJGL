package engine.rendering;

public class Camera {
	
	private float x = 0, y = 0;
	private float zoom = 1;
	
	public void lookAt(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
	public float getX()
	{
		return x;
	}
	
	public float getY()
	{
		return y;
	}
	
	public void setZoom(float zoom)
	{
		this.zoom = zoom;
	}
	
	public float getZoom()
	{
		return zoom;
	}

}
