package de.markuspi.snake;

public class Network {

	private NetworkLayer[] layers;
	private int[] dimensions;
	
	public Network(boolean initialize, int... dimensions) {
		layers = new NetworkLayer[dimensions.length - 1];	
		this.dimensions = dimensions;
		if(initialize) init();
	}
	
	public Network(int... dimensions) {
		this(true, dimensions);
	}
	
	public void init() {
		for (int i=0; i<dimensions.length - 1; i++) {
			layers[i] = new NetworkLayer(dimensions[i+1], dimensions[i], true);			
		}
	}
	
	public Network crossover(Network other, double mutationRate) {
		Network child = new Network(false, this.dimensions);
		for (int i=0; i<layers.length; i++) {
			child.layers[i] = this.layers[i].crossover(other.layers[i], mutationRate);
		}
		return child;
	}
	
	public String serialize() {
		// TODO
		return "";
	}
	
	public double[] feedForward(double[] input) {
		double[] data = input;
		for (NetworkLayer layer : layers) {
			data = layer.feedForward(data);
		}
		return data;
	}
	
	public int decide(double[] input) {
		double[] output = feedForward(input);
		int maxIndex = -1;
		double maxValue = Double.NEGATIVE_INFINITY;
		for (int i=0; i<output.length; i++) {
			if (output[i] > maxValue) {
				maxIndex = i;
				maxValue = output[i];
			}
		}
		return maxIndex;
	}
	
}
