package uk.ac.cam.oda22.core.environment;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import uk.ac.cam.oda22.core.PointInTriangleResult;
import uk.ac.cam.oda22.core.logging.Log;

/**
 * @author Oliver
 * 
 */
public class PolygonRoom extends Room {

	public final List<EnvironmentTriangle> triangles;

	public PolygonRoom(double width, double height, List<Obstacle> l) {
		super(width, height, l);

		this.triangles = this.triangulateRoom();
	}

	/**
	 * Returns whether or not an obstacle is internal (not touching the rooms
	 * sides). Note that we are assuming that all obstacles are normalised (no
	 * two non-room-boundary obstacles touching sides).
	 * 
	 * @param o
	 * @return true if the obstacle is internal, and false otherwise
	 */
	public static boolean isObstacleInternal(Obstacle o, List<Obstacle> os) {
		if (!os.contains(o)) {
			Log.error("Obstacle not found in the room");

			return false;
		}

		// For each obstacle check if it touches o.
		for (Obstacle obstacle : os) {
			if (obstacle != o && obstacle.touchesObstacle(o)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Expand all of the obstacles in the room by a given radius.
	 * 
	 * @param radius
	 * @return expanded obstacles
	 */
	public List<Obstacle> getExpandedObstacles(double radius) {
		List<Obstacle> l = new ArrayList<Obstacle>();

		for (int i = 0; i < this.obstacles.size(); i++) {
			l.add(this.obstacles.get(i).expandObstacle(radius));
		}

		return l;
	}

	public boolean isPointInEmptySpace(Point2D p) {
		boolean inSpace = false;

		int index = 0;

		while (!inSpace && index < this.triangles.size()) {
			EnvironmentTriangle t = this.triangles.get(index);

			if (!t.isObstacle
					&& t.containsPoint(p) != PointInTriangleResult.NONE) {
				inSpace = true;
			}

			index++;
		}

		return inSpace;
	}

	

	/**
	 * Triangulates the room's obstacles and empty space.
	 * 
	 * @return the triangles
	 */
	private List<EnvironmentTriangle> triangulateRoom() {
		List<EnvironmentTriangle> t = new ArrayList<EnvironmentTriangle>();

		// Add all 'obstacle' triangles.
		for (Obstacle o : obstacles) {
			if (o.points.size() > 0) {
				// Add a triangle if obstacle is a point.
				if (o.points.size() == 1) {
					t.add(new EnvironmentTriangle(true, o.points.get(0),
							o.points.get(0), o.points.get(0)));
				}
				// Add a triangle if obstacle is a line.
				else if (o.points.size() == 2) {
					t.add(new EnvironmentTriangle(true, o.points.get(0),
							o.points.get(0), o.points.get(1)));
				} else {
					// Add a triangle with one point always being the first
					// point.
					for (int i = 1; i < o.points.size() - 1; i++) {
						t.add(new EnvironmentTriangle(true, o.points.get(0),
								o.points.get(i), o.points.get(i + 1)));
					}
				}
			}
		}

		// Add all 'freespace' triangles.
		for (Obstacle p : obstacles) {
			for (Obstacle q : obstacles) {
				if (p != q) {
					// Try to form a triangle with one of p's edges and a point
					// from q.
					for (int i = 0; i < p.points.size(); i++) {
						if (p.points.size() >= 2) {
							Point2D point1 = p.points.get(i);
							Point2D point2 = p.points.get((i + 1)
									% p.points.size());

							for (int j = 0; j < q.points.size(); j++) {
								boolean cross = false;

								int index = 0;

								Point2D point3 = q.points.get(j);

								Line2D l1 = new Line2D.Double();
								l1.setLine(point1, q.points.get(j));

								Line2D l2 = new Line2D.Double();
								l2.setLine(point2, q.points.get(j));

								// Do not add the triangle if its edges cross
								// any other triangles.
								while (!cross && index < t.size()) {
									if (t.get(index).looseIntersectsLine(l1)
											|| t.get(index)
													.looseIntersectsLine(l2)) {
										cross = true;
									}

									index++;
								}

								EnvironmentTriangle s = new EnvironmentTriangle(
										false, point1, point2, point3);

								boolean unique = true;

								int index2 = 0;

								while (unique && index2 < t.size()) {
									if (s.equals(t.get(index2))) {
										unique = false;
									}

									index2++;
								}

								if (!cross && unique) {
									t.add(s);
								}
							}
						}
					}
				}
			}
		}

		return t;
	}

}
