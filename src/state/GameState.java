package state;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;

import display.Spritesheet;
import entity.Ant;
import entity.Colony;
import entity.Food;
import entity.Rock;
import entity.Scent;
import input.KeyManager;
import neuralNetwork.Neat;
import util.Ball;
import util.Vector;
import util.VisionType;

public class GameState extends State {
	
	private Spritesheet sprites;
	private Vector mapSize;
	private KeyManager keys;
	private long ticks = 0;
	private final long SECONDS_PER_GENERATION = 60;
	private final long FRAMES_PER_SECOND = 60;
	private final long TICKS_PER_GENERATION = SECONDS_PER_GENERATION * FRAMES_PER_SECOND;
	
	private final Color BG_COLOR = new Color(255, 167, 49);
	
	private final int MAX_ANTS = 100;
	private final int MAX_ROCKS = 16;
	private final int MAX_FOOD = 3;
	private final double FOOD_SIZE = 2;
	private ArrayList<Ant> ants;
	private ArrayList<Food> food;
	private ArrayList<Scent> scents;
	private ArrayList<Rock> rocks;
	
	// Colony
	private Colony colony;
	
	// NEAT
	private Neat neat;
	private final double[] VISION_RAYS = {-Math.PI * 0.5, -Math.PI * 0.25, 0, Math.PI * 0.25, Math.PI * 0.5};
	private final int INPUT_SIZE = (VISION_RAYS.length * VisionType.values().length) + 2;// (visionRays * visionTypes) + isHolding + bias=1
	private final int[] BRAIN_DIMENSIONS = {INPUT_SIZE, 8, 6, 3};
	
	private enum SIM_STATE {
		NOT_SPAWNED
		,RUNNING
		,RESET
	};
	private SIM_STATE currentState;
	
	public GameState(Vector displaySize, KeyManager keyManager) {
		// Map Size is entire display screen
		mapSize = displaySize;
		
		// Input
		keys = keyManager;// not needed for this sim
		
		// Assets
		sprites = new Spritesheet("sprites.png", 8, 8);
		
		// Set Sim State
		currentState = SIM_STATE.NOT_SPAWNED;
		
		// Create Colony
		colony = new Colony(
			sprites.getImage(4)
			,new Vector(
				mapSize.x * 0.5f
				,mapSize.y * 0.5f
			)
			,2.0
		);
		
		// Set Up NEAT
		neat = new Neat(MAX_ANTS, BRAIN_DIMENSIONS, 0.01, 0.01);
		
	}

	@Override
	public void update() {
		switch (currentState) {
			case NOT_SPAWNED:
				scents = new ArrayList<Scent>();
				spawnRocks();
				spawnFood();
				spawnAnts();
				currentState = SIM_STATE.RUNNING;
				ticks = 0;
				break;
			case RUNNING:
				updateAnts();
				scents.forEach(s -> s.update(ticks));
				ticks++;
				if (ticks >= TICKS_PER_GENERATION) {
					currentState = SIM_STATE.RESET;
				}
				break;
			case RESET:
				neat.nextGeneration();
				currentState = SIM_STATE.NOT_SPAWNED;
				break;
		}
		// System.out.println("Ticks: " + ticks);
	}

	@Override
	public void render(Graphics2D g2d) {
		switch (currentState) {
			case NOT_SPAWNED:
				break;
			case RUNNING:
				// Background
				g2d.setColor(BG_COLOR);
				g2d.fillRect(0, 0, (int)mapSize.x, (int)mapSize.y);
				
				// Rocks
				rocks.forEach(r -> r.render(g2d));

				// Scents
				scents.forEach(s -> s.render(g2d));
				
				// Ants
				ants.forEach(a -> a.render(g2d));
				
				// Food
				food.forEach(f -> f.render(g2d));
				
				// Colony
				colony.render(g2d);
				
				// Info
				g2d.setColor(Color.BLACK);
				g2d.setFont(new Font("monospace", Font.PLAIN, 24));
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2d.drawString("G:" + neat.getGeneration() + " F:" + colony.getFood() + " T:" + (60 - (ticks / 60)), 0, mapSize.y);
				break;
			case RESET:
				break;
		}
	}
	
	public void placeScent(Vector pos, double strength) {
		Scent s = new Scent(sprites.getImage(3), pos, strength);
		for (int j = 0; j < scents.size(); j++) {
			if (scents.get(j).vsBall(s)) {
				scents.get(j).addStrength(strength);
				return;
			}
		}
		for (int j = 0; j < scents.size(); j++) {
			if (!scents.get(j).isAlive()) {
				scents.set(j, s);
				return;
			}
		}
		scents.add(s);
	}
	
	public void spawnAnts() {
		ants = new ArrayList<Ant>();
		for (int i = 0; i < MAX_ANTS; i++) {
			ants.add(new Ant(
				sprites.getImage(0)
				,sprites.getImage(1)
				,colony.pos
				,1.0
			));
		}
	}
	
	public void spawnRocks() {
		rocks = new ArrayList<Rock>();
		for (int i = 0; i < MAX_ROCKS; i++) {
			Rock r = new Rock(
				sprites.getImage(5)
				,new Vector(0, 0)
				,1 + (Math.random() * 2)
			);
			boolean collides = true;
			while (collides) {
				if (Math.random() > 0.5) {
					r.pos.x = (float)(Math.random() * (mapSize.x * 0.3f)) + (mapSize.x * 0.1f);
				} else {
					r.pos.x = mapSize.x - (mapSize.x * 0.1f) - (float)(Math.random() * (mapSize.x * 0.3f));
				}
				if (Math.random() > 0.5) {
					r.pos.y = (float)(Math.random() * (mapSize.y * 0.3f)) + (mapSize.x * 0.1f);
				} else {
					r.pos.y = mapSize.y - (mapSize.y * 0.1f) - (float)(Math.random() * (mapSize.y * 0.3f));
				}
				collides = false;
				for (int j = 0; j < rocks.size(); j++) {
					if (rocks.get(j).vsBall(r)) {
						collides = true;
						break;
					}
				}
			}
			rocks.add(r);
		}
	}
	
