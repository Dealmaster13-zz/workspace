package uk.ac.cam.oda22.core;

/**
 * @author Oliver
 *
 */
public enum LineIntersectionResult {

	CROSS,
	
	// This represents both lines overlapping each other.
	COLINEAR,
	
	// This represents one of the ends of the first line touching the second line.
	A_TOUCHES_B,

	// This represents one of the ends of the second line touching the first line.
	B_TOUCHES_A,
	
	// This represents one of the ends of the first line touching one of the ends of the second line.
	JOINT,
	
	NONE
	
}
