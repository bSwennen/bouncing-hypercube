package springSystem;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import sMath.SMath;
import utility.History;

public class Energy {
	SpringSystem springSystem;
	Rectangle drawRectangle;
	// TODO get from input
	int drawTimeStep = 10; // number of pixels
	public double kineticEnergy;
	public History kineticEnergyHistory;
	public double elasticEnergy;
	public History elasticEnergyHistory;
	public double gravitationalEnergy;
	public History gravitationalEnergyHistory;
	public double totalEnergy;
	public History totalEnergyHistory;
	double maxEnergy;

	public Energy(SpringSystem system, Rectangle rectangle) {
		springSystem = system;
		drawRectangle = rectangle;
		int historySize = ((Double) (drawRectangle.getWidth() / drawTimeStep)).intValue();
		kineticEnergyHistory = new History(historySize);
		elasticEnergyHistory = new History(historySize);
		gravitationalEnergyHistory = new History(historySize);
		totalEnergyHistory = new History(historySize);
		maxEnergy = 0;
	}

	private void updateKineticEnergy() {
		double energy = 0;

		for (Particle particle : springSystem.particles)
			energy += particle.kineticEnergy();

		kineticEnergy = energy;
	}

	private void updateElasticEnergy() {
		double energy = 0;

		for (Spring spring : springSystem.springs)
			energy += spring.elasticEnergy();

		elasticEnergy = energy;
	}

	private void updateGravitEnergy() {
		double energy = 0;
		double[] gravity = springSystem.gravity;
		double[] lowerWalls = springSystem.lowerWalls;
		double[] upperWalls = springSystem.upperWalls;
		int d = gravity.length;

		for (Particle particle : springSystem.particles) {
			double factor = 0;
			for (int i = 0; i < d; i++)
				if (gravity[i] < 0)
					factor += Math.abs(gravity[i] * (particle.position[i] - lowerWalls[i]));
				// factor += gravity[i] * (walls[i][0] - particle.position[i]);
				else
					factor += Math.abs(gravity[i] * (upperWalls[i] - particle.position[i]));
			// factor += gravity[i] * (walls[i][1] - particle.position[i]);

			energy += particle.mass * factor;
		}
		gravitationalEnergy = energy;
	}

	public void update() {
		// TODO just one loop through particles here and loop through attached
		// springs for elastic energy
		updateKineticEnergy();
		updateElasticEnergy();
		updateGravitEnergy();

		totalEnergy = kineticEnergy + elasticEnergy + gravitationalEnergy;
	}

	// public void printEnergies(PrintWriter writer) {
	// writer.println(kineticEnergy + "," + elasticEnergy + "," +
	// gravitationalEnergy + "," + totalEnergy);
	// }

	public void draw(Graphics graphic) {
		int dotWidth = 5;
		int dotHeight = 5;
		double rectX = drawRectangle.getX();
		double rectY = drawRectangle.getY();
		double rectWidth = drawRectangle.getWidth();
		double rectHeight = drawRectangle.getHeight();
		double rectMidX = rectX + rectWidth / 2.;
		int dotX = ((Double) (rectMidX - dotWidth / 2.)).intValue();

		double[] energies = { kineticEnergy, elasticEnergy, gravitationalEnergy, totalEnergy };
		double max = SMath.arrayMax(energies);
		if (max > maxEnergy)
			maxEnergy = max;

		History[] energyHistorys = { kineticEnergyHistory, elasticEnergyHistory, gravitationalEnergyHistory,
				totalEnergyHistory };

		Color[] colors = { Color.GREEN, Color.RED, Color.BLUE, Color.BLACK };

		for (int i = 0; i < energies.length; i++) {
			graphic.setColor(colors[i]);
			int dotY = ((Double) (rectY + rectHeight * (1 - energies[i] / maxEnergy) - dotHeight / 2.)).intValue();
			History history = energyHistorys[i];
			history.add(dotY);

			double[] histArray = history.getArray();
			for (int j = 0; j < histArray.length; j++) {
				dotX = j * drawTimeStep;
				graphic.fillOval(dotX, (int) histArray[j], dotWidth, dotHeight);
			}
		}
	}
}