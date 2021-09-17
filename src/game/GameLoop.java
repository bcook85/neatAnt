package game;

public class GameLoop implements Runnable {
	
	private final long ONE_SECOND = 1000000000;
	
	private Game game;
	
	private boolean running = false;
	
	public GameLoop(Game game) {
		this.game = game;
	}

	@Override
	public void run() {
		running = true;
		int fps = 60;
		double timePerTick = ONE_SECOND / fps;
		double delta = 0;
		long now;
		long lastTime = System.nanoTime();
		long timer = 0;
		int ticks = 0;
		long elapsed = 0;
		while (running) {
			now = System.nanoTime();
			elapsed = now - lastTime;
			delta += elapsed / timePerTick;
			timer += elapsed;
			lastTime = now;
			if (delta >= 1) {
				ticks++;
				delta--;
				game.update();
				game.render();
			}
			if (timer >= ONE_SECOND) {
				game.updateFPS(ticks);
				ticks = 0;
				timer = 0;
			}
		}
	}
}
