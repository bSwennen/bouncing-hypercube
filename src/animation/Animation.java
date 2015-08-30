package animation;

import java.awt.EventQueue;
import javax.swing.JFrame;

//TODO pause button
public class Animation extends JFrame {
	private static final long serialVersionUID = 1L;

	public Animation() {
		add(new DrawEngine());
		setResizable(false);
		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				JFrame anim = new Animation();
				anim.setVisible(true);
			}
		});
	}
}
