package uk.ac.cam.oda22.simulation;

import java.awt.Color;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import uk.ac.cam.oda22.core.ShapeFunctions;
import uk.ac.cam.oda22.core.environment.Obstacle;
import uk.ac.cam.oda22.core.environment.PolygonRoom;
import uk.ac.cam.oda22.core.environment.VisibilityGraph;
import uk.ac.cam.oda22.core.environment.VisibilityGraphEdge;
import uk.ac.cam.oda22.core.logging.Log;
import uk.ac.cam.oda22.core.pathfinding.astar.TetheredAStarPathfinding;
import uk.ac.cam.oda22.core.robots.PointRobot;
import uk.ac.cam.oda22.core.robots.RectangularRobot;
import uk.ac.cam.oda22.core.robots.Robot;
import uk.ac.cam.oda22.core.tethers.SimpleTether;
import uk.ac.cam.oda22.core.tethers.Tether;
import uk.ac.cam.oda22.core.tethers.TetherConfiguration;
import uk.ac.cam.oda22.coverage.CoverageResult;
import uk.ac.cam.oda22.coverage.ShortestPathGrid;
import uk.ac.cam.oda22.coverage.ShortestPathGridCell;
import uk.ac.cam.oda22.coverage.simple.SimpleCoverage;
import uk.ac.cam.oda22.graphics.GraphicsFunctions;
import uk.ac.cam.oda22.graphics.IVisualiser;
import uk.ac.cam.oda22.graphics.VisualiserUsingJFrame;
import uk.ac.cam.oda22.graphics.shapes.Circle;
import uk.ac.cam.oda22.graphics.shapes.DisplayShape;
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

		PolygonRoom room = createRoom1();

		Robot robot;
		Tether tether;

		try {
			double robotRadius = 4;
			tether = createTether1_1(120);
			robot = createRobot1_2(tether, robotRadius);
		} catch (Exception e) {
			Log.error("Could not create robot with tether.");

			e.printStackTrace();

			return;
		}

		tetherSegments = 1000;

		Point2D goal = new Point2D.Double(5, 25);

		PathPlanningResult result = testPathPlanning(room, robot, goal);
		// CoverageResult result = testCoverage(room, robot);

		boolean pathFound = result != null && result.actions != null;

		// ShortestPathGrid shortestPathGrid =
		// Coverage.computeShortestPaths(room, robot);

		// Sleep for 100ms so that the visualiser has time to initialise.
		Thread.sleep(100);

		drawTether(tether, Color.darkGray, false);
		
		// Draw the graphics.
		drawRoom(room, robot.radius, true, false, false);
		drawRobot(robot);
		drawGoal(goal, pathFound);
		drawTether(robot.tether, Color.cyan, true);
		drawAnchor(robot.tether.getAnchor());
		// drawShortestPathGrid(shortestPathGrid);
		if (pathFound)
			drawPath(result.tetheredPath.path);
		if (pathFound)
			drawTetherConfiguration(result.tetheredPath.tc, Color.magenta);

		if (pathFound)
			verifyTCFromPath(tether, room.obstacles, robot.radius, result);
	}

	private static PolygonRoom createRoom1() {
		List<Obstacle> l = new ArrayList<Obstacle>();

		List<Point2D> points = new ArrayList<Point2D>();
		points.add(new Point2D.Double(35, 45));
		points.add(new Point2D.Double(60, 55));
		points.add(new Point2D.Double(50, 15));
		points.add(new Point2D.Double(10, 25));

		Obstacle o = new Obstacle(points);

		l.add(o);

		return new PolygonRoom(100, 100, l);
	}

	/**
	 * This room has a concave obstacle.
	 * 
	 * @return
	 */
	private static PolygonRoom createRoom2() {
		List<Obstacle> l = new ArrayList<Obstacle>();

		List<Point2D> points = new ArrayList<Point2D>();
		points.add(new Point2D.Double(40, 30));
		points.add(new Point2D.Double(60, 55));
		points.add(new Point2D.Double(50, 15));
		points.add(new Point2D.Double(10, 25));

		Obstacle o = new Obstacle(points);

		l.add(o);

		return new PolygonRoom(100, 100, l);
	}

	/**
	 * This room has a convex and concave obstacle.
	 * 
	 * @return
	 */
	private static PolygonRoom createRoom3() {
		List<Obstacle> l = new ArrayList<Obstacle>();

		List<Point2D> points1 = new ArrayList<Point2D>();
		points1.add(new Point2D.Double(30, 30));
		points1.add(new Point2D.Double(46, 47));
		points1.add(new Point2D.Double(40, 15));
		points1.add(new Point2D.Double(10, 25));

		l.add(new Obstacle(points1));

		List<Point2D> points2 = new ArrayList<Point2D>();
		points2.add(new Point2D.Double(60, 70));
		points2.add(new Point2D.Double(80, 70));
		points2.add(new Point2D.Double(68, 86));

		l.add(new Obstacle(points2));

		return new PolygonRoom(100, 100, l);
	}

	/**
	 * This room has two convex obstacles, roughly horizontally aligned.
	 * 
	 * @return
	 */
	private static PolygonRoom createRoom4() {
		List<Obstacle> l = new ArrayList<Obstacle>();

		List<Point2D> points1 = new ArrayList<Point2D>();
		points1.add(new Point2D.Double(20, 15));
		points1.add(new Point2D.Double(45, 20));
		points1.add(new Point2D.Double(40, 40));
		points1.add(new Point2D.Double(20, 35));

		l.add(new Obstacle(points1));

		List<Point2D> points2 = new ArrayList<Point2D>();
		points2.add(new Point2D.Double(60, 10));
		points2.add(new Point2D.Double(80, 10));
		points2.add(new Point2D.Double(80, 50));
		points2.add(new Point2D.Double(60, 40));

		l.add(new Obstacle(points2));

		return new PolygonRoom(100, 100, l);
	}

	/**
	 * This room has two rectangular obstacles, horizontally aligned.
	 * 
	 * @return
	 */
	private static PolygonRoom createRoom5() {
		List<Obstacle> l = new ArrayList<Obstacle>();

		List<Point2D> points1 = new ArrayList<Point2D>();
		points1.add(new Point2D.Double(10, 20));
		points1.add(new Point2D.Double(45, 20));
		points1.add(new Point2D.Double(45, 40));
		points1.add(new Point2D.Double(10, 40));

		l.add(new Obstacle(points1));

		List<Point2D> points2 = new ArrayList<Point2D>();
		points2.add(new Point2D.Double(55, 20));
		points2.add(new Point2D.Double(90, 20));
		points2.add(new Point2D.Double(90, 40));
		points2.add(new Point2D.Double(55, 40));

		l.add(new Obstacle(points2));

		return new PolygonRoom(100, 100, l);
	}

	/**
	 * Tether configuration type 1 for any room. This configuration is for
	 * coverage.
	 * 
	 * @return
	 * @throws Exception
	 */
	private static Tether createTether0_1(double maxTetherLength,
			double robotRadius) throws Exception {
		Point2D anchor = new Point2D.Double(50, 0);

		TetherConfiguration X = new TetherConfiguration();
		X.addPoint(new Point2D.Double(50, robotRadius));

		return new SimpleTether(anchor, maxTetherLength, X);
	}

	/**
	 * Tether for room 1 configuration type 1. This has a simple configuration.
	 * 
	 * @return
	 * @throws Exception
	 */
	private static Tether createTether1_1(double maxTetherLength)
			throws Exception {
		Point2D anchor = new Point2D.Double(0, 0);

		TetherConfiguration X = new TetherConfiguration();
		X.addPoint(new Point2D.Double(50, 15));
		X.addPoint(new Point2D.Double(80, 60));

		return new SimpleTether(anchor, maxTetherLength, X);
	}

	/**
	 * Tether for room 1 configuration type 2. This configuration wraps around
	 * the obstacle tightly.
	 * 
	 * @return
	 * @throws Exception
	 */
	private static Tether createTether1_2(double maxTetherLength)
			throws Exception {
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
	 * Tether for room 1 configuration type 3. This configuration is non-taut.
	 * 
	 * @return
	 * @throws Exception
	 */
	private static Tether createTether1_3(double maxTetherLength)
			throws Exception {
		Point2D anchor = new Point2D.Double(0, 0);

		TetherConfiguration X = new TetherConfiguration();
		X.addPoint(new Point2D.Double(90, 20));
		X.addPoint(new Point2D.Double(80, 70));
		X.addPoint(new Point2D.Double(40, 55));
		X.addPoint(new Point2D.Double(5, 60));
		X.addPoint(new Point2D.Double(5, 15));

		return new SimpleTether(anchor, maxTetherLength, X);
	}

	/**
	 * Tether for room 3 configuration type 1. This configuration is non-taut.
	 * This tether crosses itself when tightened.
	 * 
	 * @return
	 * @throws Exception
	 */
	private static Tether createTether3_1(double maxTetherLength)
			throws Exception {
		Point2D anchor = new Point2D.Double(0, 0);

		TetherConfiguration X = new TetherConfiguration();
		X.addPoint(new Point2D.Double(10, 50));
		X.addPoint(new Point2D.Double(35, 70));
		X.addPoint(new Point2D.Double(85, 10));
		X.addPoint(new Point2D.Double(90, 95));
		X.addPoint(new Point2D.Double(40, 85));
		X.addPoint(new Point2D.Double(65, 55));

		return new SimpleTether(anchor, maxTetherLength, X);
	}

	/**
	 * Tether for room 3 configuration type 2. This configuration is non-taut.
	 * 
	 * @return
	 * @throws Exception
	 */
	private static Tether createTether3_2(double maxTetherLength)
			throws Exception {
		Point2D anchor = new Point2D.Double(0, 0);

		TetherConfiguration X = new TetherConfiguration();
		X.addPoint(new Point2D.Double(10, 50));
		X.addPoint(new Point2D.Double(35, 70));
		X.addPoint(new Point2D.Double(85, 10));
		X.addPoint(new Point2D.Double(90, 95));
		X.addPoint(new Point2D.Double(40, 85));
		X.addPoint(new Point2D.Double(55, 63));

		return new SimpleTether(anchor, maxTetherLength, X);
	}

	/**
	 * Tether for room 3 configuration type 3. This configuration is non-taut.
	 * 
	 * @return
	 * @throws Exception
	 */
	private static Tether createTether3_3(double maxTetherLength)
			throws Exception {
		Point2D anchor = new Point2D.Double(0, 0);

		TetherConfiguration X = new TetherConfiguration();
		X.addPoint(new Point2D.Double(10, 50));
		X.addPoint(new Point2D.Double(35, 50));

		return new SimpleTether(anchor, maxTetherLength, X);
	}

	/**
	 * Tether for room 1 configuration type 1. This is a point robot.
	 * 
	 * @return
	 * @throws Exception
	 */
	private static Robot createRobot1_1(Tether tether) throws Exception {
		return new PointRobot(tether.getLastPoint(), 0, Math.PI / 180, tether);
	}

	/**
	 * Tether for room 1 configuration type 2. This is a rectangular robot.
	 * 
	 * @return
	 * @throws Exception
	 */
	private static Robot createRobot1_2(Tether tether, double radius)
			throws Exception {
		double sideLength = radius * Math.sqrt(2);

		return new RectangularRobot(tether.getLastPoint(), 0, Math.PI / 180,
				tether, sideLength, sideLength);
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

	private static void drawRoom(PolygonRoom room, double robotRadius,
			boolean drawExpandedObstacles, boolean drawVisibilityGraph,
			boolean drawExpandedVisibilityGraph) {
		for (Obstacle o : room.obstacles) {
			drawLines(o.edges, Color.black, 8);
		}

		if (drawVisibilityGraph) {
			// Draw the visibility graph.
			VisibilityGraph g = new VisibilityGraph(room.obstacles);

			drawVisibilityGraph(g);
		}

		if (drawExpandedObstacles || drawExpandedVisibilityGraph) {
			List<Obstacle> expandedObstacles = room
					.getExpandedObstacles(robotRadius);

			if (drawExpandedObstacles) {
				// Draw the expanded obstacles.
				for (Obstacle o : expandedObstacles) {
					drawLines(o.edges, Color.orange, 2);
				}
			}

			if (drawExpandedVisibilityGraph) {
				// Draw the expanded visibility graph.
				VisibilityGraph g = new VisibilityGraph(expandedObstacles);

				drawVisibilityGraph(g);
			}
		}
	}

	private static void drawRobot(Robot robot) {
		for (Line2D l1 : robot.getOutline().lines) {
			Line2D l2 = ShapeFunctions.translateShape(l1, robot.getPosition()
					.getX(), robot.getPosition().getY());

			Line line = new Line(l2, Color.blue, 1);

			visualiser.drawShape(line);
		}
	}

	private static void drawGoal(Point2D goal, boolean pathFound) {
		Color colour = pathFound ? Color.green : Color.red;

		Line2D[] l = ShapeFunctions.getCross(goal, 2);
		Line[] cross = GraphicsFunctions.colourLines(l, colour, 3);

		visualiser.drawShapes(cross);
	}

	private static void drawTether(Tether tether, Color colour, boolean dashed) {
		float thickness = 8;

		if (tether instanceof SimpleTether) {
			SimpleTether t = (SimpleTether) tether;

			Point2D previousPoint = t.getAnchor();

			List<Point2D> points = t.getFixedPoints();

			for (int i = 0; i < points.size(); i++) {
				Line2D l = new Line2D.Double(previousPoint, points.get(i));

				Line line = dashed ? new Line(l, colour,
						DisplayShape.getDashedStroke(thickness)) : new Line(l,
						Color.gray, thickness);

				visualiser.drawShape(line);

				previousPoint = points.get(i);
			}
		} else {
			Log.error("Unknown tether type.");
		}
	}

	private static void drawTetherConfiguration(TetherConfiguration tc,
			Color colour) {
		for (int i = 0; i < tc.points.size() - 1; i++) {
			Line2D l = new Line2D.Double(tc.points.get(i), tc.points.get(i + 1));

			Line line = new Line(l, colour, DisplayShape.getDashedStroke(8));

			visualiser.drawShape(line);
		}
	}

	private static void drawAnchor(Point2D anchor) {
		Circle circle = new Circle(anchor, 1, Color.darkGray, 3);

		visualiser.drawShape(circle);
	}

	private static void drawPath(Path path) {
		if (path.points.size() == 0) {
			return;
		}

		Point2D previousPoint = path.points.get(0);

		for (int i = 1; i < path.points.size(); i++) {
			Line2D l = new Line2D.Double(previousPoint, path.points.get(i));

			Line line = new Line(l, Color.red, 2);

			visualiser.drawShape(line);

			previousPoint = path.points.get(i);
		}
	}

	private static void drawShortestPathGrid(ShortestPathGrid g) {
		for (ShortestPathGridCell[] cs : g.cells) {
			for (ShortestPathGridCell c : cs) {
				Point2D p = new Point2D.Double(c.x, c.y);

				// Compute the colour which should be used for the grid cell.
				double f = c.potentialValue / 140;
				int rValue = (int) Math.min(f * 255, 255);
				Color colour = new Color(rValue, 255 - rValue, 0);

				Circle circle = new Circle(p, 0.2, colour, 2);

				visualiser.drawShape(circle);
			}
		}
	}

	private static void drawVisibilityGraph(VisibilityGraph g) {
		for (VisibilityGraphEdge edge : g.edges) {
			Line line = new Line(edge.getLine(), Color.yellow, 1);

			visualiser.drawShape(line);
		}
	}

	private static void drawLines(List<Line2D> lines, Color colour,
			int thickness) {
		for (Line2D l : lines) {
			Line line = new Line(l, colour, thickness);

			visualiser.drawShape(line);
		}
	}

	private static PathPlanningResult testPathPlanning(PolygonRoom room, Robot robot,
			Point2D goal) {
		PathPlanningResult result = PathPlanner.performPathPlanning(room,
				robot, goal, tetherSegments);

		if (result != null && result.actions != null) {
			for (int i = 0; i < result.actions.size(); i++) {
				System.out.println(result.actions.get(i));
			}
		}

		return result;
	}

	private static CoverageResult testCoverage(PolygonRoom room, Robot robot) {
		SimpleCoverage coverage = new SimpleCoverage();
		
		CoverageResult result = coverage.performCoverage(room, robot);

		if (result != null) {
			for (int i = 0; i < result.actions.size(); i++) {
				System.out.println(result.actions.get(i));
			}
		}

		return result;
	}

	private static void verifyTCFromPath(Tether initialTether,
			List<Obstacle> obstacles, double robotRadius,
			PathPlanningResult result) {
		TetherConfiguration tc = new TetherConfiguration(
				initialTether.getFullConfiguration());

		List<Point2D> ps = result.tetheredPath.path.points;

		// Starting at the second point in the path, move along the path,
		// updating the tether configuration sequentially.
		for (int i = 1; i < ps.size(); i++) {
			tc = TetheredAStarPathfinding.computeTetherChange(tc,
					initialTether.length, ps.get(i), obstacles, robotRadius);

			if (tc == null) {
				Log.error("Tether is malformed.");

				return;
			}
		}

		if (!result.tetheredPath.tc.equals(tc)) {
			Log.error("Tether configuration mismatch.");
		}
	}

}
