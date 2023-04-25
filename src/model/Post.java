package model;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.UUID;

public class Post implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String title;
	private String topic;
	private String description;
	private LinkedList<FXImage> postImages;
	private User poster;
	private Date postDate;
	private LinkedList<Post> postReplies;
	private Integer likes;
	
	private UUID uuid;
	//Since the UUID of each post is always unique, when a certain post in the PostCenter is being searched for, it will be searched for by the UUID since
	//it will only ever return either that specific post, or nothing, since there wont ever be any duplicate UUIDS for multiple posts.

	public Post(String title, String topic, String description, LinkedList<FXImage> postImages, User poster, UUID uuid) {
		super();
		this.title = title;
		this.topic = topic;
		this.description = description;
		this.poster = poster;
		this.postImages = postImages;
		this.uuid = uuid;
		this.postDate = new Date();
		this.postReplies = new LinkedList<Post>();
		this.likes = 0;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public User getPoster() {
		return poster;
	}

	public void setPoster(User poster) {
		this.poster = poster;
	}
	
	public LinkedList<FXImage> getPostImages() {
		return postImages;
	}

	public void setPostImages(LinkedList<FXImage> postImages) {
		this.postImages = postImages;
	}

	public UUID getUuid() {
		return uuid;
	}

	public Date getPostDate() {
		return postDate;
	}
	
	public LinkedList<Post> getPostReplies() {
		return postReplies;
	}
	
	public void setPostReplies(LinkedList<Post> postReplies) {
		this.postReplies = postReplies;
	}
	
	public Integer getLikes() {
		return likes;
	}
	
	public void setLikes(Integer likes) {
		this.likes = likes;
	}
	
	public void like() {
		likes++;
	}
	
	public void reply(ReplyPost replyPost, User currentUser) {
		postReplies.add(replyPost);
		currentUser.getUserPostReplies().add(replyPost);
	}
	
	//Called when post to be deleted is a regular "Post" object => post will be deleted from PostCenter, and from the users' "userPosts"
	public void delete(Post post, User user) {
		AppState.getInstance().getPostCenter().removePost(post);
		AppState.getInstance().getUserCenter().getUser(user.getUsername()).getUserPosts().remove(post);	
	}
	
	//Called when post to be deleted is a "ReplyPost" object => the replyPost will be deleted from post it replied to's "postReplies"
	public void deleteReply(ReplyPost replyPost) {
		replyPost.getRepliedPost().getPostReplies().remove(replyPost);
	}
	
	@Override
	public String toString() {
		String posts = "";
		Iterator<Post> itr = postReplies.iterator();
		while(itr.hasNext()) {
			posts += itr.next().toString() + ", ";
		}
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy h:mm aa");
		return "Post [Created by " + poster.getUsername() + ", title=" + title + ", topic= " + topic + ", description=" + description 
				+ ", postDate=" + df.format(postDate) + ", likes=" + likes +", postReplies=" + posts + "]";
	}
	
}
