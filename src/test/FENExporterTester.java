package test;

import fileIO.FENExporter;

public class FENExporterTester {
	public static void testExporter() {
		System.out.println(FENExporter.BoardToString(TimeLineTest.getTestTL().getPlayableBoard(),true,1));
		
	}
}
