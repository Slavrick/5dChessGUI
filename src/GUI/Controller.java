package GUI;

import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.AnchorPane;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import engine.Board;
import engine.GameState;
import engine.Timeline;
import fileIO.FENParser;
import javafx.collections.FXCollections;
import javafx.scene.layout.HBox;
import test.BranchTester;
import test.TimeLineTest;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class Controller {
	@FXML
	Label statusLabel;
	@FXML
	AnchorPane rootAnchor;
	@FXML
	BorderPane innerLayout;
	@FXML
	Canvas canvasbox;
	@FXML
	ListView<String> notationList;

	ObservableList<String> Notations;

	@FXML
	private void handleButtonAction(ActionEvent event) {
		ObservableList<String> Notations = FXCollections.observableArrayList("Single", "Double", "Suite", "Family App");
		notationList.setItems(Notations);
		System.out.println(event);
		GraphicsContext gc = canvasbox.getGraphicsContext2D();
		//Board test = FENParser.getBoardFromString(FENParser.STANDARDBOARD);
        //ChessDrawer.drawFullBoard(gc,50,50,false,test);
		canvasbox.setWidth(8000);
		canvasbox.setHeight(8000);
		GameState g = BranchTester.getTestGS();
		ChessDrawer.drawMultiverse(gc,30,30,g);
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

	public Controller() {
		
	}

}
