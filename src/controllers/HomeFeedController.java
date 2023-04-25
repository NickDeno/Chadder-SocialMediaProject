package controllers;

import java.util.LinkedList;

import model.AppState;
import model.Post;
import model.User;
import util.GUIUtilities;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.TilePane;
import javafx.scene.shape.Line;

public class HomeFeedController {
	@FXML private TilePane tilePane;
	@FXML private Label filterLabel;
	@FXML private ComboBox<String> filterBox;
	@FXML private ComboBox<String> topicBox;
	@FXML private TextField searchField;
	@FXML private Line searchFieldLine;
	@FXML private Button applyFilterBtn;
	@FXML private Button removeFilterBtn;
	
	private User currentUser;
	private final String[] filterOptions = {"All Posts", "Following", "Likes", "User", "Topic", "Title"};
	private final String[] topicOptions = {"Computer Science", "School", "Gaming", "Gym", "Sports", "Misc.", "Other"};
	private LandingController landingController;
	
	//Initializer
	public HomeFeedController() {}
	
	public void initialize() {
		currentUser = AppState.getInstance().getUserCenter().getCurrentUser();
		filterBox.getItems().addAll(filterOptions);
		topicBox.getItems().addAll(topicOptions);
		filterBox.getSelectionModel().selectFirst();
		Platform.runLater(() ->{
			displayPosts(AppState.getInstance().getPostCenter().getPosts());
		});
	}
	
	@FXML public void filterBoxOnAction(ActionEvent event) {	
		switch(filterBox.getValue()) {
			case "User":
				searchField.setVisible(true);
				searchFieldLine.setVisible(true);	
				topicBox.setVisible(false);
				filterLabel.setText("(Newest to Oldest)");
				break;
			case "Topic":
				searchField.setVisible(false);
				searchFieldLine.setVisible(false);
				topicBox.setVisible(true);
				filterLabel.setText("(Newest to Oldest)");
				break;
			case "Title":
				searchField.setVisible(true);
				searchFieldLine.setVisible(true);
				topicBox.setVisible(false);
				filterLabel.setText("(Newest to Oldest)");
				break;	
			case "Likes":
				searchField.setVisible(false);
				searchFieldLine.setVisible(false);	
				topicBox.setVisible(false);
				filterLabel.setText("(Most to Least Likes)");
				break;
			default:
				searchField.setVisible(false);
				searchFieldLine.setVisible(false);	
				topicBox.setVisible(false);
				filterLabel.setText("(Newest to Oldest)");
				break;
		}
	}
	
	@FXML public void topicBoxOnAction(ActionEvent event) {
		if(topicBox.getValue().equals("Other")) {
			searchField.setVisible(true);
			searchFieldLine.setVisible(true);
		} else {
			searchField.setVisible(false);
			searchFieldLine.setVisible(false);
		}
	}
	
	@FXML public void applyFilterBtnOnAction(ActionEvent event) {
		switch(filterBox.getValue()) {
			case "All Posts":
				tilePane.getChildren().clear();
				displayPosts(AppState.getInstance().getPostCenter().getPosts());
				break;
			case "Following":
				if(currentUser.getFollowing().values().size() == 0) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("No Followed Users.");
					alert.setHeaderText(null);
					alert.setContentText("Unable to filter posts by following since you are not following anyone.");
					alert.showAndWait();
					filterBox.getSelectionModel().selectFirst();
					tilePane.getChildren().clear();
					displayPosts(AppState.getInstance().getPostCenter().getPosts());
					resetFields();
				} else {
					tilePane.getChildren().clear();
					displayFollowingPosts();
				}
				break;
			case "User":
				User searchedUser = AppState.getInstance().getUserCenter().getUser(searchField.getText());
				if(searchedUser == null) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("No User Found.");
					alert.setHeaderText(null);
					alert.setContentText("No user found with specified username. Please try again.");
					alert.showAndWait();
					searchField.clear();
				} else if(currentUser.getBlockedUsers().get(searchedUser.getUsername()) != null) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Blocked User");
					alert.setHeaderText(null);
					alert.setContentText("Sorry. The user you searched for is in you blocked users. If you wish to see their posts, please unblock them.");
					alert.showAndWait();
					searchField.clear();	
				} else {
					tilePane.getChildren().clear();
					displayPosts(searchedUser.getUserPosts());
					resetFields();
				}
				break;
			case "Topic":
				if(topicBox.getValue() == null) {
					filterBox.getSelectionModel().selectFirst();
					tilePane.getChildren().clear();
					displayPosts(AppState.getInstance().getPostCenter().getPosts());
					resetFields();
				} else if(topicBox.getValue().equals("Other")) {
					tilePane.getChildren().clear();
					displayPosts(AppState.getInstance().getPostCenter().searchByTopic(searchField.getText()));
					resetFields();
				} else {
					tilePane.getChildren().clear();
					displayPosts(AppState.getInstance().getPostCenter().searchByTopic(topicBox.getValue()));
					resetFields();
				}
				break;
			case "Title":
				tilePane.getChildren().clear();
				displayPosts(AppState.getInstance().getPostCenter().searchByTitle(searchField.getText()));
				resetFields();		
				break;
			case "Likes":
				tilePane.getChildren().clear();
				displayPosts(AppState.getInstance().getPostCenter().searchByLikes());
				resetFields();
				break;
		}	
	}

	@FXML public void removeFilterBtnOnAction(ActionEvent event) {
		tilePane.getChildren().clear();
		filterBox.getSelectionModel().selectFirst();
		displayPosts(AppState.getInstance().getPostCenter().getPosts());
		resetFields();
	}
	
	private void resetFields() {
		searchField.clear();
		searchField.setVisible(false);
		searchFieldLine.setVisible(false);
		topicBox.setValue("");
		topicBox.setVisible(false);
	}
	
	private void displayPosts(LinkedList<Post> posts) {
		 GUIUtilities.displayPostsNewToOld(posts, tilePane, landingController);
	}
	
	private void displayFollowingPosts() {
		 GUIUtilities.displayFollowingPosts(AppState.getInstance().getPostCenter().getPosts(), tilePane, landingController);
	}
	
	public void setLandingController(LandingController landingController) {
		this.landingController = landingController;
	}
	
}
