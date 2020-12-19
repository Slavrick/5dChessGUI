package engine;

import java.util.ArrayList;

public class Turn {
	
	private Move[] moves;
	private int[] tls;
	public int numTL;
	public boolean color;
	public int turnNum;
	public int Tpresent;
	public notationMode mode;
	public prefixMode pre;
	
	public static enum prefixMode{
		NONE, TURN, TURNANDPRESENT
	}
	
	public static enum notationMode{
		RAW, ALXBRAW, PGN5, SHAD, TESSERACT 
	}
	
	//This will do no validation as of yet.
	public Turn(Move[] tmoves) {
		this.moves = tmoves;//TODO make sure this line works as intended, IE copies each and not just the ref to the array.
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
	
	public Move[] getMoves(){
		return this.moves;
	}
	
	//TODO make string based off of notation System.
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
}
