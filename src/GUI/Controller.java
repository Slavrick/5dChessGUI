package GUI;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.control.TextField;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.event.*;
import javafx.scene.input.MouseEvent;

import engine.GameState;
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
	

	@FXML
	private void handleButtonAction(ActionEvent event) {
		ObservableList<String> Notations = FXCollections.observableArrayList("Single", "Double", "Suite", "Family App");
		notationList.setItems(Notations);
		System.out.println(event);
		GraphicsContext gc = canvasbox.getGraphicsContext2D();
		//Board test = FENParser.getBoardFromString(FENParser.STANDARDBOARD);
        //ChessDrawer.drawFullBoard(gc,50,50,false,test);
		
		canvasbox.addEventHandler(MouseEvent.MOUSE_CLICKED, 
		        new EventHandler<MouseEvent>() {
		            @Override
		            public void handle(MouseEvent t) {            
			        	   lastX = t.getX();
			        	   lastY = t.getY();
		            }
		        });
		
		canvasbox.addEventHandler(MouseEvent.MOUSE_DRAGGED, 
			       new EventHandler<MouseEvent>() {
			           @Override
			           public void handle(MouseEvent e) {
			        	   double x = e.getX();
			        	   double y = e.getY();
			               gc.clearRect(e.getX() - 2, e.getY() - 2, 5, 5);
			               double xchange = x - lastX;
			               double ychange = y - lastY;
			               screenX += xchange;
			               screenY += ychange;
			               sPane.setHvalue(sPane.getHvalue() - (xchange / (canvasWidth)));
			               sPane.setVvalue(sPane.getVvalue() - (ychange / (canvasHeight)));
			               System.out.println("Change: " + xchange + ", " + ychange);
			               System.out.println("Screen: " + e.getScreenX() + ", " + e.getScreenY());
			               System.out.println("Screen: " + screenX + ", " + screenY);
			               System.out.println("H/V Vals: " + sPane.getHvalue() + ", " + sPane.getVvalue());
			               lastX = x;
			               lastY = y;
			               
			           }
			       });
		
		canvasbox.setWidth(8000);
		canvasbox.setHeight(8000);
		gc.fillRect(-1,0,0,0);
		ChessDrawer.drawMultiverse(gc,30,30,g);
		g.printMultiverse();
	}
	
	@FXML
	private void handleMenu(ActionEvent event) {
		if(event.getSource() instanceof Menu )
			System.out.println("Got Menu Event!");
		if(event.getSource() instanceof MenuItem ) {
			System.out.println("Got Menu Event!");
			System.out.println(((MenuItem)event.getSource()).getText());
		}
	}
	
	@FXML
	private void handleEventList(ActionEvent event) {
		
	}
	
	@FXML
	private void handleMove(ActionEvent event) {
		g.makeTurn(FENParser.stringToMove(movefield.getText()));
		
	}

	public Controller() {
		g = BranchTester.getTestGS();
	}

}
