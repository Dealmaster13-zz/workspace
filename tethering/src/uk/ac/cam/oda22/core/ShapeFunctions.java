package uk.ac.cam.oda22.core;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

/**
 * @author Oliver
 *
 */
public class ShapeFunctions {

	public static Line2D[] getRectangle(Point2D centre, double width, double height, double rads) {
		double radius = Vector2D.getLength(width / 2, height / 2);
		
		double a = Math.atan2(height, width);
		double a1 = rads + (Math.PI - a);
		double a2 = rads + a;
		double a3 = rads - a;
		double a4 = rads - (Math.PI - a);

		Vector2D v1 = new Vector2D(radius * Math.cos(a1), radius * Math.sin(a1));
		Vector2D v2 = new Vector2D(radius * Math.cos(a2), radius * Math.sin(a2));
		Vector2D v3 = new Vector2D(radius * Math.cos(a3), radius * Math.sin(a3));
		Vector2D v4 = new Vector2D(radius * Math.cos(a4), radius * Math.sin(a4));

		Point2D p1 = v1.addPoint(centre);
		Point2D p2 = v2.addPoint(centre);
		Point2D p3 = v3.addPoint(centre);
		Point2D p4 = v4.addPoint(centre);

		Line2D[] lines = new Line2D[4];
		lines[0] = new Line2D.Double(p1.getX(), p1.getY(), p2.getX(), p2.getY());
		lines[1] = new Line2D.Double(p2.getX(), p2.getY(), p3.getX(), p3.getY());
		lines[2] = new Line2D.Double(p3.getX(), p3.getY(), p4.getX(), p4.getY());
		lines[3] = new Line2D.Double(p4.getX(), p4.getY(), p1.getX(), p1.getY());

		return lines;
	}
	
	public static Line2D[] getCross(Point2D centre, double radius) {
		Line2D l1 = new Line2D.Double(
				centre.getX() - radius,
				centre.getY() - radius,
				centre.getX() + radius,
				centre.getY() + radius);

		Line2D l2 = new Line2D.Double(
				centre.getX() - radius,
				centre.getY() + radius,
				centre.getX() + radius,
				centre.getY() - radius);

		Line2D[] lines = new Line2D[2];
		lines[0] = l1;
		lines[1] = l2;

		return lines;
	}
	
	public static Line2D[] getArrow(Point2D position, double rotation, double length, double headLength) {
		Line2D[] lines = new Line2D[3];
		
		double headX = position.getX() + (length * Math.cos(rotation));
		double headY = position.getY() + (length * Math.sin(rotation));

		double headTailX1 = headX + (headLength * Math.cos(rotation + (Math.PI * 3 / 4)));
		double headTailY1 = headY + (headLength * Math.sin(rotation + (Math.PI * 3 / 4)));

		double headTailX2 = headX + (headLength * Math.cos(rotation - (Math.PI * 3 / 4)));
		double headTailY2 = headY + (headLength * Math.sin(rotation - (Math.PI * 3 / 4)));

		lines[0] = new Line2D.Double(position.getX(), position.getY(), headX, headY);
		lines[1] = new Line2D.Double(headX, headY, headTailX1, headTailY1);
		lines[2] = new Line2D.Double(headX, headY, headTailX2, headTailY2);
		
		return lines;
	}

	public static Line2D translateShape(Line2D line, double x, double y) {
		return new Line2D.Double(
				line.getX1() + x,
				line.getY1() + y,
				line.getX2() + x,
				line.getY2() + y);
	}
	
	public static Line2D stretchShape(Line2D line, double xScale, double yScale) {
		return new Line2D.Double(
				line.getX1() * xScale,
				line.getY1() * yScale,
				line.getX2() * xScale,
				line.getY2() * yScale);
	}

}
