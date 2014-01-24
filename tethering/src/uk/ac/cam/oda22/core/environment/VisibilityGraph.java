package uk.ac.cam.oda22.core.environment;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import uk.ac.cam.oda22.core.logging.Log;

/**
 * @author Oliver
 *
 */
public class VisibilityGraph {

	public final List<VisibilityGraphNode> nodes;

	public final List<VisibilityGraphEdge> edges;

	public final List<Obstacle> obstacles;

	public VisibilityGraph() {
		this.nodes = new ArrayList<VisibilityGraphNode>();
		this.edges = new ArrayList<VisibilityGraphEdge>();
		this.obstacles = new ArrayList<Obstacle>();
	}

	public VisibilityGraph(VisibilityGraph g) {
		this.nodes = new ArrayList<VisibilityGraphNode>();
		this.edges = new ArrayList<VisibilityGraphEdge>();
		this.obstacles = new ArrayList<Obstacle>();

		// Ensure that the new edges use the new nodes by storing the appropriate node mapping.
		Hashtable<VisibilityGraphNode, VisibilityGraphNode> nodeMapping = new Hashtable<VisibilityGraphNode, VisibilityGraphNode>();

		for (VisibilityGraphNode node : g.nodes) {
			VisibilityGraphNode newNode = new VisibilityGraphNode(node);

			this.nodes.add(newNode);

			nodeMapping.put(node, newNode);
		}

		for (VisibilityGraphEdge edge : g.edges) {
			VisibilityGraphNode startNode = nodeMapping.get(edge.startNode);
			VisibilityGraphNode endNode = nodeMapping.get(edge.endNode);

			this.edges.add(new VisibilityGraphEdge(startNode, endNode, edge.weight, edge.isObstacleEdge));
		}

		for (Obstacle o : g.obstacles) {
			this.obstacles.add(o);
		}
	}

	/**
	 * Gets the list of nodes visible to a particular point.
	 * This should not be used if point p exists in the visibility graph as a node.
	 * 
	 * @param p
	 * @return nodes visible to p
	 */
	public List<VisibilityGraphNode> getVisibleNodes(Point2D p) {
		List<VisibilityGraphNode> l = new ArrayList<VisibilityGraphNode>();

		for (VisibilityGraphNode node : this.nodes) {
			// Calculate the visibility between nodes p and 'node'.
			NodeVisibility nodeVisibility = this.getVisibility(p, node.p);

			// Add the node if it is at least partly visible.
			if (nodeVisibility.isPartlyVisible()) {
				l.add(node);
			}
		}

		return l;
	}

	/**
	 * Gets the list of nodes visible to a particular node.
	 * 
	 * @param node
	 * @return nodes visible to 'node'
	 */
	public List<VisibilityGraphNode> getVisibleNodes(VisibilityGraphNode node) {
		List<VisibilityGraphNode> l = new ArrayList<VisibilityGraphNode>();

		for (VisibilityGraphEdge e : this.edges) {
			if (e.startNode == node) {
				l.add(e.endNode);
			}
			else if (e.endNode == node) {
				l.add(e.startNode);
			}
		}

		return l;
	}

	/**
	 * Adds a node to the visibility graph at the given point.
	 * This is typically only used to add a start or goal node.
	 * 
	 * @param p
	 * @return node if added, null otherwise
	 */
	public VisibilityGraphNode addNode(Point2D p) {
		VisibilityGraphNode node = new VisibilityGraphNode(p);

		// Check if there is already a node at point p.
		VisibilityGraphNode existingNode = getNode(p);
		
		// If a node does not already exist at point p then add the node.
		if (existingNode == null) {
			this.nodes.add(node);

			for (VisibilityGraphNode n : this.nodes) {
				this.tryAddEdge(node, n);
			}

			return node;
		}

		// Return null if the 
		return existingNode;
	}

	/**
	 * Adds an obstacle and the relevant points and edges.
	 * 
	 * @param obstacle
	 * @param addPointsAndEdges
	 * @return true if the obstacle was added, false otherwise
	 */
	public boolean addObstacle(Obstacle obstacle, boolean addPointsAndEdges) {
		// Check if the obstacle intersects with any existing obstacles.
		for (Obstacle o : this.obstacles) {
			if (o.strictIntersects(obstacle)) {
				Log.warning("Added obstacle intersects with an existing obstacle.");
			}
		}

		this.obstacles.add(obstacle);

		if (addPointsAndEdges) {
			// Add a new node for each obstacle vertex, and add the visible edges to any existing nodes.
			for (Point2D p : obstacle.points) {
				this.addNode(p);
			}
		}

		return true;
	}

	/**
	 * Gets the node at a given point.
	 * 
	 * @param p
	 * @return node if exists at point p, null otherwise
	 */
	public VisibilityGraphNode getNode(Point2D p) {
		// Check if any node has the same point p.
		for (VisibilityGraphNode n : this.nodes) {
			if (n.p.equals(p)) {
				return n;
			}
		}

		return null;
	}

	/**
	 * Checks if an edge can be added between two nodes, and adds the edge if possible.
	 * Returns whether or not an edge was added.
	 * 
	 * @param p
	 * @param q
	 * @param h
	 * @return true if the edge was added, false otherwise
	 */
	private boolean tryAddEdge(VisibilityGraphNode p, VisibilityGraphNode q) {
		// Get the visibility between the two vertices by checking if any room obstacles are in the way.
		NodeVisibility visibility = this.getVisibility(p.p, q.p);

		// Add the edge if q is at least partly visible from p.
		if (visibility != NodeVisibility.SAME_POINT && visibility.isPartlyVisible()) {
			double weight = p.p.distance(q.p);

			boolean isObstacleEdge = visibility == NodeVisibility.ALONG_OBSTACLE_EDGE;

			this.addEdge(new VisibilityGraphEdge(p, q, weight, isObstacleEdge));

			return true;
		}

		return false;
	}

	private boolean addEdge(VisibilityGraphEdge edge) {
		// If the edge already exists (by its nodes) then do not add it.
		for (VisibilityGraphEdge e : this.edges) {
			if (e.equals(edge)) {
				return false;
			}
		}

		this.edges.add(edge);

		return true;
	}

	/**
	 * Calculates the visibility between two points by checking if any room obstacles are in the way.
	 * 
	 * @param p
	 * @param q
	 * @return point-to-point visibility
	 */
	private NodeVisibility getVisibility(Point2D p, Point2D q) {
		// If the points are not equal then check for visibility.
		if (!p.equals(q)) {
			Line2D l = new Line2D.Double();
			l.setLine(p, q);

			// Initialise visibility to 'fully visible.
			NodeVisibility visibility = NodeVisibility.FULLY_VISIBLE;

			int index = 0;

			// For each obstacle, check if it blocks visibility.
			while (visibility != NodeVisibility.NOT_VISIBLE && index < this.obstacles.size()) {
				Obstacle o = this.obstacles.get(index);

				// Calculate if the line formed between the two points intersects with the obstacle.
				ObstacleLineIntersectionResult intersection = o.intersectsLine(l);

				switch (intersection) {
				case CROSSED:
					visibility = NodeVisibility.NOT_VISIBLE;
					break;

				case EQUAL_LINES:
					visibility = NodeVisibility.ALONG_OBSTACLE_EDGE;
					break;

				default:
					break;
				}

				index ++;
			}

			return visibility;
		}

		return NodeVisibility.SAME_POINT;
	}

}
