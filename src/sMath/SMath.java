package sMath;

public class SMath {

	public static int powInt(int num, double power) {
		return ((Double) Math.pow(num, power)).intValue();
	}

	public static int powInt(int num, int power) {
		return ((Double) Math.pow(num, power)).intValue();
	}

	public static double arrayMax(double[] array) {
		double max = array[0];
		for (int i = 1; i < array.length; i++)
			if (array[i] > max)
				max = array[i];

		return max;
	}

	public static int arrayMaxIndex(double[] array) {
		int max = 0;
		for (int i = 1; i < array.length; i++)
			if (array[i] > max)
				max = i;

		return max;
	}
}
