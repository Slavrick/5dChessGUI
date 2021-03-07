package GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class PromotionPrompt {
	@FXML
	AnchorPane innerLayout;
	
	@FXML
	void initialize() {
		innerLayout.setPrefWidth(200);
		innerLayout.setPrefHeight(300);
	}
	
	public PromotionPrompt() {
		
	}
	
	@FXML
	private void handleSubmitButton(ActionEvent e) {
		MessageEvent m = new MessageEvent();
		m.type = MessageEvent.Promotion;
		Button b = (Button) (e.getSource());
		String text = b.getText();
		int promotionType = 0;
		if(text.equals("Queen")) {
			promotionType = 6;
		}else if(text.equals("Rook")) {
			promotionType = 4;
		}else if(text.equals("Bishop")) {
			promotionType = 3;
		}else if(text.equals("Knight")) {
			promotionType = 2;
		}else if(text.equals("Dragon")) {
			promotionType = 9;
		}else if(text.equals("Unicorn")) {
			promotionType = 8;
		}
		m.imess = promotionType;
		Globals.es.broadcastEvent(m);
	    Stage stage = (Stage) innerLayout.getScene().getWindow();
	    stage.close();
	}
}
