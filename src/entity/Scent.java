package entity;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import util.Ball;
import util.Vector;

public class Scent extends Ball {
	
	private boolean alive;
	private double strength;
	private double decayAmount;
	private double dir;
	private BufferedImage image;
	private double scale;
	private long lastUpdate;
	private final long TICK_TIME = 60;

	public Scent(BufferedImage spriteImage, Vector pos, double s) {
		super(pos.x, pos.y, (float)s * (spriteImage.getWidth() * 0.5f));
		scale = s;
		image = spriteImage;
		strength = s;
		lastUpdate = 0;
		alive = true;
	}
	
	public void update(long gameTick) {
		if (gameTick >= lastUpdate + TICK_TIME) {
			lastUpdate = gameTick;
			addStrength(-0.05);
		}
	}
	
	public void render(Graphics2D g2d) {
		if (alive) {
			AffineTransform at = new AffineTransform();
			at.translate(
				(int)Math.floor(pos.x - radius)
				,(int)Math.floor(pos.y - radius)
			);
			at.scale(
				scale
				,scale
			);
			g2d.drawImage(
				image
				,at
				,null
			);
		}
	}
	
	public void addStrength(double amount) {
		strength += amount;
		if (strength > 1) {
			strength = 1;
		} else if (strength < 0) {
			strength = 0;
			alive = false;
		}
		scale = strength;
		radius = (float)scale * (image.getWidth() * 0.5f);
	}
	
	public boolean isAlive() {
		return alive;
	}

	public double getStrength() {
		return strength;
	}

}
