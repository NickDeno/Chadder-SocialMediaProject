package model;

import java.io.File;
import java.io.Serializable;

import util.Utilities;

//Singleton Class containing the applications' UserCenter and PostCenter. Since this class uses a Singleton design pattern, this ensures there is only one instance
//of the "AppState" (UserCenter and PostCenter). Also, using this Singleton design pattern for the "AppState" allows for global access to the UserCenter and PostCenter
//throughout the program. Furthermore, since there is only one instance of the UserCenter and PostCenter, any changes made to these data centers will always be 
//applied to this one instance.
public class AppState implements Serializable {
	private static final long serialVersionUID = 1L;
	private UserCenter userCenter;
	private PostCenter postCenter;
	private static AppState instance;
	
	//Private constructor. Only accessed on the first ever launch of this program.
	private AppState(UserCenter userCenter, PostCenter postCenter) {
		this.userCenter = userCenter;
		this.postCenter = postCenter;
	}
	
	public static AppState getInstance() {
		if(instance == null && new File("backupData/AppState.dat").exists() == false) { //Condition will be true on first ever launch of program
			instance = new AppState(new UserCenter(), new PostCenter());
		} else if(instance == null) { //Condition will be true on every other launch of program besides first one
			instance = Utilities.restoreAppState();
		} //Condition will be true after launch of program
		return instance; 	
	}
	
	public void displayState() {
		userCenter.display();
		postCenter.display();
	}
	
	public UserCenter getUserCenter() {
		return userCenter;
	}

	public PostCenter getPostCenter() {
		return postCenter;
	}	
}
