package neuralNetwork;

public class Neuron {
	protected int size;
	protected double[] weights;
	protected double bias;
	
	public Neuron(int size) {
		this.size = size;
		weights = new double[size];
		for (int i = 0; i < size; i++) {
			weights[i] = ((Math.random() * 2) - 1);
		}
		bias = (Math.random() * 2) - 1;
	}
	
	public double calculateSignal(double[] inputs) {
		double sum = 0;
		for (int i = 0; i < size; i++) {
			sum += inputs[i] * weights[i];
		}
		return sum + bias;
	}
}