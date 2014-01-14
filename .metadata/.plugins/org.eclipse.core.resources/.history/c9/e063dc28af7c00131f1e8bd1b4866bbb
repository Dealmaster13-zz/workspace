package uk.ac.cam.oda22.pathplanning;

import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import uk.ac.cam.oda22.core.logging.Log;

/**
 * @author Oliver
 *
 */
public class Path {

	public final List<Point2D> points;
	
	public Path() {
		this.points = new LinkedList<Point2D>();
	}
	
	public Path(Point2D p) {
		this.points = new LinkedList<Point2D>();
		this.points.add(p);
	}
	
	public Path(List<Point2D> path) {
		this.points = path;
	}
	
	public void addPoint(Point2D p) {
		this.points.add(p);
	}
	
	public void addPoints(Collection<Point2D> ps) {
		this.points.addAll(ps);
	}
	
	public boolean removePoint() {
		if (this.points.size() == 0) {
			Log.error("Cannot remove point from empty path.");
			
			return false;
		}
		
		this.points.remove(this.points.size() - 1);
		
		return true;
	}
	
	/**
	 * Returns a new path with the list of points reversed.
	 * 
	 * @return reversed path
	 */
	public Path reverse() {
		Path reversedPath = new Path();
		
		for (int i = this.points.size() - 1; i >= 0; i--) {
			reversedPath.addPoint(this.points.get(i));
		}
		
		return reversedPath;
	}
	
	public boolean contains(Point2D point) {
		for (Point2D p : this.points) {
			if (p.equals(point)) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean isEmpty() {
		return this.points.size() == 0;
	}
	
	/**
	 * Gets the length of the path.
	 * 
	 * @return length
	 */
	public double length() {
		if (this.points.size() == 0) {
			return 0;
		}
		
		double l = 0;
		
		Point2D currentPoint = this.points.get(0);
		
		for (int i = 1; i < this.points.size(); i++) {
			Point2D nextPoint = this.points.get(i);
			
			l += currentPoint.distance(nextPoint);
			
			currentPoint = nextPoint;
		}
		
		return l;
	}
	
}
