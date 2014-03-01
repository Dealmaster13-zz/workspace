package uk.ac.cam.oda22.coverage.simple;

import java.util.ArrayList;
import java.util.List;

import uk.ac.cam.oda22.core.environment.Room;
import uk.ac.cam.oda22.core.environment.SimpleRoom;
import uk.ac.cam.oda22.core.logging.Log;
import uk.ac.cam.oda22.core.robots.Robot;
import uk.ac.cam.oda22.coverage.Coverage;
import uk.ac.cam.oda22.coverage.CoverageResult;
import uk.ac.cam.oda22.coverage.ShortestPathGrid;

/**
 * @author Oliver
 * 
 */
public class SimpleCoverage extends Coverage {

	@Override
	public CoverageResult performCoverage(Room room, Robot robot) {
		if (!(room instanceof SimpleRoom)) {
			Log.error("Coverage unimplemented for non-simple rooms");

			return null;
		}

		SimpleRoom sRoom = (SimpleRoom) room;

		// Get the robot coordinates and corresponding cell positions.
		double robotX = robot.getPosition().getX();
		double robotY = robot.getPosition().getY();
		int robotCellX = (int) Math.round(robotX / sRoom.cellSize);
		int robotCellY = (int) Math.round(robotY / sRoom.cellSize);
		RoomCellIndex robotCell = new RoomCellIndex(robotCellX, robotCellY);

		// Check if the robot is at the centre of a cell.
		if (robotX != robotCell.x * sRoom.cellSize
				|| robotY != robotCell.y * sRoom.cellSize) {
			Log.warning("Robot is not at the centre of a cell, so it will be repositioned.");
		}

		// Fail if the robot is outside of the room boundaries.
		if (robotCell.x < 0 || robotCell.x >= sRoom.horizontalCellCount
				|| robotCell.y < 0 || robotCell.y >= sRoom.verticalCellCount) {
			Log.error("Robot is outside of the room boundaries.");

			return null;
		}

		// Fail if the robot is on an obstacle.
		if (sRoom.obstacleCells[robotCell.y][robotCell.x]) {
			Log.error("Robot is on an obstacle.");

			return null;
		}

		/*
		 * Step 1: Compute the shortest path from the anchor point to every cell
		 * in the room, thus providing a discrete notion of 'saddle lines'.
		 */

		ShortestPathGrid shortestPathGrid = computeShortestPaths(sRoom, robot,
				sRoom.cellSize);

		return null;
	}

	private SimpleCoverageRouteNode findOptimalCoverageBFS(
			RoomCellIndex initialRobotCell, SimpleRoom room,
			ShortestPathGrid shortestPathGrid) {
		RoomCellIndex currentRobotCell = new RoomCellIndex(initialRobotCell);

		// This stores the list of nodes which are due to be explored.
		List<SimpleCoverageRouteNode> openList = new ArrayList<SimpleCoverageRouteNode>();

		// Add the robot's current cell to the open list.
		SimpleCoverageRouteNode startNode = new SimpleCoverageRouteNode(
				currentRobotCell, null);
		openList.add(startNode);

		// Get the number of cells which need to be covered.
		int totalCellCount = room.getOpenCellCount();

		while (openList.size() > 0) {
			// Get the next node in the open list with minimal re-covered cells.
			// This ensures that the first solution found will be the optimal
			// one.
			SimpleCoverageRouteNode n = extractMinNode(openList);

			// Stop if all cells have been covered and the node is at the start
			// point.
			if (n.getCoveredCellCount() == totalCellCount
					&& n.index.equals(initialRobotCell)) {
				return n;
			}

			// Get the valid cells adjacent to n.
			List<RoomCellIndex> cells = getAdjacentCellChoices(n.index, room);

			// For each valid adjacent cell, add a new node to the open list,
			// only if it doesn't re-cover a tile visited in the 're-covering'
			// phase. This prevents (useless) spurious solutions, since this
			// state can equally be achieved by the associated shorter path.
			for (RoomCellIndex cell : cells) {
				if (!n.isCellInRecoveringState(cell)) {
					// TODO: Check if a saddle line has been crossed.

					SimpleCoverageRouteNode newNode = new SimpleCoverageRouteNode(
							cell, n);

					openList.add(newNode);
				}
			}
		}
		
		// A solution was not found.
		return null;
	}

	public static List<RoomCellIndex> getAdjacentCellChoices(RoomCellIndex c,
			SimpleRoom room) {
		List<RoomCellIndex> l = new ArrayList<RoomCellIndex>();

		List<RoomCellIndex> adjacentCells = c.getAdjacentCells();

		// For each cell adjacent to c, check if it is a valid cell in open
		// space (not coincident with an obstacle).
		for (RoomCellIndex cell : adjacentCells) {
			if (cell.x >= 0 && cell.x < room.horizontalCellCount && cell.y >= 0
					&& cell.y < room.verticalCellCount) {
				if (!room.obstacleCells[c.y][c.x]) {
					l.add(cell);
				}
			}
		}

		return l;
	}

	public static SimpleCoverageRouteNode extractMinNode(
			List<SimpleCoverageRouteNode> openList) {
		SimpleCoverageRouteNode min = null;

		for (SimpleCoverageRouteNode n : openList) {
			if (min == null || n.compareTo(min) < 0) {
				min = n;
			}
		}

		openList.remove(min);

		return min;
	}
}
