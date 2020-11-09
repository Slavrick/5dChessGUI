package test;
import engine.Board;
import fileIO.FENParser;


public class FENParserTest {

	public static void testSTDBoard() {
		Board b = FENParser.getBoardFromString("8;8;rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR;w;KQkq;-;0;1");
		
	}
}
