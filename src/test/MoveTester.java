package test;

import java.util.ArrayList;

import engine.CoordFour;
import engine.MoveGenerator;
import engine.MoveNotation;

public class MoveTester {
	public static void TestBishopMoves() {
		ArrayList<CoordFour> dests = MoveGenerator.getMoves(BranchTester.getTestGS(), true, new CoordFour(5,3,3,-1), MoveNotation.BishopMoveset);
		System.out.println(dests);
		System.out.println(dests.size());
	}
}
