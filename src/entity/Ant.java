package entity;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import util.Ball;
import util.Vector;

public class Ant extends Ball {
	
	private BufferedImage image;
	private BufferedImage imageCarry;
	private double scale;
	private double sightRange;
	private boolean isAlive;
	private int holdAmount;
	
	private float dir;
	private float moveSpeed;
	private float turnSpeed;
	
	private double scentAmount;
	private long lastScent;
	private final long SCENT_TIME = 60;

	public Ant(BufferedImage spriteImage, BufferedImage spriteCarry, Vector pos, double s) {
		super(pos.x, pos.y, (float)s * (spriteImage.getWidth() * 0.5f));
		scale = s;
		sightRange = radius * 8.0;
		image = spriteImage;
		imageCarry = spriteCarry;
		isAlive = true;
		holdAmount = 0;
		dir = (float)(Math.random() * Math.PI * 2.0f);
		moveSpeed = 0.75f;
		turnSpeed = (float)(Math.PI * 0.075f);
		scentAmount = 0.0;
		lastScent = 0;
	}
	
	public void update(double[] moveControl, Vector mapSize, ArrayList<Rock> rocks) {
		// Move Forwards/Backwards
		vel = new Vector(0, (float)(moveControl[0] - 0.5f) * -2.0f);// (0,-1) is forward
		//vel = new Vector(0, -(float)(moveControl[0]));// (0,-1) is forward, forward only
		// Turn Left/Right
		dir += turnSpeed * ((moveControl[1] - 0.5f) * 2.0f);
		dir = (float)((dir + (Math.PI * 2)) % (Math.PI * 2));
		// Set Velocity based on dir & movement speed
		vel = vel.rot(dir).mul(moveSpeed);
		// Set desire to place scent
		scentAmount = (moveControl[2] - 0.5f) * 2.0;
		
		// Rock collision detection
		for (Rock rock : rocks) {
			Ball.resolveBallCollision(this, rock);
		}
		// Update Position
		pos = pos.add(vel);
		// Keep in-bounds
		pos.x = Math.max(radius, Math.min(pos.x, mapSize.x - radius));
		pos.y = Math.max(radius, Math.min(pos.y, mapSize.y- radius));
		// Reset velocity
		vel = new Vector(0.0f, 0.0f);
	}
	
	public double interact(Colony colony, ArrayList<Food> food) {
		double points = 0.0;
		if (holdAmount > 0) {
			if (colony.vsBall(this)) {
				points += 10;
				holdAmount = 0;
				colony.addFood(1);
			}
		} else {
			for (int i = 0; i < food.size(); i++) {
				if (food.get(i).isAlive() && food.get(i).vsBall(this)) {
					points += 1;
					holdAmount = 1;
					break;
				}
			}
		}
		return points;
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
		if (holdAmount > 0) {
			g2d.drawImage(
				imageCarry
				,at
				,null
			);
		} else {
			g2d.drawImage(
				image
				,at
				,null
			);
		}
	}

	public float getDir() {
		return dir;
	}
	
	public double getSightRange() {
		return sightRange;
	}
	
	public double getHolding() {
		return holdAmount;
	}
	
	public double getStrength(long gameTick) {
		if (gameTick >= lastScent + SCENT_TIME) {
			lastScent = gameTick;
			return scentAmount;
		}
		return 0.0;
	}
}
