package engine;
import java.util.ArrayList;

public class MoveNotation {
	
	boolean rider;
	int[] movnote;
	
	public static final int[] ROOK = {1};
	public static final int[] BISHOP = {2};
	public static final int[] UNICORN = {3};
	public static final int[] DRAGON = {4};
	public static final int[] KING = {1,2,3,4};
	public static final int[] QUEEN = {1,2,3,4};
	
	//All commented out vectors are pure foreward time travel, or pure foreward with some spatial, which means that it is impossible and we dont need to consider
	public static final CoordFour[] RookMoveset = {
			new CoordFour(1,0,0,0),
			new CoordFour(0,1,0,0),
			//new CoordFive(0,0,1,0), redundant, cannot move foreward purely
			new CoordFour(0,0,0,1),
			
			new CoordFour(-1,0,0,0),
			new CoordFour(0,-1,0,0),
			new CoordFour(0,0,-1,0),
			new CoordFour(0,0,0,-1)		
	};
	
	public static final CoordFour[] BishopMoveset = {
			new CoordFour(1,1,0,0),
			//new CoordFive(1,0,1,0),
			new CoordFour(1,0,0,1),
			//new CoordFive(0,1,1,0),
			new CoordFour(0,1,0,1),
			new CoordFour(0,0,1,1),
			
			new CoordFour(-1,1,0,0),
			//new CoordFive(-1,0,1,0),
			new CoordFour(-1,0,0,1),
			//new CoordFive(0,-1,1,0),
			new CoordFour(0,-1,0,1),
			new CoordFour(0,0,-1,1),
			
			new CoordFour(1,-1,0,0),
			new CoordFour(1,0,-1,0),
			new CoordFour(1,0,0,-1),
			new CoordFour(0,1,-1,0),
			new CoordFour(0,1,0,-1),
			new CoordFour(0,0,1,-1),	
			
			new CoordFour(-1,-1,0,0),
			new CoordFour(-1,0,-1,0),
			new CoordFour(-1,0,0,-1),
			new CoordFour(0,-1,-1,0),
			new CoordFour(0,-1,0,-1),
			new CoordFour(0,0,-1,-1),	
	};
	
	public static final CoordFour[] UnicornMoveset = {
			new CoordFour(1,1,1,0),
			new CoordFour(1,1,0,1),
			new CoordFour(1,0,1,1),
			new CoordFour(0,1,1,1),
			
			new CoordFour(-1,1,1,0),
			new CoordFour(-1,1,0,1),
			new CoordFour(-1,0,1,1),
			new CoordFour(0,-1,1,1),
			
			new CoordFour(1,-1,1,0),
			new CoordFour(1,-1,0,1),
			new CoordFour(1,0,-1,1),
			new CoordFour(0,1,-1,1),
			
			new CoordFour(1,1,-1,0),
			new CoordFour(1,1,0,-1),
			new CoordFour(1,0,1,-1),
			new CoordFour(0,1,1,-1),
			
			new CoordFour(-1,-1,1,0),
			new CoordFour(-1,-1,0,1),
			new CoordFour(-1,0,-1,1),
			new CoordFour(0,-1,-1,1),
			
			new CoordFour(-1,1,-1,0),
			new CoordFour(-1,1,0,-1),
			new CoordFour(-1,0,1,-1),
			new CoordFour(0,-1,1,-1),
			
			new CoordFour(-1,-1,-1,0),
			new CoordFour(-1,-1,0,-1),
			new CoordFour(-1,0,-1,-1),
			new CoordFour(0,-1,-1,-1),
	};
	
	public static final CoordFour[] DragonMoveset = {
			new CoordFour(1,1,1,1),
			new CoordFour(-1,1,1,1),	
			new CoordFour(1,-1,1,1),	
			new CoordFour(1,1,-1,1),
			new CoordFour(1,1,1,-1),	
			new CoordFour(-1,-1,1,1),	
			new CoordFour(-1,1,-1,1),	
			new CoordFour(-1,1,1,-1),
			new CoordFour(1,-1,-1,1),
			new CoordFour(1,-1,1,-1),
			new CoordFour(1,1,-1,-1),
			new CoordFour(1,-1,-1,-1),
			new CoordFour(-1,1,-1,-1),	
			new CoordFour(-1,-1,1,-1),	
			new CoordFour(-1,-1,-1,1),
			new CoordFour(-1,-1,-1,-1),
	};
	
	public static final CoordFour[] KnightMovement = {
			new CoordFour(2,1,0,0),
			new CoordFour(-2,1,0,0),
			new CoordFour(2,-1,0,0),
			new CoordFour(-2,-1,0,0),
			
			new CoordFour(1,2,0,0),
			new CoordFour(1,2,0,0),
			new CoordFour(2,1,0,0),
			new CoordFour(2,-1,0,0),
			new CoordFour(2,1,0,0),
			new CoordFour(2,-1,0,0),
			new CoordFour(2,1,0,0),
			new CoordFour(2,-1,0,0),
			new CoordFour(2,1,0,0),
			new CoordFour(2,-1,0,0),
	};
	
	public static final CoordFour[] whitePawnMovement = {
			new CoordFour(0,1,0,0),
			new CoordFour(0,0,0,-1)
	};
	
	public static final CoordFour[] whitePawnattack = {
			new CoordFour(1,1,0,0),
			new CoordFour(-1,1,0,0),
			new CoordFour(0,0,1,-1),
			new CoordFour(0,0,-1,-1)
	};
	
	public static final CoordFour[] blackPawnMovement = {
			new CoordFour(0,-1,0,0),
			new CoordFour(0,0,0,1),
	};
	
	public static final CoordFour[] blackPawnattack = {
			new CoordFour(1,-1,0,0),
			new CoordFour(-1,-1,0,0),
			new CoordFour(0,0,1,1),
			new CoordFour(0,0,-1,1)
	};
	
	public static int[] getMovNotation(Piece.PieceType pt) {
		switch(pt) {
		case BISHOP:
			return MoveNotation.BISHOP;
		case ROOK:
			return MoveNotation.ROOK;
		case PAWN:
		case KNIGHT:
		default:
			int[] nullmove = {};
			return nullmove;
		}
	}
	
	public static ArrayList<CoordFour> getMultiMoveset(int[] moveAxes){
		ArrayList<CoordFour> moveset = new ArrayList<CoordFour>();
		for(int i: moveAxes) {
			moveset.addAll(getMoveset(i));
		}
		return moveset;
	}	
	
	public static ArrayList<CoordFour> getMoveset(int moveAxi){
		ArrayList<CoordFour> moveset = new ArrayList<CoordFour>();
		
		switch(moveAxi)
		{
		case 1:
			for(CoordFour c : RookMoveset) {
				moveset.add(c);				
			}
			break;
		case 2:
			for(CoordFour c : BishopMoveset) {
				moveset.add(c);				
			}
			break;
		case 3:
			for(CoordFour c : UnicornMoveset) {
				moveset.add(c);				
			}
			break;
		case 4:
			for(CoordFour c : DragonMoveset) {
				moveset.add(c);				
			}
			break;
		default:
			break;
		}
		return moveset;
		
		
	}
	
}
