package engine;

public class Board {

	// public Piece[][] b;
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

	public final int numTypes = 21;

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
		return brd[x][y];
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

	public boolean isInBounds(CoordFive cf) {
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
