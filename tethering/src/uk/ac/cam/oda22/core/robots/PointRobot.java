package uk.ac.cam.oda22.core.robots;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import uk.ac.cam.oda22.core.ShapeFunctions;
import uk.ac.cam.oda22.core.tethers.Tether;

/**
 * @author Oliver
 *
 */
public class PointRobot extends Robot {

	public PointRobot(Point2D position, double rotation, double rotationalSensitivity, Tether tether) throws Exception {
		super(position, 0, rotation, rotationalSensitivity, tether);
		
		Line2D[] arrow = ShapeFunctions.getArrow(new Point2D.Double(0, 0), rotation, 20, 5);
		
		RobotOutline outline = new RobotOutline(arrow);
		
		this.setOutline(outline);
	}

}
