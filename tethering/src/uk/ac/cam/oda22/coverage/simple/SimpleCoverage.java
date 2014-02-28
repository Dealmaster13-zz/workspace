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

	private void findOptimalCoverageBFS(RoomCellIndex initialRobotCell, ShortestPathGrid shortestPathGrid) {
		RoomCellIndex currentRobotCell = new RoomCellIndex(initialRobotCell);
		
		// This stores the list of cells 
		List<RoomCellIndex> openList = new ArrayList<RoomCellIndex>();
	}

}
