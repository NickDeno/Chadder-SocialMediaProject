package controllers;

import java.io.File;

import model.AppState;
import model.FXImage;
import model.User;
import util.Utilities;
import util.GUIUtilities;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Line;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

public class SignUpController {
	@FXML private Label msgLabel;
	@FXML private TextField emailField;
	@FXML private TextField usernameField;
	@FXML private PasswordField passwordField;
	@FXML private TextField visiblePasswordField;
	@FXML private PasswordField confirmPasswordField;
	@FXML private TextField visibleConfirmPasswordField;
	@FXML private CheckBox showPasswordBox;
	@FXML private Button signUpBtn;
	@FXML private Button backBtn;
	@FXML private Line emailLine;
	@FXML private Line usernameLine;
	@FXML private Line passwordLine;
	@FXML private Line confirmPasswordLine;
	@FXML private Button browseBtn;
	@FXML private ImageView previewProfilePic;
	
	private File chosenImage;
	private byte[] chosenImageBytes;
	
	public void initialize() {
		//Sets default profile picture of user
		chosenImage = new File("src/assets/TempProfilePic.png");
		chosenImageBytes = Utilities.fileToByteArr(chosenImage);
		previewProfilePic.setImage(Utilities.byteArrToImage(chosenImageBytes));
	}
	
	@FXML public void showPasswordBoxOnAction(ActionEvent event) {
		if(showPasswordBox.isSelected()) {
			visiblePasswordField.setText(passwordField.getText());
			visiblePasswordField.setVisible(true);
			visibleConfirmPasswordField.setText(confirmPasswordField.getText());
			visibleConfirmPasswordField.setVisible(true);		
			passwordField.setVisible(false);
			confirmPasswordField.setVisible(false);
		} else {
			passwordField.setText(visiblePasswordField.getText());
			passwordField.setVisible(true);
			confirmPasswordField.setText(visibleConfirmPasswordField.getText());
			confirmPasswordField.setVisible(true);	
			visiblePasswordField.setVisible(false);
			visibleConfirmPasswordField.setVisible(false);
		}
	}
	
