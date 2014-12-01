/*
 * List of phrase types
 * 1) "appositive": Kobe Bryant <appositive> , the owner of Lakers BBQ Grill, </appositive> was defeated by the Celtics. 
 */
package language;

import java.util.ArrayList;

public class Phrase {
	
	private ArrayList<Word> wordList = new ArrayList<Word>();
	private String phraseType;
	private int id;
	private Boolean hasAppositive;
	
	public String getPhraseType() {
		return phraseType;
	}
	public void setPhraseType(String phraseType) {
		this.phraseType = phraseType;
	}
	public Boolean getHasAppositive() {
		return hasAppositive;
	}
	public void setHasAppositive(Boolean hasAppositive) {
		this.hasAppositive = hasAppositive;
	}
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
