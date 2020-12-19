package engine;

import java.util.ArrayList;

import engine.Board.pieceColor;

public class MoveGenerator {

	public static final int EMPTYSQUARE = Board.piece.EMPTY.ordinal();

	public static ArrayList<CoordFour> getMoves(int piece, GameState g, CoordFive source) {
		if (piece == 0)
			return null;
		if (piece == 1 || piece == 11) {
			return getPawnMoves(piece, g, source);
		}
		if (piece == 7 || piece == 17) {
			// piece is a king, and potentially can castle.
		}
		if (MoveNotation.pieceIsRider(piece)) {
			return MoveGenerator.getRiderMoves(g, source.color, source, MoveNotation.getMoveVectors(piece));
		} else {
			return MoveGenerator.getLeaperMovesandCaptures(g, source.color, source, MoveNotation.getMoveVectors(piece));
		}
	}

	private static ArrayList<CoordFour> getPawnMoves(int piece, GameState g, CoordFive source) {
		ArrayList<CoordFour> destCoords = new ArrayList<CoordFour>();
		//Checks whether to go one or 2 squares.
		if(source.y <= 1 || source.y >= g.height - 2) {
			destCoords.addAll(getSliderMoves(g,source.color,source,MoveNotation.getMoveVectors(piece),2));
		}else {			
			destCoords.addAll(getLeaperMoves(g, source.color, source, MoveNotation.getMoveVectors(piece)));
		}
		//Gets attack squares.
		CoordFour[] Movementvec;
		if(Board.getColorBool(piece)) {
			Movementvec = MoveNotation.whitePawnattack;
		}else {
			Movementvec = MoveNotation.blackPawnattack;
		}
		destCoords.addAll(getLeaperCaptures(g,source.color,source,Movementvec));
		return destCoords;
	}
	
	public static ArrayList<CoordFour> getLeaperMovesandCaptures(GameState g, boolean color, CoordFour sourceCoord,
			CoordFour[] movementVec) {
		ArrayList<CoordFour> destCoords = new ArrayList<CoordFour>();
		for (CoordFour leap : movementVec) {
			int piece = g.getSquare(CoordFour.add(sourceCoord, leap), color);
			if (piece == -1) {
				continue;
			}
			if (piece == EMPTYSQUARE) {
				destCoords.add(CoordFour.add(sourceCoord, leap));
			} else if (Board.getColorBool(piece) != color) {
				destCoords.add(CoordFour.add(sourceCoord, leap));
			}
		}
		return destCoords;
	}
	
	/**
	 * gets a leapers moves, but only on the captures, used for pawn movement mainly.
	 * @param g gamestate to search
	 * @param color color of coordinate
	 * @param sourceCoord source of movement
	 * @param movementVec vector to move
	 * @return a list of capturable squares.
	 */
	public static ArrayList<CoordFour> getLeaperCaptures(GameState g, boolean color, CoordFour sourceCoord,
			CoordFour[] movementVec) {
		ArrayList<CoordFour> destCoords = new ArrayList<CoordFour>();
		for (CoordFour leap : movementVec) {
			int piece = g.getSquare(CoordFour.add(sourceCoord, leap), color);
			if (piece == -1 || piece == 0) {
				continue;
			} else if (Board.getColorBool(piece) != color) {
				destCoords.add(CoordFour.add(sourceCoord, leap));
			}
		}
		return destCoords;
	}

