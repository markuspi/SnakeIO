package de.markuspi.snake;

import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

public class MatingPool<E> {

	private final NavigableMap<Double, E> map = new TreeMap<>();
	private final Random random = new Random();
	private double total = 0;

	public void add(double weight, E item) {
		if (weight <= 0)
			return;

		total += weight;
		map.put(total, item);
	}

	public void clear() {
		map.clear();
		total = 0;
	}

	public int size() {
		return map.size();
	}

	public E randomEntry() {
		double value = random.nextDouble() * total;
		return map.ceilingEntry(value).getValue();
	}

}
