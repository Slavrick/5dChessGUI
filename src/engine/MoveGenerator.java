package engine;

import java.util.ArrayList;

public class MoveGenerator {
	
	
	
	
	public static Move[] check2dMoves(Board b, boolean color) {
		ArrayList<CoordFive> movablePieces = new ArrayList<CoordFive>();
		for(int x = 0; x < b.width; x++) {
			for(int y = 0; y < b.height; y++) {
				if(b.brd[x][y]!= Board.piece.EMPTY.ordinal()) {//b.b[x][y].isColor(color)) { @TODO fix this to make it better, or just remove this later.
					movablePieces.add(new CoordFive(x,y));
				}
			}
		}
		
		for( CoordFive coord : movablePieces) {
			System.out.println(coord);
			getRookMoves(b,coord);
			
		}
		System.out.println(movablePieces);
		return null;
	}
	
	public static ArrayList<CoordFive> getRiderMoves(GameState g, CoordFive sourceCoord, CoordFive movementVec){
		ArrayList<CoordFive> destCoords = new ArrayList<CoordFive>();
		return destCoords; //@TODO get this thing working
	}
	
	public static ArrayList<CoordFive> getRookMoves(Board b, CoordFive sourceCoord) {
		if(!b.isInBounds(sourceCoord))
			return null;
		ArrayList<CoordFive> destCoords = new ArrayList<CoordFive>();
		int x = sourceCoord.x;
		int y = sourceCoord.y;
		x++;
		while(b.isInBounds(x, y) && b.brd[x][y] == Board.piece.EMPTY.ordinal()) {
			destCoords.add(new CoordFive(x,y));
			x++;
		}
		x = sourceCoord.x;
		y = sourceCoord.y;
		x--;
		while(b.isInBounds(x, y) && b.brd[x][y] == Board.piece.EMPTY.ordinal()) {
			destCoords.add(new CoordFive(x,y));
			x--;
		}
		x = sourceCoord.x;
		y = sourceCoord.y;
		y++;
		while(b.isInBounds(x, y) && b.brd[x][y] == Board.piece.EMPTY.ordinal()) {
			destCoords.add(new CoordFive(x,y));
			y++;
		}
		x = sourceCoord.x;
		y = sourceCoord.y;
		y--;
		while(b.isInBounds(x, y) && b.brd[x][y] == Board.piece.EMPTY.ordinal()) {
			destCoords.add(new CoordFive(x,y));
			y--;
		}
		
		
		System.out.println(destCoords);
		return destCoords;
	}
	
	public static ArrayList<CoordFive> getMoves(Board b, CoordFive sourceCoord, CoordFive movementVec) {
		
		return null;
	}
	
	public static ArrayList<CoordFive> getMoves(Board b, CoordFive sourceCoord, CoordFive[] movementVec) {
		ArrayList<CoordFive> moveList = new ArrayList<CoordFive>();
		for(CoordFive cf: movementVec) {
			moveList.addAll(getMoves(b,sourceCoord,cf));
		}
		return moveList;
	}
	
}
