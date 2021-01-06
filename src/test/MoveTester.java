package test;

import java.util.ArrayList;

import engine.Board;
import engine.CoordFive;
import engine.CoordFour;
import engine.GameState;
import engine.MoveGenerator;
import engine.MoveNotation;
import fileIO.FENParser;

public class MoveTester {
	public static void TestBishopMoves() {
		System.out.print("    Testing Bishop Movement: ");
		GameState tester = FENParser.FENtoGSNew("res/BishopMoveTester.FEN5.txt");
		assert tester != null;
		assert MoveGenerator.getCaptures(Board.piece.WBISHOP.ordinal(), tester, new CoordFive(2,3,5,0,true)).size() == 3;
		assert MoveGenerator.getCaptures(Board.piece.WBISHOP.ordinal(), tester, new CoordFive(3,2,5,1,true)).size() == 1;
		System.out.println("passed.");
	}
}
