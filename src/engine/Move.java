package engine;

public class Move {

	public CoordFive origin;
	public CoordFive dest;
	//1 for spatial, 2 for jumping or branching.
	public int type;
	
	public Move(CoordFive coordorigin, CoordFive coorddest) {
		origin = coordorigin;
		dest = coorddest;
		
		if(origin.L == dest.L && origin.T == dest.T) {
			type = 1;
		}
		else{
			type = 2;
		}
		
	}
	
	public String toString() {
		String moveStr = "";
		moveStr += origin.toString();
		moveStr += "-->";
		moveStr += dest.toString();
		return moveStr;
	}
}
