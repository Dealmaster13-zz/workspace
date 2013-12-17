package uk.ac.cam.oda22.pathplanning;

import java.awt.geom.Point2D;
import java.util.List;

/**
 * @author Oliver
 *
 */
public class TetherPointVisibility {

	/**
	 * The start distance from the anchor point.
	 */
	public final double startW;

	/**
	 * The end distance from the anchor point.
	 */
	public final double endW;
	
	/**
	 * The tether points by their distance from the anchor point.
	 */
	public final List<Double> wList;

	/**
	 * The tether points.
	 */
	public final List<Point2D> xList;
	
	public final List<Point2D> visibleVertices;

	/**
	 * @param w
	 * @param visibleVertices
	 */
	public TetherPointVisibility(double startW, double endW, List<Double> wList, List<Point2D> xList, List<Point2D> visibleVertices) {
		this.startW = startW;
		this.endW = endW;
		this.wList = wList;
		this.xList = xList;
		this.visibleVertices = visibleVertices;
	}
	
	/**
	 * Check if two visibility sets are equal in the points that they contain.
	 * 
	 * @param l
	 * @return true if the sets are equal, false otherwise
	 */
	public boolean isVisibilitySetEqual(List<Point2D> l) {
		if (this.visibleVertices.size() != l.size()) {
			return false;
		}
		
		for (Point2D p : l) {
			int index = 0;
			
			boolean found = false;
			
			while (!found && index < this.visibleVertices.size()) {
				if (p.equals(this.visibleVertices.get(index))) {
					found = true;
				}
				
				index ++;
			}
			
			if (!found) {
				return false;
			}
		}
		
		return true;
	}
	
}
