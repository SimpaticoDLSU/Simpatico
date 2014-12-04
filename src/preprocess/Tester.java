package preprocess;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;
import language.PreSentence;
import lexical.LexSubmodules;
import shortcuts.*;

/**
 * Run tests here instead of dumping them in main functioning classes.
 * @author laurenztolentino
 *
 */
public class Tester {
	
	public final static String defaultFile    		= "src/documents/NlpOutput.txt";
	public final static String defaultFilePath		= "src/documents/";
	public static Print p 							= new Print();
	
	public Tester() {}
	
	public static void main(String[] args)
	{
		Tester test = new Tester();
		try {
			test.testLexSub();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//test.sentenceIdentifier();
		//Nlp nlp = new Nlp();
		//nlp.StartNlp(GetSampleLegalText());
	}
	
	/**
	 * Not working.
	 * ConstantsAndVariables is not available in Stanford CoreNLP (missing files).
	 */
	public void CommonWordImplementor()
	{
		ArrayList<String> example 	= new ArrayList<String>();
		ArrayList<String> commonEng	= new ArrayList<String>();
		PreAnalysis pa				= new PreAnalysis();					
		
		example.add("The");
		example.add("Agency");
		example.add("was");
		example.add("there");
		commonEng = pa.getCommonWords();
		
		for ( String word : example) 
		{
			for( String check : commonEng )
			{
				if( word.equals(check)) {
					p.println("Match found for: ");
					p.println("" + word + " AND " + check);
				}
			}
		}
		
	}
	
	public void sentenceIdentifier()
	{
		Object[] temp;
		Nlp nlp = new Nlp();
		ArrayList<Tree> tree = nlp.convertToSentenceTrees(getSampleLegalText());
		temp = tree.toArray();
		p.println(tree.toString());
		
		p.println("printing single part: ");
		p.println("" + tree.get(0).toString());
		
	}
	
	public void TestNlp(Nlp nlp)
	{
		
		ReaderWrite rw = new ReaderWrite();	
		nlp.setFilePath(defaultFilePath);
		//rw.ReadFile(defaultFile);
		//rw.SetFilePath(this.filePath);
		//return StartNlp(GetSampleLegalText());
		// AcquireTree(GetSampleLegalText());
	}
	
	public Boolean TestNlpFileGenerate(Nlp nlp)
	{
		ReaderWrite rw = new ReaderWrite();	
		nlp.setFilePath(defaultFilePath);
		//rw.ReadFile(defaultFile);
		//rw.SetFilePath(this.filePath);
		// return StartNlp(GetSampleLegalText());
		return false;
	}
	

	
	
	/**
	 * Acquires text from SampleLegalText.txt
	 * Just a custom version of GetFileContent via a ReaderWrite
	 */
	public static String GetSampleLegalText()
	{		
		p.println("You called GetSampleLegalText");
		ReaderWrite read = new ReaderWrite(defaultFilePath + "SampleLegalText.txt");
		read.ReadFile();
		p.println("Trace: " + read.GetFilePath());
		return read.GetFileContent().toString();
	}
	
	public static String getSampleLegalText()
	{
		return GetSampleLegalText();
	}
	
	public void testLexSub() throws Exception
	{
		ArrayList<Tree> tree 		= new ArrayList<Tree>();
		ArrayList<PreSentence> ps 	= new ArrayList<PreSentence>(); 
		LexSubmodules ls 			= new LexSubmodules();
		List<CoreMap> coreMap;
		Nlp nlp 					= new Nlp();
		
		coreMap = nlp.generateSentenceCoreMapList(getSampleLegalText());
		ps = nlp.generatePreSentences(coreMap);
		ls.candidateSelection(ps);
		//ls.generateSynId(ps);
	}
	
	/*
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
	
	
	public void Test_NLPtoJMWE()
	{
		Extractor ex			= new Extractor();
		ReaderWrite rw 			= new ReaderWrite(defaultFileContainer + "SampleLegalText.txt");
		Nlp nlp					= new Nlp(rw);
		Jmwe mwe				= new Jmwe();
		ArrayList<PreSentence> mweResult;
		
		// Read the file from the specified path above. Make sure there is a path;
		rw.ReadFile();
		// Start POS Tagging by getting the file content from RW.
		nlp.StartNlp(rw.GetFileContent());
		
		try {
			mweResult = mwe.ApplyMweDetector(ex.FileToSentenceList("src/documents/NlpOutput.txt"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			
			p.println("Error encountered in using ApplyMweDetector.");
		}
		
		//p.println("MWE Result from Bridge: ");
		//p.println(mweResult);		
		TestConnectNlpToJmwe();
	}

	*/
	
}
