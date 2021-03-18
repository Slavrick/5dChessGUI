package engine;

public class Move implements Comparable<Move>{

	public CoordFour origin;
	public CoordFour dest;
	public int pieceMoved;
	// 1 for spatial, 2 for jumping(unrecognized branching, 3 for branching.
	public int type;

	public static final int NULLMOVE = -1;
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
			type = SPATIALMOVE;
		} else {
			type = JUMPINGMOVE;
		}
		pieceMoved = 0;
	}

	public Move(CoordFour coordorigin, CoordFour coorddest, int type) {
		specialType = type;
		origin = coordorigin;
		dest = coorddest;
		if (origin.L == dest.L && origin.T == dest.T) {
			type = SPATIALMOVE;
		} else {
			type = JUMPINGMOVE;
		}
		pieceMoved = 0;
	}
	
	public Move(CoordFour boardOrigin) {
		origin = boardOrigin;
		dest = new CoordFour(0,0,0,0);
		type = SPATIALMOVE;
		specialType = NULLMOVE;
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
	// XXX remove this, not used
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
	
	public String toShadString(){
		String move = "";
		int piece = this.pieceMoved;
		if(piece > Board.numTypes) {
			piece -= Board.numTypes;
		}
		if(pieceMoved != Board.EMPTYSQUARE && piece != 1) {
			move += Board.pieceChars[piece];
		}
		move = "(" + this.origin.L + "T" + this.origin.T + ")" + move;
		if (this.type == SPATIALMOVE) {
			move += this.dest.SANString();
		} else if (this.type == JUMPINGMOVE) {
			move += this.origin.SANString() + ">(" + this.dest.L + "T" + this.dest.T + ")" + this.dest.SANString();
		} else if (this.type == BRANCHINGMOVE) {
			move += ">>(" + this.dest.L + "T" + this.dest.T + ")" + this.dest.SANString();
		}
		return move;
	}

	@Override
	public int compareTo(Move m2) {
		if(m2.type > this.type) {
			return -1;
		}
		if(m2.type == this.type) {
			return 0;
		}
		return 1;
	}

	public String toRawShadString() {
		if(this.specialType == CASTLE) {
			//This probably needs to change for multiple king boards, that will come later.
			if(dest.x > origin.x) {
				return "O-O";
			}
			else {
				return "O-O-O";
			}
		}
		String move = "";
		String temporalOrigin = "(" + this.origin.L + "T" + this.origin.T + ")";
		String sanOrigin = this.origin.SANString();
		String destStr = "(" + this.dest.L + "T" + this.dest.T + ")" + this.dest.SANString();
		int piece = this.pieceMoved;
		if(piece > Board.numTypes) {
			piece -= Board.numTypes;
		}
		move = temporalOrigin;
		if(pieceMoved != Board.EMPTYSQUARE && piece != 1) {
			move += Board.pieceChars[piece];
		}
		move += sanOrigin;
		if (this.type == SPATIALMOVE) {
			move += destStr;
		} else if (this.type == JUMPINGMOVE) {
			move += ">" + destStr;
		} else if (this.type == BRANCHINGMOVE) {
			move += ">>" + destStr;
		}
		if(this.specialType > CASTLE) {
			int promoted = specialType;
			if(specialType > Board.numTypes) {
				promoted -= Board.numTypes;
			}
			move += "=" + Board.pieceChars[promoted];
		}
		return move;
	}
}
