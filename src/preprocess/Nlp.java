package preprocess;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import shortcuts.Print;
import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.dcoref.CorefCoreAnnotations.CorefChainAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.CoreMap;

public class Nlp {
	
	static Print p = new Print();
	//FilePath here includes already the file name
	final static String defaultFile    		= "src/documents/NlpOutput.txt";
	final String defaultFilePath		   	= "src/documents/";
	String fileName						   	= "";			
	private String filePath					= "";
	String filePathContainer				= "";
	String[] stopWords;
	ReaderWrite rw;
	ArrayList<String> wordList;
	ArrayList<String> posList;
	ArrayList<String> neList;	
	ArrayList<String> lemmaList;	
	
	 
	
	/*
	 * Not recommended to use.
	 * Only use when you need to call the following methods:
	 * 1) isStopWord
	 */
	public Nlp()
	{
		
	}
	
	/*
	 * Pass a reader to make life easier.
	 * Make sure you call a ReaderWrite on the method that calls this method and class.
	 */
	public Nlp(ReaderWrite reader)
	{
		this.rw = reader;
		this.setFilePath(rw.GetFilePath());		
	}
	
	/*
	 * If you have not specified a ReaderWrite(), you can specify the filePath and this constructor
	 * will create the necessary ReaderWrite for you.
	 */
	public Nlp(String filePath)
	{
		
		// Automatically set the filePath
		SetFilePath(filePath);
		rw = new ReaderWrite(filePath);
		if(filePath.equals("")) {
			p.println("The filePath you entered is blank. Will resort use defaultFilePath instead");
			filePath = defaultFilePath;
		}
	}
	
	
	public static void main(String[] args)
	{
		ReaderWrite rw = new ReaderWrite();
		
		p.println("Running Nlp.java");
		
		Nlp nlp = new Nlp(rw.testPathComplete);
		//nlp.TestNlp();
		nlp.isStopWord("couldn't");
		
	}
	
	/*
	 * Acquires text from SampleLegalText.txt
	 * Just a custom version of GetFileContent via a ReaderWrite
	 */
	public String GetSampleLegalText()
	{
		p.println("You called GetSampleLegalText");
		ReaderWrite read = new ReaderWrite(defaultFilePath + "SampleLegalText.txt");
		read.ReadFile();
		p.println("Trace: " + read.GetFilePath());
		return read.GetFileContent();
	}
	
	/*
	 * Set the filePath to be used by the entire class
	 */
	public void SetFilePath(String filePath)
	{
	
		// in case you need to update your file path.
		this.setFilePath(filePath);
	}
	
	/*
	 * Gets the filePath used by the class.
	 * Created in case you need to check.
	 */
	public String GetFilePath()
	{
		
		return this.getFilePath();
	}
	
	
	public void SetReaderWriter(ReaderWrite rw)
	{
		
		this.rw = rw;
	}
	
	
	public void SetReaderWriterFilePath (String filePathNew)
	{
		
		this.rw.SetFilePath(filePathNew);
	}
	
	
	/*
	 * This method is responsible for annotating every word in the provided text with 
	 * Part-of-Speech (POS) tags.
	 */
	public Boolean StartNlp(String text)
	{
		
		//ReaderWrite now has a defined FilePath. 		
		ArrayList<String> wordList 	= new ArrayList<String>();
		ArrayList<String> posList	= new ArrayList<String>();
		ArrayList<String> neList	= new ArrayList<String>();
		ArrayList<String> lemmaList = new ArrayList<String>();
		String temp 				= "";
		String finalOutput 			= "";
		
		/* Creates a StanfordCoreNLP Object, with POS Tagging, Lemmatization, NER, Parsing, and Corerefernce resolution*/
		Properties props = new Properties();
		props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		
		// Creates an empty Annotation just with the given text
		Annotation document = new Annotation(text);
		
		// run all Annotators on this text
		pipeline.annotate(document);
		
		// these are all the sentences in this document
		// a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
		List<CoreMap> sentences = document.get(SentencesAnnotation.class);
		
		for(CoreMap sentence: sentences)
		{
			//traversing the words in the current sentence
			// a CoreLabel is a CoreMap with additional token-specific methods
			
			for(CoreLabel token: sentence.get(TokensAnnotation.class))
			{
				// this is the text of the token
				String word	= token.get(TextAnnotation.class);
				//p.println("word: " + word);
				// this is the POS tag of the token
				String pos	= token.get(PartOfSpeechAnnotation.class);
				//p.println("pos: " + pos);
				// this is the NER label of the token
				String ne 	= token.get(NamedEntityTagAnnotation.class);
				//p.println("ne: " + ne);
				
				//this is the lemma of the token
				String lemma = token.get(LemmaAnnotation.class);
				
				// Add them to class variables.
				wordList.add(word);
				posList.add(pos);
				neList.add(ne);
				lemmaList.add(lemma);
				
				temp = word + "/" + pos + "/" + lemma;
				finalOutput = finalOutput + temp;
				finalOutput = finalOutput + "\n";
				
			}
			
			p.println("finalOutput: \n " + finalOutput);
			
			// Update the class ArrayList variables
			this.wordList 	= wordList;
			this.posList 	= posList;
			this.neList		= neList;
			this.lemmaList = lemmaList;
			
			// Create NlpOutput.txt with the content in this format: <word>/<pos>
			rw.SetFileName("NlpOutput.txt");
			rw.SetFilePath(defaultFile); //redundant creation
			rw.CreateFile(finalOutput, defaultFile);
			
			// this is the parse tree. And I have no idea what it's for
			Tree tree = sentence.get(TreeAnnotation.class);
			
			// this is the Stanford dependency graph of the current sentence
			SemanticGraph dependencies = sentence.get(CollapsedCCProcessedDependenciesAnnotation.class);
			// Also I have no idea what the one above is for
			
		}
		
		/*
		 * This is the coreference link graph
		 * Each chain stores a set of mentions that link to each other
		 * along with a method for getting the most representative mention
		 * Both sentence and token offsets start at 1!	
		 */
		Map<Integer, CorefChain> graph = document.get(CorefChainAnnotation.class); //I have no idea what this actually does
		/*
		CorefChain test = graph.get(1);
		p.println("document: " + document.toString());
		p.println("Sample run: " + test.toString());
		p.println("end of run");
		*/
		return true;
	}
	
	
	public ArrayList<String> GetWordList()
	{
		return this.wordList;
	}
	
	public ArrayList<String> GetPosList()
	{
		return this.posList;
	}
	
	public ArrayList<String> GetNeList()
	{
		return this.neList;
	}
	
	public String GetDefaultFilePath()
	{
		return this.defaultFilePath;
	}
	
	public Boolean TestNlp()
	{
		ReaderWrite rw = new ReaderWrite();	
		this.setFilePath(defaultFilePath);
		//rw.ReadFile(defaultFile);
		//rw.SetFilePath(this.filePath);
		return StartNlp(GetSampleLegalText());
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	public void LoadStopWordList()
	{
		p.println("Loading Stop Words list from stopwords.txt");
		
		String stopWordsPath 	= "src/documents/stopwords.txt";
		ReaderWrite rw			= new ReaderWrite(stopWordsPath);	
		String[] stopWords;
		String temp;
		
		rw.ReadFile();
		temp 			= rw.GetFileContent();
		stopWords 		= temp.split(" ");
		this.stopWords 	= stopWords;
	}
	
	public Boolean isStopWord(String word)
	{
		String[] stopWords = this.stopWords;
		
		
		for(String sw : stopWords)
		{
			if(word.equals(sw))
			{
				return true;
			}
		}
		
		return false;
	}
	
}
