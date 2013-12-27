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
	
	public Path(List<Point2D> path) {
		this.points = path;
	}
	
	public void addPoint(Point2D p) {
		this.points.add(p);
	}
	
	public void addPoints(Collection<Point2D> ps) {
		this.points.addAll(ps);
	}
	
	public void removePoint() {
		if (this.points.size() == 0) {
			Log.error("Cannot remove point from empty path.");
			
			return;
		}
		
		this.points.remove(this.points.size() - 1);
	}
	
}
