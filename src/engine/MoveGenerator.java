package engine;

import java.util.ArrayList;
import java.util.Collection;

import sun.awt.KeyboardFocusManagerPeerImpl;

public class MoveGenerator {

	public static final int EMPTYSQUARE = Board.piece.EMPTY.ordinal();
	public static final int WKING = 7;
	public static final int BKING = 17;

	// TODO return some sort of map on informatino of that piece? test this.
	// TODO return moves rather than coords.
	public static ArrayList<CoordFour> getCheckingPieces(GameState g, CoordFive spatialCoord) {
		ArrayList<CoordFour> attackingPieces = new ArrayList<CoordFour>();
		Board b = g.getBoard(spatialCoord);
		if(b == null) {
			System.out.println("Error: " + spatialCoord);
			return attackingPieces;
		}
		for (int x = 0; x < g.width; x++) {
			for (int y = 0; y < g.height; y++) {
				int piece = b.getSquare(x, y);
				if (piece != 0 && Board.getColorBool(piece) == spatialCoord.color) {
					CoordFive currSquare = new CoordFive(x, y, spatialCoord.T, spatialCoord.L, spatialCoord.color);
					ArrayList<CoordFour> currSquareCaps = getCaptures(piece, g, currSquare);
					for (CoordFour square : currSquareCaps) {
						int attackedPiece = g.getSquare(square, spatialCoord.color);
						if (attackedPiece == WKING || attackedPiece == BKING) {
							attackingPieces.add(currSquare);
						} // Yes, this will put say a queen who is checking like a spatial rook and a
							// temporal bishop in the list twice. Im trying to think if this is bad
					}
				} 
			}
		}

		return attackingPieces;
	}

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

	// TODO possibly make a function for only king captures.
	public static ArrayList<CoordFour> getCaptures(int piece, GameState g, CoordFive source) {
		if (piece == 1) {
			return getLeaperCaptures(g, source.color, source, MoveNotation.whitePawnattack);
		}
		if (piece == 11) {
			return getLeaperCaptures(g, source.color, source, MoveNotation.blackPawnattack);
		}
		if (MoveNotation.pieceIsRider(piece)) {
			return MoveGenerator.getRiderCaptures(g, source.color, source, MoveNotation.getMoveVectors(piece));
		} else {
			return MoveGenerator.getLeaperCaptures(g, source.color, source, MoveNotation.getMoveVectors(piece));
		}
	}

