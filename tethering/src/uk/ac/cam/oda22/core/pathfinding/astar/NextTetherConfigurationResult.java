package uk.ac.cam.oda22.core.pathfinding.astar;

import java.awt.geom.Point2D;

import uk.ac.cam.oda22.core.tethers.TetherConfiguration;

/**
 * @author Oliver
 * 
 */
public class NextTetherConfigurationResult {

	public final TetherConfiguration tc;

	public final Point2D lastWrappedPoint;

	public final Point2D lastUnwrappedPoint;

	/**
	 * @param tc
	 * @param lastWrappedPoint
	 * @param lastUnwrappedPoint
	 */
	public NextTetherConfigurationResult(TetherConfiguration tc,
			Point2D lastWrappedPoint, Point2D lastUnwrappedPoint) {
		this.tc = tc;
		this.lastWrappedPoint = lastWrappedPoint;
		this.lastUnwrappedPoint = lastUnwrappedPoint;
	}

}
