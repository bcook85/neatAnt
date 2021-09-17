package neuralNetwork;

import java.util.Arrays;

public class Neat {

	private int populationSize;
	private double mutationChance;
	private double mutationAmount;
	private int generation;
	private Brain[] brains;
	private int[] dimensions;
	
	public Neat(int populationSize, int[] dimensions, double mutationChance, double mutationAmount) {
		this.populationSize = populationSize;
		this.dimensions = dimensions;
		this.mutationChance = mutationChance;
		this.mutationAmount = mutationAmount;
		generation = 0;
		brains = new Brain[populationSize];
		for (int i = 0; i < populationSize; i++) {
			brains[i] = new Brain(dimensions);
		}
	}
	
	public void addPoints(int id, double amount) {
		if (id >= 0 && id < populationSize) {
			brains[id].addPoints(amount);
		}
	}
	
	public double[] processInput(int id, double[] inputs) {
		if (id >= 0 && id < populationSize) {
			return brains[id].feedForward(inputs);
		}
		return null;
	}
	
	public void nextGeneration() {
		Arrays.sort(brains, (a,b) -> (int)(b.getScore() - a.getScore()));
		Brain[] newBrains = new Brain[populationSize];
		for (int i = 0; i < populationSize; i++) {
			Brain parent1 = pickTop(0.1);
			Brain parent2 = pickTop(0.5);
			Brain newBrain = crossover(parent1, parent2);
			mutateBrain(newBrain);
			newBrains[i] = newBrain;
		}
		brains = newBrains;
		generation += 1;
	}
	
	public Brain pickTop(double limit) {
		return brains[(int)Math.floor(Math.random() * (brains.length * limit))];
	}
	
	public Brain crossover(Brain p1, Brain p2) {
		Brain newBrain = new Brain(dimensions);
		for (int i = 0; i < newBrain.layers.length; i++) {
			for (int j = 0; j < newBrain.layers[i].neurons.length; j++) {
				for (int k = 0; k < newBrain.layers[i].neurons[j].size; k++) {
					if (Math.random() > 0.5) {
						newBrain.layers[i].neurons[j].weights[k] = p1.layers[i].neurons[j].weights[k];
					} else {
						newBrain.layers[i].neurons[j].weights[k] = p2.layers[i].neurons[j].weights[k];
					}
				}
				if (Math.random() > 0.5) {
					newBrain.layers[i].neurons[j].bias = p1.layers[i].neurons[j].bias;
				} else {
					newBrain.layers[i].neurons[j].bias = p2.layers[i].neurons[j].bias;
				}
			}
			
		}
		return newBrain;
	}
	
	public void mutateBrain(Brain brain) {
		for (int i = 0; i < brain.layers.length; i++) {
			for (int j = 0; j < brain.layers[i].size; j++) {
				for (int k = 0; k < brain.layers[i].neurons[j].size; k++) {
					brain.layers[i].neurons[j].weights[k] = mutateValue(brain.layers[i].neurons[j].weights[k]);
				}
				brain.layers[i].neurons[j].bias = mutateValue(brain.layers[i].neurons[j].bias);
			}
		}
	}
	
	public double mutateValue(double value) {
		double output = value;
		if (Math.random() <= mutationChance) {
	      if (Math.random() > 0.5) {
	        output += mutationAmount;
	        if (output > 1) {
	          output = 1;
	        }
	      } else {
	        output -= mutationAmount;
	        if (output < -1) {
	          output = -1;
	        }
	      }
	    }
		return output;
	}
	
	public int getGeneration() {
		return generation;
	}
}
