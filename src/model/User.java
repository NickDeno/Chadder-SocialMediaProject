package model;

import java.io.File;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.TreeMap;

import util.Utilities;

public class User implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//Key field
	private String username;
	
	private String password;
	private String email;
	private String nickName;
	private String bio;
	private FXImage profilePic;
	private FXImage bannerPic;
	
	
	private LinkedList<Post> userPosts;	
	//Stores shallow copy of all posts created by this user. So whenever we want to display this users posts, we can just iterate through this linkedList and
	//display each one. This prevents the need of searching the PostCenter for every post created by this user which would be a lot slower in terms of time 
	//complexity since PostCenter contains every post created by every user. Also, since this instance of the posts is a shallow copy, whenever we update or make
	//changes to this instance of the post, it will also be changed in the PostCenter post instance since they are shallow copies of each other, and vice versa. 
	//Making the 2 instances of the user posts shallow copies ensures that when we make changes to a post, we do not need to update the post 2 times, one 
	//in this users' userPosts and one in the PostCenter.
	
	private LinkedList<ReplyPost> userPostReplies;
	//Stores shallow copy of all reply posts created by this user. The main use case for this is on deletion of account. When account is deleted, all posts and
	//post replies created by this user need to be deleted in the PostCenter. So instead of iterating through each post in PostCenter to check if it was created
	//by this user, and also check if each posts post replies contain a post created by this user, which would be very slow in terms of time complexity,
	//we can store a shallow copy of all the post replies this user created. Then on deletion of account, we can iterate through the userPosts and userPostReplies
	//LinkedLists and delete them from there. Since they are shallow copies, the corresponding instance in PostCenter will also be deleted.

	private LinkedList<User> followers;
	//Stores all users following this user. Since there is never a need to search for a specific follower in this application, a LinkedList is satisfactory
	
	private TreeMap<String, User> following;
	//Stores all users this user is following. Since throughout this application there are many cases a specific user in following needs to be searched for,
	//a TreeMap is more desired since the search time is O(log(n))
	
	private TreeMap<String, User> blockedUsers;
	//Stores all users this user has blocked. Since throughout this application there are many cases a specific user in blockedUsers needs to be searched for,
	//a TreeMap is more desired since the search time is O(log(n))
	
	public User(String username, String password, String email, FXImage profilePic) {
		super();
		this.username = username;
		this.password = password;
		this.email = email;
		this.nickName = null;
		this.bio = null;
		this.profilePic = profilePic;
		this.bannerPic = new FXImage(Utilities.fileToByteArr(new File("src/assets/DefaultBanner.png")));
		this.userPosts = new LinkedList<Post>();
		this.userPostReplies = new LinkedList<ReplyPost>();
		this.followers = new LinkedList<User>();
		this.following =  new TreeMap<String, User>();
		this.blockedUsers = new TreeMap<String, User>();	
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public FXImage getProfilePic() {
		return profilePic;
	}

	public void setProfilePic(FXImage profilePic) {
		this.profilePic = profilePic;
	}
	
	public FXImage getBannerPic() {
		return bannerPic;
	}

	public void setBannerPic(FXImage bannerPic) {
		this.bannerPic = bannerPic;
	}

	public LinkedList<Post> getUserPosts() {
		return userPosts;
	}
	
	public LinkedList<ReplyPost> getUserPostReplies(){
		return userPostReplies;
	}
	
	public LinkedList<User> getFollowers(){
		return followers;
	}
	
	public TreeMap<String, User> getFollowing(){
		return following;
	}
	
	public void setBlockedUsers(TreeMap<String, User> blockedUsers) {
		this.blockedUsers = blockedUsers;
	}
		
	public TreeMap<String, User> getBlockedUsers(){
		return blockedUsers;
	}
	
	public void follow(User user) {
		following.put(user.getUsername(), user);
	}

	@Override
	public String toString() {
		String userPosts = "";
		String followers = "[";
		String following = "[";
		String blockedUsers = "[";
		for(Post p: this.userPosts) userPosts += p.toString();	
		for(User u: this.followers) followers += u.getUsername() + ", ";
		for(User u: this.following.values()) following += u.getUsername() + ", ";
		for(User u: this.blockedUsers.values()) blockedUsers += u.getUsername() + ", ";
		return "User [username=" + username + ", password=" + password + ", email=" + email + ", bio=" + bio + ", followers=" 
				+ followers + "], following=" + following + "], blockedUsers=" + blockedUsers +  "], userPosts=" + userPosts + "]";
	}
}
