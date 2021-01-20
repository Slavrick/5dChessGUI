package engine;

public class Move {

	public CoordFour origin;
	public CoordFour dest;
	// 1 for spatial, 2 for jumping(unrecognized branching, 3 for branching.
	public int type;
	
	//0 is normal, 1 is castling, and 2+ is promotion with the type being promoted.
	//For castling, origin in the king and dest is the rook.
	public int specialType;
	
	public Move(CoordFour coordorigin, CoordFour coorddest) {
		specialType = 0;
		origin = coordorigin;
		dest = coorddest;
		if(origin.L == dest.L && origin.T == dest.T) {
			type = 1;
		}
		else{
			type = 0;
		}
		
	}
	
	public Move(CoordFour coordorigin, CoordFour coorddest, int type) {
		specialType = type;
		origin = coordorigin;
		dest = coorddest;
		if(origin.L == dest.L && origin.T == dest.T) {
			type = 1;
		}
		else{
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
	
}
