package springSystem;

import java.awt.Color;
import java.awt.Graphics;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import sMath.Vector;

public class Particle {

	double[] position;
	double[] velocity;
	double[] acceleration;
	ArrayList<Spring> attachedSprings;
	double mass;
	static final int HEIGTH = 5;
	static final int WIDTH = 5;

	Particle(double[] pos, double[] vel, double[] acc, double m) {
		position = Vector.copy(pos);

		velocity = Vector.copy(vel);
		acceleration = Vector.copy(acc);

		attachedSprings = new ArrayList<>();
		mass = m;
	}

	public void print(PrintWriter writer, double t) {
		writer.println(t + ";" + Arrays.toString(position) + ";" + Arrays.toString(velocity) + ";"
				+ Arrays.toString(acceleration));
	}

	// TODO does this use initial acc?
	// TODO acc should only exist in this function?
	public void update(double timeStep, double[] lowerWalls, double[] upperWalls, double[] gravity) {
		acceleration = getAcceleration(gravity);
		position = Vector.add(position, Vector.mult(timeStep, velocity), Vector.mult(timeStep * timeStep / 2, acceleration));

		double[] intVelocity = Vector.add(velocity, Vector.mult(timeStep / 2, acceleration));

		acceleration = getAcceleration(gravity);

		velocity = Vector.add(intVelocity, Vector.mult(timeStep / 2, acceleration));

		elasticWalls(lowerWalls, upperWalls);
	}

	private double[] getAcceleration(double[] gravity) {
		int d = position.length;
		double[] springForce = Vector.zerosDoubles(d);

		for (Spring spring : attachedSprings)
			springForce = Vector.add(springForce, spring.getForce(this));

		double[] springAcc = Vector.mult(1 / mass, springForce);
		
		return Vector.add(springAcc, gravity);
	}

	private void elasticWalls(double[] lowerWalls, double[] upperWalls) {
		for (int i = 0; i < position.length; i++) {
			if (position[i] > upperWalls[i] || position[i] < lowerWalls[i])
				velocity[i] = -velocity[i];
		}
	}

	public void draw(Graphics graphic) {
		graphic.setColor(Color.RED);
		int y = -HEIGTH / 2;
		if (position.length > 1)
			y = ((Double) (position[1] - HEIGTH / 2.)).intValue();

		graphic.fillOval(((Double) (position[0] - WIDTH / 2.)).intValue(), y, WIDTH, HEIGTH);
	}

	public double kineticEnergy() {
		return .5 * mass * Vector.dotProduct(velocity, velocity);
	}
}