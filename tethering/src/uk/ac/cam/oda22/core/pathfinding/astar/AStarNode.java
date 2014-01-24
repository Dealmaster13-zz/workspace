package uk.ac.cam.oda22.core.pathfinding.astar;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import uk.ac.cam.oda22.core.graphs.Node;

/**
 * @author Oliver
 *
 */
public class AStarNode extends Node {
	
	/**
	 * The neighbouring edges.
	 */
	public final List<AStarEdge> edges;
	
	/**
	 * The node's predecessor list(s) for the shortest path(s) as well as their tether configurations.
	 * A list of shortest paths needs to be stored because we can get to this point using many tether configurations.
	 */
	public List<AStarSubPath> subPaths;
	
	/**
	 * Whether or not the node has been discovered.
	 */
	public boolean discovered;
	
	/**
	 * To cost from the source node to this node.
	 */
	public double g;
	
	/**
	 * The heuristic cost from this node to the destination node.
	 */
	public double h;
	
	public AStarNode(Point2D p) {
		super(p);
		
		this.edges = new LinkedList<AStarEdge>();
		this.subPaths = new ArrayList<AStarSubPath>();
		this.discovered = false;
		this.g = Double.POSITIVE_INFINITY;
		this.h = Double.POSITIVE_INFINITY;
	}
	
}
