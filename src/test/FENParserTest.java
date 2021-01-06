package test;
import engine.Board;
import engine.CoordFour;
import engine.GameState;
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
		GameState g = FENParser.FENtoGSNew("res/RookTactics4.FEN.txt");
		assert g != null : g;
		assert g.getColor() == false;
		assert g.minTL == 0;
		assert g.maxTL == 1;
		assert g.getTimeline(0).Tend == 2;
		assert g.getTimeline(1).Tend == 2;
		System.out.println(" passed.");
	}
	
}
