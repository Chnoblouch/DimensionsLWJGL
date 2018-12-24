package engine.input;

import java.awt.image.BufferedImage;
import java.nio.IntBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public class Input {
	
	private boolean[] mouseButtons = new boolean[Mouse.getButtonCount()];
	private boolean[] keys = new boolean[Keyboard.KEYBOARD_SIZE];
	
	private ArrayList<InputListener> listeners = new ArrayList<>();
	
	public void create()
	{
		try 
		{
			Mouse.create();
			Keyboard.create();
			
			createEmptyCursor();
		} catch (LWJGLException e) 
		{
			e.printStackTrace();
		}
	}
	
	private void createEmptyCursor()
	{
	    IntBuffer buffer = BufferUtils.createIntBuffer(1);
	    buffer.put(0);
	    buffer.rewind();
	    
		try 
		{
			Mouse.setNativeCursor(new Cursor(1, 1, 0, 0, 1, buffer, null));
		} catch (LWJGLException e)
		{
			e.printStackTrace();
		}
	}
	
	public void addInputListener(InputListener listener)
	{
		listeners.add(listener);
	}
	
	public void removeInputListener(InputListener listener)
	{
		listeners.remove(listener);
	}
	
	public float getMouseX()
	{
		return Mouse.getX();
	}
	
	public float getMouseY()
	{
		return Display.getHeight() - Mouse.getY();
	}
	
	public void updateMouse()
	{
		for(int i = 0; i < mouseButtons.length; i++)
		{
			if(Mouse.isButtonDown(i))
			{
				if(!mouseButtons[i]) 
				{
					mouseButtons[i] = true;
					for(int j = 0; j < listeners.size(); j++) listeners.get(j).mousePressed(i);
				}
			} else
			{
				if(mouseButtons[i]) 
				{
					mouseButtons[i] = false;
					for(int j = 0; j < listeners.size(); j++) listeners.get(j).mouseReleased(i);
				}
			}
		}
	}
	
	public void updateKeyboard()
	{
		for(int i = 0; i < keys.length; i++)
		{
			if(Keyboard.isKeyDown(i))
			{
				if(!keys[i]) 
				{
					keys[i] = true;
					for(int j = 0; j < listeners.size(); j++) listeners.get(j).keyPressed(i);
				}
			} else
			{
				if(keys[i]) 
				{
					keys[i] = false;
					for(int j = 0; j < listeners.size(); j++) listeners.get(j).keyReleased(i);
				}
			}
		}
	}
	
	public void update()
	{
		updateMouse();
		updateKeyboard();
	}
}
