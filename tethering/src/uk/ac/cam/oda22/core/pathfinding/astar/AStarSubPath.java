package uk.ac.cam.oda22.core.pathfinding.astar;

import java.util.List;

import uk.ac.cam.oda22.core.tethers.TetherConfiguration;

/**
 * @author Oliver
 *
 */
public class AStarSubPath {

	/**
	 * The node's list of predecessors for this sub-path.
	 * The predecessor list is stored because each node can have multiple shortest sub-paths.
	 */
	public final List<AStarNode> predecessorList;
	
	/**
	 * The tether configuration used to get to form this sub-path.
	 */
	public final TetherConfiguration tc;
	
	public AStarSubPath(List<AStarNode> predecessorList, TetherConfiguration tc) {
		this.predecessorList = predecessorList;
		this.tc = tc;
	}
	
}
