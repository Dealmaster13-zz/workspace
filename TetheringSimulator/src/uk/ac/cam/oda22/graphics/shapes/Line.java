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

	@Override
	public DisplayShape translate(double x, double y) {
		return new Line(ShapeFunctions.translateShape(this.l, x, y), this.colour, this.thickness);
	}

	@Override
	public DisplayShape stretch(double xScale, double yScale) {
		return new Line(ShapeFunctions.stretchShape(this.l, xScale, yScale), this.colour, this.thickness);
	}
	
}
