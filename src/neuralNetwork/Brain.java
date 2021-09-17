package neuralNetwork;

public class Brain {
	
	protected final double MIN_SCORE = 0.0;
	
	protected Layer[] layers;
	protected int[] dimensions;
	private double score;
	private double fitness;
	
	public Brain(int[] dimensions) {
		this.dimensions = dimensions;
		layers = new Layer[dimensions.length - 1];
		score = MIN_SCORE;
		fitness = 0.0;
		for (int i = 1; i < dimensions.length; i++) {
			layers[i - 1] = new Layer(dimensions[i], dimensions[i - 1]);
		}
	}
	
	public static double sigmoid(double x) {
		return 1 / (1 + Math.exp(-x));
	}
	
	public static double rectifiedLinear(double x) {
		return x <= 0 ? 0 : x;
	}
	
	public double[] feedForward(double[] inputs) {
		for (int i = 0; i < layers.length; i++) {
			inputs = layers[i].feedNeurons(inputs);
			for (int j = 0; j < inputs.length; j++) {
				if (i >= layers.length -1) {
					inputs[j] = Brain.sigmoid(inputs[j]);					
				} else {
					inputs[j] = Brain.rectifiedLinear(inputs[j]);
				}
			}
		}
		return inputs;
	}

	public double getScore() {
		return score;
	}
	
	public void addPoints(double points) {
		score += points;
		if (score < MIN_SCORE) {
			score = MIN_SCORE;
		}
	}

	public void setFitness(double fitness) {
		this.fitness = fitness;
	}
	
	public double getFitness() {
		return fitness;
	}
}