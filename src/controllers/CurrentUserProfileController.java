package controllers;

import java.io.File;
import java.util.LinkedList;
import java.util.Optional;

import model.AppState;
import model.FXImage;
import model.Post;
import model.SpellCheckTextArea;
import model.User;
import util.Utilities;
import util.GUIUtilities;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class CurrentUserProfileController {
	@FXML private AnchorPane currentUserProfilePane;
	@FXML private Rectangle bannerPic;
	@FXML private Circle profilePic;
	@FXML private Label usernameLabel;
	@FXML private TextField nicknameField;
    private SpellCheckTextArea bioField;
    
    @FXML private Label viewingLabel;
    @FXML private Button postsBtn;
    @FXML private Button followersBtn;
    @FXML private Button followingBtn;
    @FXML private Label numPosts;
    @FXML private Label numFollowers;
    @FXML private Label numFollowing;
    @FXML private Line postsBtnLine;
    @FXML private Line followersBtnLine;
    @FXML private Line followingBtnLine;
    
    @FXML private ScrollPane postsScrollPane;
    @FXML private TilePane postsTilePane;
    @FXML private ListView<String> followersList;
    @FXML private ListView<String> followingList;   
    @FXML private Button changeBannerPicBtn;
    @FXML private Button changeProfilePicBtn;
    @FXML private Button backBtn;
    @FXML private Button resetBtn;
    @FXML private Button saveBtn;
    @FXML private CheckBox editFieldsBox;
    
    private User currentUser;
    private byte[] chosenProfilePicBytes;
    private byte[] chosenBannerPicBytes;
    private LandingController landingController;
    
    //Initializer
    public CurrentUserProfileController() {}
    
    public void initialize() {
    	currentUser = AppState.getInstance().getUserCenter().getCurrentUser();
    	bioField = new SpellCheckTextArea(195, 250, 20, 430, false);
    	bioField.getTextArea().getStyleClass().clear();
    	bioField.getTextArea().setStyle("-fx-font-size: 14;");
    	currentUserProfilePane.getChildren().add(bioField.getPane());
    	
    	bannerPic.setFill(Utilities.byteArrToImagePattern(currentUser.getBannerPic().returnBytes()));	
		profilePic.setFill(Utilities.byteArrToImagePattern(currentUser.getProfilePic().returnBytes()));
		usernameLabel.setText("@" + currentUser.getUsername());
		if(currentUser.getNickName() != null) nicknameField.setText(currentUser.getNickName());
		if(currentUser.getBio() != null) bioField.getTextArea().appendText(currentUser.getBio());
		numPosts.setText(String.valueOf(currentUser.getUserPosts().size()));
		numFollowers.setText(String.valueOf(currentUser.getFollowers().size()));
		numFollowing.setText(String.valueOf(currentUser.getFollowing().size()));
		for(User u: currentUser.getFollowers()) {
			followersList.getItems().add(u.getUsername());
		}
    	for(User u: currentUser.getFollowing().values()) {
			followingList.getItems().add(u.getUsername());
		}
    	
    	followersList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
    		if(newValue != null) {
    			UserProfileController userProfile =  GUIUtilities.loadPane(landingController.getContentPane(), GUIUtilities.UserProfileScene);
				userProfile.setUser(AppState.getInstance().getUserCenter().getUser(newValue));
				userProfile.setLandingController(landingController);
				landingController.resetBtns();
				landingController.getHomeBtn().setStyle("-fx-background-color: rgba(255,255,255,0.5)");
    		}
    	});
		
    	followingList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
    		if(newValue != null) {
    			UserProfileController userProfile =  GUIUtilities.loadPane(landingController.getContentPane(), GUIUtilities.UserProfileScene);
				userProfile.setUser(AppState.getInstance().getUserCenter().getUser(newValue));
				userProfile.setLandingController(landingController);
				landingController.resetBtns();
				landingController.getHomeBtn().setStyle("-fx-background-color: rgba(255,255,255,0.5)");
    		}
    	}); 	
    	Platform.runLater(() -> {	
    		displayPosts(currentUser.getUserPosts());
    	}); 	
    }

    @FXML public void changeBannerPicBtnOnAction(ActionEvent event) {
    	FileChooser fc = new FileChooser();
		fc.getExtensionFilters().addAll(new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
		File selectedFile = fc.showOpenDialog(null);
		if (selectedFile == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Invalid File");
			alert.setHeaderText(null);
			alert.setContentText("Either chosen image was invalid, or no image was chosen. Please try again.");
			alert.showAndWait();
		} else {
			chosenBannerPicBytes = Utilities.fileToByteArr(selectedFile);
			bannerPic.setFill(Utilities.byteArrToImagePattern(chosenBannerPicBytes));		
		}
    }

    @FXML public void changeProfilePicBtnOnAction(ActionEvent event) {
    	FileChooser fc = new FileChooser();
		fc.getExtensionFilters().addAll(new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
		File selectedFile = fc.showOpenDialog(null);
		if (selectedFile == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Invalid File");
			alert.setHeaderText(null);
			alert.setContentText("Either chosen image was invalid, or no image was chosen. Please try again.");
			alert.showAndWait();
		} else {
			chosenProfilePicBytes = Utilities.fileToByteArr(selectedFile);
			profilePic.setFill(Utilities.byteArrToImagePattern(chosenProfilePicBytes));			
		}
    }
    
    @FXML public void postsBtnOnAction(ActionEvent event) {
    	viewingLabel.setText("Posts");
    	postsScrollPane.setVisible(true);
    	followersList.setVisible(false);
    	followingList.setVisible(false);
    	postsBtnLine.setVisible(true);
    	followersBtnLine.setVisible(false);
    	followingBtnLine.setVisible(false);
    }

    @FXML public void followersBtnOnAction(ActionEvent event) {
    	viewingLabel.setText("Followers");
    	postsScrollPane.setVisible(false);
    	followersList.setVisible(true);
    	followingList.setVisible(false);
    	postsBtnLine.setVisible(false);
    	followersBtnLine.setVisible(true);
    	followingBtnLine.setVisible(false);
    }

    @FXML public void followingBtnOnAction(ActionEvent event) {
    	viewingLabel.setText("Following");
    	postsScrollPane.setVisible(false);
    	followersList.setVisible(false);
    	followingList.setVisible(true);
    	postsBtnLine.setVisible(false);
    	followersBtnLine.setVisible(false);
    	followingBtnLine.setVisible(true);
    }
    
    @FXML public void resetBtnOnAction(ActionEvent event) {
    	resetFields(); 	
    }

    @FXML public void saveBtnOnAction(ActionEvent event) {	
    	//This if statement will be true when there are misspelled words in the users bio
    	if(bioField.getMisspelledWords().isEmpty() == false) {
			String misspelledWords = "";
			for(int i = 0; i < bioField.getMisspelledWords().size(); i++) {
				String currWord = bioField.getMisspelledWords().get(i);
				if(i < bioField.getMisspelledWords().size()-1) {
					misspelledWords += currWord + ", ";
				} else {
					misspelledWords += currWord + ".";
				}
			}
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Misspelled Words");
			alert.setHeaderText(null);
			alert.setContentText("Your bio contains the misspelled word(s) " + misspelledWords + " Do you still wish to continue?");
			Optional<ButtonType> confirm = alert.showAndWait();
			if(confirm.isPresent() && confirm.get() == ButtonType.CANCEL) {
				return;
			}
    	}	
    	//This if statement will be true if the user changes their banner pic
    	if(currentUser.getBannerPic().returnBytes() != chosenBannerPicBytes && chosenBannerPicBytes != null) {
        	currentUser.setBannerPic(new FXImage(chosenBannerPicBytes));
    	}
    	//This if statement is true when user changes profile pic, thus posts must be reloaded after profile pic is changed
    	if(currentUser.getProfilePic().returnBytes() != chosenProfilePicBytes && chosenProfilePicBytes != null){
			currentUser.setProfilePic(new FXImage(chosenProfilePicBytes));
			landingController.setProfilePic(Utilities.byteArrToImage(chosenProfilePicBytes));
			//Reloads user posts after profile pic is changed
			displayPosts(currentUser.getUserPosts());	
		} else {
			currentUser.setNickName(nicknameField.getText());
	    	currentUser.setBio(bioField.getTextArea().getText());
			resetFields();
		}
    }
    
    @FXML public void editFieldsBoxOnAction(ActionEvent event) {
    	if(editFieldsBox.isSelected()) {
			setFieldsVisibility(true);
		} else {
			setFieldsVisibility(false);
		}
    }
    
    public void resetFields() {
    	bannerPic.setFill(Utilities.byteArrToImagePattern(currentUser.getBannerPic().returnBytes()));	
		profilePic.setFill(Utilities.byteArrToImagePattern(currentUser.getProfilePic().returnBytes()));	
		chosenBannerPicBytes = currentUser.getBannerPic().returnBytes();
		chosenProfilePicBytes = currentUser.getProfilePic().returnBytes();
    	if(currentUser.getNickName() != null) nicknameField.setText(currentUser.getNickName());
    	if(currentUser.getBio() != null) bioField.getTextArea().replaceText(currentUser.getBio());
		editFieldsBox.setSelected(false);
		setFieldsVisibility(false);
    }
    
    private void setFieldsVisibility(boolean visibility) {
    	if(visibility == true) {
    		nicknameField.setEditable(true);
    		bioField.getTextArea().setEditable(true);
    		changeBannerPicBtn.setVisible(true);
    		changeBannerPicBtn.setDisable(false);
    		changeProfilePicBtn.setVisible(true);
    		changeProfilePicBtn.setDisable(false);
		} else {
			nicknameField.setEditable(false);
    		bioField.getTextArea().setEditable(false);
    		changeBannerPicBtn.setVisible(false);
    		changeBannerPicBtn.setDisable(true);
    		changeProfilePicBtn.setVisible(false);
    		changeProfilePicBtn.setDisable(true);
		}
    }
    
    public void displayPosts(LinkedList<Post> posts) {
    	postsTilePane.getChildren().clear();
    	GUIUtilities.displayPostsNewToOld(posts, postsTilePane, landingController);
    }
	
	public void setLandingController(LandingController landingController) {
		 this.landingController = landingController;
	}
}
