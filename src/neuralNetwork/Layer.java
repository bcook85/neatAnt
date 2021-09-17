package neuralNetwork;

public class Layer {

	protected int size;
	protected Neuron[] neurons;
	
	public Layer(int neuronCount, int weightCount) {
		size = neuronCount;
		neurons = new Neuron[size];
		for (int i = 0; i < size; i++) {
			neurons[i] = new Neuron(weightCount);
		}
	}
	
	public double[] feedNeurons(double[] inputs) {
		double[] output = new double[size];
		for (int i = 0; i < size; i++) {
			output[i] = neurons[i].calculateSignal(inputs);
		}
		return output;
	}
}