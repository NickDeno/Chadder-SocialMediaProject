package controllers;

import model.AppState;
import model.User;
import util.Utilities;
import util.GUIUtilities;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SignInController {
	@FXML private PasswordField passwordField;
	@FXML private TextField visiblePasswordField;
	@FXML private CheckBox showPasswordBox;
	@FXML private TextField usernameField;
	@FXML private Label msgLabel;
	@FXML private Button signInBtn;
	@FXML private Button cancelBtn; 
	@FXML private Button gitHubBtn;
	@FXML private Hyperlink clickHereText;
	
	Stage stage;
	//Since every scene/page shares the same stage throughout the app, to achieve data persistence, we do not have to backup every time a change is made in the 
	//program. We can just set the stages' onCloseRequest and the cancelBtn on the SignInPage to backup the AppState since these are the only 2 ways to forcibly close
	//the app.
	
	public void initialize() {	
		Platform.runLater(() -> {
			stage = (Stage)signInBtn.getScene().getWindow();
			stage.setOnCloseRequest(e -> Utilities.backupAppState());
		});	
	}
	
	@FXML public void showPasswordBoxOnAction(ActionEvent event) {
		if(showPasswordBox.isSelected()) {
			visiblePasswordField.setText(passwordField.getText());
			visiblePasswordField.setVisible(true);
			passwordField.setVisible(false);
		} else {
			passwordField.setText(visiblePasswordField.getText());
			passwordField.setVisible(true);
			visiblePasswordField.setVisible(false);
		}
	}
	
	@FXML public void signInBtnOnAction(ActionEvent event) {
		String password;
		if(showPasswordBox.isSelected()) {
    		password = visiblePasswordField.getText();
    	} else {
			password =  passwordField.getText();
		}
		User tempUser = AppState.getInstance().getUserCenter().getUser(usernameField.getText());
		if(tempUser != null && tempUser.getPassword().equals(password)) {
			AppState.getInstance().getUserCenter().setCurrentUser(tempUser);
			GUIUtilities.loadNewScene(stage, GUIUtilities.LandingScene);
		} else {
			msgLabel.setText("Account not found.");
			msgLabel.setVisible(true);
			usernameField.clear();
			passwordField.clear();
			visiblePasswordField.clear();
			passwordField.setVisible(true);
			visiblePasswordField.setVisible(false);
			showPasswordBox.setSelected(false);
		}
	}
    
    @FXML public void cancelBtnOnAction(ActionEvent event) {
    	Utilities.backupAppState();
    	stage.close();
    }
    
    @FXML public void gitHubBtnOnAction(ActionEvent event) throws IOException, URISyntaxException {
    	Desktop.getDesktop().browse(new URI("https://github.com/NickDeno"));
    }
    
    @FXML public void clickHereOnAction(ActionEvent event) {
    	GUIUtilities.loadNewScene(stage, GUIUtilities.SignUpScene);
	}
    
}
