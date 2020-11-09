package test;

import engine.Board;
import engine.Piece;
import engine.MoveGenerator;

public class RookMoveTest {
	public static void CheckRookMoves() {
		Board b = NullBoardMaker.genNullBoard(8);
		System.out.println("Null board: ");
		System.out.println(b);
		b.brd[3][3] = Board.piece.WROOK.ordinal();
		b.brd[1][3] = Board.piece.WROOK.ordinal();
		System.out.println("Rook board: ");
		System.out.println(b);
		MoveGenerator.check2dMoves(b,true);
		
	}
}
