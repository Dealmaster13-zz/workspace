package uk.ac.cam.oda22.core.pathfinding.astar;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import uk.ac.cam.oda22.core.environment.Obstacle;
import uk.ac.cam.oda22.core.environment.VisibilityGraph;
import uk.ac.cam.oda22.core.environment.VisibilityGraphEdge;
import uk.ac.cam.oda22.core.environment.VisibilityGraphNode;
import uk.ac.cam.oda22.core.graphs.Node;
import uk.ac.cam.oda22.core.logging.Log;

/**
 * @author Oliver
 *
 */
public class AStarGraph {

	public final List<AStarNode> nodes;

	public final List<AStarEdge> edges;

	public final List<Obstacle> obstacles;

	public AStarGraph(List<AStarNode> nodes, List<AStarEdge> edges, List<Obstacle> obstacles) {
		this.nodes = nodes;
		this.edges = edges;
		this.obstacles = obstacles;
	}

	public AStarGraph(VisibilityGraph g) {
		this.nodes = new ArrayList<AStarNode>();
		this.edges = new ArrayList<AStarEdge>();
		this.obstacles = g.obstacles;

		// Create all of the A* nodes.
		for (VisibilityGraphNode node : g.nodes) {
			AStarNode aStarNode = new AStarNode(node.p);
			this.nodes.add(aStarNode);
		}

		// Create all of the A* edges.
		for (VisibilityGraphEdge edge : g.edges) {
			AStarNode aStarNode1 = this.getNode(edge.startNode);
			AStarNode aStarNode2 = this.getNode(edge.endNode);

			if (aStarNode1 == null || aStarNode2 == null) {
				Log.error("Nodes not found.");
			}
			else {
				double edgeCost = aStarNode1.distance(aStarNode2.p);

				AStarEdge aStarEdge = new AStarEdge(aStarNode1, aStarNode2, edgeCost);
				this.edges.add(aStarEdge);
			}
		}
	}

	/**
	 * Gets the node at a given point.
	 * 
	 * @param p
	 * @return node if exists at point p, null otherwise
	 */
	public AStarNode getNode(Point2D p) {
		// Check if any node has the same point p.
		for (AStarNode n : this.nodes) {
			if (n.p.equals(p)) {
				return n;
			}
		}

		return null;
	}

	/**
	 * Gets the node at a given node.
	 * 
	 * @param p
	 * @return node if exists at node n, null otherwise
	 */
	public AStarNode getNode(Node n) {
		return this.getNode(n.p);
	}

}
