package test;
import engine.Board;
import engine.CoordFive;
import fileIO.FENParser;

/*
 * a general purpose class for testing the FEN parser.
 */
public class FENParserTest {

	public static void testSTDBoard() {
		Board b = FENParser.getBoardFromString("8;8;rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR;w;KQkq;-;0;1");
		System.out.println(b);
	}
	
	public static void testMoveParser() {
		System.out.println("testingMoveParser: ");
		CoordFive x = FENParser.stringtoCoord("(0,0,0,0)");
		assert new CoordFive(0,0,0,0).equals(x) : x;
		x = FENParser.stringtoCoord("(10,5,10,5)");
		assert new CoordFive(10,5,10,5).equals(x) : x;
		x = FENParser.stringtoCoord("(-10,-5,-2,-1)");
		assert new CoordFive(-10,-5,-2,-1).equals(x) : x;
		System.out.println("    Done.");
	}
	
}
