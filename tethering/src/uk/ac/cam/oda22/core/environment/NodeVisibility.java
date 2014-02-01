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

	/**
	 * Checks if node visibility nv is less visible.
	 * 
	 * @param nv
	 * @return true if nv is less visible, false otherwise
	 */
	public boolean isLessVisible(NodeVisibility nv) {
		switch (this) {
		case FULLY_VISIBLE:
			return nv == ALONG_OBSTACLE_EDGE || nv == SAME_POINT
					|| nv == NOT_VISIBLE;

		case ALONG_OBSTACLE_EDGE:
			return nv == SAME_POINT || nv == NOT_VISIBLE;

		case SAME_POINT:
			return nv == NOT_VISIBLE;

		default:
			return false;
		}
	}

}
