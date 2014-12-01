package language;

import java.util.ArrayList;

import edu.stanford.nlp.trees.Tree;


public class PreSentence {
	
	private int id;
	private ArrayList<Word> wordList 	= new ArrayList<Word>();
	private Tree sentenceTree;
	private Word openingBoundary;
	private Word closingBoundary;
	private Phrase nounPhrase;
	private Phrase verbPhrase;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public ArrayList<Word> getWordList() { 
		return wordList;
	}
	public void setWordList(ArrayList<Word> wordList) {
		this.wordList = wordList;
	}
	public Tree getSentenceTree() {
		return sentenceTree;
	}
	public void setSentenceTree(Tree sentenceTree) {
		this.sentenceTree = sentenceTree;
	}
	public Word getOpeningBoundary() {
		return openingBoundary;
	}
	public void setOpeningBoundary(Word openingBoundary) {
		this.openingBoundary = openingBoundary;
	}
	public Phrase getNounPhrase() {
		return nounPhrase;
	}
	public void setNounPhrase(Phrase nounPhrase) {
		this.nounPhrase = nounPhrase;
	}
	public Phrase getVerbPhrase() {
		return verbPhrase;
	}
	public void setVerbPhrase(Phrase verbPhrase) {
		this.verbPhrase = verbPhrase;
	}
	public Word getClosingBoundary() {
		return closingBoundary;
	}
	public void setClosingBoundary(Word closingBoundary) {
		this.closingBoundary = closingBoundary;
	}
	/**
	 * Generates a "PreSentence [id= __ , wordList= __ ..." type of String.
	 * This could be very long so please use with caution.
	 * @author Laurenz
	 */
	@Override
	public String toString() {
		return "PreSentence [id=" + id + ", wordList=" + wordList
				+ ", sentenceTree=" + sentenceTree + ", openingBoundary="
				+ openingBoundary + ", closingBoundary=" + closingBoundary
				+ ", nounPhrase=" + nounPhrase + ", verbPhrase=" + verbPhrase
				+ "]";
	}
	
	
	
}
