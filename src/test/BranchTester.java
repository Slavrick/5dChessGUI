package test;

import engine.Board;
import engine.CoordFour;
import engine.GameState;
import engine.Move;
import engine.Timeline;
import fileIO.FENParser;

public class BranchTester {
	public static void testMultiverse() {
		GameState g = getTestGS();
		
	}

	public static GameState getTestGS() {
		return FENParser.FENtoGSNew("res/exampleGame.txt");
	}
}
