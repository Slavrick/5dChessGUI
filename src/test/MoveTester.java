package test;


import engine.Board;
import engine.CoordFive;
import engine.CoordFour;
import engine.GameState;
import engine.GameStateManager;
import engine.Move;
import engine.MoveGenerator;
import fileIO.FENParser;

public class MoveTester {
	public static void TestBishopMoves() {
		System.out.print("    Testing Bishop Movement: ");
		GameState tester = FENParser.shadSTDGSM("res/testPGNs/BishopMoveTester.PGN5.txt");
		assert tester != null;
		assert MoveGenerator.getCaptures(Board.piece.WBISHOP.ordinal(), tester, new CoordFive(2,3,5,0,true)).size() == 3;
		assert MoveGenerator.getCaptures(Board.piece.WBISHOP.ordinal(), tester, new CoordFive(3,2,5,1,true)).size() == 1;
		System.out.println("passed.");
	}
	
	public static void swap(int[] array, int index1, int index2) {
		int i = array[index1];
		array[index1] = array[index2];
		array[index2] = i;
	}
	
	
	public static void getAllPermutationsTest() {
		int[] initialNums = {1,2,3,4};
		//System.out.println(Arrays.toString(initialNums));
		boolean plus = false;
		int[] c = new int[initialNums.length];
		for(int i = 0 ; i < initialNums.length ; ) {
			if(c[i] < i) {
				if(i % 2 == 0) {
					swap(initialNums,0,i);
					
				}else {
					swap(initialNums, c[i], i);
				}
				//System.out.println(Arrays.toString(initialNums) + " " + Arrays.toString(c));
				plus = !plus;
				c[i]++;
				i = 0;
			}
			else {
				c[i] = 0;
				i++;
			}
		}
	}
	
	public static void testCastle() {
		System.out.print("    Testing Castling: ");
		GameStateManager g = fileIO.FENParser.shadSTDGSM("res/testPGNs/CastleTest.txt");
		g.getTimeline(0).castleKing(new Move(FENParser.SANtoCoord("e1"),FENParser.SANtoCoord("g1")));
		assert g.getTimeline(0).getPlayableBoard().getSquare(FENParser.SANtoCoord("g1")) == Board.piece.WKING.ordinal();
		g.getTimeline(0).castleKing(new Move(FENParser.SANtoCoord("e8"),FENParser.SANtoCoord("c8")));
		assert g.getTimeline(0).getPlayableBoard().getSquare(FENParser.SANtoCoord("c8")) == Board.piece.BKING.ordinal();
		System.out.println("passed");
	}
}
