package uk.ac.cam.oda22.core.pathfinding.astar;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import uk.ac.cam.oda22.core.ListFunctions;
import uk.ac.cam.oda22.core.MathExtended;
import uk.ac.cam.oda22.core.PointInTriangleResult;
import uk.ac.cam.oda22.core.Triangle2D;
import uk.ac.cam.oda22.core.Vector2D;
import uk.ac.cam.oda22.core.environment.Obstacle;
import uk.ac.cam.oda22.core.logging.Log;
import uk.ac.cam.oda22.core.tethers.TetherConfiguration;
import uk.ac.cam.oda22.pathplanning.Path;

/**
 * @author Oliver
 * 
 */
public class TetheredAStarPathfinding {

	/**
	 * Performs A* pathfinding from the source to the destination, and returns
	 * whether or not a path was found. The tether needs to be simulated as
	 * permanently taut, and must not exceed it's maximum length. If the tether
	 * configuration is undefined then standard tether-less pathfinding will be
	 * used. It is assumed that backtracking along the tether is unnecessary
	 * (which is the case if performing path planning).
	 * 
	 * @param g
	 * @param source
	 * @param destination
	 * @param initialTetherConfiguration
	 * @param maxTetherLength
	 * @param robotRadius
	 * @return shortest valid path
	 */
	public static boolean getShortestPath(AStarGraph g, AStarNode source,
			AStarNode destination,
			TetherConfiguration initialTetherConfiguration,
			double maxTetherLength, double robotRadius) {
		Point2D lastTetherPoint = ListFunctions
				.getLast(initialTetherConfiguration.points);

		// Fail if the source does not match the end of the tether
		// configuration.
		if (!MathExtended.approxEqual(source.p, lastTetherPoint, 0.00001,
				0.00001)) {
			Log.error("Mismatch between source and tether.");

			return false;
		}

		// Initialise the nodes.
		for (AStarNode node : g.nodes) {
			node.subPaths = new ArrayList<AStarSubPath>();
			node.discovered = false;
			node.g = Double.POSITIVE_INFINITY;
			node.h = node.distance(destination.p);
		}

		// Set the source's g cost to 0, and its only tether configuration to be
		// the initial configuration.
		source.g = 0;
		List<AStarNode> initialPredecessors = new ArrayList<AStarNode>();
		AStarSubPath initialSubPath = new AStarSubPath(initialPredecessors,
				initialTetherConfiguration);
		source.subPaths.add(initialSubPath);

		// Create a queue of the nodes to check neighbours.
		List<AStarNode> q = new LinkedList<AStarNode>();
		q.add(source);

		boolean optimalPathFound = false;

		while (!q.isEmpty() && !optimalPathFound) {
			// Get the lowest cost node and remove it from the queue.
			// Note that we are not using a primitive queue.
			AStarNode current = getLowestCostNode(q);

			// Return no path if there are no more nodes to check.
			if (current == null) {
				return false;
			}

			q.remove(current);

			// Stop if a better path cannot be found.
			// Note that we use a strict inequality to allow the possibility of
			// multiple optimal paths.
			if (current.g + current.h > destination.g) {
				optimalPathFound = true;
			} else {
				// For each undiscovered neighbour of the current edge, try to
				// relax their edge.
				for (AStarEdge edge : current.edges) {
					AStarNode neighbour = (current == edge.p) ? edge.q : edge.p;

					// Relax the edge.
					// Note that we use a non-strict inequality to allow the
					// possibility of multiple equal predecessors.
					if (current.g + edge.cost <= neighbour.g) {
						// For each possible sub-path, compute the changes in
						// the tether configurations.
						for (int i = 0; i < current.subPaths.size(); i++) {
							AStarSubPath subPath = current.subPaths.get(i);

							TetherConfiguration newTC = computeTetherChange(
									subPath.tc, maxTetherLength, neighbour.p,
									g.obstacles, robotRadius);

							// Create a new shortest path if the tether
							// configuration is valid.
							// Note that tether length checks have already been
							// made.
							if (newTC != null) {
								List<AStarNode> newPredecessorList = new ArrayList<AStarNode>();
								newPredecessorList
										.addAll(subPath.predecessorList);
								newPredecessorList.add(current);

								AStarSubPath newSubPath = new AStarSubPath(
										newPredecessorList, newTC);

								neighbour.subPaths.add(newSubPath);

								neighbour.g = current.g + edge.cost;

								// Add the neighbour to the queue if it hasn't
								// been
								// discovered yet.
								if (!neighbour.discovered) {
									q.add(neighbour);

									neighbour.discovered = true;
								}
							}
						}
					}
				}
			}
		}

		return destination.subPaths.size() != 0;
	}

