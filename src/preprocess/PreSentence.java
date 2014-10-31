package preprocess;

import java.util.ArrayList;


public class PreSentence {
	private ArrayList<Word> wordList = new ArrayList<Word>();
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
	
	public void addWord(Word newWord) 
	{
		this.wordList.add(newWord);
	}
}
