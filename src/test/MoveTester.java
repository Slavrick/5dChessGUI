package test;

import java.util.ArrayList;

import engine.CoordFour;
import engine.GameState;
import engine.MoveGenerator;
import engine.MoveNotation;

public class MoveTester {
	public static void TestBishopMoves() {
		System.out.println("Testing Bishop/Knigth movement Generation");
		GameState g = BranchTester.getTestGS();
		ArrayList<CoordFour> dests = MoveGenerator.getRiderMoves(g, true, new CoordFour(5,3,3,-1), MoveNotation.BISHOPMOVESET);
		//System.out.println(dests);
		//System.out.println(dests.size());
		assert dests.size() == 18;
		ArrayList<CoordFour> dests2 = MoveGenerator.getLeaperMovesandCaptures(g, true, new CoordFour(2,2,3,0), MoveNotation.KNIGHTMOVESET);
		//System.out.println(dests2);
		//System.out.println(dests2.size());
		assert dests2.size() == 15;
		// TODO fix warning
		ArrayList[] dests3 = MoveGenerator.getRiderMovesAndCaps(g, true, new CoordFour(5,3,3,-1), MoveNotation.BISHOPMOVESET);
		//System.out.println(dests3[0]);
		//System.out.println(dests3[1]);
		assert dests3[1].size() == 1;
		System.out.println("    Done.");
	}
}
