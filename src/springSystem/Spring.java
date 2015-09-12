package springSystem;

import java.awt.Graphics;
import java.awt.Color;
import sMath.Vector;

public class Spring {
	Particle[] attachedParticles;
	double k;
	double l0;

	Spring(Particle part1, Particle part2, double l0In, double kIn) {
		// TODO array can be initialized shorter?
		attachedParticles = new Particle[2];
		attachedParticles[0] = part1;
		attachedParticles[1] = part2;
		k = kIn;
		l0 = l0In;
	}

	public void draw(Graphics graphic) {
		graphic.setColor(Color.BLACK);
		int x1 = ((Double) attachedParticles[0].position[0]).intValue();
		int y1 = 0;
		if (attachedParticles[0].position.length > 1)
			y1 = ((Double) attachedParticles[0].position[1]).intValue();

		int x2 = ((Double) attachedParticles[1].position[0]).intValue();
		int y2 = 0;
		if (attachedParticles[1].position.length > 1)
			y2 = ((Double) attachedParticles[1].position[1]).intValue();

		graphic.drawLine(x1, y1, x2, y2);
	}

	public double[] getForce(Particle particle) {
		int mult = particle == attachedParticles[0] ? 1 : -1;

		double[] forceDir = Vector.add(attachedParticles[1].position, Vector.mult(-1, attachedParticles[0].position));
		double length = Vector.size(forceDir);

		return Vector.mult(mult * k * (length - l0) / length, forceDir);
	}

	public double elasticEnergy() {
		double elongation = Vector.distance(attachedParticles[1].position, attachedParticles[0].position) - l0;
		return .5 * k * elongation * elongation;
	}
}