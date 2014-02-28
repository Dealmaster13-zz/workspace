package uk.ac.cam.oda22.coverage.sweeping;

import uk.ac.cam.oda22.core.environment.Obstacle;

/**
 * @author Oliver
 * 
 */
public class SweepToObstacleResult {

	public final double maxRadsToObstacle;

	public final Obstacle nearestObstacle;

	/**
	 * @param maxRadsToObstacle
	 * @param nearestObstacle
	 */
	public SweepToObstacleResult(double maxRadsToObstacle,
			Obstacle nearestObstacle) {
		this.maxRadsToObstacle = maxRadsToObstacle;
		this.nearestObstacle = nearestObstacle;
	}

}
