package controllers;

import java.util.Iterator;
import java.util.Optional;
import java.util.TreeMap;

import model.AppState;
import model.Post;
import model.ReplyPost;
import model.User;
import util.GUIUtilities;
import util.Utilities;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

public class SettingsController {
	@FXML private AnchorPane settingsPane;
	@FXML private ListView<String> blockedUsersList;
	@FXML private CheckBox editFieldsBox;
	@FXML private TextField emailField;
	@FXML private Line emailLine;
	@FXML private Line passwordLine;
	@FXML private TextField passwordField;
	@FXML private Hyperlink deleteAccount;
	@FXML private Button blockUserBtn;
	@FXML private Button removeBlockedUserBtn;
	@FXML private Button resetBtn;
	@FXML private Button saveBtn;
	
	private TreeMap<String, User> tempBlockedUsers;
	//This will be a copy of current users' blockedUsers. Then if user adds and removes users to blockedUsers, we make changes to this copy of blockedUsers. 
	//That way,if user decides to reset changes, no changes have been made to the actual users' "blockedUsers". If the user wants to save the changes made, then we 
	//can just set the users "blockedUsers" to "tempBlockedUsers" to save the changes.
	
	private User currentUser;
	private LandingController landingController;
	
	//Initializer
	public SettingsController() {}
	
	public void initialize() {
		currentUser = AppState.getInstance().getUserCenter().getCurrentUser();
		emailField.setText(currentUser.getEmail());
		passwordField.setText(currentUser.getPassword());
		tempBlockedUsers = new TreeMap<String, User>();
		for(User u: currentUser.getBlockedUsers().values()) {
			tempBlockedUsers.put(u.getUsername(), u);
			blockedUsersList.getItems().add(u.getUsername());
		}	
	}
	
	@FXML public void editFieldsBoxOnAction(ActionEvent event) {
		if(editFieldsBox.isSelected()) {
			setFieldsVisibility(true);
		} else {
			setFieldsVisibility(false);
		}
	}
	
	@FXML public void blockUserBtnOnAction(ActionEvent event) {
		landingController.getPane().setEffect(new GaussianBlur(15));
		landingController.getPane().setMouseTransparent(true);
		BlockUserController blockUserController = GUIUtilities.loadNewUndecoratedWindow(GUIUtilities.BlockUserScene);
		blockUserController.setLandingController(landingController);
		blockUserController.setSettingsController(this);
	}
	
	@FXML public void removeBlockedUserBtnOnAction(ActionEvent event) {
		if(blockedUsersList.getSelectionModel().getSelectedItem() == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("No User Selected");
			alert.setHeaderText(null);
			alert.setContentText("Please select a user first and try again.");
			alert.showAndWait();
		} else {
			tempBlockedUsers.remove(blockedUsersList.getSelectionModel().getSelectedItem());
			blockedUsersList.getItems().remove(blockedUsersList.getSelectionModel().getSelectedItem());
		}
	}
	
