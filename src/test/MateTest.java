package test;

import engine.GameState;
import fileIO.FENParser;

public class MateTest {
	
	
	
	public static void MateCorrectnessTest() {
		System.out.print("    Testing Mate Detection:");
		correctnessTestFile("res/MateTest/test1.txt",true);
		correctnessTestFile("res/MateTest/test2.txt",true);
		System.out.println(" passed");
	}
	
	public static void MateSpeedTest() {
		//speedTestFile("res/MateTest/test1.txt",true);
		//speedTestFile("res/MateTest/test2.txt",false);
		//speedTestFile("res/MateTest/AquaBabyVsAndrey 6-14-2021.txt",true);
		//speedTestFile("res/MateTest/AquaBabyVsSamet 6-12-2021.txt",true);
		//speedTestFile("res/MateTest/AquaBabyVsWritenWrong 6-13-2021.txt",true);
		speedTestReduction("res/MateTest/test1.txt",true);
		speedTestReduction("res/MateTest/test2.txt",false);
		speedTestReduction("res/MateTest/AquaBabyVsAndrey 6-14-2021.txt",true);
		speedTestReduction("res/MateTest/AquaBabyVsSamet 6-12-2021.txt",true);
		speedTestReduction("res/MateTest/AquaBabyVsWritenWrong 6-13-2021.txt",true);
		
		//speedTestFile("res/MateTest/test1.txt",true);
		
	}
	
	
	public static void speedTestFile(String fileLocation, boolean result) {
		GameState g = FENParser.shadSTDGSM(fileLocation);
		assert g != null : "Error Loading File";
		long startTime = System.nanoTime();
		boolean mate = g.bruteForceMateDetection();
		assert mate == result;
		long endTime = System.nanoTime();
		System.out.println(fileLocation + " Took "+(endTime - startTime) + " ns"); 
	}
	
	public static void correctnessTestFile(String fileLocation, boolean result) {
		GameState g = FENParser.shadSTDGSM(fileLocation);
		boolean mate = g.bruteForceMateDetection();
		assert mate == result; 
	}
	
	public static void speedTestReduction(String fileLocation, boolean result) {
		GameState g = FENParser.shadSTDGSM(fileLocation);
		assert g != null : "Error Loading File";
		long startTime = System.nanoTime();
		boolean mate = g.isMated();
		if(mate != result) {
			System.out.println("Failed on file: " + fileLocation);
			return;
		}
		assert mate == result;
		long endTime = System.nanoTime();
		System.out.println(fileLocation + " Took "+(endTime - startTime) + " ns"); 
	}

}
