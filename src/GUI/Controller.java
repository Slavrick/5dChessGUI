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
import javafx.collections.FXCollections;
import javafx.scene.layout.HBox;
import javafx.scene.canvas.Canvas;

public class Controller {
    @FXML
    Label statusLabel;
    @FXML
    AnchorPane rootAnchor;
    @FXML
    TextField user;
    @FXML
    TextField pwd;
    @FXML
    TextField ip;
    @FXML
    TextField MinSeconds;
    @FXML
    TextField MaxSeconds;
    @FXML
    TextField Day;
    @FXML
    TextField SensorID;
    @FXML
    ChoiceBox<String> TableType;
    @FXML
    TableView queryTable;
    @FXML
    TableColumn time;
    @FXML
    BorderPane innerLayout;
    @FXML
    Canvas canvasbox;
    @FXML
    ListView<String> notationList;
    
    ObservableList<String> Notations;
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
    }
    
    public void init() {
    	ObservableList<String> Notations =FXCollections.observableArrayList (
    		    "Single", "Double", "Suite", "Family App");
    	notationList.setItems(Notations);
    }
    
}
