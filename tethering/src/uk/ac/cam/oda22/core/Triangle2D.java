package uk.ac.cam.oda22.core;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

/**
 * @author Oliver
 * 
 */
public class Triangle2D {

	public final Point2D point1;

	public final Point2D point2;

	public final Point2D point3;

	private final Line2D edge1;

	private final Line2D edge2;

	private final Line2D edge3;

	public Triangle2D(Point2D point1, Point2D point2, Point2D point3) {
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
				&& !MathExtended.approxEqual(this.edge1.getP1(), l.getP1(),
						fractionalError, absoluteError)
				&& !MathExtended.approxEqual(this.edge1.getP1(), l.getP2(),
						fractionalError, absoluteError)
				&& !MathExtended.approxEqual(this.edge1.getP2(), l.getP1(),
						fractionalError, absoluteError)
				&& !MathExtended.approxEqual(this.edge1.getP2(), l.getP2(),
						fractionalError, absoluteError)) {
			return true;
		}

		if (l.intersectsLine(this.edge2)
				&& !MathExtended.approxEqual(this.edge2.getP1(), l.getP1(),
						fractionalError, absoluteError)
				&& !MathExtended.approxEqual(this.edge2.getP1(), l.getP2(),
						fractionalError, absoluteError)
				&& !MathExtended.approxEqual(this.edge2.getP2(), l.getP1(),
						fractionalError, absoluteError)
				&& !MathExtended.approxEqual(this.edge2.getP2(), l.getP2(),
						fractionalError, absoluteError)) {
			return true;
		}

		if (l.intersectsLine(this.edge3)
				&& !MathExtended.approxEqual(this.edge3.getP1(), l.getP1(),
						fractionalError, absoluteError)
				&& !MathExtended.approxEqual(this.edge3.getP1(), l.getP2(),
						fractionalError, absoluteError)
				&& !MathExtended.approxEqual(this.edge3.getP2(), l.getP1(),
						fractionalError, absoluteError)
				&& !MathExtended.approxEqual(this.edge3.getP2(), l.getP2(),
						fractionalError, absoluteError)) {
			return true;
		}

		return false;
	}

	public PointInTriangleResult containsPoint(Point2D p) {
		/*
		 * Note that due to the calculations involved, we need to treat the case
		 * of a 1D triangle separately.
		 */

		boolean r1 = MathExtended.inRange(p.getX(), this.edge1.getX1(),
				this.edge1.getX2(), false);
		boolean r2 = MathExtended.inRange(p.getX(), this.edge2.getX1(),
				this.edge2.getX2(), false);
		boolean r3 = MathExtended.inRange(p.getX(), this.edge3.getX1(),
				this.edge3.getX2(), false);
		boolean r4 = MathExtended.inRange(p.getY(), this.edge1.getY1(),
				this.edge1.getY2(), false);
		boolean r5 = MathExtended.inRange(p.getY(), this.edge2.getY1(),
				this.edge2.getY2(), false);
		boolean r6 = MathExtended.inRange(p.getY(), this.edge3.getY1(),
				this.edge3.getY2(), false);

		// Check if the point is out of bounds.
		if (!(r1 || r2 || r3) || !(r4 || r5 || r6)) {
			return PointInTriangleResult.NONE;
		}

		// Check if the point is on an edge.
		if (MathExtended.pointOnLine(p, this.edge1) != PointOnLineResult.NONE
				|| MathExtended.pointOnLine(p, this.edge2) != PointOnLineResult.NONE
				|| MathExtended.pointOnLine(p, this.edge3) != PointOnLineResult.NONE) {
			return PointInTriangleResult.ON_EDGE;
		}

		boolean b1, b2, b3;

		b1 = this.sign(p, this.point1, this.point2) < 0;
		b2 = this.sign(p, this.point2, this.point3) < 0;
		b3 = this.sign(p, this.point3, this.point1) < 0;

		return ((b1 == b2) && (b2 == b3)) ? PointInTriangleResult.IN_TRIANGLE
				: PointInTriangleResult.NONE;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}

		Triangle2D t = (Triangle2D) o;

		double fractionalError = 0.001, absoluteError = 0.001;

		if (MathExtended.approxEqual(this.point1, t.point1, fractionalError,
				absoluteError)) {
			if (MathExtended.approxEqual(this.point2, t.point2,
					fractionalError, absoluteError)) {
				if (MathExtended.approxEqual(this.point3, t.point3,
						fractionalError, absoluteError)) {
					return true;
				}
			} else if (MathExtended.approxEqual(this.point2, t.point3,
					fractionalError, absoluteError)) {
				if (MathExtended.approxEqual(this.point3, t.point2,
						fractionalError, absoluteError)) {
					return true;
				}
			}
		} else if (MathExtended.approxEqual(this.point1, t.point2,
				fractionalError, absoluteError)) {
			if (MathExtended.approxEqual(this.point2, t.point1,
					fractionalError, absoluteError)) {
				if (MathExtended.approxEqual(this.point3, t.point3,
						fractionalError, absoluteError)) {
					return true;
				}
			} else if (MathExtended.approxEqual(this.point2, t.point3,
					fractionalError, absoluteError)) {
				if (MathExtended.approxEqual(this.point3, t.point1,
						fractionalError, absoluteError)) {
					return true;
				}
			}
		} else if (MathExtended.approxEqual(this.point1, t.point3,
				fractionalError, absoluteError)) {
			if (MathExtended.approxEqual(this.point2, t.point1,
					fractionalError, absoluteError)) {
				if (MathExtended.approxEqual(this.point3, t.point2,
						fractionalError, absoluteError)) {
					return true;
				}
			} else if (MathExtended.approxEqual(this.point2, t.point2,
					fractionalError, absoluteError)) {
				if (MathExtended.approxEqual(this.point3, t.point1,
						fractionalError, absoluteError)) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * This is a helper functions used for checking if a point lies in the
	 * triangle.
	 * 
	 * @param p1
	 * @param p2
	 * @param p3
	 * @return
	 */
	private double sign(Point2D p1, Point2D p2, Point2D p3) {
		return (p1.getX() - p3.getX()) * (p2.getY() - p3.getY())
				- (p2.getX() - p3.getX()) * (p1.getY() - p3.getY());
	}

}
