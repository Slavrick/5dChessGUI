package test;

import engine.Board;
import engine.CoordFour;
import engine.GameState;
import engine.Move;
import fileIO.FENParser;

/*
 * a general purpose class for testing the FEN parser.
 */
public class FENParserTest {

	public static void testMoveParser() {
		System.out.print("    Testing Move Parser: ");
		CoordFour x = FENParser.stringtoCoord("(0,0,0,0)");
		assert new CoordFour(0, 0, 0, 0).equals(x) : x;
		x = FENParser.stringtoCoord("(10,5,10,5)");
		assert new CoordFour(10, 5, 10, 5).equals(x) : x;
		x = FENParser.stringtoCoord("(-10,-5,-2,-1)");
		assert new CoordFour(-10, -5, -2, -1).equals(x) : x;
		System.out.println("passed.");
	}

	public static void testFENFileParser() {
		System.out.print("    Testing Parsing Rookie.FEN.txt:");
		GameState g = FENParser.shadSTDGSM("res/Puzzles/RookTactics4.PGN5.txt");
		assert g != null : g;
		assert g.getColor() == false;
		assert g.minTL == 0;
		assert g.maxTL == 1;
		assert g.getTimeline(0).Tend == 2;
		assert g.getTimeline(1).Tend == 2;
		System.out.println(" passed.");
	}

	public static void testSANParser() {
		System.out.println("    Testing SAN Parsing.");
		String san1 = "a1";
		String san2 = "h8";
		String san3 = "e4";
		String san4 = "m42";
		CoordFour c1 = FENParser.SANtoCoord(san1);
		CoordFour c2 = FENParser.SANtoCoord(san2);
		CoordFour c3 = FENParser.SANtoCoord(san3);
		CoordFour c4 = FENParser.SANtoCoord(san4);
		CoordTester.testCoord(c1, 0, 0, 0, 0);
		CoordTester.testCoord(c2, 7, 7, 0, 0);
		CoordTester.testCoord(c3, 4, 3, 0, 0);
		CoordTester.testCoord(c4, 12, 41, 0, 0);
	}

	// this needs to be beefed out anyway.
	public static void testShadParser() {
		System.out.println("    Testing Full Coord Parser.");
		CoordFour h1 = FENParser.halfStringToCoord("(0T0)a1", false);
		CoordFour h2 = FENParser.halfStringToCoord("(0T0)Na1", false);
		CoordFour h3 = FENParser.halfStringToCoord("(0T1)e3", false);
		CoordFour h4 = FENParser.halfStringToCoord("(-1T3)Nh7", false);
		CoordFour h5 = FENParser.halfStringToCoord("(-0T3)Nh7", true);
		CoordFour h6 = FENParser.halfStringToCoord("(+0T3)Nh7", true);
		CoordFour h7 = FENParser.halfStringToCoord("(2T3)Nh7", true);
		CoordFour h8 = FENParser.halfStringToCoord("(-1T3)Nh7", true);
		CoordTester.testCoord(h1, 0, 0, 0, 0);
		CoordTester.testCoord(h2, 0, 0, 0, 0);
		CoordTester.testCoord(h3, 4, 2, 1, 0);
		CoordTester.testCoord(h4, 7, 6, 3, -1);
		CoordTester.testCoord(h5, 7, 6, 3, 0);
		CoordTester.testCoord(h6, 7, 6, 3, 1);
		CoordTester.testCoord(h7, 7, 6, 3, 3);
		CoordTester.testCoord(h8, 7, 6, 3, -1);
		Move f1 = FENParser.fullStringToCoord("(0T1)Ng1(0T1)f3", false);
		Move f2 = FENParser.fullStringToCoord("(0T3)Ng1>(0T2)g3", false);
		Move f3 = FENParser.fullStringToCoord("(0T5)Qc3>>(0T1)f7", false);
		CoordTester.testCoord(f1.origin, 6, 0, 1, 0);
		CoordTester.testCoord(f1.dest, 5, 2, 1, 0);
		CoordTester.testCoord(f2.origin, 6, 0, 3, 0);
		CoordTester.testCoord(f2.dest, 6, 2, 2, 0);
		CoordTester.testCoord(f3.origin, 2, 2, 5, 0);
		CoordTester.testCoord(f3.dest, 5, 6, 1, 0);
	}

	public static void testShadFEN() {
		System.out.println("    Testing Whole Fen Parser.");
		FENParser.shadSTDGSM("res/Puzzles/Brawn Tactics 1.5DPGN.txt");
		FENParser.shadSTDGSM("res/testPGNs/AmbiguityCheck.txt");
	}

	public static void testAmbiguityInfoParser() {
		System.out.println("    Testing Ambiguity Getter.");
		CoordFour t1 = FENParser.getAmbiguityInfo("Ng3");
		CoordTester.testCoord(t1, -1, -1, -1, -1);
		CoordFour t2 = FENParser.getAmbiguityInfo("Nfg3");
		CoordTester.testCoord(t2, 5, -1, -1, -1);
		CoordFour t3 = FENParser.getAmbiguityInfo("N3g3");
		CoordTester.testCoord(t3, -1, 2, -1, -1);
		CoordFour t4 = FENParser.getAmbiguityInfo("Nc5g3");
		CoordTester.testCoord(t4, 2, 4, -1, -1);
		CoordFour t5 = FENParser.getAmbiguityInfo("(0T1)N1g3");
		CoordTester.testCoord(t5, -1, 0, -1, -1);
	}

}
