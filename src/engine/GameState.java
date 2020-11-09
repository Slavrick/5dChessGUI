package engine;

import java.util.ArrayList;

public class GameState {
	public static final boolean WHITE = true;
	public static final boolean BLACK = true;
	
	public ArrayList<Timeline> multiverse;
	public boolean color;
	public int width;
	public int height;
	public int maxTL;
	public int minTL;
	public int tlHandicap;
	
	public boolean isInBounds(CoordFive c, boolean boardColor) {
		if( c.L > maxTL || c.L < minTL) {
			return false;
		}
		if(!multiverse.get(c.L + (-1 * minTL)).timeExists(c.T, boardColor)){
			return false;
		}
		if (c.x < 0 || c.x >= width)
			return false;
		if (c.y < 0 || c.y >= height)
			return false;
		return true;
	}
	
	public Board getBoard(CoordFive c, boolean boardColor) {
		return multiverse.get(c.L + (-1 * minTL)).getBoard(c.T, boardColor);
	}
	
}
