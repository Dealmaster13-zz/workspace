package uk.ac.cam.oda22.coverage.simple;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Oliver
 * 
 */
public class RoomCellIndex {

	public int x;

	public int y;

	/**
	 * @param x
	 * @param y
	 */
	public RoomCellIndex(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public RoomCellIndex(Point2D p, double cellSize) {
		this.x = (int) Math.round(p.getX() / cellSize);
		this.y = (int) Math.round(p.getY() / cellSize);
	}

	public RoomCellIndex(RoomCellIndex c) {
		this.x = c.x;
		this.y = c.y;
	}
	
	public List<RoomCellIndex> getAdjacentCells() {
		List<RoomCellIndex> l = new ArrayList<RoomCellIndex>();
		l.add(new RoomCellIndex(this.x - 1, this.y));
		l.add(new RoomCellIndex(this.x, this.y + 1));
		l.add(new RoomCellIndex(this.x + 1, this.y));
		l.add(new RoomCellIndex(this.x, this.y - 1));
		return l;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		
		RoomCellIndex c = (RoomCellIndex) o;
		
		return this.x == c.x && this.y == c.y;
	}

}
