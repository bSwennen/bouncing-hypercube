package sMath;

public class Vector {

	// public static double[] add(double[] a, double[] b) {
	// double[] sum = new double[a.length];
	//
	// for (int i = 0; i < a.length; i++)
	// sum[i] = a[i] + b[i];
	//
	// return sum;
	// }

	public static double[] add(double[]... vectors) {
		int n = vectors[0].length;
		double[] result = new double[n];

		for (int i = 0; i < n; i++) {
			double sum = 0;
			for (double[] vector : vectors)
				sum += vector[i];

			result[i] = sum;
		}
		return result;
	}

	public static double[] add(double[] a, int[] b) {
		double[] sum = new double[a.length];

		for (int i = 0; i < a.length; i++)
			sum[i] = a[i] + b[i];

		return sum;
	}

	public static double[] mult(int a, double[] v) {
		double[] result = new double[v.length];
		for (int i = 0; i < v.length; i++)
			result[i] = a * v[i];
		return result;
	}

	public static double[] mult(double a, double[] v) {
		double[] result = new double[v.length];
		for (int i = 0; i < v.length; i++)
			result[i] = a * v[i];
		return result;
	}

	public static double[] mult(double a, int[] v) {
		double[] result = new double[v.length];
		for (int i = 0; i < v.length; i++)
			result[i] = a * v[i];
		return result;
	}

	public static double[] zerosDoubles(int d) {
		double[] result = new double[d];

		for (int i = 0; i < d; i++)
			result[i] = 0;

		return result;
	}

	public static int[] zerosInts(int d) {
		int[] result = new int[d];

		for (int i = 0; i < d; i++)
			result[i] = 0;

		return result;
	}

	/** returns the i'th basis vector of R^d */
	public static int[] basisVector(int i, int d) {
		int[] basis = new int[d];

		for (int j = 0; j < d; j++)
			basis[j] = j == i ? 1 : 0;

		return basis;
	}

	public static boolean equal(double[] a, double[] b) {
		int n = a.length;
		if (n != b.length)
			return false;

		int count = 0;
		for (int i = 0; i < n; i++)
			if (a[i] == b[i])
				count++;

		if (count == n)
			return true;

		return false;
	}

	public static double[] copy(double[] a) {
		int n = a.length;
		double[] b = new double[n];

		for (int i = 0; i < n; i++)
			b[i] = a[i];

		return b;
	}

	public static double dotProduct(double[] a, double[] b) {
		double sum = 0;

		for (int i = 0; i < a.length; i++)
			sum += a[i] * b[i];

		return sum;
	}

	public static double size(double[] a) {
		return Math.sqrt(dotProduct(a, a));
	}

	// TODO use this everywhere
	public static double[] subtract(double[] a, double[] b) {
		return add(a, mult(-1, b));
	}

	public static double distance(double[] a, double[] b) {
		return size(subtract(a, b));
	}
}