	public static AStarNode getLowestCostNode(List<AStarNode> nodes) {
		AStarNode lowestCostNode = null;
		double lowestF = Double.POSITIVE_INFINITY;

		for (AStarNode node : nodes) {
			if (node.g + node.h < lowestF) {
				lowestCostNode = node;

				lowestF = node.g + node.h;
			}
		}

		return lowestCostNode;
	}

	/**
	 * Retrieves all of the shortest paths to the destination.
	 * 
	 * @param destination
	 * @return shortest paths
	 */
	public static TetheredAStarShortestPathResult retrieveShortestPathResult(
			AStarNode destination) {
		TetheredAStarShortestPathResult result = new TetheredAStarShortestPathResult();

		int s = destination.subPaths.size();

		// Fail if no sub-path exists.
		if (s == 0) {
			Log.error("No sub-path exists.");

			return null;
		}

		for (int i = 0; i < s; i++) {
			Path path = new Path();

			// Get the predecessor list and final tether configuration.
			List<AStarNode> predecessorList = destination.subPaths.get(i).predecessorList;
			TetherConfiguration tc = destination.subPaths.get(i).tc;

			// Reassemble the forward path.
			for (int j = predecessorList.size() - 1; j >= 0; j--) {
				path.addPoint(predecessorList.get(j).p);
			}

			// Add the last point.
			path.addPoint(destination.p);

			// Add the shortest path result.
			TetheredAStarSinglePathResult shortestPathResult = new TetheredAStarSinglePathResult(
					path, tc);
			result.addShortestPath(shortestPathResult);
		}

		return result;
	}

	/**
	 * Computes the change in a tether's configuration as it's end point moves
	 * directly to a destination point. Returns null if the tether length has
	 * been exceeded.
	 * 
	 * @param tc
	 * @param maxTetherLength
	 * @param destination
	 * @param obstacles
	 * @param robotRadius
	 * @return new tether configuration if within length restrictions, null
	 *         otherwise
	 */
	public static TetherConfiguration computeTetherChange(
			TetherConfiguration tc, double maxTetherLength,
			Point2D destination, List<Obstacle> obstacles, double robotRadius) {
		double fractionalError = 0.00001;
		double absoluteError = 0.00001;

		// Fail if we have already exceeded the maximum tether length.
		if (tc.lengthExceeded(maxTetherLength, fractionalError, absoluteError)) {
			Log.warning("Tether length already exceeded before moving.");

			return null;
		}

		int tcSize = tc.points.size();

		// Stop if we are retracting down the tether.
		// Tether retraction should be carried out by a separate process.
		if (tcSize >= 2
				&& MathExtended.approxEqual(tc.points.get(tcSize - 2),
						destination, fractionalError, absoluteError)) {
			return null;
		}

		TetherConfiguration currentTC = new TetherConfiguration(tc);
		Point2D currentPoint = ListFunctions.getLast(currentTC.points);

		List<TetherConfiguration> tcHistory = new ArrayList<TetherConfiguration>();
		tcHistory.add(currentTC);

		Point2D lastWrappedPoint = null;
		Point2D lastUnwrappedPoint = null;

		// Iteratively move towards the destination, altering the tether
		// configuration appropriately on each iteration.
		while (!MathExtended.approxEqual(currentPoint, destination,
				fractionalError, absoluteError)) {
			// Get the next tether configuration by moving towards the
			// destination.
			NextTetherConfigurationResult nextTCResult = getNextTetherConfiguration(
					currentTC, destination, obstacles, lastWrappedPoint,
					lastUnwrappedPoint);

			if (nextTCResult == null) {
				return null;
			} else {
				lastWrappedPoint = nextTCResult.lastWrappedPoint;
				lastUnwrappedPoint = nextTCResult.lastUnwrappedPoint;
			}

			TetherConfiguration nextTC = nextTCResult.tc;

			int nextTCSize = nextTC.points.size();

			boolean lastPointsEqual = nextTCSize >= 2
					&& MathExtended
							.approxEqual(nextTC.points.get(nextTCSize - 1),
									nextTC.points.get(nextTCSize - 2), 0.00001,
									0.00001);

			// Fail if the tether configuration is malformed.
			if (nextTCSize == 0 || lastPointsEqual) {
				Log.error("Last two tether points coincide.");

				return null;
			}

			// Fail if the tether configuration did not change.
			// Note that it is fine if the robot did not move, so long as
			// wrapping or unwrapping of the tether occurred.
			if (currentTC.equals(nextTC)) {
				Log.error("Tether configuration stagnant.");

				return null;
			}

			// Fail if the tether configuration has been tried before.
			if (hasStateBeenRevisited(nextTC, tcHistory)) {
				Log.error("Tether configuration has been tried already.");

				return null;
			}

			// Get the new robot position.
			Point2D nextPoint = ListFunctions.getLast(nextTC.points);

			// Fail if the next point is undefined.
			if (nextPoint == null) {
				Log.error("Next point is undefined.");

				return null;
			}

			// Stop if the tether was crossed by the robot.
			if (tetherCrossed(currentPoint, nextPoint, currentTC, robotRadius)) {
				return null;
			}

			// Stop if the tether length has been exceeded.
			if (nextTC.lengthExceeded(maxTetherLength, 0.00001, 0.00001)) {
				return null;
			}

			currentTC = nextTC;
			currentPoint = nextPoint;
			tcHistory.add(currentTC);
		}

		return currentTC;
	}

