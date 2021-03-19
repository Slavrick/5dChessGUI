package test;

import engine.GameState;
import fileIO.FENParser;

public class BranchTester {
	public static void testMultiverse() {
		GameState g = getTestGS();
		
	}

	public static GameState getTestGS() {
		return FENParser.shadSTDGSM("res/testPGNs/ExmpleGame.PGN5.txt");
	}
}
