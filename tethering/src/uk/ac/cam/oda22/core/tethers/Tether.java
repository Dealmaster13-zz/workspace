package uk.ac.cam.oda22.core.tethers;

import java.awt.geom.Point2D;

/**
 * @author Oliver
 *
 */
public abstract class Tether {
	
	/**
	 * The position of the anchor point.
	 */
	protected Point2D anchor;
	
	/**
	 * The maximum length of the tether.
	 */
	public final double length;
	
	public Tether(Point2D anchor, double length) {
		this.anchor = anchor;
		this.length = length;
	}
	
	public Point2D getAnchor() {
		return this.anchor;
	}
	
	public abstract double getUsedLength();
	
	public abstract Point2D getPositionByDistance(double w);
	
	public abstract ITetherSegment getTetherSegment(double startW, double endW);

}
