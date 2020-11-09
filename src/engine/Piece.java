package engine;

public class Piece {
	
	
	public boolean nullPiece;
	public boolean color;
	public boolean isKing;
	public PieceType pt;

	public enum PieceType{
		NULL,
		BISHOP,
		KING,
		DRAGON,
		KNIGHT,
		PAWN,
		PRINCESS,
		QUEEN,
		ROOK,
		UNICORN
	}
	
	public Piece(PieceType p, boolean color) {
		pt = p;
		if(pt == PieceType.NULL) {
			nullPiece = true;
			return;
		}
		this.color = color;
		if(pt == PieceType.KING) {
			isKing = true;
		}
	}
	
	
	boolean isColor(boolean compareColor) {
		if(nullPiece)
			return false;
		return color == compareColor;
	}
	
	boolean isWhite() {
		return color;
	}
	
	boolean isBlack() {
		return !color;
	}
	
	public String toString() {
		String temp = "";
		switch(this.pt) {
		case ROOK:
			temp = "r";
			break;
		case KNIGHT:
			temp = "n";
			break;
		case BISHOP:
			temp = "b";
			break;
		default:
			temp = " ";
			break;
		}
		if(this.color) {
			temp = temp.toUpperCase();
		}
		return temp;
	}
	
	
}
