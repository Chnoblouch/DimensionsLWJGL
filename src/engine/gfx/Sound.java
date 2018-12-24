package engine.gfx;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.UnsupportedAudioFileException;

import game.Game;

public class Sound {
	
	private AudioInputStream ais;
	private AudioFormat af;
	private int size;
	private byte[] audio;
	private DataLine.Info info;
	private Clip clip;
	
	private String source;
	private FloatControl volumeControl;
	private FloatControl panoramaControl;
	
	private float volume;
	
	public Sound(String source)
	{
		this.source = source;
		
		try
		{
			InputStream audioSrc = Sound.class.getResourceAsStream(source);
			InputStream bufferedIn = new BufferedInputStream(audioSrc);
			
			ais = AudioSystem.getAudioInputStream(bufferedIn);
			af = ais.getFormat();
			size = (int) (af.getFrameSize() * ais.getFrameLength());
			audio = new byte[size];
			info = new DataLine.Info(Clip.class, af, size);
			ais.read(audio, 0, size);
		} catch (UnsupportedAudioFileException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void play(float volume, float pan, boolean loop)
	{		
		if(volume >= 6.0206) volume = 6.0206f;
		
		if(isRunning()) stop();
		
		try {
			clip = (Clip) AudioSystem.getLine(info);
			clip.getFormat();
			clip.open(af, audio, 0, size);
			clip.loop(loop ? Clip.LOOP_CONTINUOUSLY : 0);
			
			volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			volumeControl.setValue(volume);
			
			try {
				panoramaControl = (FloatControl) clip.getControl(FloatControl.Type.PAN);
				panoramaControl.setValue(pan);
			} catch (Exception e) {
				// TODO: handle exception
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void play(float volume, float pan)
	{
		play(volume, pan, false);
	}
	
	public void play(float volume, boolean t, double x, double y, double camX, double camY)
	{
		double centerX = camX + (Game.WIDTH / 2);
		double centerY = camY + (Game.HEIGHT / 2);
		
		double distanceX = centerX > x ? centerX - x : x - centerX;
		double distance = Math.hypot(centerX - x, centerY - y);
			
		float finalVolume = volume - (float) distance / 25;
		
		float panorama = x > centerX ? (float) (distanceX / 50) : -(float) (distanceX / 50);
		if(panorama > 1) panorama = 1;
		else if(panorama < -1) panorama = -1;
		
		if(finalVolume > -80) 
		{
			play(finalVolume, panorama);
		}
	}
	
	public String getSource()
	{
		return source;
	}
	
	public void setVolume(float volume)
	{
		this.volume = volume;
		if(isRunning() && volume >= -80.0f) volumeControl.setValue(volume);
	}
	
	public float getVolume()
	{
		return volume;
	}
	
	public void stop()
	{
		if(clip != null && clip.isRunning()) clip.stop();
	}
	
	public boolean isRunning()
	{
		return clip != null && clip.isRunning();
	}
	
}
