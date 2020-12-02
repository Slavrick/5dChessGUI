package engine;

import java.util.ArrayList;

import engine.Board.pieceColor;

public class MoveGenerator {
	
	public static final int EMPTYSQUARE = Board.piece.EMPTY.ordinal();
	
	public static Move[] check2dMoves(Board b, boolean color) {
		ArrayList<CoordFour> movablePieces = new ArrayList<CoordFour>();
		for(int x = 0; x < b.width; x++) {
			for(int y = 0; y < b.height; y++) {
				if(b.brd[x][y]!= Board.piece.EMPTY.ordinal()) {//b.b[x][y].isColor(color)) { @TODO fix this to make it better, or just remove this later.
					movablePieces.add(new CoordFour(x,y));
				}
			}
		}
			
		for( CoordFour coord : movablePieces) {
			System.out.println(coord);
		}
		System.out.println(movablePieces);
		return null;
	}
	
	public static ArrayList<CoordFour> getLeaperMoves(GameState g, boolean color, CoordFour sourceCoord, CoordFour[] movementVec){
		ArrayList<CoordFour> destCoords = new ArrayList<CoordFour>();
		for(CoordFour leap: movementVec) {
			int piece = g.getSquare(CoordFour.add(sourceCoord, leap), color);
			if(piece == -1) {
				continue;
			}
			if(piece == EMPTYSQUARE) {
				destCoords.add(CoordFour.add(sourceCoord, leap));
			}
			else if( Board.getColorBool(piece) != color ) {
				destCoords.add(CoordFour.add(sourceCoord, leap));
			}
		}
		return destCoords; 
	}
	
	/**
	 * Takes a list of rider vectors and returns everywhere they can go
	 * 
	 * @param g
	 * @param color
	 * @param sourceCoord
	 * @param movementVec an array of vectors the rider can move on
	 * @return a list of every coordinate that the rider can reach with given movement vector
	 */
	public static ArrayList<CoordFour> getRiderMoves(GameState g, boolean color, CoordFour sourceCoord, CoordFour[] movementVec) {
		ArrayList<CoordFour> moveList = new ArrayList<CoordFour>();
		for(CoordFour cf: movementVec) {
			if(cf.isSpatial()) {
				moveList.addAll(MoveGenerator.getSpatialRiderMoves(g, color, sourceCoord, cf));
			}
			else {
				moveList.addAll(MoveGenerator.getTemporalRiderMoves(g, color, sourceCoord, cf));
			}
		}
		return moveList;
	}
	
	/**
	 * Takes a square, vector and color, and returns all the coordinates that the piece can go to.
	 * 
	 * @param g gamestate to search
	 * @param color boolean color of piece searching
	 * @param sourceCoord where the piece starts
	 * @param movementVec the movement vector of the piece. ie a rook moving to the right would be (1,0,0,0)
	 * @return returns an arraylist of destination coordinates.
	 */
	public static ArrayList<CoordFour> getSpatialRiderMoves(GameState g, boolean color, CoordFour sourceCoord, CoordFour movementVec){
		ArrayList<CoordFour> destCoords = new ArrayList<CoordFour>();
		if(!movementVec.isSpatial()) {
			return null;
		}
		Board b = g.getBoard(sourceCoord, color);
		CoordFour currSquare = CoordFour.add(sourceCoord, movementVec);
		while(b.isInBounds(currSquare)) {
			int currPiece = b.getSquare(currSquare); //@TODO this can be optimized, getsquare has bounds checking now
			if(currPiece != EMPTYSQUARE) {
				boolean currColor = Board.getColor(currPiece) == pieceColor.WHITE;
				if(currColor == color) {
					break;
				}
				destCoords.add(currSquare);
				break;
			}
			destCoords.add(currSquare.clone());
			currSquare.add(movementVec);
			
		}
		return destCoords;
	}
	
	
	/**
	 * 
	 * @param g gamestate to search
	 * @param color color boards to search
	 * @param sourceCoord source of coordinate
	 * @param movementVec movment vecotor of rider
	 * @return all coordinates that a rider can move with given source and vector
	 */
	public static ArrayList<CoordFour> getTemporalRiderMoves(GameState g, boolean color, CoordFour sourceCoord, CoordFour movementVec){
		ArrayList<CoordFour> destCoords = new ArrayList<CoordFour>();
		CoordFour currSquare = CoordFour.add(sourceCoord, movementVec);
		int currPiece = g.getSquare(currSquare, color);
		while(currPiece != -1) {
			if(currPiece == EMPTYSQUARE) {
				destCoords.add(currSquare.clone());
			}
			else {
				boolean currColor = Board.getColor(currPiece) == pieceColor.WHITE;
				if(currColor == color) {
					break;
				}
				destCoords.add(currSquare);
				break;
			}
			currSquare.add(movementVec);
			currPiece = g.getSquare(currSquare, color);
		}
		return destCoords;
	}
	
	public static ArrayList<CoordFour> getMoves(Board b, CoordFour sourceCoord, CoordFour movementVec) {
		
		return null;
	}
	

	
}
