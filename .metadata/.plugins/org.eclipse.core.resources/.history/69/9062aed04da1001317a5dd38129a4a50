package uk.ac.cam.oda22.coverage.simple;

/**
 * @author Oliver
 * 
 *         A priority queue implementation for simple coverage route nodes.
 */
public class SCRNPriorityQueue {

	private int size;

	private SimpleCoverageRouteNode[] nodes;

	private int elementCount;

	public SCRNPriorityQueue() {
		this.size = 10;
		this.nodes = new SimpleCoverageRouteNode[10];
		this.elementCount = 0;
	}

	/**
	 * Insert a new node n using binary search.
	 * 
	 * @param n
	 */
	public void insert(SimpleCoverageRouteNode n) {
		int l = 0;
		int r = this.elementCount - 1;
		
		int m = 0;
		boolean matchFound = false;
		
		// Perform binary search to find the appropriate insertion position.
		while (l <= r && !matchFound) {
			m = (r + l) / 2;
			
			int c = n.compareTo(this.nodes[m]);
			
			if (c > 0) {
				l = m + 1;
			}
			else if (c < 0) {
				r = m - 1;
			}
			else {
				matchFound = true;
			}
		}
		
		// TODO: Insert the node at position m.
		
		this.elementCount++;
	}
}
