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
import engine.Timeline;
import fileIO.FENParser;
import javafx.collections.FXCollections;
import javafx.scene.layout.HBox;
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
		System.out.println("Calling all riders");
		ObservableList<String> Notations = FXCollections.observableArrayList("Single", "Double", "Suite", "Family App");
		notationList.setItems(Notations);
		System.out.println(event);
		GraphicsContext gc = canvasbox.getGraphicsContext2D();
		//Board test = FENParser.getBoardFromString(FENParser.STANDARDBOARD);
        //ChessDrawer.drawFullBoard(gc,50,50,false,test);
		canvasbox.setWidth(8000);
		Timeline t = TimeLineTest.getTestTL();
		ChessDrawer.drawTimeline(gc,50,200,t);
	}
	
	@FXML
	private void handleEventList(ActionEvent event) {
		
	}

	public Controller() {

	}

}
