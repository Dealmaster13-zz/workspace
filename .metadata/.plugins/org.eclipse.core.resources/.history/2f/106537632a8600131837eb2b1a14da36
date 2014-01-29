package uk.ac.cam.oda22.core.tethers;

import java.awt.geom.Point2D;

import uk.ac.cam.oda22.core.MathExtended;

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
	
	protected TetherConfiguration configuration;

	public Tether(Point2D anchor, double length, TetherConfiguration configuration) throws Exception {
		this.anchor = anchor;
		this.length = length;
		this.configuration = configuration;

		double usedLength = getUsedLength();

		// Check that the tether does not exceed its length restriction.
		if (usedLength > length && !MathExtended.approxEqual(length, usedLength, 0.001, 0)) {
			throw new Exception("Used length (" + usedLength + ") exceeds maximum length (" + length + ").");
		}
	}
	
	public Point2D getAnchor() {
		return this.anchor;
	}
	
	public TetherConfiguration getConfiguration() {
		return this.configuration;
	}
	
	public abstract double getUsedLength();
	
	public abstract Point2D getPositionByDistance(double w);
	
	public abstract Point2D getLastPoint();
	
	public abstract ITetherSegment getTetherSegment(double startW, double endW);

}
