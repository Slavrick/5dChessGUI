package main;

import test.FENParserTest;
import test.MoveTester;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class Main extends Application {
	
	public static void main(String[] args) {
		//runTests();
		launch();
	}

	public static void runTests() {
		System.out.println("Starting Tests");
		FENParserTest.testFENFileParser();
		FENParserTest.testMoveParser();
		MoveTester.TestBishopMoves();
		System.out.println("Done.");
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Open5d GUI");
		primaryStage.getIcons().add(new Image("/icon.png"));
		Parent root = FXMLLoader.load(getClass().getResource("/sample.fxml"));
		primaryStage.setScene(new Scene(root, 720, 500));
		primaryStage.show();
	}
}
