package main;

import test.CoordTester;
import test.FENParserTest;
import test.MateTest;
import test.MoveTester;
import test.TurnTester;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class Main extends Application {
	
	public static void main(String[] args) {
		runTests();
		launch();
	}

	public static void runTests() {
		System.out.println("Starting Tests");
		CoordTester.testAllCoordFourFuncs();
		MoveTester.TestBishopMoves();
		MoveTester.testCastle();
		FENParserTest.testFENFileParser();
		FENParserTest.testMoveParser();
		FENParserTest.testSANParser();
		FENParserTest.testShadParser();
		FENParserTest.testAmbiguityInfoParser();
		FENParserTest.testShadFEN();
		TurnTester.testTurnEquals();
		MateTest.MateSpeedTest();
		//MateTest.MateCorrectnessTest();
		//FENExporterTester.testExporter();
		System.out.println("Done.");
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Open5d GUI");
		primaryStage.getIcons().add(new Image("file:icon.png"));
		Parent root = FXMLLoader.load(getClass().getResource("/MainGUI.fxml"));
		primaryStage.setScene(new Scene(root, 720, 500));
		primaryStage.show();
		primaryStage.setOnCloseRequest(evt -> Platform.exit());
	}
}
