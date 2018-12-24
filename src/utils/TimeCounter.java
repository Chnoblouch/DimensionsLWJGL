package utils;

public class TimeCounter {

	private float time;
	private float counter = 0;
	
	private TimeListener timeListener;
	
	public TimeCounter(float time, TimeListener listener)
	{
		this.time = time;
		this.timeListener = listener;
	}
	
	public void setTime(float time)
	{
		this.time = time;
	}
		
	public void count(float tpf)
	{
		counter += 25 * tpf;
		if(counter >= time) timeListener.timeIsUp();
	}
	
	public void reset()
	{
		if(counter > time) counter -= time;
		else counter = 0;
	}
	
}
