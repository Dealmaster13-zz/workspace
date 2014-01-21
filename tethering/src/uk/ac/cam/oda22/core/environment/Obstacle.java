package uk.ac.cam.oda22.core.environment;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import uk.ac.cam.oda22.core.MathExtended;
import uk.ac.cam.oda22.core.Vector2D;
import uk.ac.cam.oda22.core.logging.Log;

/**
 * @author Oliver
 *
 */
public class Obstacle {

	public final List<Point2D> points;

	public final List<Line2D> edges;

	public final boolean clockwise;

	public Obstacle(List<Point2D> points) {
		this.points = points;

		this.edges = new ArrayList<Line2D>();

		this.clockwise = isClockwise(this.points);

		for (int i = 0; i < points.size(); i++) {
			int j = (i + 1) % points.size();

			Line2D line = new Line2D.Double();
			line.setLine(this.points.get(i), this.points.get(j));

			// Fail if this line intersects with any of the existing edges.
			for (Line2D l : this.edges) {
				if (MathExtended.strictIntersectsLine(line, l)) {
					Log.error("Obstacle is malformed as two or more of its edges intersect one another.");

					return;
				}
			}

			this.edges.add(line);
		}
	}

	/**
	 * Note that this does not deal with intersections within the polygon.
	 * This primitive implementation should be fine for visibility checking.
	 * 
	 * TODO: Calculate intersections within the polygon.
	 * 
	 * @param l
	 * @return
	 */
	public ObstacleLineIntersectionResult intersectsLine(Line2D l) {
		double fractionalError = 0.001, absoluteError = 0.001;

		// Check if the line is the same as any of the obstacle edges.
		for (Line2D edge : this.edges) {
			boolean b1 = MathExtended.approxEqual(edge.getP1(), l.getP1(), fractionalError, absoluteError)
					&& MathExtended.approxEqual(edge.getP2(), l.getP2(), fractionalError, absoluteError);

			boolean b2 = MathExtended.approxEqual(edge.getP1(), l.getP2(), fractionalError, absoluteError)
					&& MathExtended.approxEqual(edge.getP2(), l.getP1(), fractionalError, absoluteError);

			if (b1 || b2) {
				return ObstacleLineIntersectionResult.EQUAL_LINES;
			}
		}

		// Check if any obstacle edges intersect with the line.
		for (Line2D edge : this.edges) {
			if (MathExtended.strictIntersectsLine(l, edge)) {
				return ObstacleLineIntersectionResult.CROSSED;
			}
		}

		// Check if the edge formed spans between any two obstacle vertices.
		for (Point2D p1 : this.points) {
			if (p1.equals(l.getP1())) {
				for (Point2D p2 : this.points) {
					if (p2.equals(l.getP2())) {
						return ObstacleLineIntersectionResult.CROSSED;
					}
				}
			}
		}

		return ObstacleLineIntersectionResult.NONE;
	}

	public boolean touchesObstacle(Obstacle o) {
		// For each vertex of this obstacle, check if it touches one of o's edges.
		for (Point2D p : this.points) {
			for (Line2D e : o.edges) {
				if (MathExtended.loosePointOnLine(p, e)) {
					return true;
				}
			}
		}

		// For each vertex of o, check if it touches one of this obstacle's edges.
		for (Point2D p : o.points) {
			for (Line2D e : this.edges) {
				if (MathExtended.loosePointOnLine(p, e)) {
					return true;
				}
			}
		}

		return false;
	}

	public Obstacle mergeObstacles(Obstacle o) {
		// TODO: Implement.
		return null;
	}

