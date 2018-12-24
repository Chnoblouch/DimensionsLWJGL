package engine.input;

public interface InputListener {
	
	public void mousePressed(int button);
	public void mouseReleased(int button);
	
	public void keyPressed(int key);
	public void keyReleased(int key);
	
}
