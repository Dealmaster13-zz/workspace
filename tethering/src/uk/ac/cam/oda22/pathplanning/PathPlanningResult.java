package uk.ac.cam.oda22.pathplanning;

import java.util.List;

import uk.ac.cam.oda22.core.Result;
import uk.ac.cam.oda22.core.robots.actions.IRobotAction;

/**
 * @author Oliver
 * 
 */
public class PathPlanningResult extends Result {

	public final TetheredPath tetheredPath;

	public PathPlanningResult(List<IRobotAction> actions, TetheredPath path) {
		super(actions);

		this.tetheredPath = path;
	}

}
