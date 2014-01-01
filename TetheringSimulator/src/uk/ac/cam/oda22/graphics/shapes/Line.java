package uk.ac.cam.oda22.graphics.shapes;

import java.awt.Color;
import java.awt.geom.Line2D;

import uk.ac.cam.oda22.core.ShapeFunctions;

/**
 * @author Oliver
 *
 */
public class Line extends DisplayShape {

	public final Line2D l;

	/**
	 * @param l
	 * @param colour
	 * @param thickness
	 */
	public Line(Line2D l, Color colour, float thickness) {
		super(colour, thickness);
		
		this.l = l;
	}

	/**
	 * @param l
	 * @param thickness
	 */
	public Line(Line2D l, float thickness) {
		super(thickness);
		
		this.l = l;
	}

	@Override
	public DisplayShape translate(double x, double y) {
		return new Line(ShapeFunctions.translateShape(this.l, x, y), this.colour, this.thickness);
	}

	@Override
	public DisplayShape stretch(double xScale, double yScale) {
		return new Line(ShapeFunctions.stretchShape(this.l, xScale, yScale), this.colour, this.thickness);
	}

	@Override
	public DisplayShape flipY() {
		Line2D newLine = new Line2D.Double(this.l.getX1(), -this.l.getY1(), this.l.getX2(), -this.l.getY2());
		
		return new Line(newLine, this.colour, this.thickness);
	}
	
}