	public static ArrayList<CoordFour> getLeaperMoves(GameState g, boolean color, CoordFour sourceCoord,
			CoordFour[] movementVec){
		ArrayList<CoordFour> destCoords = new ArrayList<CoordFour>();
		for (CoordFour leap : movementVec) {
			int piece = g.getSquare(CoordFour.add(sourceCoord, leap), color);
			if (piece == -1) {
				continue;
			}
			if (piece == EMPTYSQUARE) {
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
	 * @return a list of every coordinate that the rider can reach with given
	 *         movement vector
	 */
	public static ArrayList<CoordFour> getRiderMoves(GameState g, boolean color, CoordFour sourceCoord,
			CoordFour[] movementVec) {
		ArrayList<CoordFour> moveList = new ArrayList<CoordFour>();
		for (CoordFour cf : movementVec) {
			if (cf.isSpatial()) {
				moveList.addAll(MoveGenerator.getSpatialRiderMoves(g, color, sourceCoord, cf));
			} else {
				moveList.addAll(MoveGenerator.getTemporalRiderMoves(g, color, sourceCoord, cf));
			}
		}
		return moveList;
	}

	public static ArrayList<CoordFour>[] getRiderMovesAndCaps(GameState g, boolean color, CoordFour sourceCoord,
			CoordFour[] movementVec) {
		ArrayList<CoordFour> moveList = new ArrayList<CoordFour>();
		ArrayList<CoordFour> capList = new ArrayList<CoordFour>(); // TODO group by board so we dont have more accesses.
		for (CoordFour cf : movementVec) {
			if (cf.isSpatial()) {
				ArrayList[] list = MoveGenerator.getSpatialRiderMovesAndCaptures(g, color, sourceCoord, cf);
				moveList.addAll(list[0]);
				capList.addAll(list[1]);
			} else {
				ArrayList[] list = MoveGenerator.getTemporalRiderMovesAndCaptures(g, color, sourceCoord, cf);
				moveList.addAll(list[0]);
				capList.addAll(list[1]);
			}
		}

		ArrayList<CoordFour>[] objs = new ArrayList[2];
		objs[0] = moveList;
		objs[1] = capList;
		return objs;
	}

	/**
	 * Takes a square, vector and color, and returns all the coordinates that the
	 * piece can go to.
	 * 
	 * @param g           gamestate to search
	 * @param color       boolean color of piece searching
	 * @param sourceCoord where the piece starts
	 * @param movementVec the movement vector of the piece. ie a rook moving to the
	 *                    right would be (1,0,0,0)
	 * @return returns an arraylist of destination coordinates.
	 */
	public static ArrayList<CoordFour> getSpatialRiderMoves(GameState g, boolean color, CoordFour sourceCoord,
			CoordFour movementVec) {
		ArrayList<CoordFour> destCoords = new ArrayList<CoordFour>();
		if (!movementVec.isSpatial()) {
			return null;
		}
		Board b = g.getBoard(sourceCoord, color);
		CoordFour currSquare = CoordFour.add(sourceCoord, movementVec);
		while (b.isInBounds(currSquare)) {
			int currPiece = b.getSquare(currSquare); // @TODO this can be optimized, getsquare has bounds checking now
			if (currPiece != EMPTYSQUARE) {
				boolean currColor = Board.getColor(currPiece) == pieceColor.WHITE;
				if (currColor == color) {
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

	public static ArrayList<CoordFour>[] getSpatialRiderMovesAndCaptures(GameState g, boolean color,
			CoordFour sourceCoord, CoordFour movementVec) {
		ArrayList<CoordFour> destCoords = new ArrayList<CoordFour>();
		ArrayList<CoordFour> capCoords = new ArrayList<CoordFour>();
		if (!movementVec.isSpatial()) {
			return null;
		}
		Board b = g.getBoard(sourceCoord, color);
		CoordFour currSquare = CoordFour.add(sourceCoord, movementVec);
		while (b.isInBounds(currSquare)) {
			int currPiece = b.getSquare(currSquare); // @TODO this can be optimized, getsquare has bounds checking now
			if (currPiece != EMPTYSQUARE) {
				boolean currColor = Board.getColor(currPiece) == pieceColor.WHITE;
				if (currColor == color) {
					break;
				}
				destCoords.add(currSquare.clone());
				capCoords.add(currSquare.clone());
				break;
			}
			destCoords.add(currSquare.clone());
			currSquare.add(movementVec);

		}
		// TODO fixWarning
		ArrayList<CoordFour>[] objs = new ArrayList[2];
		objs[0] = destCoords;
		objs[1] = capCoords;
		return objs;
	}

	/**
	 * 
	 * @param g           gamestate to search
	 * @param color       color boards to search
	 * @param sourceCoord source of coordinate
	 * @param movementVec movment vecotor of rider
	 * @return all coordinates that a rider can move with given source and vector
	 */
	public static ArrayList<CoordFour> getTemporalRiderMoves(GameState g, boolean color, CoordFour sourceCoord,
			CoordFour movementVec) {
		ArrayList<CoordFour> destCoords = new ArrayList<CoordFour>();
		CoordFour currSquare = CoordFour.add(sourceCoord, movementVec);
		int currPiece = g.getSquare(currSquare, color);
		while (currPiece != -1) {
			if (currPiece == EMPTYSQUARE) {
				destCoords.add(currSquare.clone());
			} else {
				boolean currColor = Board.getColor(currPiece) == pieceColor.WHITE;
				if (currColor == color) {
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

	private static ArrayList[] getTemporalRiderMovesAndCaptures(GameState g, boolean color, CoordFour sourceCoord,
			CoordFour movementVec) {
		ArrayList<CoordFour> destCoords = new ArrayList<CoordFour>();
		ArrayList<CoordFour> capCoords = new ArrayList<CoordFour>();
		CoordFour currSquare = CoordFour.add(sourceCoord, movementVec);
		int currPiece = g.getSquare(currSquare, color);
		while (currPiece != -1) {
			if (currPiece == EMPTYSQUARE) {
				destCoords.add(currSquare.clone());
			} else {
				boolean currColor = Board.getColor(currPiece) == pieceColor.WHITE;
				if (currColor == color) {
					break;
				}
				destCoords.add(currSquare.clone());
				capCoords.add(currSquare.clone());
				break;
			}
			currSquare.add(movementVec);
			currPiece = g.getSquare(currSquare, color);
		}

		ArrayList<CoordFour>[] objs = new ArrayList[2];
		objs[0] = destCoords;
		objs[1] = capCoords;
		return objs;
	}
	
	private static ArrayList<CoordFour> getSliderMoves(GameState g, boolean color, CoordFour sourceCoord,
			CoordFour[] movementVec, int range){
		if(range <= 0)
			return null;
		ArrayList<CoordFour> destCoords = new ArrayList<CoordFour>();
		for(CoordFour vec: movementVec) {
			CoordFour newsrc = CoordFour.add(vec, sourceCoord);
			int rangeLeft = range - 1;
			while(rangeLeft >= 0 && g.getSquare(newsrc, color) == EMPTYSQUARE) {
				destCoords.add(newsrc.clone());
				rangeLeft--;
				newsrc.add(vec);
			}
		}
		return destCoords;
	}
}
