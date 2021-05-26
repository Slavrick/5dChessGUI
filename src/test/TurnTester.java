package test;

import java.util.ArrayList;

import engine.CoordFour;
import engine.Move;
import engine.Turn;

public class TurnTester {
	public static void testTurnEquals() {
		Move m1 = new Move( new CoordFour(0,1,2,3), new CoordFour(1,1,2,4)); 
		Move m2 = new Move( new CoordFour(0,1,2,2), new CoordFour(1,1,2,1)); 
		Move m3 = new Move( new CoordFour(0,0,0,0), new CoordFour(0,0,0,0));
		
		Move m4 = new Move( new CoordFour(0,1,2,3), new CoordFour(1,1,2,4)); 
		Move m5 = new Move( new CoordFour(0,1,2,2), new CoordFour(1,1,2,1)); 
		Move m6 = new Move( new CoordFour(0,0,0,0), new CoordFour(0,0,0,0));
		
		Move m7 = new Move( new CoordFour(0,1,2,3), new CoordFour(1,1,2,4)); 
		Move m8 = new Move( new CoordFour(0,1,2,2), new CoordFour(1,1,2,1)); 
		Move m9 = new Move( new CoordFour(0,0,0,0), new CoordFour(1,1,1,1));
		ArrayList<Move> moves1 = new ArrayList<Move>();
		ArrayList<Move> moves2 = new ArrayList<Move>();
		ArrayList<Move> moves3 = new ArrayList<Move>();
		ArrayList<Integer> in1 = new ArrayList<Integer>(); 
		ArrayList<Integer> in2 = new ArrayList<Integer>();
		moves1.add(m1);
		moves1.add(m2);
		moves1.add(m3);
		moves2.add(m6);
		moves2.add(m4);
		moves2.add(m5);
		moves3.add(m9);
		moves3.add(m7);
		moves3.add(m8);
		in1.add(3);
		in1.add(4);
		in1.add(0);
		in1.add(1);
		in2.add(3);
		in2.add(4);
		in2.add(0);
		in2.add(1);
		
		Turn t1 = new Turn(moves1,in1);
		Turn t2 = new Turn(moves2,in2);
		Turn t3 = new Turn(moves3,in2);
		assert t1.equals(t2);
		assert !t1.equals(t3);
		
	}
}
