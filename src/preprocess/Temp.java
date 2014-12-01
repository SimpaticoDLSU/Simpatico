/*
 * REPLACE TEMP WITH ADAPTER.
 * This is the original Adapter code.
 * Temp resulted in the unknown events of the loss and death of the original Adapter.java
 */

/*
 * This was created in order to smoothen and ease the problem of abstracting the integration between CoreNLP and JMWE.
 * */
package preprocess;

import java.io.IOException;
import java.util.ArrayList;

import language.PreSentence;
import language.Word;
import shortcuts.Print;
import shortcuts.Scan;


public class Temp {
	final String defaultFileContainer 	= "src/documents/";	
	
	Print p 							= new Print();
	Scan s  							= new Scan();	
	String filePath 					= "";
	String fileName 					= "";
	String	nlpFilePath;
	
	// This is a ReaderWrite that uses testPathComplete as filePath (for testing only).
	ReaderWrite rw = new ReaderWrite();
	final String testPathComplete = rw.testPathComplete;
	
	public Temp()
	{
		// None yet
	} 

	public static void main(String[] args)
	{
		Adapter m = new Adapter();
		//m.Test_NLPtoJMWE();
		//m.Test_SentenceConversion("src/documents/NlpOutput.txt");

	}

	public void StartModule(String filePath)
	{
		
		ReaderWrite rw 	= new ReaderWrite(filePath); // Initiate RW and perform ReadFile() on the filePath
		Nlp nlp			= new Nlp(rw);
		Jmwe jmwe 		= new Jmwe();
		
		
	}
	
	public void NLPtoJMWE(String fileName) throws IOException
	{
		String filePath; 
		String mweResult		="";
		ReaderWrite	rw			= new ReaderWrite(defaultFileContainer + fileName); // Create a new ReaderWrite
		Nlp nlp					= new Nlp(rw); // Create an new NLP object that uses values from ReaderWrite (such as paths).
		Jmwe jmwe				= new Jmwe();
		
		//mweResult	= jmwe.ApplyMweDetector(nlp.GetWordList(), nlp.GetPosList());
		
		p.println("mweResult: ");
		p.println(mweResult);
		
	}
	
	
	/*
	 * Sends the data coming from NLP and then transfers them to JMWE 
	 */
	public void Test_NLPtoJMWE()
	{
		
		ReaderWrite rw 			= new ReaderWrite(defaultFileContainer + "SampleLegalText.txt");
		Nlp nlp					= new Nlp(rw);
		Jmwe mwe				= new Jmwe();
		String mweResult		= "";
		
		// Read the file from the specified path above. Make sure there is a path;
		rw.ReadFile();
		// Start POS Tagging by getting the file content from RW.
		nlp.StartNlp(rw.GetFileContent());
		
		//p.println("MWE Result from Bridge: ");
		//p.println(mweResult);		
		TestConnectNlpToJmwe();
	}

	
	public Boolean TestConnectNlpToJmwe()
	{
		p.println("Running TestConnectNlpToJmwe");
		ReaderWrite rw = new ReaderWrite();
		// Uses defaultFileContainer instead.
		Nlp nlp = new Nlp(rw.testPathComplete);
		p.println("defaultFileContainer has been set as nlpFilePath");
		//set nlpFilePath as the same as the defaultFileContainer
		// this.nlpFilePath = nlp.GetDefaultFilePath();
		//set filePath of nlp to defaultFileContainer
		nlp.SetFilePath(nlpFilePath);
		// set filePath of this class to defaultFilePath
		this.filePath	 = nlpFilePath;
		p.println("filePath = " + this.filePath);
		//ReaderWrite rw = new ReaderWrite(nlp.GetDefaultFilePath());
		p.println("Running TestNlp() at the Bridge.");
		// nlp.TestNlp();


		//Run Converter
		p.println("Running ConvertToWordList");
		ConvertToWordList(nlpFilePath);
		//test sentence
		Test_SentenceConversion(nlpFilePath);
		return true;
	}

	/*
	 * Gets the list of word and tags from NlpOutput txt file into an ArrayList of Word(s)
	 * */
	public ArrayList<Word> ConvertToWordList(String filePath)
	{
		ArrayList<Word> word 	=	 new ArrayList<Word>();
		Nlp nlp 				= new Nlp();
		ReaderWrite rw 			= new ReaderWrite(filePath);
		rw.ReadFile(filePath); // original nlpFilePath
		String originalText 	= rw.GetFileContent();
		String[] splittedText 	= originalText.split(" ");

		for(int i = 0; i < splittedText.length; i++) {
			
			String[] split = splittedText[i].split("/");
			
			Word temp = new Word(split[0]); 
			temp.setPartOfSpeech(split[1]); 
			temp.setLemma(split[0]);
			//temp.setStopWord(nlp.isStopWord(temp.getWord()));
			//p.println(splittedText[i]);
			word.add(temp);
		
		}

		return word;
	}

	
	
	/*
	 * Use Word() objects instead to generate content in sentence.
	 */
	public ArrayList<PreSentence> ConvertToSentenceList(String filePath)
	{
		ArrayList<PreSentence> sentence = new ArrayList<PreSentence>();
		ArrayList<Word> words = new ArrayList<Word>();
		ArrayList<Word> newWords = new ArrayList<Word>();
		Word tempWord;
		PreSentence tempSentence = new PreSentence();
		
		//Retrieve all words in the input file and create them as word objects
		words = ConvertToWordList(filePath);
		
		// Scan each Word object. This loop detects where to split the input into sentences by checking their punctuation marks. 
		for(int i = 0; i < words.size(); i++) {		
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
		
		//
		
		//not sure what this is
		
		return sentence;
	}
	
	/*
	 * This tests ConvertToSentenceList if it works positively. 
	 * Makes use of nlpFilePath.
	 */
	public ArrayList<PreSentence> Test_SentenceConversion(String nlpFilePath)
	{
		ArrayList<PreSentence> pSentence = new ArrayList<PreSentence>();
		pSentence = ConvertToSentenceList(nlpFilePath);
		p.println("SentenceConversion Test: " + pSentence.get(0).getWordList().get(0).getLemma());
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
	
	public Boolean ValidateNlpToJmwe()
	{
		return true;
	}


}