	/**
	 * Computes the tether configuration which is taut and is homotopy
	 * equivalent to tc.
	 * 
	 * @param tc
	 * @param obstacles
	 * @param robotRadius
	 * @return taut tether configuration
	 */
	public static TetherConfiguration getTautTetherConfiguration(
			TetherConfiguration tc, List<Obstacle> obstacles, double robotRadius) {
		int s = tc.points.size();

		// Check if the tether configuration has no length.
		if (s <= 1) {
			return new TetherConfiguration(tc);
		}

		Point2D anchorPoint = tc.points.get(0);

		// Initialise a tether configuration with just the anchor point.
		TetherConfiguration tcResult = new TetherConfiguration(anchorPoint);

		// For each tether segment, perform retraction by 'moving' to the next
		// point.
		for (int i = 1; i < s; i++) {
			Point2D nextPoint = tc.points.get(i);

			tcResult = computeTetherChange(tcResult, Double.POSITIVE_INFINITY,
					nextPoint, obstacles, robotRadius);

			if (tcResult == null) {
				return null;
			}
		}

		return tcResult;
	}

	private static boolean tetherCrossed(Point2D currentPoint,
			Point2D nextPoint, TetherConfiguration tc, double robotRadius) {
		// Stop if the robot did not move.
		if (MathExtended.approxEqual(currentPoint, nextPoint, 0.00001, 0.00001)) {
			return false;
		}

		// Stop if the tether has no edges.
		if (tc.points.size() <= 1) {
			return false;
		}

		// Get the unchanged tether configuration.
		// This is the entire original tether except for the segment
		// connected to the robot.
		Path unchangedPath = tc.getSubpath(0, tc.points.size() - 2);
		TetherConfiguration unchangedTC = new TetherConfiguration(
				unchangedPath.points);

		// Crop the tether such that the robot doesn't overlap tether directly
		// connected to the robot.
		// For well-formed tethers this should not do anything.
		TetherConfiguration croppedUnchangedTC = unchangedTC
				.getCroppedTetherToPreventRobotOverlap(currentPoint,
						robotRadius);

		// If the robot is a point-robot then just check for point
		// intersections.
		if (robotRadius == 0) {
			// Get the robot path.
			Line2D robotPath = new Line2D.Double(currentPoint.getX(),
					currentPoint.getY(), nextPoint.getX(), nextPoint.getY());

			return MathExtended.strictLineIntersectsPath(robotPath,
					croppedUnchangedTC);
		}

		// Check if the robot intersects the tether at the current point.
		if (MathExtended.strictCircleIntersectsPath(currentPoint, robotRadius,
				croppedUnchangedTC)) {
			return true;
		}

		// Check if the robot intersects the tether at the next point.
		// Note that this will disallow tether retraction.
		if (MathExtended.strictCircleIntersectsPath(nextPoint, robotRadius,
				croppedUnchangedTC)) {
			return true;
		}

		// Get the positions of the robot sides at the current and next points.
		Vector2D radiusExtension = new Vector2D(currentPoint, nextPoint);
		radiusExtension.setLength(robotRadius);
		Vector2D leftRadiusExtension = radiusExtension.getTangentVector(true);
		Vector2D rightRadiusExtension = radiusExtension.getTangentVector(false);
		Point2D currentLeftPoint = leftRadiusExtension.addPoint(currentPoint);
		Point2D currentRightPoint = rightRadiusExtension.addPoint(currentPoint);
		Point2D nextLeftPoint = leftRadiusExtension.addPoint(nextPoint);
		Point2D nextRightPoint = rightRadiusExtension.addPoint(nextPoint);

		// Get the lines representing the path made by each side of the robot.
		Line2D leftPath = new Line2D.Double(currentLeftPoint.getX(),
				currentLeftPoint.getY(), nextLeftPoint.getX(),
				nextLeftPoint.getY());
		Line2D rightPath = new Line2D.Double(currentRightPoint.getX(),
				currentRightPoint.getY(), nextRightPoint.getX(),
				nextRightPoint.getY());

		// Check if either robot side crossed the unchanged portion of the
		// tether.
		return MathExtended.strictLineIntersectsPath(leftPath, unchangedPath)
				|| MathExtended.strictLineIntersectsPath(rightPath,
						unchangedPath);
	}

