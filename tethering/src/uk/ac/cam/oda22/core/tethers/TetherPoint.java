package uk.ac.cam.oda22.core.tethers;

import java.awt.geom.Point2D;

/**
 * @author Oliver
 *
 */
public class TetherPoint {

	/**
	 * The point on the tether.
	 */
	public final Point2D x;
	
	/**
	 * The distance from the anchor point.
	 */
	public final double w;
	
	public TetherPoint(Point2D x, double w) {
		this.x = x;
		this.w = w;
	}
	
}
