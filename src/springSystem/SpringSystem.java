package springSystem;

import io.Io;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.PrintWriter;
import sMath.SMath;
import sMath.Vector;

public class SpringSystem {

	public Particle[] particles;
	public Spring[] springs;
	Energy energy;
	public double time;
	public int universeDim;
	public int systemDim;
	public int edge;
	public double timeStep;
	public double k;
	public double l0;
	public double m;
	public double initialDistance;
	public double[] initialCoords;
	public double[] initialVel;
	public double[] initialAcc;
	public double[] gravity;
	public double[] upperWalls;
	public double[] lowerWalls;
	public int numParticles;
	Rectangle drawRectangle;

	public SpringSystem(Rectangle systemRectangle, Rectangle energyDrawRectangle) {
		// TODO use this for wall defaults
		drawRectangle = systemRectangle;
		getInput();

		particles = new Particle[numParticles];

		for (int i = 0; i < numParticles; i++)
			particles[i] = new Particle(
					Vector.add(initialCoords, Vector.mult(initialDistance, intToArr(i, edge, universeDim))), initialVel,
					initialAcc, m);

		springs = new Spring[systemDim * SMath.powInt(edge, (systemDim - 1)) * (edge - 1)];
		int springInd = 0;
		for (Particle part : particles) {
			double[] position = part.position;
			// system will occupy the first systemDim dimensions of the universe
			// TODO change this by taking from input?
			for (int i = 0; i < systemDim; i++)
				if (position[i] != initialCoords[i] + initialDistance * (edge - 1))
					springs[springInd++] = new Spring(part, particleInDirection(part,
							Vector.mult(initialDistance, Vector.basisVector(i, universeDim))), l0, k);
		}
		for (Particle part : particles)
			for (Spring spring : springs) {
				if (spring.attachedParticles[0] == part || spring.attachedParticles[1] == part)
					part.attachedSprings.add(spring);
			}
		energy = new Energy(this, energyDrawRectangle);
	}

	private void getInput() {
		Io input = new Io("input/systemInput.properties");

		universeDim = input.getInt("UNIVERSE_DIMENSION");
		systemDim = input.getInt("SYSTEM_DIMENSION");
		if (universeDim < systemDim) {
			universeDim = systemDim;
			System.out.println("UNIVERSE_DIMENSION too small, corrected to SYSTEM_DIMENSION");
		}

		edge = input.getInt("EDGE");
		timeStep = input.getDouble("TIME_STEP");
		k = input.getDouble("K");
		l0 = input.getDouble("L0");
		m = input.getDouble("M");
		initialDistance = input.getDouble("INITIAL_DISTANCE_MULTIPLIER") * l0;
		initialCoords = input.getDoubleArray("INITIAL_COORDS", universeDim);
		initialVel = input.getDoubleArray("INITIAL_VEL", universeDim);
		initialAcc = input.getDoubleArray("INITIAL_ACC", universeDim);
		gravity = input.getDoubleArray("GRAVITY", universeDim);
		upperWalls = input.getDoubleArray("UPPER_WALLS", universeDim);
		lowerWalls = input.getDoubleArray("LOWER_WALLS", universeDim);
		numParticles = SMath.powInt(edge, systemDim);
		time = 0;

		boolean noFit = false;
		for (int i = 0; i < universeDim; i++) {
			if (initialCoords[i] < lowerWalls[i]) {
				initialCoords[i] = lowerWalls[i];
				noFit = true;
			}
			if (initialCoords[i] > upperWalls[i]) {
				initialCoords[i] = upperWalls[i];
				noFit = true;
			}
			if (initialCoords[i] + (edge - 1) * initialDistance > upperWalls[i]) {
				initialDistance = (upperWalls[i] - initialCoords[i]) / (edge - 1);
				noFit = true;
			}
		}
		if (noFit)
			System.out.println("The system doesn't fit between the walls, corrections have been made");
	}

	private Particle particleInDirection(Particle particle, double[] direction) {
		double[] position = Vector.add(particle.position, direction);

		for (Particle part : particles)
			if (Vector.equal(part.position, position))
				return part;

		return null;
	}

	private int[] intToArr(int number, int base, int d) {
		int[] arr = Vector.zerosInts(d);
		int digits = numDigits(number, base);
		int counted = 0;
		int factor;

		for (int i = digits - 1; i >= 0; i--) {
			factor = SMath.powInt(base, i);
			arr[i] = countDigits(number - counted, factor);
			counted += arr[i] * factor;
		}
		return arr;
	}

	private int countDigits(int number, int factor) {
		int count = 0;

		while (number - count * factor >= factor)
			count++;

		return count;
	}

	private int numDigits(int number, int base) {
		int digits = 1;

		while (number >= SMath.powInt(base, digits))
			digits++;

		return digits;
	}

	/**
	 * runs the system from time till time+runTime
	 */
	public void run(double runTime) {
		double start = time;
		for (; time < start + runTime; time += timeStep) {
			for (Particle part : particles)
				part.update(timeStep, lowerWalls, upperWalls, gravity);

			energy.update();
		}
	}

	public void draw(Graphics graphic) {
		for (Particle part : particles)
			part.draw(graphic);

		for (Spring spring : springs)
			spring.draw(graphic);
	}

	public void drawEnergy(Graphics graphic) {
		energy.draw(graphic);
	}

	public void printParticles(PrintWriter partWriter) {
		for (Particle particle : particles)
			particle.print(partWriter);
	}
}