package GUI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
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
import engine.GameStateManager;
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

	ObservableList<String> notationStringArray;

	GameStateManager g;

	static final double MAX_FONT_SIZE = 20.0;
	
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
		g = new GameStateManager(FENParser.FENtoGSNew("res/Standard.FEN.txt"));
	}

	// This func is called after all initializations from the FXML parser.
	@FXML
	void initialize() {
		// -Anonymous-Functions---------------------------------------------------------------------------------------------------------------------------
		canvasbox.addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
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
				PointerInfo mouse = MouseInfo.getPointerInfo();
				Point mousePt = mouse.getLocation();
				if (!dragging) {
					dragging = true;
					startDragx = mousePt.x;
					startDragy = mousePt.y;
				}
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
				if (e.getButton() == MouseButton.SECONDARY) {
					selectedSquare = null;
					destinations = null;
					drawStage();
				} else if (e.getButton() == MouseButton.PRIMARY) {
					CoordFive clickedCoord = getCoordClicked((int) e.getX(), (int) e.getY(), g.width, g.height);
					if (destinations != null && clickedCoord != null && clickedCoord.color == selectedSquare.color && alContains(destinations, clickedCoord)) {
						Move selectedMove = new Move(selectedSquare, clickedCoord);
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
		notationStringArray = FXCollections.observableArrayList();
		notationList.setItems(notationStringArray);
		drawStage();
		 // define max font size you need
		statusLabel.setFont(new Font(MAX_FONT_SIZE));
		setStatusLabel();
		g.printMultiverse();
	}

	//===========================Event Functions=========================================================================
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
		selectedSquare = null;
		destinations = null;
		drawStage();
	}

	@FXML
	public void handleSubmitButton(ActionEvent e) {
		g.submitMoves();
		setStatusLabel();
		notationStringArray.add(g.turns.get(g.turns.size() - 1).toString());//Sometimes one move will show a phantom move TODO fix that.
	}

	@FXML
	private void handleMenu(ActionEvent event) {
		if (event.getSource() instanceof Menu)
			System.out.println("Got Menu Event!");
		if (event.getSource() instanceof MenuItem) {
			System.out.println("Got Menu Event!");
			System.out.println(((MenuItem) event.getSource()).getId());
		}
	}

	@FXML
	private void handleEventList(ActionEvent event) {

	}
	
	@FXML
	private void loadGame(ActionEvent event) {
		File selectedFile = getFile();
		if (selectedFile != null) {
			g =  new GameStateManager(FENParser.FENtoGSNew(selectedFile));
			drawStage();
			setStatusLabel();
			screenX = 0;
			screenY = 0;
		}
	}
	
	@FXML
	private void setProperties(ActionEvent e) {
		
		//FIXME finish this.
	}

	//================================================================================================================
	
	private File getFile() {
		Popup p = new Popup();
		FileChooser fileChooser = new FileChooser();
		fileChooser.setInitialDirectory(new File(new File("").getAbsolutePath()));
		fileChooser.setTitle("Open Resource File");
		File selectedFile = fileChooser.showOpenDialog(p.getOwnerWindow());
		return selectedFile;
	}
	
	private void setStatusLabel() {
		String status = " Present:" + g.startPresent;
		if(g.color) {
			status = "White's Turn, " + status;
		}else {
			status = "Blacks's Turn, " + status;
		}
		statusLabel.setText(status);
	}

	// =========================================Canvas Functions=========================================
	//This is the worst.....
	public CoordFive getCoordClicked(int x, int y, int w, int h) {
		x += screenX;
		y += screenY;
		if (x < 0 || y < 0)
			return null;
		int L = y / ((h * ChessDrawer.squarewidth) + ChessDrawer.padding);
		int pxrank = y % ((h * ChessDrawer.squarewidth) + ChessDrawer.padding);
		if (pxrank <= ChessDrawer.padding)
			return null;
		int rank = h - ((pxrank - ChessDrawer.padding) / ChessDrawer.squarewidth) - 1;
		int T = x / ((w * ChessDrawer.squarewidth) + ChessDrawer.padding);
		int pxFile = x % ((w * ChessDrawer.squarewidth) + ChessDrawer.padding);
		int file = ((pxFile - ChessDrawer.padding) / ChessDrawer.squarewidth);
		CoordFive cf = new CoordFive(file, rank, (T / 2) + 1, L + g.minTL, (T % 2 == 0));
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
		if (destinations != null && destinations.size() > 0) {
			ChessDrawer.drawAllSquaresV(gc, new Color(1f,0f,0f,.5f ), destinations, selectedSquare.color, g.width, g.height, g.minTL, (int)screenX, (int)screenY);
		}
	}

	public void drawStage(double changex, double changey) {
		//System.out.println("(" + screenX + "," + screenY + ")");
		//System.out.println("y(" + (screenX - changex) + "," + (screenY - changey) + ")");
		GraphicsContext gc = canvasbox.getGraphicsContext2D();
		gc.clearRect(0, 0, 8000, 8000);
		ChessDrawer.drawMultiverseV(canvasbox.getGraphicsContext2D(), (int)(screenX - changex), (int)(screenY - changey), g);
		if (destinations != null && destinations.size() > 0) {
			ChessDrawer.drawAllSquaresV(gc, new Color(1f,0f,0f,.5f ), destinations, selectedSquare.color, g.width, g.height, g.minTL, (int)(screenX - changex), (int)(screenY- changey));
			
		}

	}

	/**
	 * This was created because I couldn't understand why the arraylist.contains was not working properly
	 * It is supposed to call the .equals function, but i think that it checking if two objects were the same memory, not the same contents.
	 * @param al
	 * @param target
	 * @return
	 */
	public static boolean alContains(ArrayList<CoordFour> al, CoordFour target) {
		for (CoordFour c : al) {
			if (c.equals(target))
				return true;
		}
		return false;
	}
	
	private static void panToBoard(int T, int L) {
		//TODO finish this function.
	}

}
