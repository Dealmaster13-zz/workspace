package uk.ac.cam.oda22.core.tethers;

import java.awt.geom.Point2D;
import java.util.List;

import uk.ac.cam.oda22.core.ListFunctions;
import uk.ac.cam.oda22.core.MathExtended;
import uk.ac.cam.oda22.core.logging.Log;
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
	 * Changes the last point on the tether, and returns whether or not the last
	 * point had to be removed. This is useful for movement.
	 * 
	 * @param newPoint
	 * @return true if last point was removed, false otherwise
	 */
	public boolean moveLastPoint(Point2D newPoint) {
		// Get the tether configuration which is all but the last segment.
		boolean removedPoint = this.removeLastPoint();

		// Get the last point.
		Point2D lastPoint = ListFunctions.getLast(this.points);

		// If the last point is different to the point being moved to then add
		// the new point.
		if (!lastPoint.equals(newPoint)) {
			// Add the new final segment.
			this.addPoint(newPoint);
		}

		return removedPoint;
	}

	/**
	 * Returns a cropped tether which does not overlap with the robot near its
	 * attachment point. This should work for all attachment points. Note that
	 * this will not ensure that the tether is connected to the robot.
	 * 
	 * @param robotPosition
	 * @param radius
	 * @return cropped tether
	 */
	public TetherConfiguration getCroppedTetherToPreventRobotOverlap(
			Point2D robotPosition, double radius) {
		TetherConfiguration newTC = new TetherConfiguration(this);

		int s = this.points.size();

		int index = s - 1;

		boolean crossPointFound = false;

		// Find the point at which the tether exits the robot area.
		while (index >= 0 && !crossPointFound) {
			Point2D p = this.points.get(index);

			// If the tether is outside the robot area or on the circumference,
			// then stop.
			if (robotPosition.distance(p) >= radius) {
				crossPointFound = true;
			} else {
				index--;
			}
		}

		// Remove every tether segment which is outside of the robot area.
		for (int i = s - 1; i > index; i--) {
			newTC.removeLastPoint();
		}

		return newTC;
	}

	/**
	 * Checks if the tether has a valid configuration.
	 * 
	 * @return true if the configuration is valid, false otherwise
	 */
	public boolean assertConfigurationValid() {
		return this.assertConfigurationValid(0, 0);
	}

	/**
	 * Checks if the tether has a valid configuration.
	 * 
	 * @return true if the configuration is valid, false otherwise
	 */
	public boolean assertConfigurationValid(double fractionalError,
			double absoluteError) {
		int s = this.points.size();

		if (s == 0) {
			Log.error("Tether configuration is empty.");

			return false;
		}

		if (s == 1) {
			return true;
		}

		for (int i = 0; i < s - 1; i++) {
			Point2D currentPoint = this.points.get(i);
			Point2D nextPoint = this.points.get(i + 1);

			if (MathExtended.approxEqual(currentPoint, nextPoint,
					fractionalError, absoluteError)) {
				Log.warning("Two adjacent points are roughly equal.");
			}
		}

		return true;
	}
}
