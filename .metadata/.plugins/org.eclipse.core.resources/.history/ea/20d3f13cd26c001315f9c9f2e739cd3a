package uk.ac.cam.oda22.core;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

/**
 * @author Oliver
 *
 */
public final class MathExtended {

	public static boolean ApproxEqual(double m, double n, double fractionalError, double absoluteError) {
		if (Math.abs(n - m) <= absoluteError) {
			return true;
		}
		
		if (m != 0 && n != 0) {
			return Math.abs(1 - (m / n)) <= fractionalError;
		}
		
		return false;
	}
	
	public static boolean ApproxEqual(Point2D p, Point2D q, double fractionalError, double absoluteError) {
		return ApproxEqual(p.getX(), q.getX(), fractionalError, absoluteError)
				&& ApproxEqual(p.getY(), q.getY(), fractionalError, absoluteError);
	}
	
	public static boolean ApproxEqual(Line2D l, Line2D m, double fractionalError, double absoluteError) {
		if (ApproxEqual(l.getP1(), m.getP1(), fractionalError, absoluteError)
				&& ApproxEqual(l.getP2(), m.getP2(), fractionalError, absoluteError)) {
			return true;
		}
		
		if (ApproxEqual(l.getP1(), m.getP2(), fractionalError, absoluteError)
				&& ApproxEqual(l.getP2(), m.getP1(), fractionalError, absoluteError)) {
			return true;
		}
		
		return false;
	}
	
	public static double getRadius(double w, double h) {
		return Math.sqrt((w * w) + (h * h));
	}
	
}
