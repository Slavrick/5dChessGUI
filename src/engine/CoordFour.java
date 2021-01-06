package engine;

public class CoordFour {
	/*
	 * X represents file ie. a,b,c... files y represents rank T/L represent their
	 * raw coordinates as per 5d chess rules
	 */
	public int x;
	public int y;
	public int T;
	public int L;

	public CoordFour(int x, int y, int T, int L) {
		this.x = x;
		this.y = y;
		this.T = T;
		this.L = L;
	}

	public CoordFour(int x, int y) {
		this(x, y, 0, 0);
	}

	public CoordFour clone() {
		return new CoordFour(this.x, this.y, this.T, this.L);
	}

	/**
	 * A comparison function to compare this coordinate and another.
	 * 
	 * 
	 * @param c coordinate to compare to.
	 * @return true if the two coordinates are the same.
	 */
	public boolean equals(CoordFour c) {
		return this.x == c.x && this.y == c.y && this.T == c.T && this.L == c.L;
	}

	/**
	 * 
	 * @return true if the coordinate is pure spatial, and false otherwise.
	 */
	public boolean isSpatial() {
		return this.T == 0 && this.L == 0;
	}

	/**
	 * This function takes a 5d coord and adds another coordinates values to it.
	 * 
	 * @param c Five Dimensional Coordinate to add to this Coordinate.
	 */
	public void add(CoordFour c) {
		x += c.x;
		y += c.y;
		T += c.T;
		L += c.L;
	}

	public void sub(CoordFour c) {
			x -= c.x;
			y -= c.y;
			T -= c.T;
			L -= c.L;
	}

	// turns a coord into a vector, or a coord with only 1/0's
	public void makeVector() {
		if (this.x != 0) {
			this.x = 1;
		}
		if (this.y != 0) {
			this.y = 1;
		}
		if (this.T != 0) {
			this.T = 1;
		}
		if (this.L != 0) {
			this.L = 1;
		}
	}

	public void flatten() {
		int gcd = GCD(GCD(Math.abs(x),Math.abs(y)), GCD(Math.abs(T),Math.abs(L)));
		if(gcd == 0) {			
			return;
		}
		this.x = this.x / gcd;
		this.y = this.y / gcd;
		this.T = this.T / gcd;
		this.L = this.L / gcd;
	}

	// gets the n-diagonal that a vector is
	public int getNagonal() {
		int nagonal = 0;
		if (this.x != 0)
			nagonal++;
		if (this.y != 0)
			nagonal++;
		if (this.T != 0)
			nagonal++;
		if (this.L != 0)
			nagonal++;
		return nagonal;
	}

	public String toString() {
		return "(" + L + "L." + "T" + T + "." + intToFile(x) + "" + (y + 1) + ")";
	}

	/**
	 * gets a string raw representation of this.
	 * 
	 * @return raw coord string
	 */
	public String rawCoordString() {
		return "(" + this.x + "," + this.y + "," + this.T + "," + this.L + ")";
	}

	/**
	 * get a SAN 2d coord of the given object such as a1 e4 ....
	 * 
	 * @return String SAN representation
	 */
	public String SANString() {
		return intToFile(this.x) + "" + (this.y + 1);
	}

	public static int GCD(int num1, int num2) {
		while(num1 > 0 && num2 > 0) {
			if(num1 > num2) {
				num1 -= num2;
			}
			else {
				num2 -= num1;
			}
		}
		return num1 + num2;
	}

	// sub
	public static CoordFour sub(CoordFour c1, CoordFour c2) {
		return new CoordFour(c1.x - c2.x, c1.y - c2.y, c1.T - c2.T, c1.L - c2.L);
	}
	
	// add
	public static CoordFour add(CoordFour c1, CoordFour c2) {
		CoordFour sum = new CoordFour(c2.x + c1.x, c2.y + c1.y, c2.T + c1.T, c2.L + c1.L);
		return sum;
	}

	/**
	 * returns the corrisponding file from the int file sent, 0 indexed so a is 0 b
	 * is 1 and so on.
	 * 
	 * @param file file to get char for
	 * @return char corrisponding to sent file.
	 */
	protected static char intToFile(int file) {
		return (char) (file + 97);
	}
}
