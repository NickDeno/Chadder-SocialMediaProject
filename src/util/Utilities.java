package util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.util.HashSet;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import model.AppState;

public class Utilities {
	public static HashSet<String> dictionary;
	
	public static void backupAppState() {
		try {
			FileOutputStream fos = new FileOutputStream("backupData/AppState.dat");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(AppState.getInstance());
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static AppState restoreAppState() {
		try {
			FileInputStream fis = new FileInputStream("backupData/AppState.dat");
			ObjectInputStream ois = new ObjectInputStream(fis);
			AppState appState = (AppState) ois.readObject();
			ois.close();
			return appState;
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void loadDictionary() {
		//Sets initial capacity of the HashSet to 99500(Roughly the amount of words in default dictionary.txt file)
		dictionary = new HashSet<String>(99500);
		try {
			BufferedReader br = new BufferedReader(new FileReader("rawData/dictionary.txt"));
			String newLine = "";
			while(newLine != null) {
				newLine = br.readLine();
				dictionary.add(newLine);	
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Unable to load dictionary.");
		} 
	}	
	
	//Takes a file (for this project mainly image files) and converts it into a byte array. This is done since by default, JavaFX images are not
	//serializable. So instead, we can convert the image into a byte array and save the byte array into a specified file.
	public static byte[] fileToByteArr(File file) {
		try {
			byte[] byteArr = Files.readAllBytes(file.toPath());
			return byteArr;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Image byteArrToImage(byte[] imageBuffer) {
		return new Image(new ByteArrayInputStream(imageBuffer));
	}
	
	public static ImagePattern byteArrToImagePattern(byte[] imageBuffer) {
		return new ImagePattern(new Image(new ByteArrayInputStream(imageBuffer)));
	}
	
	public static boolean isValidPassword(String password) {
		if(password.length() < 8) return false;
		boolean hasDigit = false;
		boolean hasUpperCase = false;
		for(int i = 0; i < password.length(); i++) {
			if(Character.isUpperCase(password.charAt(i))) hasUpperCase = true;
			if(Character.isDigit(password.charAt(i))) hasDigit = true;
		}
		return hasDigit && hasUpperCase;
	}

	public static boolean isValidEmail(String email) {
		if(email.isBlank()) return false;
		boolean hasAt = false;
		boolean hasDot = false;
		for(int i = 0; i < email.length(); i++) {
			if(email.charAt(i) == '@') hasAt = true;
			if(email.charAt(i) == '.') hasDot = true;	
		}
		return hasAt && hasDot;	
	}
}