	/**
	 * Gets the point at which the tether would start unwrapping around an
	 * obstacle. Unwrapping would occur at point q, the penultimate tether
	 * vertex. Point r is the point on the tether that precedes q. Point p is
	 * the robot's position, and d is the destination. This computes the
	 * intersection between the directed infinite line starting at r going
	 * through q, and finite line pd.
	 * 
	 * @param r
	 * @param q
	 * @param rq
	 * @param p
	 * @param pd
	 * @return valid intersection point, null otherwise
	 */
	private static Point2D getUnwrapPoint(Point2D r, Point2D q, Vector2D rq,
			Point2D p, Vector2D pd) {
		// Get the extended intersection point between rq and pd.
		Point2D u = MathExtended.getExtendedIntersectionPoint(r, rq, p, pd);

		// Get the equivalent line for pd.
		Line2D pdLine = MathExtended.getLine(p, pd);

		// Stop if u is not on the finite line pd.
		if (u == null || !MathExtended.loosePointOnLine(u, pdLine)) {
			return null;
		}

		// Stop if u is closer to r than q.
		// If this is the case then travelling to d will cause the tether to
		// fully wrap around the obstacle with edge rq.
		// Note that this does not necessarily mean that the tether will cross.
		if (u.distance(r) < u.distance(q)) {
			return null;
		}

		return u;
	}

