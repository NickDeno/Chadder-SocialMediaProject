package model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

public class UserCenter implements Serializable {
	private static final long serialVersionUID = 1L;	
	private TreeMap<String, User> userMap;
	private User currentUser;
	
	public UserCenter() {
		userMap = new TreeMap<String, User>();
	}
	
	public void addUser(User user) {
		userMap.put(user.getUsername(), user);
	}
	
	public User removeUser(String username) {
		return userMap.remove(username);
	}
	
	public User getUser(String username) {
		return userMap.get(username);
	}
	
	public int size() {
		return userMap.size();
	}
	
	public Collection<User> getAllUsers(){
		return userMap.values();
	}
	
	public void setCurrentUser(User user) {
		this.currentUser = user;
	}
	
	public User getCurrentUser() {
		return currentUser;
	}
	
	public void display() {
		userMap.values().forEach(user -> System.out.println(user.toString()));
	}
	
	@Override
	public String toString() {
		String s = "";	
		for(Map.Entry<String, User> entry: userMap.entrySet()) {
			s += entry.getValue().toString();
		}
		return s;
	}

}
