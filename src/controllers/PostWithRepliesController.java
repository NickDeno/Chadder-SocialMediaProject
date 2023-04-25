package controllers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.LinkedList;

import model.AppState;
import model.Post;
import model.User;
import util.GUIUtilities;
import util.Utilities;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;

public class PostWithRepliesController {
	@FXML private ImageView profilePic;
	@FXML private Label usernameLabel;
	@FXML private Label titleLabel;
	@FXML private Label dateLabel;
	@FXML private Label topicLabel;
	@FXML private Label likeCounter;
	@FXML private TextArea descriptionField;
    @FXML private TilePane tilePane; 
    @FXML private Button likeBtn;
    @FXML private Button viewImagesBtn;
    @FXML private Button replyBtn;
    @FXML private Button viewProfileBtn;
    @FXML private Button backBtn;
    @FXML private Button editPostBtn;
    
    private User currentUser;
    private Post post;
    private LandingController landingController;
    
	//Initializer
	public PostWithRepliesController() {}
	
	public void initialize() {
		currentUser = AppState.getInstance().getUserCenter().getCurrentUser();
		Platform.runLater(() -> {
			displayPosts(post.getPostReplies());
			if(currentUser.getUsername().equals(post.getPoster().getUsername())) {
				editPostBtn.setVisible(true);
				likeBtn.setLayoutX(likeBtn.getLayoutX() + 75);
				viewImagesBtn.setLayoutX(viewImagesBtn.getLayoutX() + 75);
				replyBtn.setLayoutX(replyBtn.getLayoutX() + 75);
				viewProfileBtn.setLayoutX(viewProfileBtn.getLayoutX() + 75);
			}
		});
	}
	
	@FXML public void likeBtnOnAction(ActionEvent event) {
		if(post.getPoster().getUsername().equals(currentUser.getUsername())) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText(null);
			alert.setContentText("You cannot like your own posts.");
			alert.showAndWait();
		} else {
			post.like();	
			likeCounter.setText(String.valueOf(post.getLikes()));	
		}	
	}
	
	@FXML public void viewImagesBtnOnAction(ActionEvent event) {
		if(post.getPostImages() == null || post.getPostImages().isEmpty()) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("No Images");
			alert.setHeaderText(null);
			alert.setContentText("There are no pictures linked to this post.");
			alert.showAndWait();
		} else {
			landingController.getPane().setEffect(new GaussianBlur(15));
			landingController.getPane().setMouseTransparent(true);
			PostImagesController postImagesController = GUIUtilities.loadNewUndecoratedWindow(GUIUtilities.PostImagesScene);
			postImagesController.setPostImages(post.getPostImages());
			postImagesController.setLandingController(landingController);
		}	
	}
	
	@FXML public void replyBtnOnAction(ActionEvent event) {
		landingController.getPane().setEffect(new GaussianBlur(15));
		landingController.getPane().setMouseTransparent(true);
		PostReplyController replyController = GUIUtilities.loadNewUndecoratedWindow(GUIUtilities.PostReplyScene);
		replyController.setPost(post);	
		replyController.setLandingController(landingController);
		replyController.setPostRepliesController(this);
	}
	
	@FXML public void viewProfileBtnOnAction(ActionEvent event) {
		UserProfileController userProfileController =  GUIUtilities.loadPane(landingController.getContentPane(), GUIUtilities.UserProfileScene);
		userProfileController.setUser(post.getPoster());
		userProfileController.setLandingController(landingController);	
	}
	
	@FXML public void backBtnOnAction(ActionEvent event) {
		HomeFeedController homeFeedController = GUIUtilities.loadPane(landingController.getContentPane(), GUIUtilities.HomeFeedScene);
		homeFeedController.setLandingController(landingController);
	}
	
	@FXML public void editPostBtnOnAction(ActionEvent event) {
		EditPostController editPostController = GUIUtilities.loadPane(landingController.getContentPane(), GUIUtilities.EditPostScene);
		editPostController.setLandingController(landingController);
		editPostController.setPostData(post);
	}
	
	public void setPostData(Post post) {
		this.post = post;
		profilePic.setImage(Utilities.byteArrToImage(post.getPoster().getProfilePic().returnBytes()));
		usernameLabel.setText(post.getPoster().getUsername());
		titleLabel.setText(post.getTitle());
		descriptionField.setText(post.getDescription());
		topicLabel.setText("Topic: " + post.getTopic());
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy h:mm aa");
		dateLabel.setText(df.format(post.getPostDate()));
		likeCounter.setText(String.valueOf(post.getLikes()));
	}
	
	public void displayPosts(LinkedList<Post> posts) {
		GUIUtilities.displayPostsOldToNew(posts, tilePane, landingController);
	}
	
	public void displayPost(Post post) {
		GUIUtilities.displayPost(post, tilePane, landingController);
	}
	
	public void setLandingController(LandingController landingController) {
		this.landingController = landingController;
	}
	
}
