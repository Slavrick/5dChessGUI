package test;

import engine.Board;
import engine.CoordFour;
import engine.Move;
import engine.Timeline;
import fileIO.FENParser;

public class TimeLineTest {
	public static void testTLPrint() {
		Board b = FENParser.getBoardFromString("8;8;rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR;w;KQkq;-;0;1");
		Timeline t = new Timeline(b,true,1,0);
		Move m = new Move(new CoordFour(1,3,1,0), new CoordFour(3,3,1,0));
		Move m2 = new Move(new CoordFour(6,3,1,0), new CoordFour(4,3,1,0));
		Move m3 = new Move(new CoordFour(0,1,1,0), new CoordFour(2,2,1,0));
		Move m4 = new Move(new CoordFour(7,1,1,0), new CoordFour(5,2,1,0));
		t.addSpatialMove(m,true);
		t.addSpatialMove(m2,false);
		t.addSpatialMove(m3,true);
		t.addSpatialMove(m4,false);
		t.printTimleline();
		
	}
	
	public static Timeline getTestTL() {
		Board b = FENParser.getBoardFromString("8;8;rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR;w;KQkq;-;0;1");
		Timeline t = new Timeline(b,true,1,0);
		Move m = new Move(new CoordFour(1,3,1,0), new CoordFour(3,3,1,0));
		Move m2 = new Move(new CoordFour(6,3,1,0), new CoordFour(4,3,1,0));
		Move m3 = new Move(new CoordFour(0,1,1,0), new CoordFour(2,2,1,0));
		Move m4 = new Move(new CoordFour(7,1,1,0), new CoordFour(5,2,1,0));
		t.addSpatialMove(m,true);
		t.addSpatialMove(m2,false);
		t.addSpatialMove(m3,true);
		t.addSpatialMove(m4,false);
		return t;
	}
}
