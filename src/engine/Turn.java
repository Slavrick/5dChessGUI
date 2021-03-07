package engine;

import java.util.ArrayList;

public class Turn {
	
	public Move[] moves;
	public int[] tls;
	public int numTL;
	public boolean color;
	public int turnNum;
	public int Tpresent;
	public static notationMode mode = notationMode.SHAD;
	public static prefixMode pre = prefixMode.TURN;
	
	public static enum prefixMode{
		NONE, TURN, TURNANDPRESENT
	}
	
	public static enum notationMode{
		RAW, ALXBRAW, PGN5, SHAD, TESSERACT 
	}
	
	//This will do no validation as of yet.
	public Turn(ArrayList<Move> turnMoves , ArrayList<Integer> tlEffected) {
		this.moves = new Move[turnMoves.size()];
		this.moves = turnMoves.toArray(this.moves);
		tls = new int[tlEffected.size()];
		int count = 0;
		for(int i : tlEffected) {
			tls[count] = i;
			count++;
		}
	}
	
	public Turn(ArrayList<Move> tmoves) {
		this.moves = tmoves.toArray(this.moves);
		int count = 0;
		for(Move m : this.moves) {
			count++;
			if(m.type != 1)
				count++;
		}
		this.tls = new int[count];
		count = 0;
		for(Move m : this.moves) {
			if(m.type != 1){
				tls[count] = m.origin.L;
				count++;
				tls[count] = m.dest.L;
				count++;
			}else {
				tls[count] = m.dest.L;
				count++;
			}
		}
		mode = notationMode.RAW;
		pre = prefixMode.NONE;
	}
	
	
	public Turn(Move[] tmoves) {
		this.moves = tmoves;
		int count = 0;
		for(Move m : this.moves) {
			count++;
			if(m.type != 1)
				count++;
		}
		this.tls = new int[count];
		count = 0;
		for(Move m : this.moves) {
			if(m.type != 1){
				tls[count] = m.origin.L;
				count++;
				tls[count] = m.dest.L;
				count++;
			}else {
				tls[count] = m.dest.L;
				count++;
			}
		}
		mode = notationMode.SHAD;
		pre = prefixMode.TURN;
	}

	public Move[] getMoves(){
		return this.moves;
	}
	
	public String toString() {
		String temp = "";
		switch(pre) {
		case TURN:
			temp += turnNum + ".";
			break;
		case NONE:
		default:
			break;
			
		}
		switch(mode) {
		case SHAD:
			for(Move m : moves) {
				temp += m.toShadString();
				temp += " ";
			}
			break;
		case RAW:
		default:
			for(Move m : moves) {
				temp += m.rawMoveNotation();
				temp += "; ";
			}
			break;
		}
		return temp;
	}
	
	private static class ShadNotation{
		public static String MovesToString(Turn t) {
			//Sort array First...
			String temp = "";
			for(Move m : t.getMoves()) {
				
			}//XXX finish this later
			return temp;
		}
	}
}
