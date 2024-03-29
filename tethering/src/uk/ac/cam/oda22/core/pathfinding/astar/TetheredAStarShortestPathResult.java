package uk.ac.cam.oda22.core.pathfinding.astar;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Oliver
 * 
 */
public class TetheredAStarShortestPathResult {

	public final List<TetheredAStarSinglePathResult> shortestPathResults;

	public TetheredAStarShortestPathResult() {
		this.shortestPathResults = new ArrayList<TetheredAStarSinglePathResult>();
	}

	public TetheredAStarShortestPathResult(
			TetheredAStarSinglePathResult shortestPathResult) {
		this.shortestPathResults = new ArrayList<TetheredAStarSinglePathResult>();
		this.shortestPathResults.add(shortestPathResult);
	}

	public TetheredAStarShortestPathResult(
			List<TetheredAStarSinglePathResult> shortestPathResults) {
		this.shortestPathResults = shortestPathResults;
	}

	public void addShortestPath(TetheredAStarSinglePathResult shortestPath) {
		this.shortestPathResults.add(shortestPath);
	}

}
