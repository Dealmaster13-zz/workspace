package uk.ac.cam.oda22.coverage.sweeping;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.Stack;

import uk.ac.cam.oda22.core.MathExtended;
import uk.ac.cam.oda22.core.Vector2D;
import uk.ac.cam.oda22.core.environment.Obstacle;
import uk.ac.cam.oda22.core.environment.PolygonRoom;
import uk.ac.cam.oda22.core.environment.Room;
import uk.ac.cam.oda22.core.logging.Log;
import uk.ac.cam.oda22.core.robots.Robot;
import uk.ac.cam.oda22.core.tethers.TetherConfiguration;
import uk.ac.cam.oda22.coverage.CorridorProgress;
import uk.ac.cam.oda22.coverage.Coverage;
import uk.ac.cam.oda22.coverage.CoverageResult;
import uk.ac.cam.oda22.coverage.ShortestPathGrid;
import uk.ac.cam.oda22.pathplanning.Path;

/**
 * @author Oliver
 * 
 */
public class SweepingCoverage extends Coverage {

	@Override
	public CoverageResult performCoverage(Room room, Robot robot,
			boolean returnToInitialCell) {
		if (!(room instanceof PolygonRoom)) {
			Log.error("Coverage unimplemented for non-polygonal rooms");

			return null;
		}

		PolygonRoom pRoom = (PolygonRoom) room;

		/*
		 * TODO: Perform input sanitisation.
		 */

		// Check that the robot is not a point robot (otherwise coverage is not
		// possible).
		if (robot.radius <= 0) {
			Log.error("Robot must have a positive area to perform coverage.");

			return null;
		}

		/*
		 * Step 1: Compute the shortest path from the anchor point to every
		 * point (cell) in the room, thus providing a discrete notion of 'saddle
		 * lines'.
		 */

		ShortestPathGrid shortestPathGrid = computeShortestPaths(pRoom, robot,
				robot.radius);

		// Initialise a split point stack.
		Stack<Point2D> splitPoints = new Stack<Point2D>();

		// Initialise corridor progress.
		// Note that we initially search rightwards.
		CorridorProgress corridorProgress = new CorridorProgress(0, true);

		/*
		 * TODO: Step 2: Explore the current corridor.
		 */

		/*
		 * TODO: Step 3: Return to the last split point for which a corridor is
		 * unexplored, and then go to step 2. If no such split point exists then
		 * we are finished.
		 */

		return null;
	}

	private static ISweepSegment exploreCorridor(TetherConfiguration initialTC,
			CorridorProgress corridorProgress,
			List<Obstacle> expandedObstacles, double robotRadius) {
		TetherConfiguration currentTC = new TetherConfiguration(initialTC);

		/*
		 * TODO: Step 2a: Move to the closest uncovered area at the next
		 * potential level, in a given preferred direction (determined by
		 * whether we are in a left or right corridor), along the obstacle edge
		 * or saddle line. Note that we should be touching an obstacle edge or
		 * saddle line.
		 */

		/*
		 * TODO: Extension task: allow for anchor points which are not fixed to
		 * the room boundary. Note that if the anchor point is attached to the
		 * room boundary, then a sweep will necessarily cause the robot to hit
		 * an obstacle.
		 */

		/*
		 * Step 2b: Check when the robot will first hit an obstacle via sweeping
		 * at its current radius, if one exists.
		 */

		int tcSize = currentTC.points.size();

		Point2D pivotPoint = currentTC.points.get(tcSize - 2);
		Point2D robotPoint = currentTC.points.get(tcSize - 1);

		// TODO: Compute.
		boolean clockwise = true;

		// Store the maximum radial movement without hitting an obstacle, as
		// well as the obstacle hit. Note that we must necessarily hit an
		// obstacle given an anchor point which is fixed to an obstacle.
		SweepToObstacleResult sweepToObstacleResult = getFirstObstacleInSweep(
				expandedObstacles, pivotPoint, robotPoint, clockwise,
				robotRadius);

		// Fail if the robot will not hit an obstacle in a sweep.
		if (sweepToObstacleResult == null
				|| sweepToObstacleResult.nearestObstacle == null) {
			Log.error("Robot will not hit obstacle in sweep.");

			return null;
		}

		// Store the maximum radial movement without crossing a saddle line.
		double maxRadsToSaddleLine = 0;

		/*
		 * TODO: Step 2c: Check when the robot will first hit a saddle line via
		 * sweeping at its current radius, if one exists, before hitting the
		 * first obstacle.
		 */

		/*
		 * TODO: Step 2d: Perform a sweep until the next saddle line or
		 * obstacle.
		 */

		/*
		 * TODO: Step 2e: If the robot has reached an obstacle, check if it
		 * would introduce a split point, and if so then add it to the stack and
		 * create the appropriate corridors.
		 */

		return null;
	}

