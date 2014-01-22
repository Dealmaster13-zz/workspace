package uk.ac.cam.oda22.core.environment;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import uk.ac.cam.oda22.core.logging.Log;

/**
 * @author Oliver
 *
 */
public class Room {

	public final List<Obstacle> obstacles;

	private final double width;

	private final double height;

	private final List<EnvironmentTriangle> triangles;

	public Room(double width, double height, List<Obstacle> l) {
		this.width = width;
		this.height = height;

		this.obstacles = new ArrayList<Obstacle>(); 
		this.obstacles.addAll(l);

		this.addRoomEdges();

		this.triangles = this.triangulateRoom();
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

			if (!t.isObstacle && t.containsPoint(p)) {
				inSpace = true;
			}

			index ++;
		}

		return inSpace;
	}
	
	/**
	 * Returns whether or not an obstacle is internal (not touching the rooms sides).
	 * Note that we are assuming that all obstacles are normalised (no two obstacles touching sides)
	 * 
	 * @param o
	 * @return true if the obstacle is internal, and false otherwise
	 */
	public boolean isObstacleInternal(Obstacle o) {
		if (!this.obstacles.contains(o)) {
			Log.error("Obstacle not found in the room");
			
			return false;
		}
		
		// For each obstacle check if it touches o.
		for (Obstacle obstacle : this.obstacles) {
			if (obstacle != o && obstacle.touchesObstacle(o)) {
				return false;
			}
		}
		
		return true;
	}

	/**
	 * Adds the four room edges as four obstacles with no width.
	 */
	private void addRoomEdges() {
		Point2D p1 = new Point2D.Double(0, 0);
		Point2D p2 = new Point2D.Double(width, 0);
		Point2D p3 = new Point2D.Double(0, height);
		Point2D p4 = new Point2D.Double(width, height);

		List<Point2D> el1 = new ArrayList<Point2D>();
		el1.add(p1);
		el1.add(p2);

		List<Point2D> el2 = new ArrayList<Point2D>();
		el2.add(p2);
		el2.add(p4);

		List<Point2D> el3 = new ArrayList<Point2D>();
		el3.add(p4);
		el3.add(p3);

		List<Point2D> el4 = new ArrayList<Point2D>();
		el4.add(p3);
		el4.add(p1);

		this.obstacles.add(new Obstacle(el1));
		this.obstacles.add(new Obstacle(el2));
		this.obstacles.add(new Obstacle(el3));
		this.obstacles.add(new Obstacle(el4));
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
					t.add(new EnvironmentTriangle(true, o.points.get(0), o.points.get(0), o.points.get(0)));
				}
				// Add a triangle if obstacle is a line.
				else if (o.points.size() == 2) {
					t.add(new EnvironmentTriangle(true, o.points.get(0), o.points.get(0), o.points.get(1)));
				}
				else {
					// Add a triangle with one point always being the first point.
					for (int i = 1; i < o.points.size() - 1; i++) {
						t.add(new EnvironmentTriangle(true, o.points.get(0), o.points.get(i), o.points.get(i + 1)));
					}
				}
			}
		}

		// Add all 'freespace' triangles.
		for (Obstacle p : obstacles) {
			for (Obstacle q : obstacles) {
				if (p != q) {
					// Try to form a triangle with one of p's edges and a point from q.
					for (int i = 0; i < p.points.size(); i++) {
						if (p.points.size() >= 2) {
							Point2D point1 = p.points.get(i);
							Point2D point2 = p.points.get((i + 1) % p.points.size());

							for (int j = 0; j < q.points.size(); j++) {
								boolean cross = false;

								int index = 0;

								Point2D point3 = q.points.get(j);

								Line2D l1 = new Line2D.Double();
								l1.setLine(point1, q.points.get(j));

								Line2D l2 = new Line2D.Double();
								l2.setLine(point2, q.points.get(j));

								// Do not add the triangle if its edges cross any other triangles.
								while (!cross && index < t.size()) {
									if (t.get(index).looseIntersectsLine(l1) || t.get(index).looseIntersectsLine(l2)) {
										cross = true;
									}

									index ++;
								}

								EnvironmentTriangle s = new EnvironmentTriangle(false, point1, point2, point3);

								boolean unique = true;

								int index2 = 0;

								while (unique && index2 < t.size()) {
									if (s.equals(t.get(index2))) {
										unique = false;
									}

									index2 ++;
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
