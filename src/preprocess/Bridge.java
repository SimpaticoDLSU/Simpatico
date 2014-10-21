/*
 * This was created in order to smoothen and ease the problem of abstracting the integration between CoreNLP and JMWE.
 * */
package preprocess;

import java.awt.List;
import java.util.ArrayList;

import shortcuts.Print;
import shortcuts.Scan;


public class Bridge {

	Print p = new Print();
	Scan s  = new Scan();
	String	nlpFilePath;	
	
	String filePath = "";
	
	public Bridge()
	{
		// None yet
	}
	
	public static void main(String[] args) 
	{
		Bridge m = new Bridge();
		m.TestConnectNlpToJmwe();
		
		
	}
	
	public Boolean TestConnectNlpToJmwe()
	{
			
		// Uses defaultFilePath instead.
		Nlp nlp = new Nlp();
		p.println("defaultFilePath has been set as nlpFilePath");
		//set nlpFilePath as the same as the defaultFilePath
		this.nlpFilePath = nlp.GetDefaultFilePath();
		//set filePath of nlp to defaultFilePath
		nlp.filePath	 = this.nlpFilePath;
		// set filePath of this class to defaultFilePath
		this.filePath	 = nlpFilePath;
		p.println("filePath = " + this.filePath);
		//ReaderWrite rw = new ReaderWrite(nlp.GetDefaultFilePath());
		p.println("Running TestNlp() at the Bridge."); 
		nlp.TestNlp();
		
		
		//Run Converter
		p.println("Running ConverttextToString");
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
			Word temp = new Word();
			temp.setWord(splittedText[i]);
			if(i+1 < splittedText.length)
				temp.setPartOfSpeech(splittedText[i+1]);
			temp.setLemma(splittedText[i]);
			//p.println(splittedText[i]);			
			word.add(temp);
			i++;
		}
		
		return word;
	}
	
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
