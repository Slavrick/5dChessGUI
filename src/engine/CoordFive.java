package engine;

public class CoordFive {
	// x will represent the file
	// y will represent the rank
	// T is time, L is layer as per 5dchess rules.
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
		return "(" + L + "L." + "T" + T + "." + intTofile(y) + "" + (x + 1) + ")";
	}

	public char intTofile(int file) {
		return (char) (file + 97);
	}

}
