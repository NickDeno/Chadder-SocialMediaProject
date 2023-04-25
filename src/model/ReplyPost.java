package model;

import java.util.UUID;

public class ReplyPost extends Post {
	private static final long serialVersionUID = 1L;
	
	//Post this ReplyPost was added to
	private Post repliedPost;
	
	public ReplyPost(User user, Post repliedPost ,String description) {
		super(user.getUsername() + "'s Reply", "Reply", description, null, user, UUID.randomUUID());
		this.repliedPost = repliedPost;
	}

	public Post getRepliedPost() {
		return repliedPost;
	}

	public void setRepliedPost(Post repliedPost) {
		this.repliedPost = repliedPost;
	}

}
