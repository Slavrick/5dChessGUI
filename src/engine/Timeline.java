package engine;

import java.util.ArrayList;

public class Timeline {
	ArrayList<Board> wboards;	
	ArrayList<Board> bboards;
	int Layer;
	int Tstart;
	int Tend;
	int whiteStart;
	int whiteEnd;
	int blackStart;
	int blackEnd;
	boolean colorPlayable;
	boolean colorStart;
	Board origin;
	
	public boolean timeExists(int t, boolean boardColor) {
		if(t > Tstart && t < Tend)
			return true;
		if(boardColor == GameState.WHITE) {
			if(t >= whiteStart && t <= whiteEnd) {
				return true;
			}
		}else {
			if(t >= blackStart && t <= blackEnd) {
				return true;
			}
		}
		return false;
	}

	public Board getBoard(int t, boolean boardColor) {
		if(boardColor == GameState.WHITE) {
			return wboards.get(t - whiteStart);
		}else {
			return bboards.get(t - blackStart);
		}
	}
}