	public Obstacle expandObstacle(double radius) {
		List<Point2D> l = new ArrayList<Point2D>();

		int s = this.points.size();

		if (s == 1) {
			/*
			 *  Handle the one-vertex case.
			 */

			List<Point2D> arcPoints = MathExtended.approximateArc(8, 0, Math.PI * 2, radius);

			Point2D p = this.points.get(0);

			// Add all but the last arc point (duplicate).
			for (int i = 0; i < arcPoints.size() - 1; i++) {
				l.add(MathExtended.translate(p, arcPoints.get(i)));
			}
		}
		else if (s == 2) {
			/*
			 *  Handle the two-vertex case.
			 */

			Point2D p1 = this.points.get(0);
			Point2D p2 = this.points.get(1);

			double x1 = p1.getX();
			double y1 = p1.getY();
			double x2 = p2.getX();
			double y2 = p2.getY();

			// Get the vector perpendicular to the line at the first endpoint.
			Vector2D t1 = Vector2D.getTangentVector(x1 - x2, y1 - y2, false);

			List<Point2D> arc1Points = MathExtended.approximateArc(4, t1.getAngle(), Math.PI, radius);

			// Add all of the arc points.
			for (int i = 0; i < arc1Points.size(); i++) {
				l.add(MathExtended.translate(p1, arc1Points.get(i)));
			}

			// Get the vector perpendicular to the line at the second endpoint.
			Vector2D t2 = Vector2D.getTangentVector(x2 - x1, y2 - y1, false);

			List<Point2D> arc2Points = MathExtended.approximateArc(4, t2.getAngle(), Math.PI, radius);

			// Add all of the arc points.
			for (int i = 0; i < arc2Points.size(); i++) {
				l.add(MathExtended.translate(p2, arc2Points.get(i)));
			}
		}
		else if (s >= 3) {
			/*
			 *  Handle the many (>= 3) vertex case.
			 */

			for (int i = 0; i < s; i++) {
				Point2D p = this.points.get(i);
				Point2D prev = this.points.get((i - 1 + s) % s);
				Point2D next = this.points.get((i + 1) % s);

				// Get the vectors for each edge.
				Vector2D v1 = new Vector2D(prev, p);
				Vector2D v2 = new Vector2D(p, next);

				// Get the angular change between the edges.
				double angularChange = MathExtended.getAngularChange(v1.getAngle(), v2.getAngle());

				// Skip the vertex if there is no angular change.
				if (angularChange != 0) {
					boolean vertexClockwise = angularChange < 0;

					// Get the vectors perpendicular to each edge.
					// If the obstacle is clockwise then the outer normal is on the left, otherwise it is on the right.
					Vector2D t1 = v1.getTangentVector(this.clockwise);
					Vector2D t2 = v2.getTangentVector(this.clockwise);
					
					// If the obstacle is convex at the vertex, then add an arced expansion, otherwise add a point expansion.
					// If the angular change matches the obstacle's rotational orientation then it is convex at this point.
					if (vertexClockwise == this.clockwise) {
						/*
						 * Add an arced expansion.
						 */
						
						// Get the start rotation of the arc.
						double startRads = t1.getAngle();
						
						// Get the number of radians of rotation for the arc.
						// Note that this should be negative in the clockwise case.
						double rads = MathExtended.getAngularChange(startRads, t2.getAngle());
						
						// Approximate an arc with one inner vertex for every 45 degrees.
						int numberOfInnerVertices = (int) Math.ceil(Math.abs(rads) / Math.PI * 4);
						
						List<Point2D> arcPoints = MathExtended.approximateArc(numberOfInnerVertices, startRads, rads, radius);
						
						// Add all of the arc points.
						for (int j = 0; j < arcPoints.size(); j++) {
							l.add(MathExtended.translate(p, arcPoints.get(j)));
						}
					}
					else {
						/*
						 * Add a point expansion
						 */
						
						// Get the earlier expanded edge parameters.
						Vector2D t3 = t1.setLength(radius);
						Point2D p3 = t3.addPoint(p);
						
						// Get the later expanded edge parameters.
						Vector2D t4 = t2.setLength(radius);
						Point2D p4 = t4.addPoint(p);
						
						// Get the intersection point between the two expanded edges.
						Point2D intersectionPoint = MathExtended.getIntersectionPoint(p3, v1, p4, v2);
						
						// Add the intersection point as the sole expansion vertex.
						l.add(intersectionPoint);
					}
				}
			}
		}

		// TODO: Need to normalise the obstacle in case its edges overlap.

		return new Obstacle(l);
	}

	/**
	 * Compute whether or not a polygon, given by its vertex list, is clockwise or not.
	 * This can be determined using the following methodology:
	 * 		1.	Set angle accumulator n to 0
	 * 		2.	For each edge e:
	 * 			i.	Increase n by the (signed) shortest number of degrees to rotate to the next vertex
	 * 				Take turning clockwise to be a negative rotation, and counter-clockwise to be a positive rotation
	 * 		3.	If n is negative then the polygon is clockwise, otherwise it is counter-clockwise
	 * 			n should be +/-360 by definition of the sum of the external angles of a polygon
	 * This algorithm assumes that the polygon has no crossing edges.
	 * Note that 
	 * 
	 * @param l
	 * @return true if the polygon is clockwise, false otherwise
	 */
	private static boolean isClockwise(List<Point2D> l) {
		double n = 0;

		int s = l.size();

		// If the polygon has less than three vertices then it is clockwise.
		if (s < 3) {
			return true;
		}

		for (int i = 0; i < s; i++) {
			Point2D p = l.get(i);
			Point2D prev = l.get((i - 1 + s) % s);
			Point2D next = l.get((i + 1) % s);

			Vector2D v1 = new Vector2D(prev, p);
			Vector2D v2 = new Vector2D(p, next);

			n += MathExtended.getAngularChange(v1.getAngle(), v2.getAngle());
		}

		if (!MathExtended.approxEqual(Math.abs(n), Math.PI * 2, 0.00001, 0.00001)) {
			Log.warning("Polygon has invalid external angle.");
		}

		// If n is negative then the polygon is clockwise, otherwise it is counter-clockwise
		return n < 0;
	}

}
