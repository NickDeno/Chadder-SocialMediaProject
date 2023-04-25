package controllers;

import java.io.File;
import java.util.LinkedList;
import java.util.Optional;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import model.AppState;
import model.FXImage;
import model.Post;
import model.ReplyPost;
import model.SpellCheckTextArea;
import model.User;
import util.GUIUtilities;
import util.Utilities;

public class EditPostController {
	@FXML private AnchorPane editPostPane;
	@FXML private Button backBtn;
    @FXML private Button resetBtn;
    @FXML private Button browseButton;
	@FXML private Button deletePostBtn;
	@FXML private Button saveChangesBtn;	 
	@FXML private TextField titleField;
	@FXML private ComboBox<String> topicBox;
	@FXML private TextField newTopicField;
	@FXML private Line newTopicLine;
	@FXML private ImageView postImg1;
	@FXML private ImageView postImg2;
	@FXML private ImageView postImg3;	 
	@FXML private Rectangle postImgBorder1;
	@FXML private Rectangle postImgBorder2;
	@FXML private Rectangle postImgBorder3;
	@FXML private Button deletePostImg1Btn;
	@FXML private Button deletePostImg2Btn;
	@FXML private Button deletePostImg3Btn;
	@FXML private Label msgLabel;
	private SpellCheckTextArea descriptionField;
	
	private Post post;
	private User currentUser;
	private final String[] defaultTopics = {"Computer Science", "School", "Gaming", "Gym", "Sports", "Other"};
	private LandingController landingController;
		
	private LinkedList<FXImage> tempPostImages;
	//This will be a deep copy of current posts' postImages. Then if user adds and removes images to post, we make changes to this copy of postImages. That way,
	//if user decides to reset changes, no changes have been made to the actual posts' "postImages". If the user wants to save the changes made, then we 
	//can just set the posts' "postImages" to "tempPostImages" to save the changes.
		
	public void initialize() {
		currentUser = AppState.getInstance().getUserCenter().getCurrentUser();
		topicBox.getItems().addAll(defaultTopics);
		descriptionField = new SpellCheckTextArea(780, 245, 110, 275, true);
		editPostPane.getChildren().add(descriptionField.getPane());	
		Platform.runLater(() -> {
			tempPostImages = new LinkedList<FXImage>(post.getPostImages());
			displayImages(tempPostImages);
		});
	}

    @FXML public void backBtnOnAction(ActionEvent event) {
    	PostWithRepliesController postReplies =  GUIUtilities.loadPane(landingController.getContentPane(), GUIUtilities.PostWithRepliesScene);
		postReplies.setPostData(post);
		postReplies.setLandingController(landingController);
    }
    
    @FXML public void resetBtnOnAction(ActionEvent event) {
    	tempPostImages.clear();
    	tempPostImages.addAll(post.getPostImages());
    	resetFields();
    	displayImages(tempPostImages);   	
    }
	    
    @FXML public void deletePostBtnOnAction(ActionEvent event) {
    	Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation");
		alert.setHeaderText(null);
		alert.setContentText("Are you sure you want to delete this post?");
		Optional<ButtonType> confirm = alert.showAndWait();
		if(confirm.isPresent() && confirm.get() == ButtonType.OK) {	
			try {
				//Checks to see if post to be deleted is a reply post by casting as ReplyPost. If it is, it will delete reply from the post it replied to.
				post.deleteReply(((ReplyPost)post));
			} catch(ClassCastException e) {
				//If exception was caught, => post to be deleted is a regular post => post will be deleted from PostCenter and from the current users posts. 
				post.delete(post, currentUser);
			}	
			HomeFeedController homeFeed = GUIUtilities.loadPane(landingController.getContentPane(), GUIUtilities.HomeFeedScene);
			homeFeed.setLandingController(landingController);	
		}
    }

    @FXML public void saveChangesBtnOnAction(ActionEvent event) {
    	if(!titleField.getText().isEmpty() && !descriptionField.getTextArea().getText().isEmpty()) {
	   		String newPostTopic;
	   		if(topicBox.getValue() == null || topicBox.getValue().equals("")) newPostTopic = "Misc.";
			else if(topicBox.getValue().equals("Other") && newTopicField.getText().isEmpty()) newPostTopic = "Misc.";	
			else if(topicBox.getValue().equals("Other") && !newTopicField.getText().isEmpty()) newPostTopic = newTopicField.getText();
			else newPostTopic = topicBox.getValue();
				
		   	post.setTitle(titleField.getText());
		   	post.setTopic(newPostTopic);
		   	post.setDescription(descriptionField.getTextArea().getText());
		   	post.setPostImages(tempPostImages);
		   	PostWithRepliesController postReplies =  GUIUtilities.loadPane(landingController.getContentPane(), GUIUtilities.PostWithRepliesScene);
			postReplies.setPostData(post);
			postReplies.setLandingController(landingController);
	    } else {
	    	resetFields();
			msgLabel.setText("Error. Please Try Again.");
			msgLabel.setVisible(true);
	    }
    }
	    
    @FXML public void topicBoxOnAction(ActionEvent event) {
    	if(topicBox.getValue().equals("Other")) {
			 newTopicField.setVisible(true);
			 newTopicLine.setVisible(true);
		 } else {
			 newTopicField.setVisible(false);
			 newTopicLine.setVisible(false);
		 }
    }