	@FXML public void browseBtnOnAction(ActionEvent event) {
		FileChooser fc = new FileChooser();
    	fc.getExtensionFilters().addAll(new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
    	chosenImage = fc.showOpenDialog(null);
    	if(chosenImage == null) {
    		Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Invalid File");
			alert.setHeaderText(null);
			alert.setContentText("Either chosen image was invalid, or no image was chosen. Please try again.");
			alert.showAndWait();
    	} else {
    		chosenImageBytes = Utilities.fileToByteArr(chosenImage);
    		previewProfilePic.setImage(Utilities.byteArrToImage(chosenImageBytes));
    	}
	}

	@FXML public void signUpBtnOnAction(ActionEvent event) {
		String password;
		String confirmPassword;
		if(showPasswordBox.isSelected()) {
			password = visiblePasswordField.getText();
			confirmPassword = visibleConfirmPasswordField.getText();
		} else {
			password = passwordField.getText();
			confirmPassword = confirmPasswordField.getText();
		}
		
		if (usernameField.getText().isEmpty() || AppState.getInstance().getUserCenter().getUser(usernameField.getText()) != null 
				|| !Utilities.isValidPassword(password) || !password.equals(confirmPassword) || !Utilities.isValidEmail(emailField.getText())) {
			msgLabel.setText("Failed, please try again.");
			msgLabel.setVisible(true);
			resetFields();
		} else {
			AppState.getInstance().getUserCenter().addUser(new User(usernameField.getText(), password, emailField.getText(), new FXImage(chosenImageBytes)));
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Success!");
			alert.setHeaderText(null);
			alert.setContentText("Your account has been successfully created!");
			alert.showAndWait();
			Stage stage = ((Stage)((Node)event.getSource()).getScene().getWindow());
			GUIUtilities.loadNewScene(stage, GUIUtilities.SignInScene);	
		}
	}

	@FXML public void backBtnOnAction(ActionEvent event) {
		Stage stage = ((Stage)((Node)event.getSource()).getScene().getWindow());
		GUIUtilities.loadNewScene(stage, GUIUtilities.SignInScene);
	}
	
	@FXML public void checkFieldIsValid(MouseEvent event) {
		if (event.getSource().equals(emailField)) {
			String tempStyle = emailLine.getStyle();
			emailField.textProperty().addListener((observable, oldValue, newValue) -> {
				if (!newValue.isEmpty() && Utilities.isValidEmail(newValue)) emailLine.setStyle(tempStyle + "-fx-stroke: #38ff13;");
				else emailLine.setStyle(tempStyle + "-fx-stroke: #ff0000;");
			});
		} else if (event.getSource().equals(usernameField)) {
			String tempStyle = usernameField.getStyle();
			usernameField.textProperty().addListener((observable, oldValue, newValue) -> {
				if (AppState.getInstance().getUserCenter().getUser(newValue) != null) usernameLine.setStyle(tempStyle + "-fx-stroke: #ff0000;");
				else usernameLine.setStyle(tempStyle + "-fx-stroke: #38ff13;");
			});

		} else if (event.getSource().equals(passwordField)) {
			String tempStyle = passwordField.getStyle();
			passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
				if (!newValue.isEmpty() && Utilities.isValidPassword(newValue)) passwordLine.setStyle(tempStyle + "-fx-stroke: #38ff13;");
				else passwordLine.setStyle(tempStyle + "-fx-stroke: #ff0000;");
			});
		} else if(event.getSource().equals(visiblePasswordField)) {
			String tempStyle = visiblePasswordField.getStyle();
			visiblePasswordField.textProperty().addListener((observable, oldValue, newValue) -> {
				if (!newValue.isEmpty() && Utilities.isValidPassword(newValue)) passwordLine.setStyle(tempStyle + "-fx-stroke: #38ff13;");
				else passwordLine.setStyle(tempStyle + "-fx-stroke: #ff0000;");
			});
		} else if(event.getSource().equals(confirmPasswordField)) {
			String tempStyle = confirmPasswordField.getStyle();
			confirmPasswordField.textProperty().addListener((observable, oldValue, newValue) -> {
				if (!newValue.isEmpty() && passwordField.getText().equals(newValue)) confirmPasswordLine.setStyle(tempStyle + "-fx-stroke: #38ff13;");
				else confirmPasswordLine.setStyle(tempStyle + "-fx-stroke: #ff0000;");
			});
		} else if(event.getSource().equals(visibleConfirmPasswordField)) {
			String tempStyle = visibleConfirmPasswordField.getStyle();
			visibleConfirmPasswordField.textProperty().addListener((observable, oldValue, newValue) -> {
				if (!newValue.isEmpty() && visiblePasswordField.getText().equals(newValue)) confirmPasswordLine.setStyle(tempStyle + "-fx-stroke: #38ff13;");
				else confirmPasswordLine.setStyle(tempStyle + "-fx-stroke: #ff0000;");
			});
		}
	}

	private void resetFields() {
		emailField.clear();
		usernameField.clear();
		passwordField.clear();
		passwordField.setVisible(true);
		visiblePasswordField.clear();
		visiblePasswordField.setVisible(false);
		confirmPasswordField.clear();
		confirmPasswordField.setVisible(true);
		visibleConfirmPasswordField.clear();
		visibleConfirmPasswordField.setVisible(false);
		showPasswordBox.setSelected(false);
		emailLine.setStyle("-fx-stroke: #3b93ff;");
		usernameLine.setStyle("-fx-stroke: #3b93ff;");
		passwordLine.setStyle("-fx-stroke: #3b93ff;");
		confirmPasswordLine.setStyle("-fx-stroke: #3b93ff;");
		chosenImage = new File("src/assets/TempProfilePic.png");
		chosenImageBytes = Utilities.fileToByteArr(chosenImage);
		previewProfilePic.setImage(Utilities.byteArrToImage(chosenImageBytes));
	}
}
