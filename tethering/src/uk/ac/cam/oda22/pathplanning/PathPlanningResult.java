package uk.ac.cam.oda22.pathplanning;

import java.util.List;

import uk.ac.cam.oda22.core.robots.actions.IRobotAction;

/**
 * @author Oliver
 * 
 */
public class PathPlanningResult {

	public final List<IRobotAction> actions;

	public final TetheredPath tetheredPath;

	public PathPlanningResult(List<IRobotAction> actions, TetheredPath path) {
		this.actions = actions;
		this.tetheredPath = path;
	}

}
