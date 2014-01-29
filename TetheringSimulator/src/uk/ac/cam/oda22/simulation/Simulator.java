package uk.ac.cam.oda22.simulation;

import java.awt.Color;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import uk.ac.cam.oda22.core.ShapeFunctions;
import uk.ac.cam.oda22.core.environment.Obstacle;
import uk.ac.cam.oda22.core.environment.Room;
import uk.ac.cam.oda22.core.logging.Log;
import uk.ac.cam.oda22.core.robots.PointRobot;
import uk.ac.cam.oda22.core.robots.RectangularRobot;
import uk.ac.cam.oda22.core.robots.Robot;
import uk.ac.cam.oda22.core.tethers.SimpleTether;
import uk.ac.cam.oda22.core.tethers.Tether;
import uk.ac.cam.oda22.core.tethers.TetherConfiguration;
import uk.ac.cam.oda22.graphics.GraphicsFunctions;
import uk.ac.cam.oda22.graphics.IVisualiser;
import uk.ac.cam.oda22.graphics.VisualiserUsingJFrame;
import uk.ac.cam.oda22.graphics.shapes.Circle;
import uk.ac.cam.oda22.graphics.shapes.Line;
import uk.ac.cam.oda22.pathplanning.Path;
import uk.ac.cam.oda22.pathplanning.PathPlanner;
import uk.ac.cam.oda22.pathplanning.PathPlanningResult;

/**
 * @author Oliver
 * 
 */
public class Simulator {

	/**
	 * This number represents the number of segments the tether (at maximum
	 * length) is split up into for simulation.
	 */
	private static int tetherSegments;

	/**
	 * The visualiser.
	 */
	private static IVisualiser visualiser;

	/**
	 * @param args
	 * @throws InterruptedException 
	 * @throws Exception
	 */
	public static void main(String[] args) throws InterruptedException {
		// Create the visualiser.
		createVisualiser();

		Room room = createRoom1();

		Robot robot;

		try {
			Tether tether = createTether1_2(200);
			robot = createRobot1_2(tether);
		} catch (Exception e) {
			Log.error("Could not create robot with tether.");

			e.printStackTrace();

			return;
		}

		tetherSegments = 1000;

		Point2D goal = new Point2D.Double(30, 5);

		PathPlanningResult result = testPathPlanning(room, robot, goal);

		// Sleep for one second so that the visualiser has time to initialise.
		Thread.sleep(1000);
		
		// Draw the graphics.
		drawRoom(room, robot.radius);
		drawRobot(robot);
		drawGoal(goal);
		drawTether(robot.tether);
		drawAnchor(robot.tether.getAnchor());
		drawPath(result.tetheredPath.path);
	}

	private static Room createRoom1() {
		List<Obstacle> l = new ArrayList<Obstacle>();

		List<Point2D> points = new ArrayList<Point2D>();
		points.add(new Point2D.Double(35, 45));
		points.add(new Point2D.Double(60, 55));
		points.add(new Point2D.Double(50, 15));
		points.add(new Point2D.Double(10, 25));

		Obstacle o = new Obstacle(points);

		l.add(o);

		return new Room(100, 100, l);
	}

	/**
	 * This room has a concave obstacle.
	 * 
	 * @return
	 */
	@SuppressWarnings("unused")
	private static Room createRoom2() {
		List<Obstacle> l = new ArrayList<Obstacle>();

		List<Point2D> points = new ArrayList<Point2D>();
		points.add(new Point2D.Double(40, 30));
		points.add(new Point2D.Double(60, 55));
		points.add(new Point2D.Double(50, 15));
		points.add(new Point2D.Double(10, 25));

		Obstacle o = new Obstacle(points);

		l.add(o);

		return new Room(100, 100, l);
	}

