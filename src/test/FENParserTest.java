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
		assert new CoordFour(0,0,0,0).equals(x) : x;
		x = FENParser.stringtoCoord("(10,5,10,5)");
		assert new CoordFour(10,5,10,5).equals(x) : x;
		x = FENParser.stringtoCoord("(-10,-5,-2,-1)");
		assert new CoordFour(-10,-5,-2,-1).equals(x) : x;
		System.out.println("passed.");
	}
	
	public static void testFENFileParser() {
		System.out.print("    Testing Parsing Rookie.FEN.txt:");
		GameState g = FENParser.FENToGS("res/RookTactics4.FEN.txt");
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
		CoordFour h1 = FENParser.halfStringToCoord("(0T0)a1");
		CoordFour h2 = FENParser.halfStringToCoord("(0T0)Na1");
		CoordFour h3 = FENParser.halfStringToCoord("(0T1)e3");
		CoordFour h4 = FENParser.halfStringToCoord("(-1T3)Nh7");
		CoordFour h5 = FENParser.halfStringToCoord("(-0T3)Nh7");
		CoordTester.testCoord(h1, 0, 0, 0, 0);
		CoordTester.testCoord(h2, 0, 0, 0, 0);
		CoordTester.testCoord(h3, 4, 2, 1, 0);
		CoordTester.testCoord(h4, 7, 6, 3, -1);
		CoordTester.testCoord(h5, 7, 6, 3, 0);
		Move f1 = FENParser.fullStringToCoord("(0T1)Ng1(0T1)f3");
		Move f2 = FENParser.fullStringToCoord("(0T3)Ng1>(0T2)g3");
		Move f3 = FENParser.fullStringToCoord("(0T5)Qc3>>(0T1)f7");		
		CoordTester.testCoord(f1.origin, 6, 0, 1, 0);
		CoordTester.testCoord(f1.dest,   5, 2, 1, 0);
		CoordTester.testCoord(f2.origin, 6, 0, 3, 0);
		CoordTester.testCoord(f2.dest,   6, 2, 2, 0);
		CoordTester.testCoord(f3.origin, 2, 2, 5, 0);
		CoordTester.testCoord(f3.dest,   5, 6, 1, 0);
	}
	
	public static void testShadFEN() {
		FENParser.shadSTDGSM("res/Brawn Tactics 1.5DPGN.txt");
	}
	
}
