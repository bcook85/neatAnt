package entity;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import util.Ball;
import util.Vector;

public class Rock extends Ball {

	private double dir;
	private double scale;
	private BufferedImage image;
	
	public Rock(BufferedImage spriteImage, Vector pos, double s) {
		super(pos.x, pos.y, (float)s * (spriteImage.getWidth() * 0.5f));
		dir = Math.random() * Math.PI * 2;
		scale = s;
		image = spriteImage;
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
		at.rotate(
			dir
			,(int)Math.floor(image.getWidth() * 0.5)
			,(int)Math.floor(image.getHeight() * 0.5)
		);
		g2d.drawImage(
			image
			,at
			,null
		);
	}
}
