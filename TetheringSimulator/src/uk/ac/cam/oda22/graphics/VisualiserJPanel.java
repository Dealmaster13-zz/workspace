package uk.ac.cam.oda22.graphics;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import uk.ac.cam.oda22.graphics.shapes.Circle;
import uk.ac.cam.oda22.graphics.shapes.DisplayShape;
import uk.ac.cam.oda22.graphics.shapes.Line;

/**
 * @author Oliver
 *
 */
public class VisualiserJPanel extends JPanel implements IVisualiser {

	private static final long serialVersionUID = 4146428062464803588L;
	
	private static final Object drawLock = new Object();
	
	private List<Line> lines;
	
	private List<Circle> circles;
	
	private Point2D offset;
	
	private double xScale;
	
	private double yScale;
	
	public VisualiserJPanel() {
		this.lines = new LinkedList<Line>();
		this.circles = new LinkedList<Circle>();
		
		this.offset = new Point2D.Double(50, 200);
		this.xScale = 2;
		this.yScale = 2;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		draw(g);
	}

	@Override
	public void drawLine(Line line) {
		synchronized (drawLock) {
			this.lines.add(line);
			updateGraphics();
		}
	}

	@Override
	public void drawLines(Line[] lines) {
		for (Line line : lines) {
			drawLine(line);
		}
	}

	@Override
	public void drawCircle(Circle circle) {
		synchronized (drawLock) {
			this.circles.add(circle);
			updateGraphics();
		}
	}

	private void draw(Graphics g) {
		synchronized (drawLock) {
			Graphics2D graphics = (Graphics2D)g;

			// Add all lines to the graphics.
			for (Line line : this.lines) {
				graphics.setColor(line.colour);
				graphics.setStroke(new BasicStroke(line.thickness));

				Line newLine = (Line) transformShape(line);
				
				graphics.draw(newLine.l);
			}
			
			// Add all circles to the graphics.
			for (Circle circle : this.circles) {
				graphics.setColor(circle.colour);
				graphics.setStroke(new BasicStroke(circle.thickness));

				Circle newCircle = (Circle) transformShape(circle);
				
				graphics.draw(newCircle.c);
			}
		}
	}
	
	private void updateGraphics() {
	    SwingUtilities.invokeLater(new Runnable() {
	        public void run() {
	            validate();
	            repaint();
	        }
	    });
	}
	
	private DisplayShape transformShape(DisplayShape s) {
		return s.flipY().translate(this.offset.getX(), this.offset.getY()).stretch(this.xScale, this.yScale);
	}

}