	@FXML public void deleteAccountOnAction(ActionEvent event) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation");
		alert.setHeaderText(null);
		alert.setContentText("Are you sure you want to delete your account?");
		Optional<ButtonType> confirm = alert.showAndWait();
		if(confirm.isPresent() && confirm.get() == ButtonType.OK) {
//			Removes every reply post instance that was made by this user from PostCenter
			Iterator<ReplyPost> itr  = currentUser.getUserPostReplies().iterator();
			while(itr.hasNext()) {
				ReplyPost replyPost = itr.next();
				AppState.getInstance().getPostCenter().getPost(replyPost.getRepliedPost().getUuid()).getPostReplies().remove(replyPost);
			}
			
//			Removes every post instance that was made by this user from PostCenter
			Iterator<Post> itr2 = currentUser.getUserPosts().iterator();
			while(itr2.hasNext()) {
				Post post = itr2.next();
				AppState.getInstance().getPostCenter().removePost(post);
			}
			
//			Removes this user from every user that they follow 's followers list.
			Iterator<User> itr3 = currentUser.getFollowing().values().iterator();
			while(itr3.hasNext()) {
				User followedUser = itr3.next();
				followedUser.getFollowers().remove(currentUser);
				
			}
//			Removes this user from every user that follows them 's following list.
			Iterator<User> itr4 = currentUser.getFollowers().iterator();
			while(itr4.hasNext()) {
				User followingUser = itr4.next();
				followingUser.getFollowing().remove(currentUser.getUsername());
				
			}
//          Removes user from UserCenter
			AppState.getInstance().getUserCenter().removeUser(currentUser.getUsername());
			GUIUtilities.loadNewScene(((Stage)((Node)event.getSource()).getScene().getWindow()) , GUIUtilities.SignInScene);
		}
	}
	
	@FXML public void resetBtnOnAction(ActionEvent event) {
		resetFields();
	}
	
	@FXML public void saveBtnOnAction(ActionEvent event) {
		if(!Utilities.isValidEmail(emailField.getText()) || !Utilities.isValidPassword(passwordField.getText())) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Invalid Fields");
			alert.setHeaderText(null);
			alert.setContentText("One or more of entered fields was invalid. Please try again.");
			alert.showAndWait();
			resetFields();
		} else {
			currentUser.setEmail(emailField.getText());
			currentUser.setPassword(passwordField.getText());
			currentUser.setBlockedUsers(tempBlockedUsers);
			HomeFeedController homeFeed = GUIUtilities.loadPane(landingController.getContentPane(), GUIUtilities.HomeFeedScene);
			homeFeed.setLandingController(landingController);
			landingController.resetBtns();
			landingController.getHomeBtn().setStyle("-fx-background-color: rgba(255,255,255,0.5)");
		}
	}
	
	@FXML public void checkFieldIsValid(MouseEvent event) {
		if (event.getSource().equals(emailField)) {
			String tempStyle = emailLine.getStyle();
			emailField.textProperty().addListener((observable, oldValue, newValue) -> {
				if (!newValue.isEmpty() && Utilities.isValidEmail(newValue)) emailLine.setStyle(tempStyle + "-fx-stroke: #38ff13;");
				else emailLine.setStyle(tempStyle + "-fx-stroke: #ff0000;");
			});
		} else if (event.getSource().equals(passwordField)) {
			String tempStyle = passwordField.getStyle();
			passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
				if (!newValue.isEmpty() && Utilities.isValidPassword(newValue)) passwordLine.setStyle(tempStyle + "-fx-stroke: #38ff13;");
				else passwordLine.setStyle(tempStyle + "-fx-stroke: #ff0000;");
			});
		}
	}
	
	private void resetFields() {
		emailField.setText(currentUser.getEmail());
		passwordField.setText(currentUser.getPassword());
		emailLine.setStyle("-fx-stroke: #3b93ff;");
		passwordLine.setStyle("-fx-stroke: #3b93ff;");
		blockedUsersList.getItems().clear();
		tempBlockedUsers.clear();
		for(User u: currentUser.getBlockedUsers().values()) {
			tempBlockedUsers.put(u.getUsername(), u);
			blockedUsersList.getItems().add(u.getUsername());
		}	
		editFieldsBox.setSelected(false);
		setFieldsVisibility(false);
	}
	
	private void setFieldsVisibility(boolean bool) {
		if(bool == true) {
			emailField.setDisable(false);
			emailField.setEditable(true);
			passwordField.setDisable(false);
			passwordField.setEditable(true);
			blockUserBtn.setVisible(true);
			blockUserBtn.setDisable(false);
			removeBlockedUserBtn.setVisible(true);
			removeBlockedUserBtn.setDisable(false);
			blockedUsersList.setEditable(true);
			blockedUsersList.setFocusTraversable(true);
		} else {
			emailField.setDisable(true);
			emailField.setEditable(false);
			passwordField.setDisable(true);
			passwordField.setEditable(false);
			blockUserBtn.setVisible(false);
			blockUserBtn.setDisable(true);
			removeBlockedUserBtn.setVisible(false);
			removeBlockedUserBtn.setDisable(true);
			blockedUsersList.setEditable(false);
			blockedUsersList.setFocusTraversable(false);
			blockedUsersList.getSelectionModel().clearSelection();
		}
	}
	
	public void setLandingController(LandingController landingController) {
		 this.landingController = landingController;
	 }
	
	public TreeMap<String, User> getTempBlockedUsers(){
		return tempBlockedUsers;
	}
	
	public ListView<String> getBlockedUsersList(){
		return blockedUsersList;
	}
	
}
