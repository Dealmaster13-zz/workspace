package uk.ac.cam.oda22.core.environment;

import java.awt.geom.Point2D;

import uk.ac.cam.oda22.core.Triangle2D;

/**
 * @author Oliver
 *
 */
public class EnvironmentTriangle extends Triangle2D {

	public final boolean isObstacle;

	public EnvironmentTriangle(boolean isObstacle, Point2D point1, Point2D point2, Point2D point3) {
		super(point1, point2, point3);
		
		this.isObstacle = isObstacle;
	}

}
