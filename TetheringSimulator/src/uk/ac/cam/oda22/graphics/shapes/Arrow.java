package uk.ac.cam.oda22.graphics.shapes;

import java.awt.Color;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import uk.ac.cam.oda22.core.logging.Log;

/**
 * @author Oliver
 *
 */
public class Arrow extends DisplayShape {

	private final Point2D position;
	
	private final double rotation;
	
	private final double length;
	
	private final double headLength;
	
	public final Line2D[] ls;

	public Arrow(Point2D position, double rotation, double length, double headLength, Color colour, float thickness) {
		super(colour, thickness);
		
		this.position = position;
		this.rotation = rotation;
		this.length = length;
		this.headLength = headLength;
		
		this.ls = getLines(position, rotation, length, headLength);
	}

	/**
	 * @param l
	 * @param thickness
	 */
	public Arrow(Point2D position, double rotation, double length, double headLength, float thickness) {
		super(thickness);
		
		this.position = position;
		this.rotation = rotation;
		this.length = length;
		this.headLength = headLength;

		this.ls = getLines(position, rotation, length, headLength);
	}
	
	@Override
	public DisplayShape translate(double x, double y) {
		Point2D newPosition = new Point2D.Double(this.position.getX() + x, this.position.getY() + y);
		
		return new Arrow(newPosition, this.rotation, this.length, this.headLength, this.colour, this.thickness);
	}

	@Override
	public DisplayShape stretch(double xScale, double yScale) {
		if (xScale != yScale) {
			Log.warning("Scale parameters are not equal, so horizontal scaling factor will be used.");
		}
		
		return new Arrow(this.position, this.rotation, this.length * xScale, this.headLength * xScale, this.colour, this.thickness);
	}
	
	private static Line2D[] getLines(Point2D position, double rotation, double length, double headLength) {
		Line2D[] lines = new Line2D[3];
		
		double headX = position.getX() + (length * Math.cos(rotation));
		double headY = position.getY() + (length * Math.sin(rotation));

		double headTailX1 = headX + (headLength * Math.cos(rotation + (Math.PI * 3 / 4)));
		double headTailY1 = headX + (headLength * Math.sin(rotation + (Math.PI * 3 / 4)));

		double headTailX2 = headX + (headLength * Math.cos(rotation - (Math.PI * 3 / 4)));
		double headTailY2 = headX + (headLength * Math.sin(rotation - (Math.PI * 3 / 4)));

		lines[0] = new Line2D.Double(position.getX(), position.getY(), headX, headY);
		lines[1] = new Line2D.Double(headX, headY, headTailX1, headTailY1);
		lines[2] = new Line2D.Double(headX, headY, headTailX2, headTailY2);
		
		return lines;
	}
	
}
