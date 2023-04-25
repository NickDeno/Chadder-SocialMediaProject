package model;

import java.text.BreakIterator;
import java.util.LinkedList;

import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.StyleClassedTextArea;

import javafx.geometry.Insets;
import javafx.scene.control.IndexRange;
import javafx.scene.layout.StackPane;
import util.Utilities;

//Custom TextArea using RichTextFX Plugin. By default, the JavaFX TextArea cannot set a style to a certain part of the text in the TextArea, for example
//underlining specific words that are misspelled. The RichTextFX StyleClassedTextArea contains a ".setStyleClass" method which allows the user to set a style
//from a beginning index and ending index. This will be used when implementing the spell check feature.
public class SpellCheckTextArea {
	private StyleClassedTextArea textArea;
	private StackPane stackPane;
	
	private LinkedList<IndexRange> errors;
	//Stores the IndexRange of each misspelled word in the text. The IndexRange object just holds 2 int values, in this case the startingIdx and endingIdx of the word
	//Then using the .setStyleClass of the RichTextFx StyleClassedTextArea, we can add a red underline style to each IndexRange in the TextArea to show that those 
	//words are misspelled.
	
	private LinkedList<String> misspelledWords;
	//Stores the misspelled words of the TextArea. Each time a user types in the TextArea, the spellCheck method is called to determine which words are misspelled.
	//Then, those words are added to this list to keep stored. This can be useful for when a user tries to create a post, we can prompt the user and show them 
	//all the currently misspelled words in their post by displaying the misspelledWords list.
	
	public SpellCheckTextArea(int width, int height, int posX, int posY, boolean isEditable) {
		textArea = new StyleClassedTextArea();
		textArea.setMinWidth(width);
		textArea.setMinHeight(height);
		textArea.setWrapText(true);
		textArea.setPadding(new Insets(7));
		textArea.setUseInitialStyleForInsertion(true);
		textArea.setEditable(isEditable);
		//Adds ID for CSS Styling
		textArea.setId("spellCheckTextArea");
		
		stackPane = new StackPane();
		stackPane.setMinWidth(width);
		stackPane.setMinHeight(height);
		stackPane.getChildren().add(new VirtualizedScrollPane<>(textArea));
		stackPane.setLayoutX(posX);
		stackPane.setLayoutY(posY);	
		
		errors = new LinkedList<IndexRange>();
		misspelledWords = new LinkedList<String>();

		textArea.textProperty().addListener((observable, oldText, newText) -> {
			if(newText.length() <= 0) {
				return;
			} else {
				//Clears previous spell check.
				textArea.setStyleClass(0, newText.length(), "");
				misspelledWords.clear();
				errors.clear();
				
				//Rechecks current text for spelling errors and underlines misspelled words
				spellCheck(newText);
				for(IndexRange error: errors) {
					textArea.setStyleClass(error.getStart(), error.getEnd(), "underlined");	
				}	
			}
		});	
	}
	
	//Iterates through given text and checks if each word is misspelled. To do this, it gets the current word and checks if its not in the "dictionary" HashTable, 
	//=> the word is misspelled. If the word is misspelled, the startingIdx and endingIdx of the word in the text will be added as an IndexRange object into the 
	//errorsList. Then, the misspelled word is added to the misspelledWords List
	public void spellCheck(String text) {
		BreakIterator itr = BreakIterator.getWordInstance();
		itr.setText(text);	
		int lastIdx = 0;
		while(lastIdx != BreakIterator.DONE) {
			int firstIdx = lastIdx;
			lastIdx = itr.next();
			if(lastIdx != BreakIterator.DONE && Character.isLetterOrDigit(text.charAt(firstIdx))) {
				String currWord = text.substring(firstIdx, lastIdx).toLowerCase();
				//Where Dictionary HashTable is utilized.
				if(!Utilities.dictionary.contains(currWord)) {
					errors.add(new IndexRange(firstIdx, lastIdx));
					misspelledWords.add(currWord);
				}
			}
		}	
	}
	
	public LinkedList<String> getMisspelledWords(){
		return misspelledWords;
	}
	
	public StyleClassedTextArea getTextArea() {
		return textArea;
	}
	
	public StackPane getPane() {
		return stackPane;
	}
	
}
