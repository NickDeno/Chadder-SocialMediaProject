package controllers;

import model.AppState;
import model.User;
import util.GUIUtilities;
import util.Utilities;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class LandingController {
	@FXML private BorderPane borderPane;
	@FXML private Label userLabel;
	@FXML private Label numFollowingLabel;
	@FXML private Label numFollowersLabel;
	@FXML private Button homeBtn;
	@FXML private Button profileBtn;
	@FXML private Button settingsBtn;
	@FXML private Button postBtn;
	@FXML private Circle profilePic;
	@FXML private AnchorPane contentPane;
	
	private User currentUser;
	private Effect defaultProfilePicEffect;
	
	//Initializer
	public LandingController() {}
	
	public void initialize() {
		currentUser = AppState.getInstance().getUserCenter().getCurrentUser();
		userLabel.setText(currentUser.getUsername());
		profilePic.setFill(Utilities.byteArrToImagePattern(currentUser.getProfilePic().returnBytes()));
		defaultProfilePicEffect = profilePic.getEffect();
		numFollowingLabel.setText(String.valueOf(currentUser.getFollowing().size()));
		numFollowersLabel.setText(String.valueOf(currentUser.getFollowers().size()));
		
		HomeFeedController homeFeedController = GUIUtilities.loadPane(contentPane, GUIUtilities.HomeFeedScene);
		homeFeedController.setLandingController(this);
		homeBtn.setStyle("-fx-background-color: rgba(255,255,255,0.5)");
	}
	
	@FXML public void homeBtnOnAction(ActionEvent event) {
		HomeFeedController homeFeedController = GUIUtilities.loadPane(contentPane, GUIUtilities.HomeFeedScene);
		homeFeedController.setLandingController(this);
		resetBtns();
		homeBtn.setStyle("-fx-background-color: rgba(255,255,255,0.5)");
	}
	
	@FXML public void profileBtnOnAction(ActionEvent event) {
		CurrentUserProfileController currentUserProfileController = GUIUtilities.loadPane(contentPane, GUIUtilities.CurrentUserProfileScene);
		currentUserProfileController.setLandingController(this);
		resetBtns();
		profileBtn.setStyle("-fx-background-color: rgba(255,255,255,0.5)");
	}
	
	@FXML public void settingsBtnOnAction(ActionEvent event) {
		SettingsController settingsController = GUIUtilities.loadPane(contentPane, GUIUtilities.SettingsScene);
		settingsController.setLandingController(this);
		resetBtns();
		settingsBtn.setStyle("-fx-background-color: rgba(255,255,255,0.5)");
	}
	
	@FXML public void createPostBtnOnAction(ActionEvent event) {
		CreatePostController createPostController = GUIUtilities.loadPane(contentPane, GUIUtilities.CreatePostScene);
		createPostController.setLandingController(this);
		resetBtns();
	}
	
	@FXML public void logoutButtonOnAction(ActionEvent event) {
		Utilities.backupAppState();
		Stage stage = ((Stage)((Node)event.getSource()).getScene().getWindow());
		GUIUtilities.loadNewScene(stage, GUIUtilities.SignInScene);	
	}
	
	@FXML public void profilePicOnEntered(MouseEvent event) {
		ColorAdjust colorAdjust = new ColorAdjust();
		colorAdjust.setBrightness(-0.2);
		profilePic.setEffect(colorAdjust);	
	}
	
	@FXML public void profilePicOnExited(MouseEvent event) {
		profilePic.setEffect(defaultProfilePicEffect);
	}
	
	@FXML public void profilePicOnPressed(MouseEvent event) {
		ColorAdjust colorAdjust = new ColorAdjust();
		colorAdjust.setBrightness(0.5);
		profilePic.setEffect(colorAdjust);
	}
	@FXML public void profilePicOnReleased(MouseEvent event) {
		profilePic.setEffect(defaultProfilePicEffect);
		CurrentUserProfileController currentUserProfile = GUIUtilities.loadPane(contentPane, GUIUtilities.CurrentUserProfileScene);
		currentUserProfile.setLandingController(this);
		resetBtns();
		profileBtn.setStyle("-fx-background-color: rgba(255,255,255,0.5)");
	}
	
	public void resetBtns() {
		homeBtn.setStyle(null);
		homeBtn.getStyleClass().add("transparentButton");
		profileBtn.setStyle(null);
		profileBtn.getStyleClass().add("transparentButton");
		settingsBtn.setStyle(null);
		settingsBtn.getStyleClass().add("transparentButton");
	}
	
	public void setNumFollowingLabel(int numFollowing) {
		numFollowingLabel.setText(String.valueOf(numFollowing));
	}
	
	public void setProfilePic(Image image) {
		profilePic.setFill(new ImagePattern(image));
	}

	public AnchorPane getContentPane() {
		return contentPane;
	}
	
	public BorderPane getPane() {
		return borderPane;
	}
	
	public Button getHomeBtn() {
		return homeBtn;
	}
	
	
}
