package uk.ac.cam.oda22.core.environment;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import uk.ac.cam.oda22.core.MathExtended;

/**
 * @author Oliver
 *
 * TODO: Implement expansion of an obstacle for non-point robots.
 *
 */
public class EnvironmentTriangle {

	public final boolean isObstacle;

	public final Point2D point1;

	public final Point2D point2;

	public final Point2D point3;

	private final Line2D edge1;

	private final Line2D edge2;

	private final Line2D edge3;

	public EnvironmentTriangle(boolean isObstacle, Point2D point1, Point2D point2, Point2D point3) {
		this.isObstacle = isObstacle;
		this.point1 = point1;
		this.point2 = point2;
		this.point3 = point3;

		this.edge1 = new Line2D.Double();
		this.edge1.setLine(this.point1, this.point2);
		
		this.edge2 = new Line2D.Double();
		this.edge2.setLine(this.point2, this.point3);
		
		this.edge3 = new Line2D.Double();
		this.edge3.setLine(this.point3, this.point1);
	}

	public boolean looseIntersectsLine(Line2D l) {
		double fractionalError = 0.001, absoluteError = 0.001;

		if (l.intersectsLine(this.edge1)
				&& !MathExtended.approxEqual(this.edge1.getP1(), l.getP1(), fractionalError, absoluteError)
				&& !MathExtended.approxEqual(this.edge1.getP1(), l.getP2(), fractionalError, absoluteError)
				&& !MathExtended.approxEqual(this.edge1.getP2(), l.getP1(), fractionalError, absoluteError)
				&& !MathExtended.approxEqual(this.edge1.getP2(), l.getP2(), fractionalError, absoluteError)) {
			return true;
		}

		if (l.intersectsLine(this.edge2)
				&& !MathExtended.approxEqual(this.edge2.getP1(), l.getP1(), fractionalError, absoluteError)
				&& !MathExtended.approxEqual(this.edge2.getP1(), l.getP2(), fractionalError, absoluteError)
				&& !MathExtended.approxEqual(this.edge2.getP2(), l.getP1(), fractionalError, absoluteError)
				&& !MathExtended.approxEqual(this.edge2.getP2(), l.getP2(), fractionalError, absoluteError)) {
			return true;
		}

		if (l.intersectsLine(this.edge3)
				&& !MathExtended.approxEqual(this.edge3.getP1(), l.getP1(), fractionalError, absoluteError)
				&& !MathExtended.approxEqual(this.edge3.getP1(), l.getP2(), fractionalError, absoluteError)
				&& !MathExtended.approxEqual(this.edge3.getP2(), l.getP1(), fractionalError, absoluteError)
				&& !MathExtended.approxEqual(this.edge3.getP2(), l.getP2(), fractionalError, absoluteError)) {
			return true;
		}

		return false;
	}

	public boolean containsPoint(Point2D p)
	{
		boolean b1, b2, b3;

		b1 = this.sign(p, this.point1, this.point2) < 0;
		b2 = this.sign(p, this.point2, this.point3) < 0;
		b3 = this.sign(p, this.point3, this.point1) < 0;

		return ((b1 == b2) && (b2 == b3));
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}

		EnvironmentTriangle t = (EnvironmentTriangle)o;

		double fractionalError = 0.001, absoluteError = 0.001;

		if (MathExtended.approxEqual(this.point1, t.point1, fractionalError, absoluteError)) {
			if (MathExtended.approxEqual(this.point2, t.point2, fractionalError, absoluteError)) {
				if (MathExtended.approxEqual(this.point3, t.point3, fractionalError, absoluteError)) {
					return true;
				}
			}
			else if (MathExtended.approxEqual(this.point2, t.point3, fractionalError, absoluteError)) {
				if (MathExtended.approxEqual(this.point3, t.point2, fractionalError, absoluteError)) {
					return true;
				}
			}
		}
		else if (MathExtended.approxEqual(this.point1, t.point2, fractionalError, absoluteError)) {
			if (MathExtended.approxEqual(this.point2, t.point1, fractionalError, absoluteError)) {
				if (MathExtended.approxEqual(this.point3, t.point3, fractionalError, absoluteError)) {
					return true;
				}
			}
			else if (MathExtended.approxEqual(this.point2, t.point3, fractionalError, absoluteError)) {
				if (MathExtended.approxEqual(this.point3, t.point1, fractionalError, absoluteError)) {
					return true;
				}
			}
		}
		else if (MathExtended.approxEqual(this.point1, t.point3, fractionalError, absoluteError)) {
			if (MathExtended.approxEqual(this.point2, t.point1, fractionalError, absoluteError)) {
				if (MathExtended.approxEqual(this.point3, t.point2, fractionalError, absoluteError)) {
					return true;
				}
			}
			else if (MathExtended.approxEqual(this.point2, t.point2, fractionalError, absoluteError)) {
				if (MathExtended.approxEqual(this.point3, t.point1, fractionalError, absoluteError)) {
					return true;
				}
			}
		}

		return false;
	}

	private double sign(Point2D p1, Point2D p2, Point2D p3)
	{
		return (p1.getX() - p3.getX()) * (p2.getY() - p3.getY()) - (p2.getX() - p3.getX()) * (p1.getY() - p3.getY());
	}

}
