package uk.ac.cam.oda22.core.astar;

import java.util.LinkedList;
import java.util.List;

import uk.ac.cam.oda22.pathplanning.Path;

/**
 * @author Oliver
 *
 */
public class AStarPathfinding {

	/**
	 * Performs A* pathfinding from the source to the destination, and 
	 * returns whether or not a path was found.
	 * 
	 * @param source
	 * @param destination
	 * @param nodes
	 * @return
	 */
	public static boolean getShortestPath(AStarNode source, AStarNode destination, List<AStarNode> nodes) {
		//Initialise the nodes.
		for (AStarNode node : nodes) {
			node.predecessor = null;
			node.discovered = false;
			node.f = Double.POSITIVE_INFINITY;
			node.g = node.distance(destination.p);
		}

		// Set the source's f cost to 0.
		source.f = 0;

		// Create a queue of the nodes to check neighbours.
		List<AStarNode> q = new LinkedList<AStarNode>();
		q.add(source);

		boolean optimalPathFound = false;

		while (!q.isEmpty() && !optimalPathFound) {
			// Get the lowest cost node and remove it from the queue.
			AStarNode current = getLowestCostNode(q);
			q.remove(current);

			// Stop if a better path cannot be found.
			if (current.f + current.g >= destination.f + destination.g) {
				optimalPathFound = true;
			}
			else
			{
				for (AStarEdge edge : current.edges) {
					AStarNode neighbour = (current == edge.p) ? edge.q : edge.p;
					
					// Relax the edge.
					if (current.f + edge.cost < neighbour.f) {
						neighbour.predecessor = current;
						
						neighbour.f = current.f + edge.cost;
					}
					
					// Add the neighbour to the queue if it hasn't been discovered yet.
					if (!neighbour.discovered) {
						q.add(neighbour);
						
						neighbour.discovered = true;
					}
				}
			}
		}

		return destination.predecessor != null;
	}

	public static AStarNode getLowestCostNode(List<AStarNode> nodes) {
		AStarNode lowestCostNode = null;
		double lowestH = Double.POSITIVE_INFINITY;

		for (AStarNode node : nodes) {
			if (node.f + node.g < lowestH) {
				lowestCostNode = node;

				lowestH = node.f + node.g;
			}
		}

		return lowestCostNode;
	}
	
	public static Path retrievePath(AStarNode destination) {
		Path path = new Path();
		
		List<AStarNode> reversePath = new LinkedList<AStarNode>();
		
		AStarNode currentNode = destination;
		
		// Retrieve the reverse path.
		while (currentNode != null) {
			reversePath.add(currentNode);
			
			currentNode = currentNode.predecessor;
		}
		
		// Reassemble the forward path.
		for (int i = reversePath.size() - 1; i >= 0; i--) {
			path.addPoint(reversePath.get(i).p);
		}
		
		return path;
	}

}
