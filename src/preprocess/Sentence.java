package preprocess;
import java.util.ArrayList;


public class Sentence {
	private ArrayList<Word> wordList;
	private int id;
	
	public ArrayList<Word> getWordList() {
		return wordList;
	}
	public void setWordList(ArrayList<Word> wordList) {
		this.wordList = wordList;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
}