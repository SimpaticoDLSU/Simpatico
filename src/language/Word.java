package language;
 
import java.util.ArrayList;
 
public class Word {
 
    public static final int SINGLE_WORD = 0;
    public static final int COMPOUND_WORD = 1;
    public static final int MULTI_WORD = 2;
    public static final int NER = 3;
 
    private int id;
    private String word;
    private String lemma;
    private boolean isIgnore;
    private boolean isComplex;
    private boolean isStopWord;
    private String partOfSpeech;
    private int wordType = SINGLE_WORD;
    private ArrayList<String> substitute;
    private String bestSubstitute;
    private boolean isClausePart = false;
    private boolean isEndOfSentence = false;
    private boolean isOpeningBoundary = false;
    private boolean isClosingBoundary = false;
    private boolean isAppositive = false;
    private Long senseId = (long) 0;
 
    public Word() {
        this.substitute = new ArrayList<String>();
    }
 
    public Word(String word) {
        this.word = word;
    }
 
    public String[] getTokens() {
        String[] tokens = {};
        if (word.contains(" ")) {
            tokens = word.split(" ");
        } else if (word.contains("-")) {
            tokens = word.split("-");
        }
        return tokens;
    }
 
    public void appendWord(String appendedWord) {
        this.word = this.word + " " + appendedWord;
 
    }
 
    public Word(int id, String word, String partOfSpeech, String lemma) {
        this.id = id;
        this.word = word;
        this.partOfSpeech = partOfSpeech;
        this.lemma = lemma;
        this.isStopWord = false;
    }
 
    public Word(String word, String partOfSpeech, String lemma) {
        this.word = word;
        this.partOfSpeech = partOfSpeech;
        this.lemma = lemma;
        this.isStopWord = false;
    }
 
    public Word(String word, String partOfSpeech, String lemma, boolean isStopWord) {
        this.partOfSpeech = partOfSpeech;
        this.word = word;
        this.lemma = lemma;
        this.isStopWord = isStopWord;
    }
 
    public Word(String word, String partOfSpeech, String lemma, boolean isStopWord, boolean isComplex) {
        this.partOfSpeech = partOfSpeech;
        this.word = word;
        this.lemma = lemma;
        this.isStopWord = isStopWord;
        this.isComplex = isComplex;
    }
 
    public int getWordType() {
        return wordType;
    }
 
    public boolean isIgnore() {
        return isIgnore;
    }
 
    public void isIgnore(boolean isIgnore) {
        this.isIgnore = isIgnore;
    }
 
    public void setWordType(int wordType) {
        this.wordType = wordType;
    }
 
    public String getBestSubstitute() {
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
 
    public boolean getIsClausePart() {
        return this.isClausePart;
    }
 
    public void setIsClausePart(boolean isClausePart) {
        this.isClausePart = isClausePart;
    }
 
    public boolean getIsEndOfSentence() {
        return this.isEndOfSentence;
    }
 
    public void setIsEndOfSentence(boolean isEndOfSentence) {
        this.isEndOfSentence = isEndOfSentence;
    }
 
    public void setIsOpeningBoundary(boolean isOpeningBoundary) {
        this.isOpeningBoundary = isOpeningBoundary;
    }
 
    public boolean hasTense() {
        return partOfSpeech.equals("VBD") || partOfSpeech.equals("VBG") || partOfSpeech.equals("VBN") || partOfSpeech.equals("VBP") || partOfSpeech.equals("VBZ");
    }
 
    public boolean getIsOpeningBoundary() {
        return this.isOpeningBoundary;
    }
 
    public void setIsClosingBoundary(boolean isClosingBoundary) {
        this.isClosingBoundary = isClosingBoundary;
    }
 
    public boolean getIsClosingBoundary() {
        return this.isClosingBoundary;
    }
 
    public boolean getIsAppositive() {
        return this.isAppositive;
    }
 
    public void setIsAppositive(boolean isAppositive) {
        this.isAppositive = isAppositive;
    }
 
    public int getId() {
        return id;
    }
 
    public void setId(int id) {
        this.id = id;
    }
 
    public void setIgnore(boolean isIgnore) {
        this.isIgnore = isIgnore;
    }
 
    public void setClausePart(boolean isClausePart) {
        this.isClausePart = isClausePart;
    }
 
    public void setEndOfSentence(boolean isEndOfSentence) {
        this.isEndOfSentence = isEndOfSentence;
    }
 
    public void setOpeningBoundary(boolean isOpeningBoundary) {
        this.isOpeningBoundary = isOpeningBoundary;
    }
 
    public void setClosingBoundary(boolean isClosingBoundary) {
        this.isClosingBoundary = isClosingBoundary;
    }
 
    public void setAppositive(boolean isAppositive) {
        this.isAppositive = isAppositive;
    }
 
    public void setSenseId(Long valueOf) {
        // TODO Auto-generated method stub
        this.senseId = valueOf;
    }
 
    public int getSenseId() {
        // TODO Auto-generated method stub
        return this.senseId.intValue();
    }
 
}