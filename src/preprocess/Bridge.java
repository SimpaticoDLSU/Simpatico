/*
 * This was created in order to smoothen and ease the problem of abstracting the integration between CoreNLP and JMWE.
 * */
package preprocess;

import java.io.IOException;
import java.util.ArrayList;

import shortcuts.Print;
import shortcuts.Scan;


public class Bridge {
	final String defaultFileContainer 	= "/Users/laurenztolentino/Eclipse/workspace/Simpatico/src/preprocess/";	
	
	Print p 							= new Print();
	Scan s  							= new Scan();	
	String filePath 					= "";
	String fileName 					= "";
	String	nlpFilePath;
	
	// This is a ReaderWrite that uses testPathComplete as filePath (for testing only).
	ReaderWrite rw = new ReaderWrite();
	final String testPathComplete = rw.testPathComplete;
	public Bridge()
	{
		// None yet
	}

	public static void main(String[] args)
	{
		Bridge m = new Bridge();
		m.Test_NLPtoJMWE();

	}


	public void NLPtoJMWE(String fileName) throws IOException
	{
		String filePath; 
		String mweResult		="";
		ReaderWrite	rw			= new ReaderWrite(defaultFileContainer + fileName); // Create a new ReaderWrite
		Nlp nlp					= new Nlp(rw); // Create an new NLP object that uses values from ReaderWrite (such as paths).
		Jmwe jmwe				= new Jmwe();
		
		mweResult	= jmwe.ApplyMweDetector(nlp.GetWordList(), nlp.GetPosList());
		
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
		
		try {
			mweResult = mwe.ApplyMweDetector(nlp.GetWordList(), nlp.GetPosList());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// 909 means error. Make sure to at least add 909 in cases wherein you will need this :)
			mweResult = "909";
			p.println("Error encountered in using ApplyMweDetector.");
		}
		
		p.println("MWE Result from Bridge: ");
		p.println(mweResult);		
		
	}

	public Boolean TestConnectNlpToJmwe()
	{

		ReaderWrite rw = new ReaderWrite();
		// Uses defaultFileContainer instead.
		Nlp nlp = new Nlp(rw.testPathComplete);
		p.println("defaultFileContainer has been set as nlpFilePath");
		//set nlpFilePath as the same as the defaultFileContainer
		this.nlpFilePath = nlp.GetDefaultFilePath();
		//set filePath of nlp to defaultFileContainer
		nlp.filePath	 = this.nlpFilePath;
		// set filePath of this class to defaultFilePath
		this.filePath	 = nlpFilePath;
		p.println("filePath = " + this.filePath);
		//ReaderWrite rw = new ReaderWrite(nlp.GetDefaultFilePath());
		p.println("Running TestNlp() at the Bridge.");
		nlp.TestNlp();


		//Run Converter
		p.println("Running ConvertToWordList");
		ConvertToWordList(nlpFilePath);

		return true;
	}

	/*
	 * Gets the list of word and tags from NlpOutput txt file into an ArrayList of Word(s)
	 * */
	public ArrayList<Word> ConvertToWordList(String filePath)
	{
		ArrayList<Word>  word = new ArrayList<Word>();

		p.println("Getting content from: " + filePath);
		ReaderWrite rw = new ReaderWrite(filePath);
		rw.ReadFile(nlpFilePath);
		String originalText = rw.GetFileContent();
		//p.println("oringinalText is: " + originalText);
		String[] splittedText = originalText.split("/");

		for(int i = 0; i < splittedText.length; i++) {
			Word temp = new Word(splittedText[i]);
			
			if(i+1 < splittedText.length)
				temp.setPartOfSpeech(splittedText[i+1]);
			temp.setLemma(splittedText[i]);
			//p.println(splittedText[i]);
			word.add(temp);
			i++;
		}

		return word;
	}

	/*
	 * Use Word instead to generate content in sentence.
	 * */
	public ArrayList<Sentence> ConvertToSentenceList()
	{
		ArrayList<Sentence> sentence = new ArrayList<Sentence>();
		Sentence temp = new Sentence();
		//for loop
		//iterate from text file
		sentence.add(temp);
		return sentence;
	}

	public Boolean ValidateNlpToJmwe()
	{
		return true;
	}


}
