/*
 * Extractor grabs words or sentences somewhere.
 */
package preprocess;

import java.io.IOException;
import java.util.ArrayList;

import shortcuts.Print;
import shortcuts.Scan;

public class Extractor {

	final static String defaultFileContainer = "src/documents/";
	
	Print p 	= new Print();
	Scan s 		= new Scan();
	
	public Extractor()
	{
		
	}
	
	public ArrayList<Word> StringToWordList(ArrayList<String> word, ArrayList<String> pos, ArrayList<String> lemma, ArrayList<Boolean> stopword)
	{
		ArrayList<Word> wordList	= new ArrayList<Word>();
		Word w	 					= new Word();
		String originalText 	= "";
		
		for (int i = 0; i < word.size(); i++) {
			w.setWord(word.get(i));
			w.setLemma(lemma.get(i));
			w.setPartOfSpeech(pos.get(i));
			w.setStopWord(stopword.get(i));
			
			wordList.add(w);			
			w = new Word();
		}
		
		return wordList;
	}
	
	/*
	 * Gets the list of word and tags from NlpOutput txt file into an ArrayList of Word(s)
	 * */
	public ArrayList<Word> FileToWordList(String filePath)
	{
		ArrayList<Word> word 	=	 new ArrayList<Word>();
		Nlp nlp 				= new Nlp();
		ReaderWrite rw 			= new ReaderWrite(filePath);
		rw.ReadFile(filePath); // original nlpFilePath
		String originalText 	= rw.GetFileContent();
		String[] splittedText 	= originalText.split(" ");
		
		String stopWordsPath 	= "src/documents/stopwords.txt";
		ReaderWrite rwr 			= new ReaderWrite(stopWordsPath);
		String[] stopWords;
		String temp;
		rw.ReadFile();
		temp = rwr.GetFileContent();
		stopWords = temp.split(" ");
		
		for(int i = 0; i < splittedText.length; i++) {
			
			String[] split = splittedText[i].split("/");
			
			Word wordTemp = new Word(split[0]); 
			wordTemp.setPartOfSpeech(split[1]); 
			wordTemp.setLemma(split[2]);
			
			wordTemp.setStopWord(false);
			for(String sw : stopWords)
			{	
				if(wordTemp.getWord().toUpperCase().equals(sw.toUpperCase()))
				{
					
					wordTemp.setStopWord(true);
				}
			}
			
			
			//p.println(splittedText[i]);
			word.add(wordTemp);
		
		}

		return word;
		
		
		
	}
	
	public ArrayList<PreSentence> WordListToSentenceList(ArrayList<Word> wordList)
	{
		ArrayList<PreSentence> sentenceList = new ArrayList<PreSentence>();
		ArrayList<Word> newWords = new ArrayList<Word>();
		Word tempWord;
		PreSentence tempSentence = new PreSentence();
		
		for(int i = 0; i < wordList.size(); i++) 
		{		
			// If a Word() is not equal to a punctuation mark .,?,! then add them to newWords
			
			if (!(wordList.get(i).getWord().equals(".") || wordList.get(i).getWord().equals("?") || wordList.get(i).getWord().equals("!")) ) {
			
				newWords.add(wordList.get(i));
				
			}
			// If they are a punctuation mark, add them to the sentence object. The other lines make sure that this effectively splits the original text into sentences. 
			else {
				
				newWords.add(wordList.get(i));
				tempSentence.setWordList(newWords);
				sentenceList.add(tempSentence);
				tempSentence = new PreSentence();
				newWords = new ArrayList<Word>();
			}
		}
		
		return sentenceList;
	}
	
	/*
	 * Use Word() objects instead to generate content in sentence.
	 */
	public ArrayList<PreSentence> FileToSentenceList(String filePath)
	{
		ArrayList<PreSentence> sentence = new ArrayList<PreSentence>();
		ArrayList<Word> words = new ArrayList<Word>();
		ArrayList<Word> newWords = new ArrayList<Word>();
		Word tempWord;
		PreSentence tempSentence = new PreSentence();
		
		//Retrieve all words in the input file and create them as word objects
		words = FileToWordList(filePath);
		
		// Scan each Word object. This loop detects where to split the input into sentences by checking their punctuation marks. 
		for(int i = 0; i < words.size(); i++) 
		{		
			// If a Word() is not equal to a punctuation mark .,?,! then add them to newWords
			
			if (!(words.get(i).getWord().equals(".") || words.get(i).getWord().equals("?") || words.get(i).getWord().equals("!")) ) {
			
				newWords.add(words.get(i));
				
			}
			// If they are a punctuation mark, add them to the sentence object. The other lines make sure that this effectively splits the original text into sentences. 
			else {
				
				newWords.add(words.get(i));
				tempSentence.setWordList(newWords);
				sentence.add(tempSentence);
				tempSentence = new PreSentence();
				newWords = new ArrayList<Word>();
			}
		}
		
		//not sure what this is
		
		return sentence;
	}
	
	/*
	 * This tests ConvertToSentenceList if it works positively. 
	 * Makes use of nlpFilePath.
	 */
	public ArrayList<PreSentence> Test_FileToSentenceList(String nlpFilePath)
	{
		ArrayList<PreSentence> pSentence = new ArrayList<PreSentence>();
		pSentence = FileToSentenceList(nlpFilePath);
		for(PreSentence s : pSentence){
			for(Word w : s.getWordList()){
				System.out.println(w.getWord()+ " "+w.getPartOfSpeech()+ " "+w.getLemma());
			}
		}
		return pSentence;
	}
	
	/*
	 * Sentence is a String and not a series of Word() objects.
	 */
	public ArrayList<String> SplitSentenceToStrings()
	{
		ArrayList<String> sentenceStrings = new ArrayList<String>();
		
		return sentenceStrings;
	}
	
	
}
