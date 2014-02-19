package uk.ac.cam.oda22.core.pathfinding.astar;

import java.awt.geom.Point2D;

import uk.ac.cam.oda22.core.tethers.TetherConfiguration;

/**
 * @author Oliver
 * 
 */
public class NextTetherConfigurationRotationResult extends
		NextTetherConfigurationResult {

	/**
	 * The rotation made from the previous configuration.
	 */
	public final double rads;

	public NextTetherConfigurationRotationResult(TetherConfiguration tc,
			Point2D lastWrappedPoint, Point2D lastUnwrappedPoint, double rads) {
		super(tc, lastWrappedPoint, lastUnwrappedPoint);

		this.rads = rads;
	}

}
