package state;

import java.awt.Graphics2D;

public abstract class State {
	
	public State() {
		
	}
	
	public abstract void update();
	public abstract void render(Graphics2D g2d);
}