	/**
	 * Returns whether or not a tether segment qp can wrap around vertex v given
	 * the orientation of its two associated obstacle edges. This only checks
	 * angular values, and does not consider whether or not the tether is long
	 * enough. This does not consider the tether rotating more than 180 degrees.
	 * 
	 * @param q
	 * @param p
	 * @param qp
	 * @param qd
	 * @param v
	 * @param o
	 * @return true if the tether can wrap, false otherwise
	 */
	private static boolean isWrappingPossible(Point2D q, Point2D p,
			Vector2D qp, Vector2D qd, Point2D v, Obstacle o) {
		// Fail if the obstacle and/or its vertex is invalid.
		if (v == null || o.points.size() == 0) {
			Log.error("Invalid obstacle.");

			return false;
		}

		// If point v is the only point in the obstacle then the tether can wrap
		// around it.
		if (o.points.size() == 1) {
			return true;
		}

		// If q and v or p and v are the same point then the tether cannot wrap
		// point v.
		if (q.equals(v) || p.equals(v)) {
			return false;
		}

		// Get the direction in which the tether will rotate.
		boolean clockwise = isTetherMovementClockwise(qp, qd);

		// Get the previous and next vertices in the obstacle.
		Point2D u = o.getPreviousVertex(v);
		Point2D w = o.getNextVertex(v);

		// If the current robot position (p) is the same point as either
		// neighbouring obstacle vertex then the tether cannot wrap around point
		// v.
		if (p.equals(u) || p.equals(w)) {
			return false;
		}

		// Get the vectors from q to the obstacle vertices.
		Vector2D qv = new Vector2D(q, v);
		Vector2D qu = new Vector2D(q, u);
		Vector2D qw = new Vector2D(q, w);

		// Get the angular changes between qp and the three vectors from q to
		// each obstacle vertex.
		double aQPV = MathExtended.getAngularChange(qp, qv);
		double aQPU = MathExtended.getAngularChange(qp, qu);
		double aQPW = MathExtended.getAngularChange(qp, qw);

		// If any obstacle vertex is on the wrong side (opposite side to tether
		// rotation), then the tether cannot wrap around v.
		// This means that we only consider up to half a revolution of tether
		// rotation.
		if ((clockwise && (aQPV > 0 || aQPU > 0 || aQPW > 0))
				|| (!clockwise && (aQPV < 0 || aQPU < 0 || aQPW < 0))) {
			return false;
		}

		// If v is collinear with q and either neighbouring obstacle vertex, x,
		// then x must lie no farther away from q than v does.
		if ((aQPV == aQPU && u.distance(q) > v.distance(q))
				|| (aQPV == aQPW && w.distance(q) > v.distance(q))) {
			return false;
		}

		// Check if the angle from q to v is closer than that to u and w in
		// comparison to qp.
		// Note that this method is still valid for obstacles with two vertices
		// (i.e. vPrev and vNext are the same).
		if (clockwise) {
			// If either neighbouring obstacle vertex, x, and q are distinct
			// then the angle from q to v must be less negative than that to x.
			if ((!u.equals(q) && aQPV < aQPU) || (!w.equals(q) && aQPV < aQPW)) {
				return false;
			}
		} else {
			// If either neighbouring obstacle vertex, x, and q are distinct
			// then the angle from q to v must be less positive than that to x.
			if ((!u.equals(q) && aQPV > aQPU) || (!w.equals(q) && aQPV > aQPW)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Computes the point at which the tether wraps or unwraps around an
	 * obstacle, on its way to point d. A resultant tether configuration is
	 * returned, whose final point may have reached d.
	 * 
	 * @param tc
	 * @param d
	 * @param obstacles
	 * @return next tether configuration
	 */
	private static NextTetherConfigurationResult getNextTetherConfiguration(
			TetherConfiguration tc, Point2D d, List<Obstacle> obstacles,
			Point2D lastWrapPoint, Point2D lastUnwrapPoint) {
		// Assert that the tether configuration is valid.
		if (!tc.assertConfigurationValid()) {
			return null;
		}

		TetherConfiguration newTC = new TetherConfiguration(tc);

		// Fail if the destination is undefined.
		if (d == null) {
			Log.error("Destination is undefined.");

			return null;
		}

		int s = tc.points.size();

		if (s == 0) {
			/*
			 * Note that we should never enter this state if the check is
			 * handled correctly in the configuration validity check.
			 */

			// If the tether configuration is empty then fail.
			Log.error("Invalid tether configuration (empty).");

			return null;
		} else if (s == 1) {
			// We cannot wrap around an obstacle vertex if we start at the
			// anchor point.
			// Since we cannot wrap nor unwrap, move straight to d, and alter
			// the tether configuration accordingly.
			// Add d as the next point on the tether.
			newTC.addPoint(d);

			// Assert that the tether configuration is valid.
			if (!tc.assertConfigurationValid()) {
				return null;
			}

			NextTetherConfigurationResult result = new NextTetherConfigurationResult(
					newTC, null, null);

			return result;
		}

		/*
		 * If s equals 2 then we cannot unwrap since there is no tether (segment
		 * rq) to unwrap, and specifically no point r. As such we will set the
		 * appropriate variables to null, namely r, rq and u. Note that the code
		 * is structured such that if r is undefined then unwrapping will not be
		 * checked.
		 */

		// Get the last point p, penultimate point, q, and previous point, r, on
		// the tether.
		// q and r both define possible wrap and unwrap points.
		// Line q-r lie along an obstacle edge, while line p-q is necessarily in
		// open space for a non-point robot.
		// The last tether segment pivots around q.
		// Note that p is the robot's position and hence the point from which
		// movement starts.
		Point2D p = tc.points.get(s - 1);
		Point2D q = tc.points.get(s - 2);
		Point2D r = s >= 3 ? tc.points.get(s - 3) : null;

		// Get the vectors p -> d, q -> p, q -> d and r -> q.
		Vector2D pd = new Vector2D(p, d);
		Vector2D qp = new Vector2D(q, p);
		Vector2D qd = new Vector2D(q, d);
		Vector2D rq = r != null ? new Vector2D(r, q) : null;

		// Get the angle from the penultimate tether point to the current tether
		// point.
		double startAngle = qp.getAngle();

		// Starting at 'pivot point' q, get the change in angle going from p and
		// d.
		double angularChange = MathExtended.getAngularChange(startAngle,
				qd.getAngle());

		// If the angular change is zero then the tether will not wrap around
		// anything, nor will it unwrap.
		// This assumes that we only make legitimate movements, i.e. the robot's
		// path from p to d is clear of obstacles.
		if (angularChange == 0) {
			// Change the last point on the tether (from p) to d.
			newTC.moveLastPoint(d);

			// Assert that the tether configuration is valid.
			if (!tc.assertConfigurationValid()) {
				return null;
			}

			NextTetherConfigurationResult result = new NextTetherConfigurationResult(
					newTC, null, null);

			return result;
		}

		boolean doNotUnwrap = lastWrapPoint != null && lastWrapPoint.equals(q);

		// Get the point at which unwrapping would occur along the line pd.
		// This is the intersection point between (directed infinite line) rq
		// and (finite line) pd, if it exists.
		// It is not sufficient to simply compare angles of qp, rq and qd.
		// This point should still be defined if r, q and p are collinear.
		// We should not unwrap if we just wrapped around q.
		Point2D u = (r != null && !doNotUnwrap) ? getUnwrapPoint(r, q, rq, p,
				pd) : null;

		// Create a triangle qpu (if u exists) or qpd (if u does not exist).
		// If an obstacle point lies within this triangle then the tether will
		// wrap around the one closest in angle to qp.
		// For qpu, if no obstacle lies within the triangle then an unwrap will
		// occur.
		// For qpd, if no obstacle lies within the triangle then the robot can
		// proceed straight to d (whilst keeping it's tether taut).
		Triangle2D wrapZone = u != null ? new Triangle2D(q, p, u)
				: new Triangle2D(q, p, d);

		// Store the minimum angle between line qp and the line from q to an
		// obstacle vertex, as well as the obstacle vertex itself.
		double minAngleToQP = Double.POSITIVE_INFINITY;
		Point2D minVertex = null;

		/*
		 * Note that since rq lie on an obstacle edge, p is necessarily on the
		 * side of the obstacle. If this isn't the case then the tether is not
		 * taut.
		 */

		// Check all obstacle vertices for this first wrap point.
		for (Obstacle o : obstacles) {
			for (Point2D v : o.points) {
				// Check if the obstacle vertex is where we are currently at in
				// case of 'fatal' floating point inaccuracies.
				boolean isCurrentPoint = MathExtended.approxEqual(p, v,
						0.000001, 0.000001);

				// Skip this point if it was previously unwrapped or if it is at
				// the robot's current location.
				if ((lastUnwrapPoint == null || !v.equals(lastUnwrapPoint))
						&& !isCurrentPoint) {
					// Check if the neighbouring points of v do not prevent the
					// wrapping of the tether.
					// This is used to take care of cases where q, p and v are
					// collinear, or q is collinear with v and one of its
					// neighbouring vertices.
					boolean canWrap = isWrappingPossible(q, p, qp, qd, v, o);

					// Check if the vertex lies within the wrap zone.
					boolean inWrapZone = wrapZone.containsPoint(v) != PointInTriangleResult.NONE;

					// If the vertex is valid from wrapping, and lies within the
					// wrap zone, then check if it has the smallest angle to qp.
					if (canWrap && inWrapZone) {
						// Get the vector from q to the obstacle vertex.
						Vector2D qv = new Vector2D(q, v);

						// Use the magnitude of the angle, since d (and thus v)
						// could be on either side of qp.
						double angleToQP = Math.abs(MathExtended
								.getAngularChange(startAngle, qv.getAngle()));

						if (angleToQP < minAngleToQP) {
							minAngleToQP = angleToQP;
							minVertex = v;
						} else if (angleToQP == minAngleToQP) {
							// Given that the angular changes are equal, the
							// point closer to p should be chosen. This means
							// that there will be no wrapping around the point
							// closer to q.
							if (v.distance(p) < minVertex.distance(p)) {
								minAngleToQP = angleToQP;
								minVertex = v;
							}
						}
					}
				}
			}
		}

		// If the tether will wrap around a point, then alter the tether
		// configuration accordingly.
		if (minVertex != null) {
			Vector2D qv = new Vector2D(q, minVertex);

			// Get the extended intersection point between lines qv (v is
			// minVertex) and pd.
			// Note that this intersection point would necessarily lie on the
			// finite line pd.
			Point2D newP = MathExtended.getExtendedIntersectionPoint(q, qv, p,
					pd);

			if (newP == null) {
				Log.error("Next tether point is undefined.");

				return null;
			}

			/*
			 * Remove the last point on the tether (p), and add new vertices,
			 * minVertex and newP.
			 */

			newTC.removeLastPoint();

			// Add v if it is distinct from newP.
			if (!MathExtended.approxEqual(minVertex, newP, 0.00001, 0.00001)) {
				newTC.addPoint(minVertex);
			}

			newTC.addPoint(newP);

			// Assert that the tether configuration is valid.
			if (!tc.assertConfigurationValid()) {
				return null;
			}

			NextTetherConfigurationResult result = new NextTetherConfigurationResult(
					newTC, minVertex, null);

			return result;
		}

		// If an unwrap point exists then unwrap, and alter the tether
		// configuration accordingly.
		if (u != null) {
			// Remove the last two points on the tether (p and pivot point q),
			// and add new vertex u.
			newTC.removeLastPoint();
			newTC.removeLastPoint();
			newTC.addPoint(u);

			// Assert that the tether configuration is valid.
			if (!tc.assertConfigurationValid()) {
				return null;
			}

			// Note that q is obstacle vertex at which unwrapping occurs rather
			// than u.
			NextTetherConfigurationResult result = new NextTetherConfigurationResult(
					newTC, null, q);

			return result;
		}

		// Since we cannot wrap nor unwrap, move straight to d, and alter the
		// tether configuration accordingly.
		// Change the last point on the tether (from p) to d.
		newTC.moveLastPoint(d);

		// Assert that the tether configuration is valid.
		if (!tc.assertConfigurationValid()) {
			return null;
		}

		NextTetherConfigurationResult result = new NextTetherConfigurationResult(
				newTC, null, null);

		return result;
	}

	private static boolean isTetherMovementClockwise(Vector2D qp, Vector2D qd) {
		return MathExtended.getAngularChange(qp.getAngle(), qd.getAngle()) < 0;
	}

	/**
	 * Checks if the given tether configuration has been tried before or not.
	 * 
	 * @param newTC
	 * @param tcHistory
	 * @return true if the tether configuration is not new, false otherwise
	 */
	private static boolean hasStateBeenRevisited(TetherConfiguration newTC,
			List<TetherConfiguration> tcHistory) {
		for (TetherConfiguration tc : tcHistory) {
			if (newTC.equals(tc)) {
				return true;
			}
		}

		return false;
	}

}
