package uk.ac.cam.oda22.core;

import java.awt.geom.Point2D;
import java.util.List;

/**
 * @author Oliver
 *
 */
public final class ListFunctions {

	public static boolean isPointInList(Point2D point, List<Point2D> list) {
		for (Point2D p : list) {
			if (point.equals(p)) {
				return true;
			}
		}
		
		return false;
	}
	
	public static <T> T getLast(List<T> list) {
		return list.get(list.size() - 1);
	}
	
	public static <T> T removeLast(List<T> list) {
		return list.remove(list.size() - 1);
	}
	
}
