package utility;

import java.util.Arrays;

public class History {
	private int maxSize;
	private int size;
	private double[] history;

	// TODO make iteratable
	public History(int max) {
		maxSize = max;
		size = 0;
		history = new double[maxSize];
	}

	public void add(double a) {
		if (size < maxSize)
			history[size++] = a;
		else {
			// shift left
			for (int i = 0; i < size - 1; i++)
				history[i] = history[i + 1];
			history[size - 1] = a;
		}
	}

	public double[] getArray() {
		return Arrays.copyOfRange(history, 0, size);
	}

	public double getValue(int i) {
		return history[i];
	}

	public int getSize() {
		return size;
	}
}