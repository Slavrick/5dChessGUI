package main;

import engine.Board;
import engine.Board.piece;
import fileIO.FENParser;
import test.BranchTester;
import test.FENParserTest;
import test.RookMoveTest;
import test.TimeLineTest;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import GUI.WindowFrame;

import javafx.stage.Stage;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class Main extends Application {
	
	public static void main(String[] args) {
		FENParser.FENtoGS("res/Rookie.FEN.txt");
		System.out.println(FENParser.stringToMove("(4,2,1,0)(3,3,1,0)"));
		FENParser.FENtoTL("r2k/4/2K1/P2K;;-;b1;2;(0,3,1,1)(0,1,1,1);(2,1,2,1)(1,2,2,1)", 4, 4, 0).printTimleline();
		FENParserTest.testMoveParser();
		launch();
	}

	public static void testPrint() {
		String[] strs = "nbru/4/4/KPPP;;-;w1;0;".split(";");
		for (String str : strs) {
			System.out.println(str);
		}
	}

	public static void BoardTest() {
		RookMoveTest.CheckRookMoves();
		System.out.println("tests Complete");
		System.out.println((int) piece.BROOK.ordinal());
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Open5d GUI");
		Parent root = FXMLLoader.load(getClass().getResource("/sample.fxml"));
		primaryStage.setScene(new Scene(root, 720, 500));
		primaryStage.show();
	}
}
