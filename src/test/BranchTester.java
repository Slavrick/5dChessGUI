package test;

import engine.Board;
import engine.CoordFour;
import engine.GameState;
import engine.Move;
import engine.Timeline;
import fileIO.FENParser;

public class BranchTester {
	public static void testMultiverse() {
		Board b = FENParser.getBoardFromString("8;8;rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR;w;KQkq;-;0;1");
		Timeline t = new Timeline(b,true,1,0);
		GameState g = new GameState(t);
		Move m = new Move(new CoordFour(1,3,1,0), new CoordFour(3,3,1,0));
		Move m2 = new Move(new CoordFour(6,3,1,0), new CoordFour(4,3,1,0));
		Move m3 = new Move(new CoordFour(0,1,2,0), new CoordFour(2,2,2,0));
		Move m4 = new Move(new CoordFour(7,1,2,0), new CoordFour(5,2,1,0));
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
		Move m = new Move(new CoordFour(3,1,1,0), new CoordFour(3,3,1,0));
		Move m2 = new Move(new CoordFour(3,6,1,0), new CoordFour(3,4,1,0));
		Move m3 = new Move(new CoordFour(1,0,2,0), new CoordFour(2,2,2,0));
		Move m4 = new Move(new CoordFour(1,7,2,0), new CoordFour(1,5,1,0));
		Move m5 = new Move(new CoordFour(2,0,2,-1), new CoordFour(5,3,2,-1));
		Move m6 = new Move(new CoordFour(1,5,2,-1), new CoordFour(2,3,2,-1));
		g.makeTurn(m);
		g.makeTurn(m2);
		g.makeTurn(m3);
		g.makeTurn(m4);
		g.makeTurn(m5);
		g.makeTurn(m6);
		return g;
	}
}
