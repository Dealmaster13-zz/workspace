package uk.ac.cam.oda22.core.environment;

/**
 * @author Oliver
 *
 */
public enum NodeVisibility {

	FULLY_VISIBLE,
	
	ALONG_OBSTACLE_EDGE,
	
	SAME_POINT,
	
	NOT_VISIBLE;
	
	public boolean isPartlyVisible() {
		return this == FULLY_VISIBLE || this == ALONG_OBSTACLE_EDGE;
	}
	
}
