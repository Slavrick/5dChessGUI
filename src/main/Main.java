package main;

import engine.Board;
import engine.Board.piece;

import test.BranchTester;
import test.FENParserTest;
import test.RookMoveTest;
import test.TimeLineTest;

import GUI.WindowFrame;

import javafx.stage.Stage;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application{
	public static void main(String[] args) {
		FENParserTest.testSTDBoard();
		TimeLineTest.testTLPrint();
		BranchTester.testMultiverse();
		launch();
	}
	
	public static void BoardTest() {
		RookMoveTest.CheckRookMoves();
		System.out.println("tests Complete");
		System.out.println((int) piece.BROOK.ordinal());
	}
	

	@Override
	public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("AquaBaby's Open5d GUI");
        Parent root = FXMLLoader.load(getClass().getResource("/sample.fxml"));
        primaryStage.setScene(new Scene(root, 720, 500));
        primaryStage.show();
	}
}