	public void spawnFood() {
		food = new ArrayList<Food>();
		for (int i = 0; i < MAX_FOOD; i++) {
			boolean collides = true;
			Vector foodPos = new Vector(0, 0);
			Food f = new Food(sprites.getImage(2), foodPos, FOOD_SIZE);
			while (collides) {
				if (Math.random() > 0.5) {
					foodPos.x = (float)Math.random() * (mapSize.x * 0.3f) + (mapSize.x * 0.1f);
				} else {
					foodPos.x = mapSize.x - (mapSize.x * 0.1f) - (float)(Math.random() * (mapSize.x * 0.3f));
				}
				if (Math.random() > 0.5) {
					foodPos.y = (float)Math.random() * (mapSize.y * 0.3f) + (mapSize.y * 0.1f);
				} else {
					foodPos.y = mapSize.y - (mapSize.y * 0.1f) - (float)(Math.random() * (mapSize.y * 0.3f));
				}
				f.pos = foodPos;
				collides = false;
				for (int j = 0; j < rocks.size(); j++) {
					if (rocks.get(j).vsBall(f)) {
						collides = true;
						break;
					}
				}
			}
			food.add(f);
		}
	}
	
	public void updateAnts() {
		for (int i = 0; i < ants.size(); i++) {
			// Generate Input
			double[] input = createAntVision(ants.get(i));			
			// Generate Output
			double[] output = neat.processInput(i, input);
			// Update Position
			ants.get(i).update(output, mapSize, rocks);//mapSize, obstacles
			// Place Scent
			double strength = ants.get(i).getStrength(ticks);
			if (strength > 0) {
				placeScent(ants.get(i).pos, strength);
			}
			// Interact with world
			double points = ants.get(i).interact(colony, food);
			neat.addPoints(i, points);
		}
	}
	
	public double[] createAntVision(Ant ant) {
		double[] visionOutput = new double[(VISION_RAYS.length * VisionType.values().length) + 2];
		int visionType = 0;
		Vector startPos = ant.pos.add(Vector.fromAngle(ant.getDir()).mul(ant.radius));
		// Rocks
		for (int i = 0; i < VISION_RAYS.length; i++) {
			double foundDist = ant.getSightRange();
			Vector endPoint = startPos.add(Vector.fromAngle(ant.getDir() + (float)VISION_RAYS[i]).mul((float)foundDist));
			if (endPoint.x < 0 || endPoint.x >= mapSize.x || endPoint.y < 0 || endPoint.y >= mapSize.y) {
				foundDist = 0;
			} else {
				for (int j = 0; j < rocks.size(); j++) {
					double dist = Ball.vsRayGetDist(
						startPos
						,ant.pos.add(Vector.fromAngle(ant.getDir() + (float)VISION_RAYS[i]).mul((float)foundDist))
						,rocks.get(j)
					);
					if (dist < foundDist) {
						foundDist = dist;
					}
				}
			}
			visionOutput[i + visionType] = 1 - Math.max(0, Math.min(foundDist / ant.getSightRange(), 1));
		}
		visionType += VISION_RAYS.length;
		// Food
		for (int i = 0; i < VISION_RAYS.length; i++) {
			double foundDist = ant.getSightRange();
			for (int j = 0; j < food.size(); j++) {
				double dist = Ball.vsRayGetDist(
					startPos
					,startPos.add(Vector.fromAngle(ant.getDir() + (float)VISION_RAYS[i]).mul((float)foundDist))
					,food.get(j)
				);
				if (dist < foundDist) {
					foundDist = dist;
				}
			}
			visionOutput[i + visionType] = 1 - Math.max(0, Math.min(foundDist / ant.getSightRange(), 1));
		}
		visionType += VISION_RAYS.length;
		// Scent
		for (int i = 0; i < VISION_RAYS.length; i++) {
			double foundDist = ant.getSightRange();
			double foundStrength = 0;
			for (int j = 0; j < scents.size(); j++) {
				if (scents.get(j).isAlive()) {
					double dist = Ball.vsRayGetDist(
						startPos
						,startPos.add(Vector.fromAngle(ant.getDir() + (float)VISION_RAYS[i]).mul((float)foundDist))
						,scents.get(j)
					);
					if (dist < foundDist && scents.get(j).getStrength() > foundStrength) {
						foundStrength = scents.get(j).getStrength();
					}
				}
			}
			visionOutput[i + visionType] = foundStrength;
		}
		visionType += VISION_RAYS.length;
		// Colony
		for (int i = 0; i < VISION_RAYS.length; i++) {
			double foundDist = ant.getSightRange();
			double dist = Ball.vsRayGetDist(
				startPos
				,startPos.add(Vector.fromAngle(ant.getDir() + (float)VISION_RAYS[i]).mul((float)foundDist))
				,colony
			);
			if (dist < foundDist) {
				foundDist = dist;
			}
			visionOutput[i + visionType] = 1 - Math.max(0, Math.min(foundDist / ant.getSightRange(), 1));
		}
		visionType += VISION_RAYS.length;
		
		// Holding
		visionOutput[(VISION_RAYS.length * VisionType.values().length)] = ant.getHolding();
		// Bias
		visionOutput[(VISION_RAYS.length * VisionType.values().length) + 1] = 1;
		
		return visionOutput;
	}

}