	/**
	 * Computes the point at which the robot first hits an obstacle in a sweep.
	 * Note that we use expanded obstacles to simplify calculations by simply
	 * performing circle-obstacle intersection.
	 * 
	 * @param expandedObstacles
	 * @param pivotPoint
	 * @param sweepRadius
	 * @param clockwise
	 * @param robotRadius
	 * @return first obstacle in sweep
	 */
	private static SweepToObstacleResult getFirstObstacleInSweep(
			List<Obstacle> expandedObstacles, Point2D pivotPoint,
			Point2D robotPoint, boolean clockwise, double robotRadius) {
		// Store the maximum rotation before hitting an obstacle, as well as the
		// obstacle itself. Note that we start at positive infinity as we work
		// down to find the smallest rotation for all obstacles.
		double maxRadsToObstacle = Double.POSITIVE_INFINITY;
		Obstacle nearestObstacle = null;

		// Get the sweep radius.
		double sweepRadius = pivotPoint.distance(robotPoint);

		// Get the vector from the pivot point to the robot point.
		Vector2D v1 = new Vector2D(pivotPoint, robotPoint);

		for (Obstacle o : expandedObstacles) {
			// Get the perimeter of the obstacle as a path.
			Path perimeter = o.getPerimeter();

			// Get the intersection points between the sweep circle and the
			// perimeter.
			List<Point2D> intersectionPoints = MathExtended
					.strictGetCirclePathIntersectionPoints(pivotPoint,
							sweepRadius, perimeter);

			// For each intersection point, check if it is the closest.
			for (Point2D point : intersectionPoints) {
				// Get the vector from the pivot point to the intersection
				// point.
				Vector2D v2 = new Vector2D(pivotPoint, point);

				// Assert that v2 has the correct length.
				if (!MathExtended.approxEqual(v2.getLength(), sweepRadius,
						0.0001, 0.0001)) {
					Log.error("Incorrect intersection point.");
				} else {
					// Get the angle from v1 to v2, rotating in a particular
					// direction.
					double angularRotation = MathExtended.getAngularChange(v1,
							v2, clockwise);

					if (angularRotation < maxRadsToObstacle) {
						maxRadsToObstacle = angularRotation;
						nearestObstacle = o;
					}
				}
			}
		}

		return new SweepToObstacleResult(maxRadsToObstacle, nearestObstacle);
	}

	/**
	 * Computes the point at which the robot first hits a saddle line in a
	 * sweep. Note that we use expanded obstacles to simplify calculations by
	 * simply performing circle-obstacle intersection.
	 * 
	 * @param obstacles
	 * @param pivotPoint
	 * @param sweepRadius
	 * @param clockwise
	 * @param robotRadius
	 * @return first obstacle in sweep
	 */
	/*
	 * private static SweepToSaddleLineResult getFirstSaddleLineInSweep(
	 * List<Obstacle> obstacles, TetherConfiguration tc, boolean clockwise,
	 * double robotRadius, double maxRads) { // Store the maximum rotation
	 * before hitting an obstacle, as well as the // obstacle itself. Note that
	 * we start at positive infinity as we work // down to find the smallest
	 * rotation for all obstacles. double maxRadsToObstacle =
	 * Double.POSITIVE_INFINITY; Obstacle nearestObstacle = null;
	 * 
	 * // Get the sweep radius. double sweepRadius =
	 * pivotPoint.distance(robotPoint);
	 * 
	 * // Get the vector from the pivot point to the robot point. Vector2D v1 =
	 * new Vector2D(pivotPoint, robotPoint);
	 * 
	 * for (Obstacle o : obstacles) { // Get the perimeter of the obstacle as a
	 * path. Path perimeter = o.getPerimeter();
	 * 
	 * // Get the intersection points between the sweep circle and the //
	 * perimeter. List<Point2D> intersectionPoints = MathExtended
	 * .strictGetCirclePathIntersectionPoints(pivotPoint, sweepRadius,
	 * perimeter);
	 * 
	 * // For each intersection point, check if it is the closest. for (Point2D
	 * point : intersectionPoints) { // Get the vector from the pivot point to
	 * the intersection // point. Vector2D v2 = new Vector2D(pivotPoint, point);
	 * 
	 * // Assert that v2 has the correct length. if
	 * (!MathExtended.approxEqual(v2.getLength(), sweepRadius, 0.0001, 0.0001))
	 * { Log.error("Incorrect intersection point."); } else { // Get the angle
	 * from v1 to v2, rotating in a particular // direction. double
	 * angularRotation = MathExtended.getAngularChange(v1, v2, clockwise);
	 * 
	 * if (angularRotation < maxRadsToObstacle) { maxRadsToObstacle =
	 * angularRotation; nearestObstacle = o; } } } }
	 * 
	 * return new SweepToObstacleResult(maxRadsToObstacle, nearestObstacle); }
	 */

	private static ISweepSegment performSweep(Point2D position, double rads,
			CorridorProgress corridorProgress) {
		/*
		 * TODO: Step 2c: Perform a sweep of given rotation.
		 */

		return null;
	}

	/**
	 * Returns whether or not a sweep segment intersects a saddle line.
	 * 
	 * @param sweepSegment
	 * @param saddleLine
	 * @return true if intersection, and false otherwise.
	 */
	private static boolean sweepSegmentIntersectsSaddleLine(
			ISweepSegment sweepSegment, SaddleLine saddleLine) {
		// TODO: Implement.
		return false;
	}

}
