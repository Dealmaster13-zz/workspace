package uk.ac.cam.oda22.coverage.simple;

/**
 * @author Oliver
 * 
 */
public class SimpleCoverageRouteNode {

	/**
	 * The room cell index of the node.
	 */
	public final RoomCellIndex index;

	/**
	 * The node which comes before this node.
	 */
	public final SimpleCoverageRouteNode previousNode;

	/**
	 * The re-coverage count which corresponds to the previous node list.
	 */
	public final int recoverageCount;

	/**
	 * The status of whether or not the robot is re-covering tiles at this
	 * stage.
	 */
	public final boolean recovering;

	public SimpleCoverageRouteNode(RoomCellIndex index,
			SimpleCoverageRouteNode previousNode) {
		this.index = index;
		this.previousNode = previousNode;

		// Get the re-coverage count of the previous node.
		int previousRecoverageCount = previousNode != null ? previousNode.recoverageCount
				: 0;

		// Check if this node has already been visited.
		this.recovering = isRevisited(this.index, this.previousNode);

		// Increment the recoverage count if the node has been revisited.
		this.recoverageCount = previousRecoverageCount
				+ (this.recovering ? 1 : 0);
	}

	private static boolean isRevisited(RoomCellIndex index,
			SimpleCoverageRouteNode previousNode) {
		SimpleCoverageRouteNode n = previousNode;

		while (n != null) {
			if (n.index.equals(index)) {
				return true;
			}
		}

		return false;
	}

}
