package engine;

public class Move {

	public CoordFour origin;
	public CoordFour dest;
	//1 for spatial, 2 for jumping or branching.
	public int type;
	
	public Move(CoordFour coordorigin, CoordFour coorddest) {
		origin = coordorigin;
		dest = coorddest;
		if(origin.L == dest.L && origin.T == dest.T) {
			type = 1;
		}
		else{
			type = 2;
		}
		
	}
	
	public String rawMoveNotation() {
		return this.origin.rawCoord() + this.dest.rawCoord();
	}
	
	public String toString() {
		String moveStr = "";
		moveStr += origin.toString();
		moveStr += "-->";
		moveStr += dest.toString();
		return moveStr;
	}
}
