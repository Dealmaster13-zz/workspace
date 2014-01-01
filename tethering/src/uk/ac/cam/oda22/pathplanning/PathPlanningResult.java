package uk.ac.cam.oda22.pathplanning;

import java.util.List;

import uk.ac.cam.oda22.core.robots.actions.IRobotAction;

/**
 * @author Oliver
 *
 */
public class PathPlanningResult {

	public final List<IRobotAction> actions;
	
	public final Path path;
	
	public PathPlanningResult(List<IRobotAction> actions, Path path) {
		this.actions = actions;
		this.path = path;
	}
	
}
