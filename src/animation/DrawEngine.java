package animation;

import io.Io;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import javax.swing.JPanel;
import javax.swing.Timer;

import springSystem.SpringSystem;

public class DrawEngine extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	SpringSystem system;
	private Timer timer;
	double runTimeStep;
	int xResolution;
	int yResolution;
	int drawTimer;

	// TODO this to input file
	// and check input
	int energyRectX;
	int energyRectY;
	int energyRectWidth;
	int energyRectHeight;

	int systemRectX;
	int systemRectY;
	int systemRectWidth;
	int systemRectHeight;

	Rectangle systemRectangle;
	Rectangle energyRectangle;

	public DrawEngine() {
		Io input = new Io("input/drawInput.properties");

		// TODO get color from input as well
		runTimeStep = input.getDouble("RUN_TIME_STEP");
		xResolution = input.getInt("X_RESOLUTION");
		yResolution = input.getInt("Y_RESOLUTION");
		drawTimer = input.getInt("DRAW_TIMER");

		energyRectX = 0;
		energyRectHeight = 200;
		energyRectY = yResolution - energyRectHeight;
		energyRectWidth = xResolution;

		systemRectX = 0;
		systemRectY = 0;
		systemRectWidth = xResolution;
		systemRectHeight = yResolution - energyRectHeight;

		energyRectangle = new Rectangle(energyRectX, energyRectY, energyRectWidth, energyRectHeight);
		systemRectangle = new Rectangle(systemRectX, systemRectY, systemRectWidth, systemRectHeight);

		setBackground(Color.WHITE);
		setPreferredSize(new Dimension(xResolution, yResolution));
		try {
			system = new SpringSystem(systemRectangle, energyRectangle, new PrintWriter("output/particles.csv"),
					new PrintWriter("output/energy.csv"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		timer = new Timer(drawTimer, this);
		timer.start();
	}

	public void paintComponent(Graphics graphic) {
		super.paintComponent(graphic);
		system.draw(graphic);
		system.drawEnergy(graphic);
	}

	public void actionPerformed(ActionEvent action) {
		repaint();
		system.run(runTimeStep);
	}
}