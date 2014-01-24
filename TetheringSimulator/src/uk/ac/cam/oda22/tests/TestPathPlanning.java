package uk.ac.cam.oda22.tests;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import uk.ac.cam.oda22.core.environment.Obstacle;
import uk.ac.cam.oda22.core.environment.Room;
import uk.ac.cam.oda22.core.robots.PointRobot;
import uk.ac.cam.oda22.core.robots.Robot;
import uk.ac.cam.oda22.core.tethers.SimpleTether;
import uk.ac.cam.oda22.core.tethers.Tether;
import uk.ac.cam.oda22.core.tethers.TetherConfiguration;
import uk.ac.cam.oda22.pathplanning.PathPlanner;
import uk.ac.cam.oda22.pathplanning.PathPlanningResult;

/**
 * @author Oliver
 *
 */
public class TestPathPlanning {

	private PathPlanningResult result;
	
	@BeforeClass
	public void oneTimeSetUp() {
		List<Obstacle> l = new ArrayList<Obstacle>();

		List<Point2D> points = new ArrayList<Point2D>();
		points.add(new Point2D.Double(35, 45));
		points.add(new Point2D.Double(60, 55));
		points.add(new Point2D.Double(50, 15));
		points.add(new Point2D.Double(10, 25));

		Obstacle o = new Obstacle(points);

		l.add(o);

		Room room = new Room(100, 100, l);

		Point2D u = new Point2D.Double(80, 60);

		Point2D anchor = new Point2D.Double(0, 0);

		TetherConfiguration X = new TetherConfiguration();
		X.addPoint(new Point2D.Double(50, 15));
		X.addPoint(new Point2D.Double(80, 60));

		Tether t;
		
		try {
			t = new SimpleTether(anchor, 150, X);
		} catch (Exception e) {
			e.printStackTrace();
			
			return;
		}

		Robot robot;
		
		try {
			robot = new PointRobot(u, 0, Math.PI / 180, t);
		} catch (Exception e) {
			e.printStackTrace();
			
			return;
		}
		
		Point2D goal = new Point2D.Double(20, 20);
		
		this.result = PathPlanner.performPathPlanning(room, robot, goal, 1000);
	}
	
	@Test
	public void testActionsNotNull() {
		Assert.assertNotNull(this.result.actions);
	}
	
	@Test
	public void testActionsNonEmpty() {
		Assert.assertTrue(this.result.actions.size() > 0);
	}
	
}
