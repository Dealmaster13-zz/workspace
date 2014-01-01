package uk.ac.cam.oda22.graphics.shapes;

import java.awt.Color;

/**
 * @author Oliver
 *
 */
public abstract class DisplayShape {

	public final Color colour;

	public final float thickness;
	
	public DisplayShape(Color colour, float thickness) {
		this.colour = colour;
		this.thickness = thickness;
	}
	
	public DisplayShape(float thickness) {
		this.colour = Color.black;
		this.thickness = thickness;
	}
	
	public abstract DisplayShape translate(double x, double y);
	
	public abstract DisplayShape stretch(double xScale, double yScale);
	
	public abstract DisplayShape flipY();

}
