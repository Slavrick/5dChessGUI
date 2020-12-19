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
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Popup;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.io.File;
import java.util.ArrayList;

import engine.Board;
import engine.CoordFive;
import engine.CoordFour;
import engine.GameState;
import engine.Move;
import engine.MoveGenerator;
import fileIO.FENParser;

public class Controller {
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
	@FXML
	Label statusLabel;

	ObservableList<String> Notations;

	GameState g;

	// Canvas Constants
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
	boolean release = false;
	
	CoordFive selectedSquare;
	ArrayList<CoordFour> destinations;

	// This func is called in the very start of initialization of this class
	public Controller() {
		g = FENParser.FENtoGS("res/Standard.FEN.txt");
	}

	// This func is called after all initializations from the FXML parser.
	@FXML
	void initialize() {
		// -Anonymous-Functions---------------------------------------------------------------------------------------------------------------------------
		canvasbox.addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				//System.out.println("Release");
				if(dragging) {
					dragging = false;
					release = true;
					screenX -= xchange;
					screenY -= ychange;
					drawStage();
				}

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
				}
				// gc.clearRect(e.getX() - 2, e.getY() - 2, 5, 5);
				xchange = (mousePt.x - startDragx) * 1.1;
				ychange = (mousePt.y - startDragy) * 1.1;
				drawStage(xchange,ychange);
			}
		});

		// this is the jankiest way to handle this.... but im not sure how to do it
		// otherwise haha.
		canvasbox.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				//System.out.println((e.getX() + screenX) + ", " + (e.getY() + screenY));
				if (e.getButton() == MouseButton.SECONDARY) {
					selectedSquare = null;
					destinations = null;
					drawStage();
				} else if (e.getButton() == MouseButton.PRIMARY) {
					CoordFive clickedCoord = getCoordClicked((int) e.getX(), (int) e.getY(), g.width, g.height);
					if (destinations != null && clickedCoord != null && clickedCoord.color == selectedSquare.color && alContains(destinations, clickedCoord)) {
						Move selectedMove = new Move(selectedSquare, clickedCoord);
						System.out.println(selectedMove);
						g.makeMove(selectedMove);
						selectedSquare = null;
						destinations = null;
						drawStage();
					} else if (clickedCoord != null && g.coordIsPlayable(clickedCoord)) {
						if(updateDestinations(clickedCoord))
							selectedSquare = clickedCoord;
						drawStage();
					}
				}

			}
		});
		//---------------------------------------------------------------------------------------------------------------------------
		Notations = FXCollections.observableArrayList();
		Notations.add("23w T31.Ne3 Qf3");
		notationList.setItems(Notations);
		Notations.add("TestThing");
		drawStage();
		setStatusLabel();
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
	public void handleUndoButton(ActionEvent e) {
		g.undoTempMoves();
		drawStage();
	}

	@FXML
	public void handleSubmitButton(ActionEvent e) {
		System.out.println(g.submitMoves());
		setStatusLabel();
	}

	@FXML
	private void handleMove(ActionEvent event) {
		CoordFive c = new CoordFive(FENParser.stringtoCoord(movefield.getText()), true);
		ChessDrawer.drawSquare(canvasbox.getGraphicsContext2D(), g.width, g.height, g.minTL, c, Color.AQUA);
	}

	@FXML
	private void handleMenu(ActionEvent event) {
		if (event.getSource() instanceof Menu)
			System.out.println("Got Menu Event!");
		if (event.getSource() instanceof MenuItem) {
			System.out.println("Got Menu Event!");
			System.out.println(((MenuItem) event.getSource()).getId());
		}
		File selectedFile = getFile();
		if (selectedFile != null) {
			System.out.println(selectedFile.toString());
		} // TODO add game loading
	}

	@FXML
	private void handleEventList(ActionEvent event) {

	}
	
	@FXML
	private void loadGame(ActionEvent event) {
		File selectedFile = getFile();
		if (selectedFile != null) {
			g = FENParser.FENtoGS(selectedFile);
			drawStage();
		}
	}


	private File getFile() {
		Popup p = new Popup();
		FileChooser fileChooser = new FileChooser();
		fileChooser.setInitialDirectory(new File(new File("").getAbsolutePath()));
		fileChooser.setTitle("Open Resource File");
		File selectedFile = fileChooser.showOpenDialog(p.getOwnerWindow());
		return selectedFile;
	}
	
	private void setStatusLabel() {
		String status = g.color + " " + g.present;
		statusLabel.setText(status);
	}

	// =========================================Canvas Functions=========================================

	public CoordFive getCoordClicked(int x, int y, int w, int h) {
		x += screenX;
		y += screenY;
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
		CoordFive cf = new CoordFive(file, rank, (T / 2) + 1, L + g.minTL, (T % 2 == 0));
		// System.out.println(cf);
		return cf;
	}

	public boolean updateDestinations(CoordFive c) {
		int piece = g.getSquare(c, c.color);
		if(piece == 0 || Board.getColorBool(piece) != c.color)
			return false;
		destinations = MoveGenerator.getMoves(piece, g, new CoordFive(c, c.color));
		return true;
	}

	public void drawStage() {
		//System.out.println("n(" + screenX + "," + screenY + ")");
		GraphicsContext gc = canvasbox.getGraphicsContext2D();
		gc.clearRect(0, 0, 8000, 8000);
		ChessDrawer.drawMultiverseV(canvasbox.getGraphicsContext2D(), (int) screenX, (int) screenY, g);
		if (destinations != null) {
			ChessDrawer.drawAllSquaresV(gc, Color.AQUAMARINE, destinations, selectedSquare.color, g.width, g.height, g.minTL, (int)screenX, (int)screenY);
		}
	}

	public void drawStage(double changex, double changey) {
		//System.out.println("(" + screenX + "," + screenY + ")");
		//System.out.println("y(" + (screenX - changex) + "," + (screenY - changey) + ")");
		GraphicsContext gc = canvasbox.getGraphicsContext2D();
		gc.clearRect(0, 0, 8000, 8000);
		ChessDrawer.drawMultiverseV(canvasbox.getGraphicsContext2D(), (int)(screenX - changex), (int)(screenY - changey), g);
		if (destinations != null) {
			ChessDrawer.drawAllSquaresV(gc, Color.AQUAMARINE, destinations, selectedSquare.color, g.width, g.height, g.minTL, (int)(screenX - changex), (int)(screenY- changey));
			
		}

	}

	public static boolean alContains(ArrayList<CoordFour> al, CoordFour target) {
		for (CoordFour c : al) {
			if (c.equals(target))
				return true;
		}
		return false;
	}

}
