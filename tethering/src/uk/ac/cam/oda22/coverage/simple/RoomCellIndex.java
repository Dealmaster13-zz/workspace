package uk.ac.cam.oda22.coverage.simple;

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

	public RoomCellIndex(RoomCellIndex c) {
		this.x = c.x;
		this.y = c.y;
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
