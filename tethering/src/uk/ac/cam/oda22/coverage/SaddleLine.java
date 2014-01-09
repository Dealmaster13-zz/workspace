package uk.ac.cam.oda22.coverage;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Oliver
 *
 */
public class SaddleLine {

	public final List<SaddleLineSegment> segments;
	
	public SaddleLine() {
		this.segments = new LinkedList<SaddleLineSegment>();
	}
	
	public SaddleLine(SaddleLineSegment s) {
		this.segments = new LinkedList<SaddleLineSegment>();
		this.segments.add(s);
	}
	
	public SaddleLine(List<SaddleLineSegment> segments) {
		this.segments = segments;
	}
	
}
