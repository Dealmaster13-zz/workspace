package uk.ac.cam.oda22.pathplanning;

import java.awt.geom.Point2D;
import java.util.List;

import uk.ac.cam.oda22.core.tethers.TetherPoint;

/**
 * @author Oliver
 *
 */
public class VisibilityChangeList {

	/**
	 * The visible vertices.
	 */
	public final List<Point2D> vertices;
	
	/**
	 * The tether points.
	 */
	public final List<TetherPoint> tetherPoints;
	
	public VisibilityChangeList(List<Point2D> vertices, List<TetherPoint> points) {
		this.vertices = vertices;
		this.tetherPoints = points;
	}
	
	/**
	 * Gets the point closest to the robot.
	 * If reverse is true then treat w as the distance from the robot.
	 * 
	 * @param reverse
	 * @return
	 */
	public TetherPoint getClosestPoint(boolean reverse) {
		double w = reverse ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;
		TetherPoint tetherPoint = null;
		
		for (TetherPoint p : this.tetherPoints) {
			if (reverse ? p.w < w : p.w > w) {
				w = p.w;
				tetherPoint = p;
			}
		}
		
		return tetherPoint;
	}
	
}
