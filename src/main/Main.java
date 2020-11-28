package main;

import test.FENParserTest;
import test.MoveTester;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class Main extends Application {
	
	public static void main(String[] args) {
		FENParserTest.testFENFileParser();
		FENParserTest.testMoveParser();
		MoveTester.TestBishopMoves();
		launch();
	}

	public static void testPrint() {
		String[] strs = "nbru/4/4/KPPP;;-;w1;0;".split(";");
		for (String str : strs) {
			System.out.println(str);
		}
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Open5d GUI");
		Parent root = FXMLLoader.load(getClass().getResource("/sample.fxml"));
		primaryStage.setScene(new Scene(root, 720, 500));
		primaryStage.show();
	}
}
