package test;
import engine.Board;
/*
 * A class to make a blank board. This is largely depricated due to the creation of the FEN parser
 */
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
