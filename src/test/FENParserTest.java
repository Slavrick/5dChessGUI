package test;
import engine.Board;
import engine.CoordFive;
import engine.GameState;
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
	
	public static void testFENFileParser() {
		GameState g = FENParser.FENtoGS("res/Rookie.FEN.txt");
		//assert g != null : g;
		if(g != null) {
			g.printMultiverse();			
		}
	}
	
	public static void testTimeLineParser(){
		FENParser.FENtoTL("r2k/4/2K1/P2K;;-;b1;2;(0,3,1,1)(0,1,1,1);(2,1,2,1)(1,2,2,1)", 4, 4, 0).printTimleline();
	}
	
}
