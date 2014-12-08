package language;
import java.util.ArrayList;

/**
 * The Text class represents the sentences, phrases, and words in the document as well as the tree structure for each sentence. 
 * 
 * Text contains the words, sentences, and phrases found within a text document.
 * Text should contain preprocessed information from Nlp.java and PreAnalysis.java
 * @author laurenztolentino
 *
 */
public class Text {
	
	private ArrayList<String> sentenceTrees;
	private ArrayList<PreSentence> preSentences;
	private ArrayList<Phrase> phrases;
	private ArrayList<Word> words;
	
	/**
	 * Gets the sentenceTrees in the Text class.
	 * sentenceTrees are in the format of (ROOT (S ( ... ) ) )
	 * @return
	 * ArrayList of String with each index containing the format (ROOT (S ( ... ) ) ) of each sentence
	 */
	public ArrayList<String> getSentenceTrees() {
		return sentenceTrees;
	}
	/**
	 * Sets the sentence tree 
	 * @param sentenceTrees 
	 * must be ArrayList of strings following the format (ROOT (S ( ... ) ) )
	 */
	public void setSentenceTrees(ArrayList<String> sentenceTrees) {
		this.sentenceTrees = sentenceTrees;
	}
	/**
	 * @return 
	 * an ArrayList of the PreSentence class.
	 * The PreSentence is an ArrayList of the Word Class.
	 */
	public ArrayList<PreSentence> getPreSentences() {
		return preSentences;
	}
	/**
	 * Set the PreSentence ArrayList of the Text class.
	 * @param preSentences
	 * Must be an ArrayList of the PreSentence class.
	 */
	public void setPreSentences(ArrayList<PreSentence> preSentences) {
		this.preSentences = preSentences;
	}
	/**
	 * Gets the Phrases in the text
	 * @return
	 */
	public ArrayList<Phrase> getPhrases() {
		return phrases;
	}
	/**
	 * Sets the Phrases in the Text class.
	 * @param phrases
	 * Must be an ArrayList of the Phrase object/class
	 */
	public void setPhrases(ArrayList<Phrase> phrases) {
		this.phrases = phrases;
	}
	/**
	 * Gets the Word()s class in the Text class/object.
	 * @return
	 *  an ArrayLIst of the Word objec.
	 */
	public ArrayList<Word> getWords() {
		return words;
	}
	public void setWords(ArrayList<Word> words) {
		this.words = words;
	}
	
	
}
