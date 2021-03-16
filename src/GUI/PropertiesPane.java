package GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class PropertiesPane {
	@FXML
	AnchorPane innerLayout;
	
	@FXML
	TextField squareWidth;
	
	@FXML
	TextField padding;
	
	@FXML
	void initialize() {
		innerLayout.setPrefWidth(100);
		innerLayout.setPrefHeight(100);
	}
	
	public PropertiesPane() {}
	
	@FXML
	private void handleSubmitButton(ActionEvent e) {
		int sw = Integer.parseInt(squareWidth.getText());
		int pdg = Integer.parseInt(padding.getText());
		ChessDrawer.squarewidth = sw;
		ChessDrawer.padding = pdg;
		ChessDrawer.halfPadding = pdg / 2;
	}
}
