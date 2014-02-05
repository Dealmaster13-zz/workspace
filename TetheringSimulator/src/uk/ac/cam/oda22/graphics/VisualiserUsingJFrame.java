package uk.ac.cam.oda22.graphics;

import java.awt.geom.Point2D;

import javax.swing.JFrame;

import uk.ac.cam.oda22.graphics.shapes.DisplayShape;

/**
 * @author Oliver Allbless
 *
 */
public class VisualiserUsingJFrame extends JFrame implements IVisualiser {

	private static final long serialVersionUID = 2748836618770659263L;

	private VisualiserJPanel panel;
	
	public VisualiserUsingJFrame(Point2D offset, double xScale, double yScale) {
		this.init(offset, xScale, yScale);
	}

	public void init(Point2D offset, double xScale, double yScale) {
		this.setTitle("Simulation");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.panel = new VisualiserJPanel(offset, xScale, yScale);
		this.add(panel);

		this.pack();
		this.setVisible(true);

		this.setSize(800, 600);
		this.setLocationRelativeTo(null);
	}

	@Override
	public void drawShape(DisplayShape s) {
		this.panel.drawShape(s);
	}

	@Override
	public void drawShapes(DisplayShape[] ss) {
		this.panel.drawShapes(ss);
	}

}
