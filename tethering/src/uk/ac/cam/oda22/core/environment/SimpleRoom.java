package uk.ac.cam.oda22.core.environment;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import uk.ac.cam.oda22.core.logging.Log;

/**
 * @author Oliver
 * 
 */
public class SimpleRoom extends Room {

	public final int horizontalCellCount;

	public final int verticalCellCount;

	public final boolean[][] obstacleCells;

	public final double cellSize;

	public SimpleRoom(int horizontalCellCount, int verticalCellCount,
			boolean[][] obstacleCells, double cellSize) {
		super(horizontalCellCount * cellSize, verticalCellCount * cellSize,
				new ArrayList<Obstacle>());

		// Assert that the cell counts and obstacle array dimensions match.
		if (obstacleCells.length != verticalCellCount
				|| obstacleCells[0].length != horizontalCellCount) {
			Log.error("Mismatching cell counts and obstacle array dimensions.");
		}

		this.horizontalCellCount = horizontalCellCount;
		this.verticalCellCount = verticalCellCount;
		this.obstacleCells = obstacleCells;
		this.cellSize = cellSize;

		this.generateObstacles(obstacleCells, cellSize);
	}

	/**
	 * Gets the number of cells not covered by an obstacle.
	 * 
	 * @return open cell count
	 */
	public int getOpenCellCount() {
		return (horizontalCellCount * verticalCellCount)
				- this.getObstacleCellCount();
	}

	/**
	 * Gets the number of cells covered by an obstacle.
	 * 
	 * @return obstacle cell count
	 */
	public int getObstacleCellCount() {
		int count = 0;

		for (int i = 0; i < obstacleCells.length; i++) {
			for (int j = 0; j < obstacleCells[i].length; j++) {
				if (obstacleCells[i][j]) {
					count++;
				}
			}
		}

		return count;
	}

	/**
	 * Creates cell-sized obstacles and adds them to the obstacle list.
	 * 
	 * @param obstacleCells
	 */
	private void generateObstacles(boolean[][] obstacleCells, double cellSize) {
		for (int i = 0; i < obstacleCells.length; i++) {
			for (int j = 0; j < obstacleCells[i].length; j++) {
				List<Point2D> points = new ArrayList<Point2D>();

				// Create an obstacle spanning from index (j, i) to index (j+1,
				// i+1).
				points.add(new Point2D.Double(j * cellSize, i * cellSize));
				points.add(new Point2D.Double(j * cellSize, (i + 1) * cellSize));
				points.add(new Point2D.Double((j + 1) * cellSize, i * cellSize));
				points.add(new Point2D.Double((j + 1) * cellSize, (i + 1)
						* cellSize));

				this.obstacles.add(new Obstacle(points));
			}
		}
	}

}
