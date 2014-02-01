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
	 * Changes the last point on the tether, and returns whether or not the last
	 * point had to be removed. This is useful for movement.
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

	/**
	 * Returns a cropped tether which does not overlap with the robot near its
	 * attachment point. This should work for all attachment points.
	 * Note that this will not ensure that the tether is connected to the robot.
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
}