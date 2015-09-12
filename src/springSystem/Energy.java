package springSystem;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.PrintWriter;

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
	PrintWriter writer;

	public Energy(SpringSystem system, Rectangle rectangle, PrintWriter energyWriter) {
		springSystem = system;
		drawRectangle = rectangle;
		int historySize = ((Double) (drawRectangle.getWidth() / drawTimeStep)).intValue();
		kineticEnergyHistory = new History(historySize);
		elasticEnergyHistory = new History(historySize);
		gravitationalEnergyHistory = new History(historySize);
		totalEnergyHistory = new History(historySize);
		maxEnergy = 0;
		writer = energyWriter;
		writer.println("t;kineticEnergy;gravitationalEnergy;elasticEnergy;totalEnergy");
		// to initialize energies
		update();
	}

	public void update() {
		double[] gravity = springSystem.gravity;
		double[] lowerWalls = springSystem.lowerWalls;
		double[] upperWalls = springSystem.upperWalls;
		int d = gravity.length;

		double kinE = 0;
		double elastE = 0;
		double gravE = 0;

		for (Particle particle : springSystem.particles) {
			kinE += particle.kineticEnergy();

			double factor = 0;
			for (int i = 0; i < d; i++)
				if (gravity[i] < 0)
					factor += Math.abs(gravity[i] * (particle.position[i] - lowerWalls[i]));
				else
					factor += Math.abs(gravity[i] * (upperWalls[i] - particle.position[i]));

			gravE += particle.mass * factor;

		}
		for (Spring spring : springSystem.springs)
			elastE += spring.elasticEnergy();

		kineticEnergy = kinE;
		gravitationalEnergy = gravE;
		elasticEnergy = elastE;

		totalEnergy = kineticEnergy + elasticEnergy + gravitationalEnergy;
	}

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
		// this will be 0 at first => divide by zero later
		if (max > maxEnergy)
			maxEnergy = max;

		History[] energyHistories = { kineticEnergyHistory, elasticEnergyHistory, gravitationalEnergyHistory,
				totalEnergyHistory };

		Color[] colors = { Color.GREEN, Color.RED, Color.BLUE, Color.BLACK };

		for (int i = 0; i < energies.length; i++) {
			graphic.setColor(colors[i]);
			int dotY = ((Double) (rectY + rectHeight * (1 - energies[i] / maxEnergy) - dotHeight / 2.)).intValue();
			History history = energyHistories[i];
			history.add(dotY);

			double[] histArray = history.getArray();
			for (int j = 0; j < histArray.length; j++) {
				dotX = j * drawTimeStep;
				graphic.fillOval(dotX, (int) histArray[j], dotWidth, dotHeight);
			}
		}
	}

	public void print(double t) {
		writer.println(t + ";" + kineticEnergy + ";" + gravitationalEnergy + ";" + elasticEnergy + ";" + totalEnergy);
	}
}