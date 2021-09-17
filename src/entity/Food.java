package entity;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import util.Ball;
import util.Vector;

public class Food extends Ball {
	
	private boolean alive;
	private int amount = 0;
	private double scale;
	private BufferedImage image;

	public Food(BufferedImage spriteImage, Vector pos, double s) {
		super(pos.x, pos.y, (float)s * (spriteImage.getWidth() * 0.5f));
		scale = s;
		image = spriteImage;
		alive = true;
	}
	
	public void update() {
	}
	
	public void render(Graphics2D g2d) {
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
	
	public boolean isAlive() {
		return alive;
	}

}
