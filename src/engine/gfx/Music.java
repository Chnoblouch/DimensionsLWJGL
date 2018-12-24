package engine.gfx;

public class Music {
	
	private Sound intro;
	private Sound main;
	
	private boolean hasIntro;
	private long introTime;
	
	private boolean isInIntro = false;
	private boolean playing = false;
	
	public boolean fadeOut = false;
	private float volume = 0;
	
	private long startTime;
	
	public Music(Sound intro, Sound main, long introTime)
	{
		this.intro = intro;
		this.main = main;
		this.introTime = introTime;
		this.hasIntro = true;
	}
	
	public Music(Sound main)
	{
		this.main = main;
		this.hasIntro = false;
	}
	
	public void play()
	{
		playing = true;
		
		if(hasIntro)
		{
			isInIntro = true;
			intro.play(0, 0);
			startTime = System.currentTimeMillis();
		} else main.play(0, 0, true);
	}
	
	public boolean isPlaying()
	{
		return playing;
	}
	
	public void fadeOut()
	{
		fadeOut = true;
	}
	
	public void setVolume(float volume)
	{
		this.volume = volume;
		
		if(hasIntro) intro.setVolume(volume);
		main.setVolume(volume);
	}
	
	public void stop()
	{
		playing = false;
		
		if(hasIntro)
		{
			if(!isInIntro) main.stop();
			else intro.stop();
		} else main.stop();
	}
	
	public void update(float tpf)
	{				
		if(playing && hasIntro && isInIntro)
		{
			if(System.currentTimeMillis() >= startTime + introTime)
			{
				main.play(0, 0, true);
				isInIntro = false;
			}
		}
		
		if(playing && fadeOut)
		{			
			if(main.getVolume() > -50)setVolume(volume - tpf);
			else 
			{
				stop();
				setVolume(0);
				fadeOut = false;
			}
		}
	}
}
