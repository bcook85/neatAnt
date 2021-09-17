package game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import display.Display;
import input.KeyManager;
import state.GameState;
import state.StateManager;
import util.Vector;

public class Game {
	
	private Display display;
	private final Vector screenSize = new Vector(400, 400);
	protected final String GAME_TITLE = "NEAT Ant";
	private KeyManager keys;
	private GameState gameState;
	
	public Game() {
		// Init Engine
		display = new Display(GAME_TITLE, (int)screenSize.x, (int)screenSize.y);
		keys = new KeyManager();
		display.getFrame().addKeyListener(keys);
		// States
		gameState = new GameState(screenSize, keys);
		StateManager.setState(gameState);
	}
	
	public void update() {
		StateManager.getState().update();
	}
	
	public void render() {
		BufferStrategy bs = display.getCanvas().getBufferStrategy();
		Graphics2D g2d = (Graphics2D)bs.getDrawGraphics();
		
		// Clear Screen
		g2d.setColor(Color.BLACK);
		g2d.fillRect(0, 0, display.getCanvas().getWidth(), display.getCanvas().getHeight());

		StateManager.getState().render(g2d);
		
		// Dispose & Show
		g2d.dispose();
		bs.show();
	}
	
	public void updateFPS(int fps) {
		display.getFrame().setTitle(GAME_TITLE + " - " + fps + "fps");
	}
}
