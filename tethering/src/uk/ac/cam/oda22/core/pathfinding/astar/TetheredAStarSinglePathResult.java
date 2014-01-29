package uk.ac.cam.oda22.core.pathfinding.astar;

import uk.ac.cam.oda22.core.tethers.TetherConfiguration;
import uk.ac.cam.oda22.pathplanning.Path;

/**
 * @author Oliver
 *
 */
public class TetheredAStarSinglePathResult {

	public final Path path;
	
	public final TetherConfiguration tetherConfiguration;
	
	public TetheredAStarSinglePathResult(Path path, TetherConfiguration tetherConfiguration) {
		this.path = path;
		this.tetherConfiguration = tetherConfiguration;
	}
	
}
