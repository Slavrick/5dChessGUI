package main;
import test.BranchTester;
import test.FENParserTest;
import test.RookMoveTest;
import test.TimeLineTest;
import engine.Board;
import engine.Board.piece;


public class Main {
	public static void main(String[] args) {
		FENParserTest.testSTDBoard();
		TimeLineTest.testTLPrint();
		BranchTester.testMultiverse();
	}
	
	public static void BoardTest() {
		RookMoveTest.CheckRookMoves();
		System.out.println("tests Complete");
		System.out.println((int) piece.BROOK.ordinal());
	}
}
