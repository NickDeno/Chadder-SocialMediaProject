 package controllers;

import java.util.Collection;

import model.AppState;
import model.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class BlockUserController {
	@FXML private Button blockSelectedUserBtn;
	@FXML private Button cancelBtn;
	@FXML private ListView<String> userList;
	
	private User currentUser;
	private LandingController landingController;
	private SettingsController settingsController;
	
	public void initialize() {
		currentUser = AppState.getInstance().getUserCenter().getCurrentUser();
		Collection<User> allUsers = AppState.getInstance().getUserCenter().getAllUsers();
		for(User u: allUsers) {
			userList.getItems().add(u.getUsername());
		}
		Platform.runLater(() -> {
			((Stage)cancelBtn.getScene().getWindow()).setOnCloseRequest(e -> {
				landingController.getPane().setEffect(null);
				landingController.getPane().setMouseTransparent(false);
			});
		});
	}
	
	@FXML public void blockSelectedUserBtnOnAction(ActionEvent event) {
		if(userList.getSelectionModel().getSelectedItem() == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Invalid Selection");
			alert.setHeaderText(null);
			alert.setContentText("Please select a user to block and try again.");
			alert.showAndWait();
			return;
		} 	
		//If selected user is the current user
		if(userList.getSelectionModel().getSelectedItem().equals(currentUser.getUsername())) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Invalid Selection");
			alert.setHeaderText(null);
			alert.setContentText("Sorry. You cannot block yourself.");
			alert.showAndWait();
			userList.getSelectionModel().clearSelection();
			return; 
		}
		//If selected user is already present in current users blockedUsers
		if(settingsController.getTempBlockedUsers().containsKey(userList.getSelectionModel().getSelectedItem())) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Invalid Selection");
			alert.setHeaderText(null);
			alert.setContentText("This user is already blocked.");
			alert.showAndWait();
			userList.getSelectionModel().clearSelection();
		} else {
			User blockedUser = AppState.getInstance().getUserCenter().getUser(userList.getSelectionModel().getSelectedItem());
			settingsController.getTempBlockedUsers().put(blockedUser.getUsername(), blockedUser);
			settingsController.getBlockedUsersList().getItems().add(blockedUser.getUsername());
			landingController.getPane().setEffect(null);
			landingController.getPane().setMouseTransparent(false);
			((Stage)((Node)event.getSource()).getScene().getWindow()).close();	
		}	
	}
	
	@FXML public void cancelBtnOnAction(ActionEvent event) {
		landingController.getPane().setEffect(null);
		landingController.getPane().setMouseTransparent(false);
		((Stage)((Node)event.getSource()).getScene().getWindow()).close();
	}
	
	public void setLandingController(LandingController landingController) {
		 this.landingController = landingController;
	}
	
	public void setSettingsController(SettingsController settingsController) {
		this.settingsController = settingsController;
	}
	
}
