package preprocess;

import java.util.ArrayList;


public class Word {
	private String word;
	private String lemma;
	private boolean isComplex;
	private boolean isStopWord;
	private String partOfSpeech;
	private ArrayList<String> substitute;
	private String bestSubstitute;
	
	public Word()
	{
		// None
	}
	
	public Word(String word)
	{
		this.word = word;
	}
	
	public Word(String word, String partOfSpeech, String lemma)
	{
		this.word 			= word;
		this.partOfSpeech 	= partOfSpeech;
		this.lemma 			= lemma;
	}
	
	public Word(String word, String partOfSpeech, String lemma, boolean isStopWord)
	{
		this.partOfSpeech 	= partOfSpeech;
		this.word 			= word;
		this.lemma			= lemma;
		this.isStopWord		= isStopWord;		
	}
	
	public Word(String word, String partOfSpeech, String lemma, boolean isStopWord, boolean isComplex)
	{
		this.partOfSpeech 	= partOfSpeech;
		this.word 			= word;
		this.lemma			= lemma;
		this.isStopWord		= isStopWord;		
		this.isComplex 		= isComplex;
	}
	
	public String getBestSubstitute() 
	{
		return bestSubstitute;
	}

	public void setBestSubstitute(String bestSubstitute) {
		this.bestSubstitute = bestSubstitute;
	}

	public String getWord() {
		return word;
	}
	
	
	public void setWord(String word) {
		this.word = word;
	}
	
	public String getLemma() {
		return lemma;
	}
	public void setLemma(String lemma) {
		this.lemma = lemma;
	}
	public boolean isComplex() {
		return isComplex;
	}
	public void setComplex(boolean isComplex) {
		this.isComplex = isComplex;
	}
	public boolean isStopWord() {
		return isStopWord;
	}
	public void setStopWord(boolean isStopWord) {
		this.isStopWord = isStopWord;
	}
	public String getPartOfSpeech() {
		return partOfSpeech;
	}
	public void setPartOfSpeech(String partOfSpeech) {
		this.partOfSpeech = partOfSpeech;
	}
	public ArrayList<String> getSubstitute() {
		return substitute;
	}
	public void setSubstitute(ArrayList<String> substitute) {
		this.substitute = substitute;
	}
	
}