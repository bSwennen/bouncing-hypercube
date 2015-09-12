package io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Io {
	Properties prop;

	public Io(String path) {
		prop = new Properties();

		try {
			// InputStream input =
			// Io.class.getResourceAsStream("input2/systemInput.properties");
			// "input/systemInput.properties"
			InputStream input = new FileInputStream(path);
			prop.load(input);
			input.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public double getDouble(String key) {
		return Double.parseDouble(prop.getProperty(key));
	}

	public int getInt(String key) {
		return Integer.parseInt(prop.getProperty(key));
	}

	public double[] getDoubleArray(String key, int d) {
		double[] array = new double[d];
		String inString = prop.getProperty(key);
		String[] stringArray = inString.split(",");
		int length = stringArray.length;
		if (length > d) {
			System.out.println("The array entered for " + key + " is too long, excess part has been dropped.");
			length = d;
		}
		// if array to short, fill with last entered value
		for (int i = 0; i < length; i++)
			array[i] = Double.parseDouble(stringArray[i].replace("{", "").replace("}", ""));
		// TODO problem if length = 0, maybe throw exception
		for (int i = length; i < d; i++)
			array[i] = array[i - 1];

		return array;
	}

	// public double[][] getDoubleArrayArray(String key, int d) {
	// double[] array = new double[d];
	// String inString = prop.getProperty(key);
	// String[] stringArray = inString.split(",");
	// int length = stringArray.length;
	//
	// // if array to short, fill with last entered value
	// for (int i = 0; i < length; i++)
	// array[i] = Double.parseDouble(stringArray[0].replace("{", "").replace("}",
	// ""));
	// // problem if length = 0, maybe throw exception
	// for (int i = length; i < d; i++)
	// array[i] = array[i - 1];
	//
	// return array;
	// }
}