package uk.ac.cam.oda22.core.environment;

import java.awt.geom.Point2D;

import uk.ac.cam.oda22.core.graphs.Node;

/**
 * @author Oliver
 *
 */
public class VisibilityGraphNode extends Node {

	public VisibilityGraphNode(Point2D node) {
		super(node);
	}
	
	public VisibilityGraphNode(VisibilityGraphNode node) {
		super(new Point2D.Double(node.p.getX(), node.p.getY()));
	}
	
}
