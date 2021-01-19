package engine;

public class Move {

	public CoordFour origin;
	public CoordFour dest;
	// 1 for spatial, 2 for jumping(unrecognized branching, 3 for branching.
	public int type;
	
	public Move(CoordFour coordorigin, CoordFour coorddest) {
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
