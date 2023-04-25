package util;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import model.AppState;
import model.Post;
import controllers.LandingController;
import controllers.PostController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class GUIUtilities {
	public static final String SignInScene = "/views/SignIn.fxml";
	public static final String SignUpScene = "/views/SignUp.fxml";
	public static final String LandingScene = "/views/Landing.fxml";
	public static final String HomeFeedScene = "/views/HomeFeed.fxml";
	public static final String CurrentUserProfileScene = "/views/CurrentUserProfile.fxml";
	public static final String SettingsScene = "/views/Settings.fxml";
	public static final String CreatePostScene = "/views/CreatePost.fxml";
	public static final String PostWithRepliesScene = "/views/PostWithReplies.fxml";
	public static final String PostImagesScene = "/views/PostImages.fxml";
	public static final String PostReplyScene = "/views/PostReply.fxml";
	public static final String BlockUserScene = "/views/BlockUser.fxml";
	public static final String UserProfileScene = "/views/UserProfile.fxml";
	public static final String EditPostScene = "/views/EditPost.fxml";
	
	public static final String stylesheet = "views/Stylesheet.css";
	
	//Loads new window with specified fxmlFile and returns FXMLController of the fxmlFile being loaded
	public static <T> T loadNewWindow(String fxmlFileName) {
		try {
			FXMLLoader loader = new FXMLLoader(GUIUtilities.class.getResource(fxmlFileName));
			Parent root = loader.load();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(stylesheet);
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.setResizable(false);
			stage.getIcons().add(new Image("/assets/ChadderIcon.png"));
			stage.show();
			return loader.getController();
		} catch (IOException e) {
			System.out.println("Unable to load new window with " + fxmlFileName);
			e.printStackTrace();
			return null;
		}
	}
	
	//Loads new undecorated window with specified fxmlFile and returns FXMLController of the fxmlFile being loaded
	public static <T> T loadNewUndecoratedWindow(String fxmlFileName) {
		try {
			FXMLLoader loader = new FXMLLoader(GUIUtilities.class.getResource(fxmlFileName));
			Parent root = loader.load();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(stylesheet);
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.setResizable(false);
			stage.initStyle(StageStyle.UNDECORATED);
			stage.getIcons().add(new Image("/assets/ChadderIcon.png"));
			stage.show();
			return loader.getController();
		} catch (IOException e) {
			System.out.println("Unable to load new undecorated window with " + fxmlFileName);
			e.printStackTrace();
			return null;
		}
	}
	
	// Loads new scene into specified stage with specified fxmlFile. Returns the FXMLController of the fxmlFile being loaded
	public static <T> T loadNewScene(Stage stage, String fxmlFileName) {
		try {
			FXMLLoader loader = new FXMLLoader(GUIUtilities.class.getResource(fxmlFileName));
			Parent root = loader.load();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(stylesheet);
			stage.setScene(scene);
			stage.show();
			return loader.getController();
		} catch (IOException e) {
			System.out.println("Unable to load scene with " + fxmlFileName);
			e.printStackTrace();
			return null;
		}
	}
	
	//Loads specified AnchorPane with specified fxmlFile. Returns controller of the loaded fxmlFile.
	public static <T> T loadPane(AnchorPane ap, String fxmlFileName) {
		try {
			FXMLLoader loader = new FXMLLoader(GUIUtilities.class.getResource(fxmlFileName));
			AnchorPane pane = loader.load();
			pane.getStylesheets().add(stylesheet);
			ap.getChildren().clear();
			ap.getChildren().add(pane);
			return loader.getController();		
		} catch (IOException e) {
			System.out.println("Unable to load content pane with " + fxmlFileName);
			e.printStackTrace();
			return null;
		}
	}
	
	/* Displays a linked list of posts into a specified tilePane. Since in this case we want to display posts from newest to oldest, this method will iterate
	 * over the linked list from rear to front. Each time it iterates, it checks if the posts' poster(user who posted that post) is in the current users 
	 * "blockedUsers". If they are, it skips over displaying that post */
	public static void displayPostsNewToOld(LinkedList<Post> posts, TilePane tilePane, LandingController landingController) {	
		Iterator<Post> itr = posts.descendingIterator();
		while(itr.hasNext()) {
			Post post = itr.next();
			if(!AppState.getInstance().getUserCenter().getCurrentUser().getBlockedUsers().containsKey(post.getPoster().getUsername())) { //O(Log(n))
				displayPost(post, tilePane, landingController);
			}
		}
	}
	
	/* This method functions virtually the same as "displayPostsNewToOld" method. However in this case the linked list of posts is iterated from front 
	 * to rear. This means that the newest posts will be at the bottom, and the oldest will be at the top. */
	public static void displayPostsOldToNew(LinkedList<Post> posts, TilePane tilePane, LandingController landingController) {
		Iterator<Post> itr = posts.iterator();
		while(itr.hasNext()) {
			Post post = itr.next();
			if(!AppState.getInstance().getUserCenter().getCurrentUser().getBlockedUsers().containsKey(post.getPoster().getUsername())) { //O(Log(n))
				displayPost(post, tilePane, landingController);
			}
		}
	}
	
	/* This method functions virtually the same as "displayPostsNewToOld" method. However, for each iteration, in addition to checking if the current posts'
	 * poster is in the current users "blockedUsers", it also checks if the posts' poster in the current users following. If the poster is not in the current 
	 * users' "blockedUsers", but in the current users following, it will display post. */
	public static void displayFollowingPosts(LinkedList<Post> posts, TilePane tilePane, LandingController landingController) {
		Iterator<Post> itr = posts.descendingIterator();
		while(itr.hasNext()){
			Post post = itr.next();
			if(!AppState.getInstance().getUserCenter().getCurrentUser().getBlockedUsers().containsKey(post.getPoster().getUsername())) {  //O(Log(n))
				if(AppState.getInstance().getUserCenter().getCurrentUser().getFollowing().containsKey(post.getPoster().getUsername())) { //O(Log(n))
					displayPost(post, tilePane, landingController);
				}
			}
		}
	}
	
	//Creates an anchorPane that displays the "Post.fxml" file. Then passes through a specified posts' data into the "Post.fxml" file such as poster, desc., etc..
	public static void displayPost(Post post, TilePane tilePane, LandingController landingController) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader();
			fxmlLoader.setLocation(GUIUtilities.class.getResource("/views/Post.fxml"));
			AnchorPane ap = fxmlLoader.load();
			PostController postController = fxmlLoader.getController();
			postController.setPostData(post);
			postController.setLandingController(landingController);
			tilePane.getChildren().add(ap);	
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
}
