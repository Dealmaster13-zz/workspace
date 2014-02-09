package uk.ac.cam.oda22.coverage;

import java.awt.geom.Point2D;
import java.util.Stack;

import uk.ac.cam.oda22.core.environment.Obstacle;
import uk.ac.cam.oda22.core.environment.Room;
import uk.ac.cam.oda22.core.environment.VisibilityGraph;
import uk.ac.cam.oda22.core.logging.Log;
import uk.ac.cam.oda22.core.pathfinding.astar.TetheredAStarPathfinding;
import uk.ac.cam.oda22.core.pathfinding.astar.TetheredAStarShortestPathResult;
import uk.ac.cam.oda22.core.pathfinding.astar.TetheredAStarSinglePathResult;
import uk.ac.cam.oda22.core.robots.Robot;
import uk.ac.cam.oda22.core.tethers.TetherConfiguration;
import uk.ac.cam.oda22.pathplanning.TetheredPath;

/**
 * @author Oliver
 * 
 */
public final class Coverage {

	public static CoverageResult performCoverage(Room room, Robot robot) {
		/**
		 * TODO: Perform input sanitisation. Note that the robot must have a
		 * positive (non-zero) area for coverage to be possible.
		 */

		if (robot.radius <= 0) {
			Log.error("Robot must have a positive area to perform coverage.");

			return null;
		}

		/**
		 * TODO: Compute the shortest path from the anchor point to every point
		 * in the room, thus providing a discrete notion of 'saddle lines'.
		 */

		// Initialise a split point stack.
		Stack<Point2D> splitPoints = new Stack<Point2D>();

		// Initialise corridor progress.
		CorridorProgress corridorProgress = new CorridorProgress(0);

		/**
		 * TODO: Sweep around the anchor point until an obstacle is hit which
		 * splits the potential in two directions along the obstacle. Call this
		 * the primary split point and add it to S.
		 */

		/**
		 * TODO: Explore the right-hand corridor. Move sufficiently far along
		 * the obstacle edge such that the robot is at the edge of the covered
		 * area. Note that this new position should be the appropriate position
		 * for the current potential level.
		 */

		return null;
	}

	/**
	 * Computes the shortest path from the anchor point to every other point in
	 * the room.
	 * 
	 * @param o
	 * @param room
	 * @return saddle line
	 */
	private static ShortestPathGrid computeShortestPaths(Robot robot, Room room) {
		Point2D anchorPoint = robot.tether.getAnchor();

		double gridSeparation = robot.radius * 2;

		int horizontalCount = (int) Math.ceil((room.width - gridSeparation / 2)
				/ gridSeparation);
		int verticalCount = (int) Math.ceil((room.height - gridSeparation / 2)
				/ gridSeparation);

		TetheredPath[][] shortestPaths = new TetheredPath[verticalCount][horizontalCount];

		VisibilityGraph g = new VisibilityGraph(room.obstacles);

		TetherConfiguration initialTC = new TetherConfiguration(anchorPoint);

		for (int i = 0; i < verticalCount; i++) {
			for (int j = 0; j < horizontalCount; j++) {
				Point2D destination = new Point2D.Double((j * gridSeparation)
						+ (gridSeparation / 2), (i * gridSeparation)
						+ (gridSeparation / 2));

				TetheredAStarShortestPathResult shortestPathResult = TetheredAStarPathfinding
						.getShortestPaths(anchorPoint, destination, g,
								initialTC, robot.tether.length, robot.radius);

				// Set the shortest path.
				if (shortestPathResult != null
						&& shortestPathResult.shortestPathResults.size() > 0) {
					// Get the first path result.
					TetheredAStarSinglePathResult path = shortestPathResult.shortestPathResults
							.get(0);

					shortestPaths[i][j] = new TetheredPath(path.path,
							path.tetherConfiguration);
				} else {
					shortestPaths[i][j] = null;
				}
			}
		}

		return new ShortestPathGrid(shortestPaths);
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