    @FXML public void browseButtonOnAction(ActionEvent event) {
    	if(tempPostImages.size() == 3) {
    		Alert alert = new Alert(AlertType.ERROR);
    		alert.setTitle("Images Limit Reached");
			alert.setHeaderText(null);
			alert.setContentText("If you would like to add a new image, please delete one of the exisitng ones first.");
			alert.showAndWait();
    	} else {
    		FileChooser fc = new FileChooser();
    		fc.getExtensionFilters().addAll(new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));
    		File selectedFile = fc.showOpenDialog(null);
    		if (selectedFile == null) {
    			Alert alert = new Alert(AlertType.ERROR);
    			alert.setTitle("Invalid File");
    			alert.setHeaderText(null);
    			alert.setContentText("Either chosen image was invalid, or no image was chosen. Please try again.");
    			alert.showAndWait();
    		} else {
    			byte[] chosenImageBytes = Utilities.fileToByteArr(selectedFile);
    			tempPostImages.add(new FXImage(chosenImageBytes));
    			displayImage(chosenImageBytes);			
    		}
    	}
    }
	    
    @FXML public void deletePostImg1BtnOnAction(ActionEvent event) {
    	if(tempPostImages.size() == 1) {
    		tempPostImages.removeFirst();
    		postImg1.setImage(null);
    		deletePostImg1Btn.setVisible(false);
    	} else if(tempPostImages.size() == 2) {
    		tempPostImages.removeFirst();
    		postImg1.setImage(postImg2.getImage());
    		postImg2.setImage(null);
    		deletePostImg2Btn.setVisible(false);
    	} else if(tempPostImages.size() == 3) {
    		tempPostImages.removeFirst();
    		postImg1.setImage(postImg2.getImage());
    		postImg2.setImage(postImg3.getImage());
    		postImg3.setImage(null);
    		deletePostImg3Btn.setVisible(false);		
    	}
    }
    
    @FXML public void deletePostImg2BtnOnAction(ActionEvent event) {
    	if(tempPostImages.size() == 2) {
    		tempPostImages.remove(1);
    		postImg2.setImage(null);
    		deletePostImg2Btn.setVisible(false);
    	} else if(tempPostImages.size() == 3) {
    		tempPostImages.remove(1);
    		postImg2.setImage(postImg3.getImage());
    		postImg3.setImage(null);
    		deletePostImg3Btn.setVisible(false);
    	}    	
    }
    
    @FXML public void deletePostImg3BtnOnAction(ActionEvent event) {
    	tempPostImages.removeLast();
    	postImg3.setImage(null);
    	deletePostImg3Btn.setVisible(false);	
    }
    
    private void resetFields() {
    	titleField.setText(post.getTitle());
    	topicBox.setValue(post.getTopic());
    	descriptionField.getTextArea().replaceText(post.getDescription());
    	postImg1.setImage(null);
    	postImg2.setImage(null);
    	postImg3.setImage(null);
    	deletePostImg1Btn.setVisible(false);
    	deletePostImg2Btn.setVisible(false);
    	deletePostImg3Btn.setVisible(false);
    	msgLabel.setVisible(false);
    }
	    
    private void displayImage(byte[] chosenImageBytes) {
		if(postImg1.getImage() == null) {
			postImg1.setImage(Utilities.byteArrToImage(chosenImageBytes));
			deletePostImg1Btn.setVisible(true);
		} else if(postImg2.getImage() == null) {
			postImg2.setImage(Utilities.byteArrToImage(chosenImageBytes));
			deletePostImg2Btn.setVisible(true);
		} else if(postImg3.getImage() == null) {
			postImg3.setImage(Utilities.byteArrToImage(chosenImageBytes));
			deletePostImg3Btn.setVisible(true);
		} 
	}
    
    private void displayImages(LinkedList<FXImage> postImages) {
    	if(postImages.size() == 1) {
			postImg1.setImage(Utilities.byteArrToImage(postImages.get(0).returnBytes()));
			deletePostImg1Btn.setVisible(true);
		} else if(postImages.size() == 2) {
			postImg1.setImage(Utilities.byteArrToImage(postImages.get(0).returnBytes()));
			postImg2.setImage(Utilities.byteArrToImage(postImages.get(1).returnBytes()));
			deletePostImg1Btn.setVisible(true);
			deletePostImg2Btn.setVisible(true);
		} else if(postImages.size() == 3) {
			postImg1.setImage(Utilities.byteArrToImage(postImages.get(0).returnBytes()));
			postImg2.setImage(Utilities.byteArrToImage(postImages.get(1).returnBytes()));
			postImg3.setImage(Utilities.byteArrToImage(postImages.get(2).returnBytes()));
			deletePostImg1Btn.setVisible(true);
			deletePostImg2Btn.setVisible(true);
			deletePostImg3Btn.setVisible(true);
		}
    }
	    
    public void setLandingController(LandingController landingController) {
		this.landingController = landingController;
	 }
	    
    public void setPostData(Post post) {
	   	this.post = post;
	   	titleField.setText(post.getTitle());
	   	topicBox.setValue(post.getTopic());
	   	descriptionField.getTextArea().replaceText(post.getDescription());
    }
}
