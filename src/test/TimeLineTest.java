package test;

import engine.Board;
import engine.CoordFour;
import engine.Move;
import engine.Timeline;
import fileIO.FENParser;

public class TimeLineTest {
	public static void testTLPrint() {
		Timeline t = FENParser.getTimelineFromString("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR;KQkq;-;w1", 0, 8, 8);
		Move m = new Move(new CoordFour(4,1,1,0), new CoordFour(4,3,1,0));
		Move m2 = new Move(new CoordFour(4,6,1,0), new CoordFour(4,4,1,0));
		Move m3 = new Move(new CoordFour(6,0,1,0), new CoordFour(5,2,1,0));
		Move m4 = new Move(new CoordFour(1,7,1,0), new CoordFour(2,5,1,0));	
		t.addSpatialMove(m, true);
		t.addSpatialMove(m2, false);
		t.addSpatialMove(m3, true);
		t.addSpatialMove(m4, false);
		t.printTimleline();
	}
	
	public static Timeline getTestTL() {
		Timeline t = FENParser.getTimelineFromString("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR;KQkq;-;w1", 0, 8, 8);
		Move m = new Move(new CoordFour(4,1,1,0), new CoordFour(4,3,1,0));
		Move m2 = new Move(new CoordFour(4,6,1,0), new CoordFour(4,4,1,0));
		Move m3 = new Move(new CoordFour(6,0,1,0), new CoordFour(5,2,1,0));
		Move m4 = new Move(new CoordFour(1,7,1,0), new CoordFour(2,5,1,0));	
		t.addSpatialMove(m, true);
		t.addSpatialMove(m2, false);
		t.addSpatialMove(m3, true);
		t.addSpatialMove(m4, false);
		return t;
	}
}
