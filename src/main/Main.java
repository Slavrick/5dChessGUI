package main;
import test.RookMoveTest;
import engine.Board;
import engine.Board.piece;


public class Main {
	public static void main(String[] args) {
		RookMoveTest.CheckRookMoves();
		System.out.println("tests Complete");
		System.out.println((int) piece.BROOK.ordinal());
	}
}
