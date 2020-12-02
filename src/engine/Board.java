package engine;

public class Board {

	//make this private, would be better for certain reasons.
	public int[][] brd;
	public int height;
	public int width;
	public int time;
	public int layer;
	public int color;
	
	public boolean brdColor;
	public boolean wkingSideCastle;
	public boolean wqueenSideCastle;
	public boolean bkingSideCastle;
	public boolean bqueenSideCastle;

	public static final int numTypes = 21;

	public static enum pieceColor {
		NONE, WHITE, BLACK
	}
	
	public static enum piece {
		EMPTY, WPAWN, WKNIGHT, WBISHOP, WROOK, WPRINCESS, WQUEEN, WKING, WUNICORN, WDRAGON, WBRAWN, BPAWN, BKNIGHT,
		BBISHOP, BROOK, BPRINCESS, BQUEEN, BKING, BUNICORN, BDRAGON, BBRAWN
	}

	public static final char[] pieceChars = { '_', 'P', 'N', 'B', 'R', 'S', 'Q', 'K', 'U', 'D', 'W', 'p', 'n', 'b', 'r',
			's', 'q', 'k', 'u', 'd', 'w' };
	
	public Board(Board b) {
		this.brd = new int[b.width][b.height];
		for(int i = 0; i < b.width; i++) {
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

	public int getSquare(int x, int y) {
		return brd[y][x];
	}
	
	public int getSquare(CoordFour c) {
		if(isInBounds(c))
			return brd[c.y][c.x];
		else
			return -1;
	}

	/*
	 * public Piece getPieceAt(int x, int y) { return b[x][y]; }
	 */

	public boolean isInBounds(int x, int y) {
		if (x < 0 || x >= width)
			return false;
		if (y < 0 || y >= height)
			return false;
		return true;
	}

	public boolean isInBounds(CoordFour cf) {
		return isInBounds(cf.x, cf.y);
	}

	public static pieceColor getColor(int pieceCode) {
		if (pieceCode == piece.EMPTY.ordinal()) {
			return pieceColor.NONE;
		}
		if (pieceCode >= piece.BPAWN.ordinal()) {
			return pieceColor.BLACK;
		}
		return pieceColor.WHITE;
	}
	
	/**
	 * 
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

}
