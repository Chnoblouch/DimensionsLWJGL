package game;

import java.awt.Toolkit;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;

import javax.management.MBeanServer;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;

import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import engine.gfx.FBO;
import engine.gfx.Font;
import engine.gfx.Sounds;
import engine.gfx.SpriteLoader;
import engine.gfx.Sprites;
import engine.input.Input;
import engine.input.InputListener;
import engine.rendering.Camera;
import engine.rendering.GameDisplay;
import engine.rendering.Screen;
import game.creature.Player;
import game.effects.Effect;
import game.gui.HealthBar;
import game.gui.ManaBar;
import game.gui.TextBox;
import game.gui.inventory.Inventory;
import game.gui.inventory.InventorySlotRenderable;
import game.levels.Level;
import game.levels.LevelDarkWorld;
import game.levels.LevelFrostWorld;
import game.levels.LevelOverworld;
import game.levels.LevelSkyWorld;
import game.particle.ChangeLevelScreen;
import game.particle.YouDiedScreen;
import game.saving.SaveManager;
import utils.MapLoader;

public class Game 
implements InputListener
{
	private boolean running = false;
	public boolean paused = false;
	
	public static final int WIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	public static final int HEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	public static final int FPS_CAP = 60;
	
	private GameDisplay display;
	private Screen screen;
	
	private Input input;
	
	private Camera camera;
	
	public Player player;
	private Level level;
	public int levelID = 0;
	private Level[] levels = new Level[4];
	private Map[] maps = new Map[4];
	
	public Inventory inventory;
	private HealthBar healthBar;
	private ManaBar manaBar;
	private TextBox textbox;
	
	private ChangeLevelScreen changeLevelScreen;
	private YouDiedScreen youDiedScreen;
	
	private MusicHandler musicHandler;
	
	private FBO shadowFBO;
		
	private float lastFrame = getCurrentTime();
	private float tpf = 0.65f;
	private ArrayList<Float> timesPerFrame = new ArrayList<>();
	
	private float lastFPS = getCurrentTime();
	private float frameCounter;
	private float fps;
	
	private String language = "german";
	
	private boolean loaded = false;
	
	private boolean zoomOut = false;
	private boolean zoomIn = false;
	private float zoomStage = 0;
	
	private boolean debug = false;
				
	public Game()
	{
		// TODO: release item after putting away
		// TODO: horse animation
		// TODO: crops
		// TODO: crystal staff in hand
		// TODO: more arrows
		// TODO: capes
		// TODO: textloader language
		try {
			createJmxConnectorServer();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void createJmxConnectorServer() throws IOException {
	    LocateRegistry.createRegistry(1234);
	    MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
	    JMXServiceURL url = new JMXServiceURL("service:jmx:rmi://localhost/jndi/rmi://localhost:1234/jmxrmi");
	    JMXConnectorServer svr = JMXConnectorServerFactory.newJMXConnectorServer(url, null, mbs);
	    svr.start();
	}
	
	public void start()
	{
		display = new GameDisplay();
		display.setFullscreen(true);
		display.setFPSCap(FPS_CAP);
		display.create();
				
		screen = new Screen();
		screen.create();
		
		input = new Input();
		input.addInputListener(this);
		input.create();
		
		camera = new Camera();
		screen.setCamera(camera);
		
		inventory = new Inventory(this);
		healthBar = new HealthBar(this);
		manaBar = new ManaBar(this);
		textbox = new TextBox();
				
		changeLevelScreen = new ChangeLevelScreen();
		youDiedScreen = new YouDiedScreen(this);
		
		musicHandler = new MusicHandler(this);
		
		shadowFBO = new FBO(Display.getWidth(), Display.getHeight(), FBO.DEPTH_BUFFER_NONE);
		screen.setShadowMap(shadowFBO.getColorTexture());
		
		running = true;
						
		initWorld(false);
		initGameLoop();
		
		stop();
		
		System.exit(-1);
	}
		
	private void initGameLoop()
	{		
		lastFrame = getCurrentTime();
		lastFPS = getCurrentTime();
		
		while(running && !Display.isCloseRequested())
		{		
			input.update();
			
			if(!paused)
			{
				if(!player.isDeath()) level.update(tpf);
				else player.update(tpf);
				
				changeLevelScreen.update(tpf);
				youDiedScreen.update(tpf);
				
				calculateCameraPos();
			}
			
			musicHandler.update(tpf);
			
			screen.clear();
			
			healthBar.update(tpf);
			manaBar.update(tpf);
			healthBar.setFreeze(player.hasEffect(Effect.ID_FREEZING));
			
			renderWorld();
			renderGUI();
												
			display.update();
									
			timesPerFrame.add((getCurrentTime() - lastFrame) / 25.0f);
			lastFrame = getCurrentTime();
						
			if(getCurrentTime() - lastFPS > 1000)
			{
				fps = frameCounter;
				System.out.println(frameCounter + " fps");
				frameCounter = 0;
				lastFPS += 1000;
				
				tpf = 0.0f;
				for(int i = 0; i < timesPerFrame.size(); i++) tpf += timesPerFrame.get(i);
				tpf /= timesPerFrame.size();
				timesPerFrame.clear();
			}
			
			frameCounter ++;
		}
	}
	
	public void initWorld(boolean load)
	{
		createMaps(load);
		
		levels[0] = new LevelOverworld(this);
		levels[1] = new LevelDarkWorld(this);
		levels[2] = new LevelSkyWorld(this);
		levels[3] = new LevelFrostWorld(this);
		
		levelID = 0;
		level = levels[levelID];
		
		player = new Player(this);
		player.setPosition(level.getPlayerSpawn().x, level.getPlayerSpawn().y);
		level.addObject(player);
				
		if(load) SaveManager.loadGameState(this);
	}
	
	public void clearWorld()
	{
		for(int i = 0; i < maps.length; i++) maps[i] = null;
		for(int i = 0; i < levels.length; i++) levels[i] = null;
		
		levelID = 0;
		level = null;
		
		player = null;
	}
	
	public void createMaps(boolean load)
	{		
		if(!load)
		{
			maps[0] = MapLoader.loadMap("/map/overworld.map");
//			maps[0] = MapGeneratorOverworld.generate();
			maps[1] = MapLoader.loadMap("/map/darkworld.map");
			maps[2] = MapLoader.loadMap("/map/skyworld.map");
			maps[3] = MapLoader.loadMap("/map/frostworld.map");
		} else 
		{
			maps[0] = new Map();
			maps[1] = new Map();
			maps[2] = new Map(); 
			maps[3] = new Map();
			
			SaveManager.loadMaps(this);
		}
	}
	
	public void load()
	{
		loaded = true;
		
		clearWorld();
		initWorld(true);
		SaveManager.loadGameState(this);
	}
	
	public boolean exists()
	{
		return loaded;
	}
	 
	public void changeLevel(int level)
	{
		this.level.removeObject(player);
		this.level.collisionSpace.remove(player);
		
		levelID = level;
		this.level = levels[levelID];
		
		this.level.addObject(player);
		player.setPosition(this.level.getPlayerSpawn().x, this.level.getPlayerSpawn().y);
		
		if(level == 1) Sounds.wind.play(0, 0);
		else Sounds.wind.stop();
	}
	
	private void calculateCameraPos()
	{
//		float angle = Angles.getAngle(Display.getWidth() / 2, Display.getHeight() / 2, input.getMouseX(), input.getMouseY());
//		Vector2f moveDir = Angles.getMoveDirection(angle);
		
		float defaultX = player.getX() + 128 - Display.getWidth() / 2;
		float defaultY = player.getY() + 128 - Display.getHeight() / 2;
		
		float zoomX = player.getX() + 128 - Display.getWidth() + getInput().getMouseX();
		float zoomY = player.getY() + 128 - Display.getHeight() + getInput().getMouseY();
		
		float finalZoomStage = zoomStage * zoomStage * (3.0f - 2.0f * zoomStage);
		float finalX = zoomX * finalZoomStage + defaultX * (1.0f - finalZoomStage);
		float finalY = zoomY * finalZoomStage + defaultY * (1.0f - finalZoomStage);
		
		camera.lookAt(finalX, finalY);
		
		if(zoomOut && zoomStage < 1) 
		{
			zoomStage += 0.025f * tpf;
			if(zoomStage >= 0.9975) zoomStage = 1;
		}
		
		if(zoomIn && zoomStage > 0)
		{
			zoomStage -= 0.025f * tpf;
			if(zoomStage <= 0.025f) zoomStage = 0;
		}
	}
	
	private void renderWorld()
	{	
//		screen.prepare(screen.getShadowShader());
//		shadowFBO.bindToDraw();
//		screen.clear();
//		level.renderShadows(screen);
//		shadowFBO.unbind();
//		screen.finish();
				
		screen.prepare(screen.getObjectShader());
		level.render(screen);
		screen.finish();
	}
	
	private void renderGUI()
	{
		screen.prepare(screen.getGUIShader());
		
		if(debug)
		{
			screen.renderFont("FPS: " + fps, 16, Display.getHeight() - 128, 32, Font.COLOR_WHITE, false);
			screen.renderFont("TPF: " + tpf / 25.0f / 1000.0f + " ms", 16, Display.getHeight() - 96, 32, Font.COLOR_WHITE, false);
			screen.renderFont("Objects: " + level.objects.size(), 16, Display.getHeight() - 64, 32, Font.COLOR_WHITE, false);
			screen.renderFont("Collision Space: " + level.collisionSpace.getObjects().size(), 16, 
					  		  Display.getHeight() - 32, 32, Font.COLOR_WHITE, false);
		}
		
		level.renderGUI(screen);
		
		if(player != null)
		{
			healthBar.render(screen);
			manaBar.render(screen);
			
			for(int i = 0; i < player.getEffectHandling().getEffects().size(); i++)
				player.getEffectHandling().getEffects().get(i).render(screen, 16 + i * 112, input.getMouseX(), input.getMouseY());
			
			for(int i = 0; i < player.getEffectHandling().getEffects().size(); i++)
				player.getEffectHandling().getEffects().get(i).renderInfo(screen, 16 + i * 112, input.getMouseX(), input.getMouseY());
			
			textbox.render(screen);

			inventory.render(screen);
		}
		
		if(!player.isDeath())
			screen.renderGUI(Sprites.cursor.getSprite(0, 0, 12, 12), input.getMouseX(), input.getMouseY(), 48, 48, 0, 1);
		
		changeLevelScreen.render(screen);
		youDiedScreen.render(screen);
				
		screen.finish();
	}
	
	public void resetLanguage(String language)
	{
		this.language = language;
		
		for(int i = 0; i < inventory.getSlots().size(); i++)
		{
			InventorySlotRenderable s = inventory.getSlots().get(i);
			if(!s.isEmpty()) s.getItem().resetLanguage(language);
		}
		
		for(int i = 0; i < levels.length; i++)
		{
			Level l = levels[i];
			
			for(int j = 0; j < l.objects.size(); j++)
			{
				l.objects.get(j).resetLanguage(language);
			}
		}
	}
	
	public void showChangeLevelScreen()
	{
		changeLevelScreen.run();
	}
	
	public boolean finishedChangeLevelEffect()
	{
		return changeLevelScreen.finish;
	}
	
	public void showYouDiedScreen()
	{
		youDiedScreen.run();
	}
	
	public void hideYouDiedScreen()
	{
		youDiedScreen.hide();
	}
	
	public void useSpyglass()
	{
		zoomOut = true;
		zoomIn = false;
	}
	
	public void releaseSpyglass()
	{
		zoomIn = true;
		zoomOut = false;
	}
	
	public GameDisplay getDisplay()
	{
		return display;
	}
	
	public Screen getScreen()
	{
		return screen;
	}
	
	public Input getInput()
	{
		return input;
	}
	
	public Camera getCamera()
	{
		return camera;
	}
	
	public Level[] getLevels()
	{
		return levels;
	}
	
	public Level getLevel()
	{
		return level;
	}
	
	public Map[] getMaps()
	{
		return maps;
	}
	
	public TextBox getTextBox()
	{
		return textbox;
	}
	
	public String getLanguage()
	{
		return language;
	}
	
	public float getCurrentTime()
	{
		return Sys.getTime() * 1000 / Sys.getTimerResolution();
	}
	
	public void stop()
	{
		shadowFBO.clear();
		screen.delete();
		SpriteLoader.clear();
		display.close();
	}

	@Override
	public void mousePressed(int button) 
	{
		inventory.mousePressed(button);
		
		boolean textboxOver = textbox.over;
		
		if(!inventory.visible && textbox.over) player.mousePressed(button);
		if(textboxOver == textbox.over) textbox.mousePressed(button);
	}

	@Override
	public void mouseReleased(int button) 
	{
		inventory.mouseReleased(button);		
		if(!inventory.visible && textbox.over) player.mouseReleased(button);
	}

	@Override
	public void keyPressed(int key)
	{
		player.keyPressed(key);
		
		if(key == Keyboard.KEY_1) resetLanguage("english");
		if(key == Keyboard.KEY_2) resetLanguage("german");
		
		if(key == Keyboard.KEY_0 && Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) debug = !debug;
		
		if(key == Keyboard.KEY_ESCAPE) System.exit(-1);
	}

	@Override
	public void keyReleased(int key) 
	{
		player.keyReleased(key);
	}
	
	public static void main(String[] args)
	{
		new Game().start();
		
//		for(int count = 0; count < 10; count++)
//		{
//			ArrayList<String> syllables = new ArrayList<>();
//			syllables.add("ka");
//			syllables.add("nera");
//			syllables.add("de");
//			syllables.add("lan");
//			syllables.add("wan");
//			syllables.add("ru");
//			syllables.add("ay");
//			syllables.add("ia");
//			syllables.add("gero");
//			syllables.add("wena");
//			syllables.add("dra");
//			String name = "";
//			
//			for(int i = 0; i < 2 + new Random().nextInt(3); i++)
//			{
//				String syllable = syllables.get(new Random().nextInt(syllables.size()));
//				name = name + syllable;
//				
//				syllables.remove(syllable);
//			}
//			
//			name = name.substring(0, 1).toUpperCase() + name.substring(1, name.length());
//			
//			System.out.println(name);
//		}
	}
}
