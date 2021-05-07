package engine;

import java.util.ArrayList;
import java.util.Collection;

import GUI.Controller;
import sun.awt.KeyboardFocusManagerPeerImpl;

public class MoveGenerator {

	public static final int EMPTYSQUARE = Board.piece.EMPTY.ordinal();
	public static final int WKING = 7;
	public static final int BKING = 17;
	public static final int UNMOVEDROOK = -4;

	public boolean canCaptureSquare(GameState g, boolean color, CoordFour origin, CoordFour target, int pieceType) {
		boolean rider = Board.getColorBool(pieceType);
		if (pieceType <= 0 || pieceType > (Board.numTypes) * 3) {
			return false;
		}
		CoordFour vectorTo = CoordFour.sub(target, origin);
		// FIXME finish this function.
		return false;
	}

	public static ArrayList<CoordFour> getAllCheckingPieces(GameState g) {
		ArrayList<CoordFour> attackingPieces = new ArrayList<CoordFour>();
		for (Timeline t : g.multiverse) {
			if (t.colorPlayable != g.color) {
				attackingPieces.addAll(getCheckingPieces(g, new CoordFive(0, 0, 0, t.Tend, !g.color)));
			}
		}
		return attackingPieces;
	}

	// TODO make this based off of layer rather than abs board.
	// TODO return some sort of map on informatino of that piece? test this.
	// TODO return moves rather than coords.
	public static ArrayList<CoordFour> getCheckingPieces(GameState g, CoordFive spatialCoord) {
		ArrayList<CoordFour> attackingPieces = new ArrayList<CoordFour>();
		Board b = g.getBoard(spatialCoord);
		if (b == null) {
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
						attackedPiece = attackedPiece < 0 ? attackedPiece * -1 : attackedPiece;
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

	// Generates all moves from a given board.
	public static ArrayList<Move> getAllMoves(GameState g, boolean color, int T, int L) {
		ArrayList<Move> moves = new ArrayList<Move>();
		Board b = g.getBoard(new CoordFive(0, 0, T, L, color));
		for (int x = 0; x < g.width; x++) {
			for (int y = 0; y < g.height; y++) {
				int piece = b.getSquare(x, y);
				if (Board.getColorBool(piece) == color) {
					CoordFour srcLocation = new CoordFour(x, y, T, L);
					ArrayList<CoordFour> moveLocations = getMoves(piece, g, new CoordFive(srcLocation, color));
					if (moveLocations == null) {
						continue;
					}
					for (CoordFour dest : moveLocations) {
						moves.add(new Move(srcLocation, dest));
					}
				}
			}
		}
		return moves;
	}

	public static ArrayList<CoordFour> getMoves(int piece, GameState g, CoordFive source) {
		boolean unMoved = false;
		if (piece < 0) {
			unMoved = true;
			piece *= -1;
		}
		if (piece == 0)
			return null;
		if (piece == Board.piece.WPAWN.ordinal() || piece == Board.piece.BPAWN.ordinal()) {
			return getPawnMoves(piece, g, source, unMoved);
		}
		if(piece == Board.piece.WBRAWN.ordinal() || piece == Board.piece.BBRAWN.ordinal()) {
			ArrayList<CoordFour> moves = getPawnMoves(piece, g, source, unMoved);
			moves.addAll(getCaptures(piece,g,source));
			return moves;
		}
		if (piece == 7 || piece == 17) {
			ArrayList<CoordFour> moves = new ArrayList<CoordFour>();
			if (unMoved) {
				CoordFour rookLocq = kingCanCastle(g.getBoard(source), source, true);
				CoordFour rookLock = kingCanCastle(g.getBoard(source), source, false);
				if (rookLocq != null) {
					moves.add(rookLocq);
				}
				if (rookLock != null) {
					moves.add(rookLock);
				}
			}
			moves.addAll(MoveGenerator.getLeaperMovesandCaptures(g, source.color, source,
					MoveNotation.getMoveVectors(piece)));
			return moves;
		}
		if (MoveNotation.pieceIsRider(piece)) {
			return MoveGenerator.getRiderMoves(g, source.color, source, MoveNotation.getMoveVectors(piece));
		} else {
			return MoveGenerator.getLeaperMovesandCaptures(g, source.color, source, MoveNotation.getMoveVectors(piece));
		}
	}

	// TODO possibly make a function for only king captures.
	public static ArrayList<CoordFour> getCaptures(int piece, GameState g, CoordFive source) {
		boolean unMoved = false;
		if (piece < 0) {
			unMoved = true;
			piece *= -1;
		}
		if (piece == Board.piece.WPAWN.ordinal()) {
			return getLeaperCaptures(g, source.color, source, MoveNotation.whitePawnattack);
		}
		if (piece == Board.piece.BPAWN.ordinal()) {
			return getLeaperCaptures(g, source.color, source, MoveNotation.blackPawnattack);
		}
		if (piece == Board.piece.WBRAWN.ordinal()) {
			return getLeaperCaptures(g, source.color, source, MoveNotation.whiteBrawnattack);
		}
		if (piece == Board.piece.BBRAWN.ordinal()) {
			return getLeaperCaptures(g, source.color, source, MoveNotation.blackBrawnattack);
		}
		if (MoveNotation.pieceIsRider(piece)) {
			return MoveGenerator.getRiderCaptures(g, source.color, source, MoveNotation.getMoveVectors(piece));
		} else {
			return MoveGenerator.getLeaperCaptures(g, source.color, source, MoveNotation.getMoveVectors(piece));
		}
	}

	private static ArrayList<CoordFour> getPawnMoves(int piece, GameState g, CoordFive source, boolean unmoved) {
		ArrayList<CoordFour> destCoords = new ArrayList<CoordFour>();
		// Checks whether to go one or 2 squares.
		if (unmoved) {
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
		//Add en passent
		if(g.getBoard(source).enPassentSquare != null) {
			CoordFour enPassent = g.getBoard(source).enPassentSquare;
			CoordFour left;
			CoordFour right;
			if(source.color) {
				left = CoordFour.add(source,MoveNotation.whitePawnattack[0]);
				right = CoordFour.add(source,MoveNotation.whitePawnattack[1]);
			}else {
				left = CoordFour.add(source,MoveNotation.blackPawnattack[0]);
				right = CoordFour.add(source,MoveNotation.blackPawnattack[1]);
			}
			if(left.spatialEquals(enPassent)) {
				destCoords.add(left);
			}
			else if(right.spatialEquals(enPassent)) {
				destCoords.add(right);
			}
		}
		return destCoords;
	}

	public static ArrayList<CoordFour> getLeaperMovesandCaptures(GameState g, boolean color, CoordFour sourceCoord,
			CoordFour[] movementVec) {
		ArrayList<CoordFour> destCoords = new ArrayList<CoordFour>();
		for (CoordFour leap : movementVec) {
			int piece = g.getSquare(CoordFour.add(sourceCoord, leap), color);
			if (piece == Board.ERRORSQUARE) {
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
			if (piece == EMPTYSQUARE || piece == Board.ERRORSQUARE) {
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
			if (piece == Board.ERRORSQUARE) {
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
		while (true) {
			int currPiece = b.getSquare(currSquare);
			if (currPiece == Board.ERRORSQUARE) {
				return null;
			}
			if (currPiece != EMPTYSQUARE) {
				boolean currColor = Board.getColorBool(currPiece);
				if (currColor == color) {
					return null;
				}
				return currSquare;
			}
			currSquare.add(movementVec);
		}
	}

	private static CoordFour getTemporalRiderCaptures(GameState g, boolean color, CoordFive source,
			CoordFour movementVec) {
		CoordFour currSquare = CoordFour.add(source, movementVec);
		while (true) {
			int currPiece = g.getSquare(currSquare, color);
			if (currPiece == Board.ERRORSQUARE) {
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
			int currPiece = b.getSquare(currSquare); //TODO this can be optimized, getsquare has bounds checking now
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
			int currPiece = b.getSquare(currSquare); // TODO this can be optimized, getsquare has bounds checking now
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
		while (currPiece != Board.ERRORSQUARE) {
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
		while (currPiece != Board.ERRORSQUARE) {
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

	//returns the location of an unmoved rook.
	public static CoordFour kingCanCastle(Board b, CoordFive kingSquare, boolean kside) {
		int unmvdRk = UNMOVEDROOK;
		if (!kingSquare.color) {
			unmvdRk -= Board.numTypes;
		}
		if (kside) {
			// Check For Clearance.
			CoordFour left = new CoordFour(1, 0, 0, 0);
			CoordFour index = CoordFour.add(kingSquare, left);
			while (b.getSquare(index) == EMPTYSQUARE) {
				index.add(left);
			}
			int firstNonEmpty = b.getSquare(index);
			if (firstNonEmpty != unmvdRk) {
				return null;
			}
			// Check For check
			CoordFive target = kingSquare.clone();
			if (MoveGenerator.isSquareAttacked(b, target)) {
				return null;
			}
			target.add(left);
			if (MoveGenerator.isSquareAttacked(b, target)) {
				return null;
			}
			target.add(left);
			if (MoveGenerator.isSquareAttacked(b, target)) {
				return null;
			}
			return index;
		} else {
			// Check For Clearance.
			CoordFour right = new CoordFour(-1, 0, 0, 0);
			CoordFour index = CoordFour.add(kingSquare, right);
			while (b.getSquare(index) == EMPTYSQUARE) {
				index.add(right);
			}
			int firstNonEmpty = b.getSquare(index);
			if (firstNonEmpty != unmvdRk) {
				return null;
			}
			// Check For check
			CoordFive target = kingSquare.clone();
			if (MoveGenerator.isSquareAttacked(b, target)) {
				return null;
			}
			target.add(right);
			if (MoveGenerator.isSquareAttacked(b, target)) {
				return null;
			}
			target.add(right);
			if (MoveGenerator.isSquareAttacked(b, target)) {
				return null;
			}
			return index;
		}
	}

	// For now, somewhat counterIntuitively this checks for pieces of opposite CF
	// color,
	//TODO fix this, it doesnt work but look around given square on a queen/knight basis rather than searching like this.
	private static boolean isSquareAttacked(Board b, CoordFive target) {
		for (int x = 0; x < b.width; x++) {
			for (int y = 0; y < b.height; y++) {
				int piece = b.getSquare(x, y);
				if (piece != EMPTYSQUARE && Board.getColorBool(piece) != target.color) {
					Move attack = new Move(new CoordFour(x, y, 0, 0), target);
					if (MoveGenerator.validateSpatialPath(b, piece, attack)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	//TODO not sure this works with pawns/brawns. -- it doesnt.
	private static boolean validateSpatialPath(Board b, int piece, Move attack) {
		CoordFour attackVector = CoordFour.sub(attack.origin, attack.dest);
		attackVector.flatten();
		if (!arrContains(MoveNotation.getMoveVectors(piece), attackVector)) {
			return false;
		}
		CoordFour index = attack.origin.clone();
		index.add(attackVector);
		while (!index.equals(attack.dest)) {
			if (b.getSquare(index) != EMPTYSQUARE) {
				return false;
			}
			index.add(attackVector);
		}
		return true;
	}

	public static boolean arrContains(CoordFour[] array, CoordFour target) {
		for (CoordFour element : array) {
			if (element.equals(target)) {
				return true;
			}
		}
		return false;
	}
	
	//this will return the origin square given a coord, used for parsing the moves such as Nf3
	//where you would not know the destination and would have to search for it.
	//this only works for spatial moves, really full coordinates should be entered for anything other than a spatial move.
	public static CoordFour reverseLookup(GameState g, CoordFive destSquare, int pieceType, int rank, int file) {
		Board b = g.getBoard(destSquare);
		if(b == null) {
			return null;
		}
		if(MoveNotation.pieceIsRider(pieceType)) {
			CoordFour[] moveVecs = MoveNotation.getMoveVectors(pieceType);
			for(CoordFour vector : moveVecs) {
				if(vector.isSpatial()) {
					CoordFour result = destSquare.clone();
					while(true) {
						result = CoordFour.sub(result, vector);
						int square = b.getSquare(result);
						square = square < 0 ? square * -1 : square;
						if(square == EMPTYSQUARE) {
							continue;
						}
						if(square == Board.ERRORSQUARE) {
							break;
						}
						if(square == pieceType) {
							if( (file == -1 || result.x == file) && (rank == -1 || result.y == rank)) {
								return result;
							}
						}
						break;
					}
				}
			}
		}else {
			CoordFour[] moveVecs = MoveNotation.getMoveVectors(pieceType);
			if(pieceType == Board.piece.WPAWN.ordinal()) {
				moveVecs = MoveNotation.whitePawnRLkup;
			}else if(pieceType == Board.piece.WPAWN.ordinal() + Board.numTypes) {
				moveVecs = MoveNotation.blackPawnRLkup;
			}else if(pieceType == Board.piece.WBRAWN.ordinal()) {
				moveVecs = MoveNotation.whiteBrawnRLkup;
			}else if(pieceType == Board.piece.WBRAWN.ordinal() + Board.numTypes) {
				moveVecs = MoveNotation.blackBrawnRLkup;
			}
			for(CoordFour vector : moveVecs) {
				if(vector.isSpatial()) {
					CoordFour result = CoordFour.sub(destSquare, vector);
					if(b.getSquare(result) == pieceType || b.getSquare(result) * -1 == pieceType) {
						if( (file == -1 || result.x == file) && (rank == -1 || result.y == rank)) {
							return result;
						}
					}
				}
			}
		}
		return null;
	}
	
	//search for piece, returns first index of the piece, searching from 0, width 0, height
	public static CoordFour findPiece(Board b, int target) {
		for (int x = 0; x < b.width; x++) {
			for (int y = 0; y < b.height; y++) {
				if(b.getSquare(x,y) == target) {
					return new CoordFour(x,y,0,0);
				}
			}
		}
		return null;
	}
}
