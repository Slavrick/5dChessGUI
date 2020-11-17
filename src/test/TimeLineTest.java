package test;

import engine.Board;
import engine.CoordFive;
import engine.Move;
import engine.Timeline;
import fileIO.FENParser;

public class TimeLineTest {
	public static void testTLPrint() {
		Board b = FENParser.getBoardFromString("8;8;rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR;w;KQkq;-;0;1");
		Timeline t = new Timeline(b,true,1,0);
		Move m = new Move(new CoordFive(1,3,1,0), new CoordFive(3,3,1,0));
		Move m2 = new Move(new CoordFive(6,3,1,0), new CoordFive(4,3,1,0));
		Move m3 = new Move(new CoordFive(0,1,1,0), new CoordFive(2,2,1,0));
		Move m4 = new Move(new CoordFive(7,1,1,0), new CoordFive(5,2,1,0));
		t.addSpatialMove(m,true);
		t.addSpatialMove(m2,false);
		t.addSpatialMove(m3,true);
		t.addSpatialMove(m4,false);
		t.printTimleline();
		
	}
	
	public static Timeline getTestTL() {
		Board b = FENParser.getBoardFromString("8;8;rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR;w;KQkq;-;0;1");
		Timeline t = new Timeline(b,true,1,0);
		Move m = new Move(new CoordFive(1,3,1,0), new CoordFive(3,3,1,0));
		Move m2 = new Move(new CoordFive(6,3,1,0), new CoordFive(4,3,1,0));
		Move m3 = new Move(new CoordFive(0,1,1,0), new CoordFive(2,2,1,0));
		Move m4 = new Move(new CoordFive(7,1,1,0), new CoordFive(5,2,1,0));
		t.addSpatialMove(m,true);
		t.addSpatialMove(m2,false);
		t.addSpatialMove(m3,true);
		t.addSpatialMove(m4,false);
		return t;
	}
}
