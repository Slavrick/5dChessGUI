package test;

import engine.CoordFour;

public class CoordTester {
	
	public static void testAllCoordFourFuncs() {
		ArithmaticTest();
		vecTest();
	}
	
	public static void ArithmaticTest() {
		System.out.print("    Arithmatic testing:");
		CoordFour add1 = new CoordFour(2,2,3,4);
		CoordFour sum = CoordFour.add(add1, add1);
		testCoord(sum,4,4,6,8);
		CoordFour add2 = new CoordFour(2,3,7,10);
		add2.add(add1);
		testCoord(add2,4,5,10,14);
		CoordFour sub1 = new CoordFour(4,4,1,0);
		sub1.sub(new CoordFour(5,5,1,0));
		testCoord(sub1, -1, -1, 0, 0);
		assert sub1.getNagonal() == 2;
		System.out.println(" passed.");
	}
	
	public static void vecTest() {
		System.out.print("    Flatten Testing:");
		CoordFour test = new CoordFour(3,9,6,18);
		test.flatten();
		testCoord(test,1,3,2,6);
		CoordFour test2 = new CoordFour(3,9,0,0);
		test2.flatten();
		testCoord(test2,1,3,0,0);
		CoordFour test3 = new CoordFour(-2,-2,0,-4);
		test3.flatten();
		testCoord(test3,-1,-1,0,-2);
		CoordFour test4 = new CoordFour(0,0,0,0);
		test4.flatten();
		testCoord(test4,0,0,0,0);
		System.out.println(" passed.");
	}
	
	public static void testCoord(CoordFour test, int x, int y, int T, int L) {
		assert test.x == x;
		assert test.y == y;
		assert test.T == T;
		assert test.L == L;
	}
}
