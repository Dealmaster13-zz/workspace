package uk.ac.cam.oda22.pathplanning;

import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;

import uk.ac.cam.oda22.core.logging.Log;

/**
 * @author Oliver
 *
 */
public class Path {

	public final List<Point2D> path;
	
	public Path() {
		this.path = new LinkedList<Point2D>();
	}
	
	public Path(List<Point2D> path) {
		this.path = path;
	}
	
	public void addPoint(Point2D p) {
		this.path.add(p);
	}
	
	public void removePoint() {
		if (this.path.size() == 0) {
			Log.error("Cannot remove point from empty path.");
			
			return;
		}
		
		this.path.remove(this.path.size() - 1);
	}
	
}
