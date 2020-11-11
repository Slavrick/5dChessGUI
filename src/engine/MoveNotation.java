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
	
	public static final CoordFive[] RookMoveset = {
			new CoordFive(1,0,0,0),
			new CoordFive(0,1,0,0),
			//new CoordFive(0,0,1,0), redundant, cannot move foreward purely
			new CoordFive(0,0,0,1),
			
			new CoordFive(-1,0,0,0),
			new CoordFive(0,-1,0,0),
			new CoordFive(0,0,-1,0),
			new CoordFive(0,0,0,-1)		
	};
	
	public static final CoordFive[] BishopMoveset = {
			new CoordFive(1,1,0,0),
			new CoordFive(1,0,1,0),
			new CoordFive(1,0,0,1),
			new CoordFive(0,1,1,0),
			new CoordFive(0,1,0,1),
			new CoordFive(0,0,1,1),
			
			new CoordFive(-1,1,0,0),
			new CoordFive(-1,0,1,0),
			new CoordFive(-1,0,0,1),
			new CoordFive(0,-1,1,0),
			new CoordFive(0,-1,0,1),
			new CoordFive(0,0,-1,1),
			
			new CoordFive(1,-1,0,0),
			new CoordFive(1,0,-1,0),
			new CoordFive(1,0,0,-1),
			new CoordFive(0,1,-1,0),
			new CoordFive(0,1,0,-1),
			new CoordFive(0,0,1,-1),	
			
			new CoordFive(-1,-1,0,0),
			new CoordFive(-1,0,-1,0),
			new CoordFive(-1,0,0,-1),
			new CoordFive(0,-1,-1,0),
			new CoordFive(0,-1,0,-1),
			new CoordFive(0,0,-1,-1),	
	};
	
	public static final CoordFive[] UnicornMoveset = {
			new CoordFive(1,1,1,0),
			new CoordFive(1,1,0,1),
			new CoordFive(1,0,1,1),
			new CoordFive(0,1,1,1),
			
			new CoordFive(-1,1,1,0),
			new CoordFive(-1,1,0,1),
			new CoordFive(-1,0,1,1),
			new CoordFive(0,-1,1,1),
			
			new CoordFive(1,-1,1,0),
			new CoordFive(1,-1,0,1),
			new CoordFive(1,0,-1,1),
			new CoordFive(0,1,-1,1),
			
			new CoordFive(1,1,-1,0),
			new CoordFive(1,1,0,-1),
			new CoordFive(1,0,1,-1),
			new CoordFive(0,1,1,-1),
			
			new CoordFive(-1,-1,1,0),
			new CoordFive(-1,-1,0,1),
			new CoordFive(-1,0,-1,1),
			new CoordFive(0,-1,-1,1),
			
			new CoordFive(-1,1,-1,0),
			new CoordFive(-1,1,0,-1),
			new CoordFive(-1,0,1,-1),
			new CoordFive(0,-1,1,-1),
			
			new CoordFive(-1,-1,-1,0),
			new CoordFive(-1,-1,0,-1),
			new CoordFive(-1,0,-1,-1),
			new CoordFive(0,-1,-1,-1),
	};
	
	public static final CoordFive[] DragonMoveset = {
			new CoordFive(1,1,1,1),
			new CoordFive(-1,1,1,1),	
			new CoordFive(1,-1,1,1),	
			new CoordFive(1,1,-1,1),
			new CoordFive(1,1,1,-1),	
			new CoordFive(-1,-1,1,1),	
			new CoordFive(-1,1,-1,1),	
			new CoordFive(-1,1,1,-1),
			new CoordFive(1,-1,-1,1),
			new CoordFive(1,-1,1,-1),
			new CoordFive(1,1,-1,-1),
			new CoordFive(1,-1,-1,-1),
			new CoordFive(-1,1,-1,-1),	
			new CoordFive(-1,-1,1,-1),	
			new CoordFive(-1,-1,-1,1),
			new CoordFive(-1,-1,-1,-1),
	};
	
	public static final CoordFive[] KnightMovement = {
			new CoordFive(2,1,0,0),
			new CoordFive(-2,1,0,0),
			new CoordFive(2,-1,0,0),
			new CoordFive(-2,-1,0,0),
			
			new CoordFive(1,2,0,0),
			new CoordFive(1,2,0,0),
			new CoordFive(2,1,0,0),
			new CoordFive(2,-1,0,0),
			new CoordFive(2,1,0,0),
			new CoordFive(2,-1,0,0),
			new CoordFive(2,1,0,0),
			new CoordFive(2,-1,0,0),
			new CoordFive(2,1,0,0),
			new CoordFive(2,-1,0,0),
	};
	
	public static final CoordFive[] pawnMovement = {
			new CoordFive(0,1,0,0),
			new CoordFive(0,-1,0,0),
			

			new CoordFive(1,1,0,0),
			
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
	
	public static ArrayList<CoordFive> getMultiMoveset(int[] moveAxes){
		ArrayList<CoordFive> moveset = new ArrayList<CoordFive>();
		for(int i: moveAxes) {
			moveset.addAll(getMoveset(i));
		}
		return moveset;
	}	
	
	public static ArrayList<CoordFive> getMoveset(int moveAxi){
		ArrayList<CoordFive> moveset = new ArrayList<CoordFive>();
		
		switch(moveAxi)
		{
		case 1:
			for(CoordFive c : RookMoveset) {
				moveset.add(c);				
			}
			break;
		case 2:
			for(CoordFive c : BishopMoveset) {
				moveset.add(c);				
			}
			break;
		case 3:
			for(CoordFive c : UnicornMoveset) {
				moveset.add(c);				
			}
			break;
		case 4:
			for(CoordFive c : DragonMoveset) {
				moveset.add(c);				
			}
			break;
		default:
			break;
		}
		return moveset;
		
		
	}
	
}