	/**
	 * Tether for room 1 configuration type 1.
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	private static Tether createTether1_1(double maxTetherLength) throws Exception {
		Point2D anchor = new Point2D.Double(0, 0);

		TetherConfiguration X = new TetherConfiguration();
		X.addPoint(new Point2D.Double(50, 15));
		X.addPoint(new Point2D.Double(80, 60));

		return new SimpleTether(anchor, maxTetherLength, X);
	}

	/**
	 * Tether for room 1 configuration type 2.
	 * 
	 * @return
	 * @throws Exception
	 */
	private static Tether createTether1_2(double maxTetherLength) throws Exception {
		Point2D anchor = new Point2D.Double(0, 0);

		TetherConfiguration X = new TetherConfiguration();
		X.addPoint(new Point2D.Double(50, 15));
		X.addPoint(new Point2D.Double(60, 55));
		X.addPoint(new Point2D.Double(35, 45));
		X.addPoint(new Point2D.Double(10, 25));
		X.addPoint(new Point2D.Double(5, 10));

		return new SimpleTether(anchor, maxTetherLength, X);
	}

	/**
	 * Tether for room 1 configuration type 1.
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	private static Robot createRobot1_1(Tether tether) throws Exception {
		return new PointRobot(tether.getLastPoint(), 0, Math.PI / 180, tether);
	}

	/**
	 * Tether for room 1 configuration type 2.
	 * 
	 * @return
	 * @throws Exception
	 */
	private static Robot createRobot1_2(Tether tether) throws Exception {
		return new RectangularRobot(tether.getLastPoint(), 0, Math.PI / 180,
				tether, 4, 4);
	}

	private static void createVisualiser() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Point2D offset = new Point2D.Double(50, 120);
				double xScale = 4;
				double yScale = 4;

				visualiser = new VisualiserUsingJFrame(offset, xScale, yScale);
			}
		});
	}

	private static void drawRoom(Room room, double robotRadius) {
		for (Obstacle o : room.obstacles) {
			for (Line2D edge : o.edges) {
				Line line = new Line(edge, Color.black, 2);

				visualiser.drawLine(line);
			}
		}
		
		List<Obstacle> expandedObstacles = room.getExpandedObstacles(robotRadius);
		
		for (Obstacle o : expandedObstacles) {
			for (Line2D edge : o.edges) {
				Line line = new Line(edge, Color.yellow, 2);

				visualiser.drawLine(line);
			}
		}
	}

	private static void drawRobot(Robot robot) {
		for (Line2D l1 : robot.getOutline().lines) {
			Line2D l2 = ShapeFunctions.translateShape(l1, robot.getPosition()
					.getX(), robot.getPosition().getY());

			Line line = new Line(l2, Color.blue, 1);

			visualiser.drawLine(line);
		}
	}

	private static void drawGoal(Point2D goal) {
		Line2D[] l = ShapeFunctions.getCross(goal, 5);
		Line[] cross = GraphicsFunctions.colourLines(l, Color.green, 1);

		visualiser.drawLines(cross);
	}

	private static void drawTether(Tether tether) {
		if (tether instanceof SimpleTether) {
			SimpleTether t = (SimpleTether) tether;

			Point2D previousPoint = t.getAnchor();

			List<Point2D> points = t.getFixedPoints();

			for (int i = 0; i < points.size(); i++) {
				Line2D l = new Line2D.Double(previousPoint, points.get(i));

				Line line = new Line(l, Color.gray, 3);

				visualiser.drawLine(line);

				previousPoint = points.get(i);
			}
		} else {
			Log.error("Unknown tether type.");
		}
	}

	private static void drawAnchor(Point2D anchor) {
		Circle circle = new Circle(anchor, 2, Color.darkGray, 1);

		visualiser.drawCircle(circle);
	}

	private static void drawPath(Path path) {
		if (path.points.size() == 0) {
			return;
		}

		Point2D previousPoint = path.points.get(0);

		for (int i = 1; i < path.points.size(); i++) {
			Line2D l = new Line2D.Double(previousPoint, path.points.get(i));

			Line line = new Line(l, Color.red, 1);

			visualiser.drawLine(line);

			previousPoint = path.points.get(i);
		}
	}

	private static PathPlanningResult testPathPlanning(Room room, Robot robot,
			Point2D goal) {
		PathPlanningResult result = PathPlanner.performPathPlanning(room,
				robot, goal, tetherSegments);

		for (int i = 0; i < result.actions.size(); i++) {
			System.out.println(result.actions.get(i));
		}

		return result;
	}

}
