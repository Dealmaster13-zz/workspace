package uk.ac.cam.oda22.core.environment;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * @author Oliver
 *
 */
public class VisibilityGraph {

	public final List<VisibilityGraphNode> nodes;
	
	public final List<VisibilityGraphEdge> edges;
	
	public VisibilityGraph() {
		this.nodes = new ArrayList<VisibilityGraphNode>();
		this.edges = new ArrayList<VisibilityGraphEdge>();
	}
	
	public VisibilityGraph(VisibilityGraph g) {
		this.nodes = new ArrayList<VisibilityGraphNode>();
		this.edges = new ArrayList<VisibilityGraphEdge>();
		
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
	}
	
	public boolean addNode(VisibilityGraphNode node) {
		// If the node already exists (by location) then do not add it.
		for (VisibilityGraphNode n : this.nodes) {
			if (n.equals(node)) {
				return false;
			}
		}
		
		this.nodes.add(node);
		
		return true;
	}
	
	public boolean addEdge(VisibilityGraphEdge edge) {
		// If the edge already exists (by its nodes) then do not add it.
		for (VisibilityGraphEdge e : this.edges) {
			if (e.equals(edge)) {
				return false;
			}
		}
		
		this.edges.add(edge);
		
		return true;
	}

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
	
}
