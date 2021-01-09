package engine;

public class Board {

	//TODO possibly make this hold the position of pieces.
	
	// make this board private, so incorrect indexing doesnt happen
	public int[][] brd;
	public int height;
	public int width;
	// The board has no need for its location within the multiverse
	public boolean wkingSideCastle;
	public boolean wqueenSideCastle;
	public boolean bkingSideCastle;
	public boolean bqueenSideCastle;
	public CoordFour enPassentSquare;

	public static final int numTypes = 21;

	public static enum piece {
		EMPTY, WPAWN, WKNIGHT, WBISHOP, WROOK, WPRINCESS, WQUEEN, WKING, WUNICORN, WDRAGON, WBRAWN, BPAWN, BKNIGHT,
		BBISHOP, BROOK, BPRINCESS, BQUEEN, BKING, BUNICORN, BDRAGON, BBRAWN
	}

	public static final char[] pieceChars = { '_', 'P', 'N', 'B', 'R', 'S', 'Q', 'K', 'U', 'D', 'W', 'p', 'n', 'b', 'r',
			's', 'q', 'k', 'u', 'd', 'w' };

	public Board(Board b) {
		this.brd = new int[b.width][b.height];
		for (int i = 0; i < b.width; i++) {
			this.brd[i] = b.brd[i].clone();
		}
		this.height = b.height;
		this.width = b.width;
		this.wkingSideCastle = b.wkingSideCastle;
		this.wqueenSideCastle = b.wqueenSideCastle;
		this.bkingSideCastle = b.bkingSideCastle;
		this.bqueenSideCastle = b.bqueenSideCastle;
	}

	public Board(int height, int width) {
		// b = new Piece[width][height];
		brd = new int[width][height];
		this.height = height;
		this.width = width;
		wkingSideCastle = true;
		wqueenSideCastle = true;
		bkingSideCastle = true;
		bqueenSideCastle = true;

	}

	/**
	 * Gets a piececode at the coordinate specified. does bounds checking
	 * 
	 * @param x the 'file' to get. Starts at 0, so a=0, h=7
	 * @param y the 'rank' to get. Starts at 0, so 0 would be the first rank.
	 * @return an integer piece defined in the enum above, or -1 if the coord is out
	 *         of bounds.
	 */
	public int getSquare(int x, int y) {
		if (isInBounds(x, y))
			return brd[y][x];
		return -1;
	}

	/**
	 * Gets the code of the square location. does bounds checking on the coordinate
	 * generated
	 * 
	 * @param c coordinate to get piece code
	 * @return an integer piece defined in the enum above, or -1 if the coord is out
	 *         of bounds.
	 */
	public int getSquare(CoordFour c) {
		if (isInBounds(c))
			return brd[c.y][c.x];
		else
			return -1;
	}

	/**
	 * Determine whether a coordinate is in bounds
	 * 
	 * @param x the 'file' to check. Starts at 0, so a=0, h=7
	 * @param y the 'rank' to check. Starts at 0, so 0 would be the first rank.
	 * @return boolean whether the x,y was in bounds.
	 */
	public boolean isInBounds(int x, int y) {
		if (x < 0 || x >= width)
			return false;
		if (y < 0 || y >= height)
			return false;
		return true;
	}

	/**
	 * determines whether a coordinate is in bounds, according to spatial dimensions
	 * 
	 * @param cf coordinate to test
	 * @return boolean whether the coordinate is in bounds spatially
	 */
	public boolean isInBounds(CoordFour cf) {
		return isInBounds(cf.x, cf.y);
	}

	/** TODO maybe this should be in the Timeline class?
	 * Casltes a king on the present board by checking if it is possible and then swapping the pieces.
	 * @param color the color to swap
	 * @param side the side to swap to( queen or king -- false or true respectively)
	 * @return whether or not the caslte happened.
	 */
	public boolean CastleKing(boolean color, boolean side) {
		if(color) {
			if(side && wkingSideCastle) {
				wkingSideCastle = false;
				wqueenSideCastle = false;
				//TODO add code swapping pieces.
				return true;
			}
			else if(!side && wqueenSideCastle) {
				bkingSideCastle = false;
				bqueenSideCastle = false;
				return false;
			}else {
				return false;
			}
		}else if (!color) {
			return false;
		}else {
			return false;
		}
	}

	/**
	 * Gets a string representation of the board. this string starts with the 1st
	 * rank, and each rank is a line of text.
	 */
	public String toString() {
		String temp = "";
		for (int x = 0; x < this.width; x++) {
			for (int y = 0; y < this.height; y++) {
				temp += pieceChars[brd[x][y]];
			}
			temp += "\n";
		}
		return temp;
	}

	/**
	 * Gets the color of the piececode that was sent(as defined above in the piece
	 * enum above).
	 * 
	 * @param pieceCode integer that matches the enum
	 * @return true for white false for black and false for empty.
	 */
	public static boolean getColorBool(int pieceCode) {
		if (pieceCode == piece.EMPTY.ordinal()) {
			return false;
		}
		if (pieceCode >= piece.BPAWN.ordinal()) {
			return GameState.BLACK;
		}
		return GameState.WHITE;
	}


}
