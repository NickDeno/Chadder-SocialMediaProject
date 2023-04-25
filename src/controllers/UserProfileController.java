package controllers;

import java.util.LinkedList;

import model.AppState;
import model.Post;
import model.User;
import util.GUIUtilities;
import util.Utilities;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.TilePane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public class UserProfileController {
	@FXML private Rectangle bannerPic;
	@FXML private Circle profilePic;
	@FXML private Label usernameLabel;
	@FXML private Label nicknameLabel;
    @FXML private TextArea bioField;
    @FXML private Button followBtn;
    @FXML private Button unfollowBtn;
    @FXML private Button backBtn;
    
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
    
    
    private User user;
    private User currentUser;
    private LandingController landingController;
    
    //Initializer
    public UserProfileController() {}
    
    public void initialize() {
    	currentUser = AppState.getInstance().getUserCenter().getCurrentUser();
    	followersList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
    		if(newValue != null) {
    			UserProfileController userProfile =  GUIUtilities.loadPane(landingController.getContentPane(), GUIUtilities.UserProfileScene);
				userProfile.setUser(AppState.getInstance().getUserCenter().getUser(newValue));
				userProfile.setLandingController(landingController);
    		}
    	});
		
    	followingList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
    		if(newValue != null) {
    			UserProfileController userProfile =  GUIUtilities.loadPane(landingController.getContentPane(), GUIUtilities.UserProfileScene);
				userProfile.setUser(AppState.getInstance().getUserCenter().getUser(newValue));
				userProfile.setLandingController(landingController);
    		}
    	});
    	Platform.runLater(() -> {
    		bannerPic.setFill(Utilities.byteArrToImagePattern(user.getBannerPic().returnBytes()));
    		profilePic.setFill(Utilities.byteArrToImagePattern(user.getProfilePic().returnBytes()));
    		usernameLabel.setText("@" + user.getUsername());
    		if(user.getNickName() != null) nicknameLabel.setText(user.getNickName());
    		if(user.getBio() != null) bioField.setText(user.getBio());
    		numPosts.setText(String.valueOf(user.getUserPosts().size()));
    		numFollowers.setText(String.valueOf(user.getFollowers().size()));
    		numFollowing.setText(String.valueOf(user.getFollowing().size())); 	
    		for(User u: user.getFollowers()) {
    			followersList.getItems().add(u.getUsername());
    		}
        	for(User u: user.getFollowing().values()) {
    			followingList.getItems().add(u.getUsername());
    		}
    		
    		if(currentUser.getUsername().equals(user.getUsername())) {
    			followBtn.setVisible(false);
    			followBtn.setDisable(true);
    			unfollowBtn.setVisible(false);
    			unfollowBtn.setDisable(true);
    		} else if(currentUser.getFollowing().get(user.getUsername()) != null) {
    			followBtn.setVisible(false);
    			followBtn.setDisable(true);
    		} else {
    			unfollowBtn.setVisible(false);
    			unfollowBtn.setVisible(true);
    		}	   		
    		displayPosts(user.getUserPosts());	
    	});
    }
    
    @FXML public void backBtnOnAction(ActionEvent event) {
    	HomeFeedController homeFeed = GUIUtilities.loadPane(landingController.getContentPane(), GUIUtilities.HomeFeedScene);
    	homeFeed.setLandingController(landingController);
    	landingController.resetBtns();
    	landingController.getHomeBtn().setStyle("-fx-background-color: rgba(255,255,255,0.5)");
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

    @FXML public void followBtnOnAction(ActionEvent event) {
    	user.getFollowers().add(currentUser);
    	currentUser.getFollowing().put(user.getUsername(), user);
    	followersList.getItems().add(currentUser.getUsername());
    	numFollowers.setText(String.valueOf(user.getFollowers().size()));
    	landingController.setNumFollowingLabel(currentUser.getFollowing().size());
    	
    	followBtn.setVisible(false);
    	followBtn.setDisable(true);
    	unfollowBtn.setVisible(true);
    	unfollowBtn.setDisable(false);
    	
    }

    @FXML public void unfollowBtnOnAction(ActionEvent event) {
    	user.getFollowers().remove(currentUser);
    	currentUser.getFollowing().remove(user.getUsername());
    	followersList.getItems().remove(currentUser.getUsername());
    	numFollowers.setText(String.valueOf(user.getFollowers().size()));
    	landingController.setNumFollowingLabel(currentUser.getFollowing().size());
    	
    	unfollowBtn.setVisible(false);
    	unfollowBtn.setDisable(true);
    	followBtn.setVisible(true);
    	followBtn.setDisable(false);
    }
    
    public void displayPosts(LinkedList<Post> posts) {
    	postsTilePane.getChildren().clear();
    	GUIUtilities.displayPostsNewToOld(posts, postsTilePane, landingController);
    }
    
    public void setUser(User user) {
    	this.user = user;
    }
    
    public void setLandingController(LandingController landingController) {
    	this.landingController = landingController;
    }
}
