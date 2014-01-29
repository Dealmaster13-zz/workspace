package uk.ac.cam.oda22.pathplanning;

import uk.ac.cam.oda22.core.tethers.TetherConfiguration;

/**
 * @author Oliver
 *
 */
public class TetheredPath {

	public final Path path;
	
	public final TetherConfiguration tc;
	
	public TetheredPath(Path path, TetherConfiguration tc) {
		this.path = path;
		this.tc = tc;
	}
	
}
