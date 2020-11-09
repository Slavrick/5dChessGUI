package test;
import engine.Board;
import engine.Piece;

public class NullBoardMaker {

	public static Board genNullBoard(int width) {
		Board b = new Board(width,width);
		for(int i = 0; i < width; i++) {
			for(int j = 0; j < width; j++) {
				b.brd[i][j] = Board.piece.EMPTY.ordinal();
			}
		}
		return b;
	}
}
