package uk.ac.cam.oda22.coverage;

import java.util.LinkedList;
import java.util.List;

import uk.ac.cam.oda22.core.logging.Log;

/**
 * @author Oliver
 *
 */
public class Sweep {

	public final List<ISweepSegment> segments;

	public Sweep() {
		this.segments = new LinkedList<ISweepSegment>();
	}

	public Sweep(ISweepSegment s) {
		this.segments = new LinkedList<ISweepSegment>();
		this.segments.add(s);
	}

	public Sweep(List<ISweepSegment> sweepSegments) {
		this.segments = sweepSegments;
	}
	
	public void addSegment(ISweepSegment s) {
		this.segments.add(s);
	}
	
	public void addSegments(List<ISweepSegment> ss) {
		this.segments.addAll(ss);
	}
	
	public boolean removeSegment() {
		if (this.segments.size() == 0) {
			Log.error("Cannot remove segment from empty sweep.");
			
			return false;
		}
		
		this.segments.remove(this.segments.size() - 1);
		
		return true;
	}
	
	public boolean contains(ISweepSegment segment) {
		for (ISweepSegment s : this.segments) {
			if (s.equals(segment)) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean isEmpty() {
		return this.segments.size() == 0;
	}

}
