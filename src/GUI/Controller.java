package GUI;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.control.TextField;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.event.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Popup;
import javafx.stage.Window;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.io.File;
import java.util.ArrayList;

import engine.CoordFive;
import engine.CoordFour;
import engine.GameState;
import engine.Move;
import engine.MoveGenerator;
import engine.MoveNotation;
import fileIO.FENParser;
import test.BranchTester;

public class Controller {
	@FXML
	Label statusLabel;
	@FXML
	AnchorPane rootAnchor;
	@FXML
	BorderPane innerLayout;
	@FXML
	ScrollPane sPane;
	@FXML
	Canvas canvasbox;
	@FXML
	ListView<String> notationList;
	@FXML
	TextField movefield;

	ObservableList<String> Notations;

	GameState g;

	double lastX = 0;
	double lastY = 0;
	double screenX = 0;
	double screenY = 0;
	double canvasWidth = 8000;
	double canvasHeight = 8000;

	double startDragx;
	double startDragy;
	double xchange;
	double ychange;

	boolean dragging = false;
	
	CoordFive selectedSquare;
	ArrayList<CoordFour> destinations;

	@FXML
	void initialize() {
		ObservableList<String> Notations = FXCollections.observableArrayList("Single", "Double", "Suite", "Family App");
		notationList.setItems(Notations);
		GraphicsContext gc = canvasbox.getGraphicsContext2D();

		canvasbox.addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				System.out.println("Release");
				dragging = false;

			}
		});

		canvasbox.addEventHandler(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				// System.out.println("DragEvent");
				PointerInfo mouse = MouseInfo.getPointerInfo();
				Point mousePt = mouse.getLocation();
				if (!dragging) {
					dragging = true;
					startDragx = mousePt.x;
					startDragy = mousePt.y;
					screenX -= xchange;
					screenY -= ychange;
					if (screenX < 0) {
						screenX = 0;
					}
					if (screenY < 0) {
						screenY = 0;
					}
				}
				// gc.clearRect(e.getX() - 2, e.getY() - 2, 5, 5);
				xchange = (mousePt.x - startDragx) * 1.1;
				ychange = (mousePt.y - startDragy) * 1.1;
				sPane.setHvalue(((screenX - xchange) / (canvasWidth)));
				sPane.setVvalue((screenY - ychange) / (canvasHeight));
				// System.out.println("Change: " + xchange + ", " + ychange);
				// System.out.println("Screen: " + e.getScreenX() + ", " + e.getScreenY());
				// System.out.println("Screen: " + screenX + ", " + screenY);
				// System.out.println("H/V Vals: " + sPane.getHvalue() + ", " +
				// sPane.getVvalue());

			}
		});
		
		//this is the jankiest way to handle this.... but im not sure how to do it otherwise haha.
		canvasbox.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				System.out.println((e.getX() + screenX) + ", " + (e.getY() + screenY));
				CoordFive clickedCoord = getCoordClicked((int) e.getX(), (int) e.getY(), g.width, g.height);
				if(destinations != null && clickedCoord != null && alContains(destinations,clickedCoord)) {
					Move selectedMove = new Move(selectedSquare,clickedCoord);
					System.out.println(selectedMove);
					g.makeMove(selectedMove);
					selectedSquare = null;
					destinations = null;
					drawStage();
				}
				else if(clickedCoord != null && g.coordIsPlayable(clickedCoord)) {					
					selectedSquare = clickedCoord;
					updateDestinations(selectedSquare);
					drawStage();
				}
			}
		});
		canvasbox.setWidth(8000);
		canvasbox.setHeight(8000);
		gc.fillRect(-1, 0, 0, 0);
		ChessDrawer.drawMultiverse(gc, 0, 0, g);
		g.printMultiverse();
	}

	@FXML
	private void handleButtonAction(ActionEvent event) {
		GraphicsContext gc = canvasbox.getGraphicsContext2D();
		gc.setFill(Color.AQUA);
		gc.fillRect(0, 0, 100, 100);
		drawStage();
		g.printMultiverse();
	}

	@FXML
	private void handleMenu(ActionEvent event) {
		if (event.getSource() instanceof Menu)
			System.out.println("Got Menu Event!");
		if (event.getSource() instanceof MenuItem) {
			System.out.println("Got Menu Event!");
			System.out.println(((MenuItem) event.getSource()).getText());
		}
		File selectedFile = getFile();
		if (selectedFile != null) {
			System.out.println(selectedFile.toString());
		} // TODO add game loading
	}

	private File getFile() {
		Popup p = new Popup();
		FileChooser fileChooser = new FileChooser();
		fileChooser.setInitialDirectory(new File(new File("").getAbsolutePath()));
		fileChooser.setTitle("Open Resource File");
		File selectedFile = fileChooser.showOpenDialog(p.getOwnerWindow());
		return selectedFile;
	}

	@FXML
	private void handleEventList(ActionEvent event) {

	}

	@FXML
	private void handleMove(ActionEvent event) {
		CoordFive c = new CoordFive(FENParser.stringtoCoord(movefield.getText()), true);
		ChessDrawer.drawSquare(canvasbox.getGraphicsContext2D(), g.width, g.height, g.minTL, c, Color.AQUA);
	}

	public Controller() {
		g = FENParser.FENtoGS("res/Rookie.FEN.txt");
	}

	public CoordFive getCoordClicked(int x, int y, int w, int h) {
		if (x < 0 || y < 0)
			return null;
		int L = y / ((h * ChessDrawer.squarewidth) + ChessDrawer.padding);
		int pxrank = y % ((h * ChessDrawer.squarewidth) + ChessDrawer.padding);
		if (pxrank <= 50)
			return null;
		int rank = h - ((pxrank - 50) / 32) - 1;
		int T = x / ((w * ChessDrawer.squarewidth) + ChessDrawer.padding);
		int pxFile = x % ((w * ChessDrawer.squarewidth) + ChessDrawer.padding);
		int file = ((pxFile - 50) / 32);
		CoordFive cf = new CoordFive(file, rank, (T / 2) + 1, L, (T % 2 == 0));
		//System.out.println(cf);
		return cf;
	}
	
	public void drawMoves(CoordFive c) {
		int piece = g.getSquare(c, c.color);
		CoordFour[] moveset = MoveNotation.getMoveVectors(piece);
		destinations = MoveGenerator.getRiderMoves(g, c.color, c, moveset);
		ChessDrawer.drawAllSquares(canvasbox.getGraphicsContext2D(), g.width, g.height, g.minTL, Color.AQUAMARINE, destinations, c.color);
	}
	
	public void updateDestinations(CoordFive c) {
		int piece = g.getSquare(c, c.color);
		CoordFour[] moveset = MoveNotation.getMoveVectors(piece);
		destinations = MoveGenerator.getRiderMoves(g, c.color, c, moveset);
	}
	
	public void drawStage() {
		GraphicsContext gc = canvasbox.getGraphicsContext2D();
		gc.clearRect(0, 0, 1000, 1000);
		ChessDrawer.drawMultiverse(canvasbox.getGraphicsContext2D(), 0, 0, g);
		if(destinations != null) {			
			ChessDrawer.drawAllSquares(gc, g.width, g.height, g.minTL, Color.AQUAMARINE, destinations, selectedSquare.color);
		}
		
	}
	
	public static boolean alContains(ArrayList<CoordFour> al, CoordFour target) {
		for(CoordFour c : al) {
			if(c.equals(target))
				return true;
		}
		return false;
	}
	
	@FXML
	public void handleUndoButton(ActionEvent e) {
		g.undoTempMoves();
		drawStage();
	}

}
