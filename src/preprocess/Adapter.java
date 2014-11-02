/*
 * Use this class to connect Nlp to Jmwe.
 * This class also makes use of the Extractor
 * */
package preprocess;

import java.io.IOException;
import java.util.ArrayList;

import shortcuts.Print;
import shortcuts.Scan;


public class Adapter {
	final String defaultFileContainer 	= "src/documents/";	
	
	Print p 							= new Print();
	Scan s  							= new Scan();	
	String filePath 					= "";
	String fileName 					= "";
	String	nlpFilePath;
	
	// This is a ReaderWrite that uses testPathComplete as filePath (for testing only).
	ReaderWrite rw = new ReaderWrite();
	final String testPathComplete = rw.testPathComplete;
	
	public Adapter()
	{
		// None yet
	} 

	public static void main(String[] args)
	{
		Adapter m = new Adapter();
		m.Test_NLPtoJMWE();
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
		
		//p.println("MWE Result from Bridge: ");
		//p.println(mweResult);		
		TestConnectNlpToJmwe();
	}

	
	public Boolean TestConnectNlpToJmwe()
	{
		p.println("Running TestConnectNlpToJmwe");
		Extractor ex = new Extractor();
		ReaderWrite rw = new ReaderWrite();
		// Uses defaultFileContainer instead.
		Nlp nlp = new Nlp(rw.testPathComplete);
		p.println("defaultFileContainer has been set as nlpFilePath");
		//set nlpFilePath as the same as the defaultFileContainer
		this.nlpFilePath = nlp.GetDefaultFilePath();
		//set filePath of nlp to defaultFileContainer
		nlp.SetFilePath(nlpFilePath);
		// set filePath of this class to defaultFilePath
		this.filePath	 = nlpFilePath;
		p.println("filePath = " + this.filePath);
		//ReaderWrite rw = new ReaderWrite(nlp.GetDefaultFilePath());
		p.println("Running TestNlp() at the Bridge.");
		nlp.TestNlp();
		

		//Run Converter
		p.println("Running ConvertToWordList");
		ex.FileToWordList(nlpFilePath+"NlpOutput.txt");
		//test sentence
		ex.Test_FileToSentenceList(nlpFilePath+"NlpOutput.txt");
		return true;
	}

	
	
	public Boolean ValidateNlpToJmwe()
	{
		return true;
	}


}

