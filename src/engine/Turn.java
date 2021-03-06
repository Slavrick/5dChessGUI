package engine;

import java.util.ArrayList;
import java.util.Comparator;

public class Turn {
	
	public Move[] moves;
	public int[] tls;
	//This is ply, not actual turn num, so 2 would be blacks first turn and 3 would be whites 2nd
	public int turnNum;

	//XXX implement or delete these fields
	public int numTL;
	public boolean color;
	public int Tpresent;
	
	public static notationMode mode = notationMode.SHAD;
	public static prefixMode pre = prefixMode.TURN;
	
	public static enum prefixMode{
		NONE, TURN, TURNANDPRESENT
	}
	
	public static enum notationMode{
		COORDINATE, SHAD, SHADRAW 
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
	
	public Turn(Move[] tmoves) { //I dont even know but this makes the thing not undoable?
		ArrayList<Move> removedNull = new ArrayList<Move>();
		for (Move m : tmoves) {
			if (m != null) {
				removedNull.add(m);   
			}			   
		}
		this.moves = removedNull.toArray(new Move[0]);
		int count = 0;
		for(Move m : this.moves) {
			count++;
			if(m.type != 1)
				count++;
		}
		this.tls = new int[count];
		count = 0;
		for(Move m : this.moves) {
			switch(m.type){
				case Move.BRANCHINGMOVE://TODO I need to rework this, the only way you can tell where a branching move effected is through the gamestate
				case Move.JUMPINGMOVE:
					tls[count] = m.origin.L;
					count++;
					tls[count] = m.dest.L;
					count++;
					break;
				case Move.SPATIALMOVE:
				case Move.NULLMOVE:
					tls[count] = m.origin.L;
					count++;
					break;
			}
		}
		mode = notationMode.SHAD;
		pre = prefixMode.TURN;
	}
	
	public Turn() {
		moves = null;
		tls = null;
		turnNum = 0;
	}

	public Move[] getMoves(){
		return this.moves;
	}
	
	public boolean equals(Turn t) {
		if(this.moves == null && t.moves == null) {
			return true;
		}
		if(this.moves == null || t.moves == null) {
			return false;
		}
		if(this.moves.length != t.moves.length) {
			return false;
		}
		for(Move ogMove : this.moves) {
			boolean found = false;
			for(Move compareTo : t.moves) {
				if(ogMove.equals(compareTo)) {
					found = true;
					break;
				}
			}
			if(!found) {
				return false;
			}
		}
		return true;
	}
	
	public String toString() {
		if(moves == null) {
			return "";
		}
		String temp = "";
		switch(pre) {
		case TURN:
			temp += ((turnNum + 1) / 2) + "" + (turnNum % 2 == 1 ? 'w' : 'b') + ".";
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
		case SHADRAW:
			for(Move m: moves) {
				temp += m.toRawShadString();
				temp += " ";
			}
			break;
		case COORDINATE:
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
	
	private class TLMoveComparator implements Comparator<Move>{
		//Sorts in ascending order
		@Override
		public int compare(Move o1, Move o2) {
			if(o1.origin.L > o2.origin.L) {
				return 1;
			}
			if(o1.origin.L < o2.origin.L) {
				return -1;
			}
			return 0;
		}
	}
}
