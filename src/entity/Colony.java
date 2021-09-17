package entity;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import util.Ball;
import util.Vector;

public class Colony extends Ball {
	
	private int food;
	private double scale;
	private BufferedImage image;
	
	public Colony(BufferedImage spriteImage, Vector pos, double s) {
		super(pos.x, pos.y, (float)s * (spriteImage.getWidth() * 0.5f));
		scale = s;
		image = spriteImage;
	}

	public double getScale() {
		return scale;
	}
	
	public void update() {
		// no idea
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
	
	public void addFood(int amount) {
		food += amount;
	}
	
	public int getFood() {
		return food;
	}

}