	private static ArrayList<CoordFour> getPawnMoves(int piece, GameState g, CoordFive source) {
		ArrayList<CoordFour> destCoords = new ArrayList<CoordFour>();
		// Checks whether to go one or 2 squares.
		if (source.y <= 1 || source.y >= g.height - 2) {
			destCoords.addAll(getSliderMoves(g, source.color, source, MoveNotation.getMoveVectors(piece), 2));
		} else {
			destCoords.addAll(getLeaperMoves(g, source.color, source, MoveNotation.getMoveVectors(piece)));
		}
		// Gets attack squares.
		CoordFour[] Movementvec;
		if (Board.getColorBool(piece)) {
			Movementvec = MoveNotation.whitePawnattack;
		} else {
			Movementvec = MoveNotation.blackPawnattack;
		}
		destCoords.addAll(getLeaperCaptures(g, source.color, source, Movementvec));
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
	
	public boolean canCaptureSquare(GameState g, boolean color, CoordFour origin, CoordFour target, int pieceType) {
		boolean rider = Board.getColorBool(pieceType);
		if(pieceType <= 0 || pieceType > Board.numTypes) {
			return false;
		}
		CoordFour vectorTo = CoordFour.sub(origin, target);
		//FIXME finish this function.
		return false;
	}
	
	/**
	 * gets a leapers moves, but only on the captures, used for pawn movement
	 * mainly.
	 * 
	 * @param g           gamestate to search
	 * @param color       color of coordinate
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
			CoordFour[] movementVec) {
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
			CoordFour[] movementVec) { // TODO make this only return moves and not captures.(or maybe make a new
										// function that does that)
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

	private static ArrayList<CoordFour> getRiderCaptures(GameState g, boolean color, CoordFive source,
			CoordFour[] moveVectors) {
		ArrayList<CoordFour> moveList = new ArrayList<CoordFour>();
		for (CoordFour cf : moveVectors) {
			if (cf.isSpatial()) {
				CoordFour capture = MoveGenerator.getSpatialRiderCapture(g, color, source, cf);
				if (capture != null) {
					moveList.add(capture);
				}
			} else {// TODO group by board on this func(it already partially is in the notation
					// definitions);
				CoordFour capture = MoveGenerator.getTemporalRiderCaptures(g, color, source, cf);
				if (capture != null) {
					moveList.add(capture);
				}
			}
		}
		return moveList;
	}

	private static CoordFour getSpatialRiderCapture(GameState g, boolean color, CoordFive source,
			CoordFour movementVec) {
		if (!movementVec.isSpatial()) {
			return null;
		}
		Board b = g.getBoard(source, color);
		CoordFour currSquare = CoordFour.add(source, movementVec);
		while (b.isInBounds(currSquare)) {
			int currPiece = b.getSquare(currSquare); // TODO this can be optimized, getsquare has bounds checking now
			if (currPiece != EMPTYSQUARE) {
				boolean currColor = Board.getColorBool(currPiece);
				if (currColor == color) {
					return null;
				}
				return currSquare;
			}
			currSquare.add(movementVec);
		}
		return null;
	}

	private static CoordFour getTemporalRiderCaptures(GameState g, boolean color, CoordFive source,
			CoordFour movementVec) {
		CoordFour currSquare = CoordFour.add(source, movementVec);
		while (true) {
			int currPiece = g.getSquare(currSquare, color);
			if (currPiece == -1) {
				break;
			}
			if (currPiece != EMPTYSQUARE) {
				if (Board.getColorBool(currPiece) != color) {
					return currSquare;
				} else {
					return null;
				}
			}
			currSquare.add(movementVec);
		}
		return null;
	}

	public static ArrayList<CoordFour>[] getRiderMovesAndCaps(GameState g, boolean color, CoordFour sourceCoord,
			CoordFour[] movementVec) {
		ArrayList<CoordFour> moveList = new ArrayList<CoordFour>();
		ArrayList<CoordFour> capList = new ArrayList<CoordFour>(); //TODO group by board so we dont have more accesses.
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
			int currPiece = b.getSquare(currSquare); // TODO this can be optimized, getsquare has bounds checking now
			if (currPiece != EMPTYSQUARE) {
				boolean currColor = Board.getColorBool(currPiece);
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
				boolean currColor = Board.getColorBool(currPiece);
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
				boolean currColor = Board.getColorBool(currPiece);
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

	private static ArrayList<CoordFour>[] getTemporalRiderMovesAndCaptures(GameState g, boolean color,
			CoordFour sourceCoord, CoordFour movementVec) {
		ArrayList<CoordFour> destCoords = new ArrayList<CoordFour>();
		ArrayList<CoordFour> capCoords = new ArrayList<CoordFour>();
		CoordFour currSquare = CoordFour.add(sourceCoord, movementVec);
		int currPiece = g.getSquare(currSquare, color);
		while (currPiece != -1) {
			if (currPiece == EMPTYSQUARE) {
				destCoords.add(currSquare.clone());
			} else {
				boolean currColor = Board.getColorBool(currPiece);
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
			CoordFour[] movementVec, int range) {
		if (range <= 0)
			return null;
		ArrayList<CoordFour> destCoords = new ArrayList<CoordFour>();
		for (CoordFour vec : movementVec) {
			CoordFour newsrc = CoordFour.add(vec, sourceCoord);
			int rangeLeft = range - 1;
			while (rangeLeft >= 0 && g.getSquare(newsrc, color) == EMPTYSQUARE) {
				destCoords.add(newsrc.clone());
				rangeLeft--;
				newsrc.add(vec);
			}
		}
		return destCoords;
	}
}
