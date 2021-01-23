package engine;

public class Move {

	public CoordFour origin;
	public CoordFour dest;
	// 1 for spatial, 2 for jumping(unrecognized branching, 3 for branching.
	public int type;

	public static final int NORMALMOVE = 0;
	public static final int CASTLE = 1;
	public static final int PROMOTION = 2;
	public static final int SPATIALMOVE = 1;
	public static final int JUMPINGMOVE = 2;
	public static final int BRANCHINGMOVE = 3;

	// 0 is normal, 1 is castling, and 2+ is promotion with the type being promoted.
	// For castling, origin in the king and dest is the rook.
	public int specialType;

	public Move(CoordFour coordorigin, CoordFour coorddest) {
		specialType = 0;
		origin = coordorigin;
		dest = coorddest;
		if (origin.L == dest.L && origin.T == dest.T) {
			type = 1;
		} else {
			type = JUMPINGMOVE;
		}

	}

	public Move(CoordFour coordorigin, CoordFour coorddest, int type) {
		specialType = type;
		origin = coordorigin;
		dest = coorddest;
		if (origin.L == dest.L && origin.T == dest.T) {
			type = 1;
		} else {
			type = 0;
		}
	}

	public String rawMoveNotation() {
		return this.origin.rawCoordString() + this.dest.rawCoordString();
	}

	public String toString() {
		String moveStr = "";
		moveStr += origin.toString();
		moveStr += "-->";
		moveStr += dest.toString();
		return moveStr;
	}

	// This implements shads notation, but you need a piece for this...
	public String toString(char piece) {
		String move = "";
		if (this.type == SPATIALMOVE) {
			move += "(" + this.origin.L + "T" + this.origin.T + ")" + piece + this.dest.SANString();
		} else if (this.type == JUMPINGMOVE) {
			move += "(" + this.origin.L + "T" + this.origin.T + ")" + piece + this.origin.SANString() + ">("
					+ this.dest.L + "T" + this.dest.T + ")" + this.dest.SANString();
		} else if (this.type == BRANCHINGMOVE) {
			move += "(" + this.origin.L + "T" + this.origin.T + ")" + piece + this.origin.SANString() + ">>("
					+ this.dest.L + "T" + this.dest.T + ")" + this.dest.SANString();
		}
		return move;
	}

}
