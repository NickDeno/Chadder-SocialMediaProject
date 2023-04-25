package controllers;

import model.AppState;
import model.Post;
import model.ReplyPost;
import model.SpellCheckTextArea;
import model.User;

import java.util.Optional;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class PostReplyController {
	@FXML private AnchorPane postReplyPane;
	@FXML private Button cancelBtn;
    @FXML private Button replyBtn;
    private SpellCheckTextArea descriptionField;
    
    private Post post;
    private User currentUser;
    private LandingController landingController;
    private PostWithRepliesController postRepliesController;
    
    public PostReplyController() {} 
    
    public void initialize() {
    	currentUser = AppState.getInstance().getUserCenter().getCurrentUser();
    	descriptionField = new SpellCheckTextArea(670, 255, 75, 45, true);
    	postReplyPane.getChildren().add(descriptionField.getPane());
    	Platform.runLater(() -> {
    		((Stage)cancelBtn.getScene().getWindow()).setOnCloseRequest(e -> {
    			landingController.getPane().setEffect(null);
    			landingController.getPane().setMouseTransparent(false);
    		});
    	});
    }
    
    @FXML public void cancelBtnOnAction(ActionEvent event) {
    	landingController.getPane().setEffect(null);
    	landingController.getPane().setMouseTransparent(false);
    	((Stage)((Node)event.getSource()).getScene().getWindow()).close();
    }

    @FXML public void replyBtnOnAction(ActionEvent event) {
    	if(descriptionField.getMisspelledWords().isEmpty() == false) {
			String misspelledWords = "";
			for(int i = 0; i < descriptionField.getMisspelledWords().size(); i++) {
				String currWord = descriptionField.getMisspelledWords().get(i);
				if(i < descriptionField.getMisspelledWords().size()-1) {
					misspelledWords += currWord + ", ";
				} else {
					misspelledWords += currWord + ".";
				}
			}			
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Misspelled Words");
			alert.setHeaderText(null);
			alert.setContentText("Your post reply description contains the misspelled word(s) " + misspelledWords + " Do you still wish to continue?");
			Optional<ButtonType> confirm = alert.showAndWait();
			if(confirm.isPresent() && confirm.get() == ButtonType.CANCEL) {
				return;
			}
    	} 	
    	ReplyPost newReply = new ReplyPost(currentUser, post, descriptionField.getTextArea().getText());
    	post.reply(newReply, currentUser);
    	postRepliesController.displayPost(newReply);
    	landingController.getPane().setEffect(null);
    	landingController.getPane().setMouseTransparent(false);
    	((Stage)((Node)event.getSource()).getScene().getWindow()).close();
    }
    
    public void setPost(Post post) {	
    	this.post = post;
    }
    
    public void setLandingController(LandingController landingController) {
		 this.landingController = landingController;
	 }

	public void setPostRepliesController(PostWithRepliesController postRepliesController) {
		this.postRepliesController = postRepliesController;
	}
}
