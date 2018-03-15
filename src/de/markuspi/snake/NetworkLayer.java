package de.markuspi.snake;

import java.util.Random;

public class NetworkLayer {
	
	private double[][] weights;
	private double[] biases;
	
	private int num_neurons;
	private int num_inputs;
	
	private Random rnd = new Random();
	
	public NetworkLayer(int num_neurons, int num_inputs, boolean initialize) {
		this.num_neurons = num_neurons;
		this.num_inputs = num_inputs;
		
		this.weights = new double[num_neurons][num_inputs];
		this.biases = new double[num_neurons];
		
		if(initialize) init();
	}
	
	public void init() {
		for (int n=0; n<num_neurons; n++) {
			for (int i=0; i<num_inputs; i++) {
				this.weights[n][i] = rnd.nextGaussian();
			}
			this.biases[n] = rnd.nextGaussian();
		}
	}
	
	public NetworkLayer crossover(NetworkLayer other, double mutationRate) {
		NetworkLayer child = new NetworkLayer(num_neurons, num_inputs, false);
		
		for (int n=0; n<num_neurons; n++) {
			for (int i=0; i<num_inputs; i++) {
				if (rnd.nextDouble() < mutationRate) {
					child.weights[n][i] = rnd.nextGaussian();
				} else {
					child.weights[n][i] = rnd.nextBoolean() ? this.weights[n][i] : other.weights[n][i];					
				}
			}
			
			if (rnd.nextDouble() < mutationRate) {
				child.biases[n] = rnd.nextGaussian();
			} else {
				child.biases[n] = rnd.nextBoolean() ? this.biases[n] : other.biases[n];
			}
		}
		
		return child;
	}
	
	public String serialize() {
		String str = "";
		for (int n=0; n<num_neurons; n++) {
			str += biases[n] + ";";
			for (int i=0; i<num_inputs; i++) {
				str += weights[n][i] + ";";
			}
			str += "|";
		}
		return str;
	}
	
	public double[] feedForward(double[] input) {
		double[] output = new double[num_neurons];
		
		for (int n=0; n<num_neurons; n++) {
			double sum = biases[n];
			
			for (int i=0; i<num_inputs; i++) {
				sum += weights[n][i] * input[i];
			}
			
			output[n] = sigmoid(sum);
		}
		
		return output;
	}
	
	public static double sigmoid(double inp) {
		return 1D / (1D + Math.exp(-inp)); 
	}

}
