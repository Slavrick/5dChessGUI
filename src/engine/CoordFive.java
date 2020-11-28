package engine;

public class CoordFive {
	/*
	 * X represents file ie. a,b,c... files
	 * y represents rank
	 * T/L represent their raw coordinates as per 5d chess rules
	 */
	public int x;
	public int y;
	public int T;
	public int L;

	public CoordFive(int x, int y, int T, int L) {
		this.x = x;
		this.y = y;
		this.T = T;
		this.L = L;
	}

	public CoordFive(int x, int y) {
		this(x, y, 0, 0);
	}

	public String toString() {
		return "(" + L + "L." + "T" + T + "." + intTofile(x) + "" + (y + 1) + ")";
	}

	public char intTofile(int file) {
		return (char) (file + 97);
	}
	
	/**
	 * This function takes a 5d coord and adds another coordinates values to it.
	 * 
	 * @param c Five Dimensional Coordinate to add to this Coordinate.
	 */
	public void add(CoordFive c) {
		x += c.x;
		y += c.y;
		T += c.T;
		L += c.L;
	}

	
	/**
	 * 
	 * @return true if the coordinate is pure spatial, and false otherwise.
	 */
	public boolean isSpatial() {
		return this.T == 0 && this.L == 0;
	}
	
	/**
	 * A comparison function to compare this coordinate and another.
	 * 
	 * 
	 * @param c coordinate to compare to.
	 * @return true if the two coordinates are the same.
	 */
	public boolean equals(CoordFive c) {
		return this.x == c.x && this.y == c.y && this.T == c.T && this.L == c.L;
	}

}
