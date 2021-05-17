package GUI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.control.TextField;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.event.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import engine.Board;
import engine.CoordFive;
import engine.CoordFour;
import engine.GameStateManager;
import engine.Move;
import engine.MoveGenerator;
import engine.Turn;
import fileIO.FENExporter;
import fileIO.FENParser;


public class Controller implements MessageListener{
	//Included in the FXML 
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
	@FXML
	Label infoBox;
	@FXML
	RadioButton fullView;
	@FXML
	RadioButton whiteView;
	@FXML
	RadioButton blackView;
	
	//GUI Variables
	ToggleGroup group;
	ObservableList<String> notationStringArray;
	
	ArrayList<DrawableArrow> arrows;
	
	static final double MAX_FONT_SIZE = 20.0;
	static final int FULL_VIEW = 0;
	static final int WHITE_VIEW = 1;
	static final int BLACK_VIEW = 2;
	
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
	int viewType = FULL_VIEW;
	
	
	//Gamestate variables
	GameStateManager g;
	CoordFive selectedSquare;
	ArrayList<CoordFour> destinations;
	Move promotionMoveBuffer;
	

	// This func is called in the very start of initialization of this class
	public Controller() {
		g = FENParser.shadSTDGSM("res/Standard.PGN5.txt");
		arrows = new ArrayList<DrawableArrow>();
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
					if(promotionMoveBuffer != null) {
						return;
					}
					CoordFive clickedCoord = getCoordClicked((int) e.getX(), (int) e.getY(), g.width, g.height);
					if (destinations != null && clickedCoord != null && clickedCoord.color == selectedSquare.color && alContains(destinations, clickedCoord)) {
						Move selectedMove = new Move(selectedSquare, clickedCoord);
						//If it would be a promotion, do something special.
						if(selectedMove.type == Move.SPATIALMOVE && ( selectedMove.dest.y == 0 || selectedMove.dest.y == g.height - 1 )) {
							int pieceMoved = g.getSquare(selectedMove.origin, g.color);
							pieceMoved = pieceMoved < 0 ? pieceMoved * -1 : pieceMoved;
							if(pieceMoved == Board.piece.WPAWN.ordinal() || pieceMoved == Board.piece.BPAWN.ordinal() ||
									pieceMoved == Board.piece.WBRAWN.ordinal() || pieceMoved == Board.piece.BBRAWN.ordinal()) {
								promotionMoveBuffer = selectedMove;
								showPromotionPrompt();
								selectedSquare = null;
								destinations = null;
								return;
							}
						}
						if(g.makeMove(selectedMove)) {
							screenX += ChessDrawer.squarewidth * g.height + ChessDrawer.padding;							
						}
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
		
		innerLayout.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent event) {
				switch(event.getCode()) {
				case S:
					handleSubmitButton(null);
					break;
				case D:
					handleUndoButton(null);
				default:
					break;
				}
			}
		});
		//---------------------------------------------------------------------------------------------------------------------------
		notationStringArray = FXCollections.observableArrayList();
		notationList.setItems(notationStringArray);
		drawStage();
		statusLabel.setFont(new Font(MAX_FONT_SIZE));
		setStatusLabel();
		setNotationList();
		if(Globals.es == null) {
			Globals.es = new EventSource();
			Globals.es.addListener(this);
		}
		group = new ToggleGroup();
		fullView.setToggleGroup(group);
		whiteView.setToggleGroup(group);
		blackView.setToggleGroup(group);
		
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
		promotionMoveBuffer = null;
		drawStage();
	}

	@FXML
	public void handleSubmitButton(ActionEvent e) {
		boolean submitted = g.submitMoves();
		setStatusLabel();
		if(submitted) {
			for(Move m : g.turns.get(g.turns.size()-1).moves) {
					arrows.add(new DrawableArrow(m,!g.color,g.width,g.height,g.currTurn));
			}
			this.infoBox.setText("");
			setNotationList();
			boolean mated = g.bruteForceMateDetection();
			//System.out.println(mated);
			if(mated) {
				String color;
				if(g.color) {
					color = "Black";
				}else {
					color = "White";
				}
				statusLabel.setText("Game Over - " + color + " wins!" );
			}
		}
		drawStage();
	}
	
	@FXML
	private void handlePanButton(ActionEvent event) {
		for(int i = g.minTL; i <= g.maxTL; i++) {
			if(g.getTimeline(i).colorPlayable == g.color) {				
				panToBoard(g.getTimeline(i).Tend,i);
			}
		}
		drawStage();
	}
	
	@FXML
	private void handleListEvent(MouseEvent event) throws IOException {
		int turnIndex = notationList.getSelectionModel().getSelectedIndex();
		g.setTurn(turnIndex - 1);
		this.drawStage();
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
	private void handleEventList(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/PromotionPrompt.fxml"));
		Scene properties = new Scene(root, 200, 200);
		Stage stage = new Stage();
		stage.setScene(properties);
		stage.setResizable(false);
		stage.show();
	}
	
	@FXML
	private void loadShadGame(ActionEvent event) {
		File selectedFile = getFile();
		if (selectedFile != null) {
			GameStateManager temp = FENParser.shadSTDGSM(selectedFile);
			if(temp == null) {
				MessageEvent me = new MessageEvent("Could Not Load GameState, Please Check if the file is valid");
				Globals.es.broadcastEvent(me);
				return;
			}
			g = temp;
			setStatusLabel();
			setNotationList();
			screenX = 0;
			screenY = 0;
			drawStage();
		}
	}
	
	@FXML
	private void saveGameState(ActionEvent event) {
		Popup p = new Popup();
		FileChooser fileChooser = new FileChooser();
		fileChooser.setInitialDirectory(new File(new File("").getAbsolutePath()));
		fileChooser.setTitle("Save Game");
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Text File", "*.txt"));
		File selectedFile = fileChooser.showSaveDialog(p.getOwnerWindow());
		if(selectedFile == null) {
			return;
		}
		FENExporter.exportString(selectedFile, FENExporter.GameStateToFEN(this.g));
	}
	
	@FXML
	private void setProperties(ActionEvent e) throws IOException {
		Popup p = new Popup();
		Parent root = FXMLLoader.load(getClass().getResource("/Properties.fxml"));
		Scene properties = new Scene(root, 200, 200);
		Stage stage = new Stage();
		stage.setScene(properties);
		stage.setResizable(false);
		stage.show();
	}
	
	@FXML
	private void handleRadioView(ActionEvent event) {
		if(event.getSource() == fullView) {
			viewType = FULL_VIEW;		
		}
		else if(event.getSource() == whiteView) {
			viewType = WHITE_VIEW;
		}
		else if(event.getSource() == blackView) {
			viewType = BLACK_VIEW;
		}
		drawStage();
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
	
	private void setNotationList() {
		notationStringArray.clear();
		notationStringArray.add("Initial Position");
		for(Turn t : g.turns) {
			notationStringArray.add(t.toString());
		}
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
		int L = y / ((h * ChessDrawer.squarewidth) + ChessDrawer.padding);
		if(y < 0) {
			L--;
		}
		int pxrank = y % ((h * ChessDrawer.squarewidth) + ChessDrawer.padding);
		if(pxrank < 0) {
			pxrank += ((h * ChessDrawer.squarewidth) + ChessDrawer.padding);
		}
		if (pxrank <= ChessDrawer.padding)
			return null;
		int rank = h - ((pxrank - ChessDrawer.padding) / ChessDrawer.squarewidth) - 1;
		int T = x / ((w * ChessDrawer.squarewidth) + ChessDrawer.padding);
		int pxFile = x % ((w * ChessDrawer.squarewidth) + ChessDrawer.padding);
		int file = ((pxFile - ChessDrawer.padding) / ChessDrawer.squarewidth);
		if(x < 0) {// workaround for T0
			file += w;
			T -= 3;
		}
		CoordFive cf = new CoordFive(file, rank, (T / 2) + 1, L, (T % 2 == 0));
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
		switch(viewType) {
		case WHITE_VIEW:
			ChessDrawer.drawColoredMultiverse(canvasbox.getGraphicsContext2D(), (int) screenX, (int) screenY, g,true);
			break;
		case BLACK_VIEW:
			ChessDrawer.drawColoredMultiverse(canvasbox.getGraphicsContext2D(), (int) screenX, (int) screenY, g,false);
			break;
		default:
		case FULL_VIEW:
			ChessDrawer.drawMultiverseV(canvasbox.getGraphicsContext2D(), (int) screenX, (int) screenY, g);
			break;
		}
		if (destinations != null && destinations.size() > 0) {
			ChessDrawer.drawAllSquaresV(gc, new Color(1f,0f,0f,.5f ), destinations, selectedSquare.color, g.width, g.height, g.minTL, (int)screenX, (int)screenY);
		}
		for(DrawableArrow da : arrows) {
			if(da.turnnum <= g.currTurn) {
				ChessDrawer.drawMoveLine(canvasbox.getGraphicsContext2D(), da, (int)(screenX), (int)(screenY));			
			}
		}
	}

	public void drawStage(double changex, double changey) {
		//System.out.println("(" + screenX + "," + screenY + ")");
		//System.out.println("y(" + (screenX - changex) + "," + (screenY - changey) + ")");
		GraphicsContext gc = canvasbox.getGraphicsContext2D();
		gc.clearRect(0, 0, 8000, 8000);
		switch(viewType) {
		case WHITE_VIEW:
			ChessDrawer.drawColoredMultiverse(canvasbox.getGraphicsContext2D(), (int)(screenX - changex), (int)(screenY - changey), g,true);
			break;
		case BLACK_VIEW:
			ChessDrawer.drawColoredMultiverse(canvasbox.getGraphicsContext2D(), (int)(screenX - changex), (int)(screenY - changey), g,false);
			break;
		default:
		case FULL_VIEW:
			ChessDrawer.drawMultiverseV(canvasbox.getGraphicsContext2D(), (int)(screenX - changex), (int)(screenY - changey), g);
			break;
		}
		if (destinations != null && destinations.size() > 0) {
			ChessDrawer.drawAllSquaresV(gc, new Color(1f,0f,0f,.5f ), destinations, selectedSquare.color, g.width, g.height, g.minTL, (int)(screenX - changex), (int)(screenY- changey));			
		}
		for(DrawableArrow da : arrows) {
			if(da.turnnum <= g.currTurn) {
				ChessDrawer.drawMoveLine(canvasbox.getGraphicsContext2D(), da, (int)(screenX - changex), (int)(screenY - changey));				
			}
		}
	}

	private void showPromotionPrompt(){
		Parent root;
		try {
			root = FXMLLoader.load(getClass().getResource("/PromotionPrompt.fxml"));			
		}catch(Exception e) {
			System.out.println("Could Not load Promotion Prompt");
			return;
		}
		Scene properties = new Scene(root, 175, 100);
		Stage stage = new Stage();
		stage.setTitle("Select a promotion Type!");
		stage.setScene(properties);
		stage.setResizable(false);
		stage.show();
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
	
	private void panToBoard(int T, int L) {
		int pany = (L - g.minTL) * (ChessDrawer.padding + (ChessDrawer.squarewidth * g.height)) - 30;
		int panx = ((T - 1) * 2) * (ChessDrawer.padding + (ChessDrawer.squarewidth * g.width)) - 30;
		screenX = panx;
		screenY = pany;
	}

	@Override
	public void handleMessage(MessageEvent m) {
		if(m.type == MessageEvent.Promotion) {
			promotionMoveBuffer.specialType = m.imess;
			if(!g.color) {
				promotionMoveBuffer.specialType += Board.numTypes;
			}
			if(g.makeMove(promotionMoveBuffer)) {
				screenX += ChessDrawer.squarewidth * g.width + ChessDrawer.padding;
			}
			drawStage();
			promotionMoveBuffer = null;
		}
		if(m.type == MessageEvent.INFO) {
			infoBox.setText(m.message);
		}
		
	}

}
