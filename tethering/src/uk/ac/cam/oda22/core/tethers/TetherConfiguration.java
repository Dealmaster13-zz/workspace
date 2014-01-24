package uk.ac.cam.oda22.core.tethers;

import java.awt.geom.Point2D;
import java.util.List;

import uk.ac.cam.oda22.pathplanning.Path;

/**
 * @author Oliver
 *
 */
public class TetherConfiguration extends Path {

	public TetherConfiguration() {
		super();
	}

	public TetherConfiguration(Point2D p) {
		super(p);
	}

	public TetherConfiguration(List<Point2D> path) {
		super(path);
	}

	public TetherConfiguration(TetherConfiguration tc) {
		super();

		this.addPoints(tc.points);
	}

	/**
	 * Changes the last point on the tether, and returns whether or not the last point had to be removed.
	 * This is useful for movement.
	 * 
	 * @param newPoint
	 * @return true if last point was removed, false otherwise
	 */
	public boolean moveLastPoint(Point2D newPoint) {
		// Get the tether configuration which is all but the last segment.
		boolean removedPoint = this.removeLastPoint();

		// Add the new final segment.
		this.addPoint(newPoint);

		return removedPoint;
	}

}
