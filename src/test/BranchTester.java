package test;

import engine.Board;
import engine.CoordFive;
import engine.GameState;
import engine.Move;
import engine.Timeline;
import fileIO.FENParser;

public class BranchTester {
	public static void testMultiverse() {
		Board b = FENParser.getBoardFromString("8;8;rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR;w;KQkq;-;0;1");
		Timeline t = new Timeline(b,true,1,0);
		GameState g = new GameState(t);
		Move m = new Move(new CoordFive(1,3,1,0), new CoordFive(3,3,1,0));
		Move m2 = new Move(new CoordFive(6,3,1,0), new CoordFive(4,3,1,0));
		Move m3 = new Move(new CoordFive(0,1,2,0), new CoordFive(2,2,2,0));
		Move m4 = new Move(new CoordFive(7,1,2,0), new CoordFive(5,2,1,0));
		g.makeTurn(m);
		g.makeTurn(m2);
		g.makeTurn(m3);
		g.makeTurn(m4);
		g.printMultiverse();
	}

	public static GameState getTestGS() {
		Board b = FENParser.getBoardFromString("8;8;rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR;w;KQkq;-;0;1");
		Timeline t = new Timeline(b,true,1,0);
		GameState g = new GameState(t);
		Move m = new Move(new CoordFive(1,3,1,0), new CoordFive(3,3,1,0));
		Move m2 = new Move(new CoordFive(6,3,1,0), new CoordFive(4,3,1,0));
		Move m3 = new Move(new CoordFive(0,1,2,0), new CoordFive(2,2,2,0));
		Move m4 = new Move(new CoordFive(7,1,2,0), new CoordFive(5,2,1,0));
		g.makeTurn(m);
		g.makeTurn(m2);
		g.makeTurn(m3);
		g.makeTurn(m4);
		return g;
	}
}
