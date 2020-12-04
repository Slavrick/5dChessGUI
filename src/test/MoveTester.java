package test;

import java.util.ArrayList;

import engine.CoordFour;
import engine.GameState;
import engine.MoveGenerator;
import engine.MoveNotation;

public class MoveTester {
	public static void TestBishopMoves() {
		GameState g = BranchTester.getTestGS();
		ArrayList<CoordFour> dests = MoveGenerator.getRiderMoves(g, true, new CoordFour(5,3,3,-1), MoveNotation.BISHOPMOVESET);
		System.out.println(dests);
		System.out.println(dests.size());
		ArrayList<CoordFour> dests2 = MoveGenerator.getLeaperMoves(g, true, new CoordFour(2,2,3,0), MoveNotation.KNIGHTMOVESET);
		System.out.println(dests2);
		System.out.println(dests2.size());
		// TODO fix warning
		ArrayList[] dests3 = MoveGenerator.getRiderMovesAndCaps(g, true, new CoordFour(5,3,3,-1), MoveNotation.BISHOPMOVESET);
		System.out.println(dests3[0]);
		System.out.println(dests3[1]);
	}
}
