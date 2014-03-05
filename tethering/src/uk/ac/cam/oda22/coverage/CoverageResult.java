package uk.ac.cam.oda22.coverage;

import java.util.List;

import uk.ac.cam.oda22.core.Result;
import uk.ac.cam.oda22.core.robots.actions.IRobotAction;
import uk.ac.cam.oda22.pathplanning.Path;

/**
 * @author Oliver
 * 
 */
public class CoverageResult extends Result {

	public final Path path;

	public CoverageResult(List<IRobotAction> actions, Path path) {
		super(actions);
		
		this.path = path;
	}

}
