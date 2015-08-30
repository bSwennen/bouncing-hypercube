package animation;

import java.awt.Graphics;

public class DrawObject {
	double xPos = 0;
	double yPos = 0;
	double xStep = 1;
	double yStep = 1;
	double xMin;
	double xMax;
	double yMin;
	double yMax;
	double startTime = 0;
	double timeStep = .1;
	double time;

	public DrawObject(double xMinIn, double xMaxIn, double yMinIn, double yMaxIn) {
		xMin = xMinIn;
		xMax = xMaxIn;
		yMin = yMinIn;
		yMax = yMaxIn;
		time = startTime;
	}

	public void draw(Graphics graphic) {
		graphic.drawRect(((Double) xPos).intValue(), ((Double) yPos).intValue(), 10, 10);
	}

	public void run(double timeStep) {
		for (double t = time; t < time + timeStep; t++) {
			xPos += xStep;
			yPos += yStep;

			if (xPos < xMin || xPos > xMax)
				xStep *= -1;

			if (yPos < yMin || yPos > yMax)
				yStep *= -1;
		}
	}
}
