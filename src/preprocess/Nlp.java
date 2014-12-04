package preprocess;

import java.util.ArrayList;

import java.util.Iterator;
import java.util.LinkedList;


import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import language.PreSentence;
import language.Text;
import language.Word;
import shortcuts.Print;
import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.dcoref.CorefCoreAnnotations.CorefChainAnnotation;
import edu.stanford.nlp.ling.CoreAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.ChunkAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.CommonWordsAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.WordSenseAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.patterns.surface.ConstantsAndVariables;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.ChunkAnnotationUtils;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.BasicDependenciesAnnotation;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedDependenciesAnnotation;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;
import edu.stanford.nlp.trees.GrammaticalRelation;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;

import edu.stanford.nlp.util.*;
import edu.stanford.nlp.time.SUTime;
import edu.stanford.nlp.time.SUTimeMain;

import edu.stanford.nlp.util.CoreMap;


/**
 * 
 * @author laurenztolentino
 *	NLP Class tags Part of Speech and possible xml file creation during running :(
 */
public class Nlp {
	
	static Print p = new Print();
	//FilePath here includes already the file name
	
	private final String generalProperties 	= "tokenize, ssplit, pos, lemma, ner, parse, dcoref";
	private final String defaultFile		= "src/documents/NlpOutput.txt";
	String fileName						   	= "";			
	private String filePath					= "";
	String filePathContainer				= "";
	String[] stopWords;
	Boolean classError = false;
	private ReaderWrite rw;
	private ArrayList<String> wordList			= new ArrayList<String>();
	private ArrayList<String> posList			= new ArrayList<String>();	
	private ArrayList<String> neList			= new ArrayList<String>();	
	private ArrayList<String> lemmaList 		= new ArrayList<String>();	
	private ArrayList<Tree> sentenceTreeList	= new ArrayList<Tree>();
	
	Text text;
	 
	/**
	 * Not recommended to use.
	 * Only use when you need to call the following methods:
	 * 1) isStopWord -- has deprecated 
	 */
	public Nlp()
	{
		
	}
	
	/**
	 * Pass a reader to make life easier.
	 * Make sure you call a ReaderWrite on the method that calls this method and class.
	 */
	public Nlp(ReaderWrite reader)
	{
		this.rw = reader;
		this.setFilePath(rw.GetFilePath());		
	}
	
	/**
	 * If you have not specified a ReaderWrite(), you can specify the filePath and this constructor
	 * will create the necessary ReaderWrite for you.
	 * If filePath is blank, constructor makes use of the defaultFilePath at src/documents
	 */
	public Nlp(String filePath)
	{
		
		// Automatically set the filePath
		SetFilePath(filePath);
		rw = new ReaderWrite(filePath);
		if(filePath.equals("")) {
			p.println("The filePath you entered is blank. Will resort use defaultFilePath instead");
			filePath = Tester.defaultFilePath;
		}
	}
	
	/**
	 * Made only for testing.
	 * @param args
	 */
	public static void main(String[] args)
	{
		p.println("Running Nlp.java");
		
	}
	
	public Boolean AcquireAnnotatedText()
	{
		Boolean result = false;
		
		
		return result;
	}
	
	/**
	 * Start annotating the document
	 * @param text 
	 * Text to be processed and annotated
	 * @return
	 * Status if it ran properly or not  
	 */
	public Boolean StartNlp(String text)
	{
		Boolean boolTest = false;
		
		//convertToSentenceTrees(text);
		//iterateToText(generateSentenceCoreMapList(text));
		
		iterateToTextFile(generateSentenceCoreMapList(text));
		boolTest = this.classError;
		return boolTest;
	}
	
	public Boolean StartNlpNoFile(String text)
	{
		Boolean boolTest = false;
		//convertToSentenceTrees(text);
		//iterateToText(generateSentenceCoreMapList(text));
		
		return boolTest;
	}
	
	/*
	Just call this method in StartNLP()
	*/
	public ArrayList<Tree> convertToSentenceTrees(String text)
	{
		ArrayList<Tree> sentenceTree = new ArrayList<Tree>();
		List<CoreMap> sentences;
	
		sentences 		= generateSentenceCoreMapList(text);
		sentenceTree 	= iterateToTrees(sentences);
	
		return sentenceTree;
	}
	
	public List<CoreMap> generateSentenceCoreMapList(String text)
	{
		List<CoreMap> sentences;
		/* Creates a StanfordCoreNLP object with POS, Lemma, etc. */
		Properties props = new Properties();
		props.put("annotators", generalProperties);
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		/* Creates an empty Annotation with just the string of the entire text */
		Annotation document = new Annotation(text);
		pipeline.annotate(document);
		sentences = document.get(SentencesAnnotation.class);
		return sentences;
	}
	
	public ArrayList<Tree> iterateToTrees(List<CoreMap> sentences)
	{
		ArrayList<Tree> sentenceTree = new ArrayList<Tree>();
	
		for( CoreMap sentence: sentences) 
		{
			Tree tree = sentence.get(TreeAnnotation.class);
			sentenceTree.add(tree);
		}
	
		return sentenceTree;
	}
	
	public ArrayList<PreSentence> generatePreSentences(List<CoreMap> sentences)
	{
		ArrayList<PreSentence> sentenceList = new ArrayList<PreSentence>();
		ArrayList<Word> words = new ArrayList<Word>();
		
		for ( CoreMap sentence : sentences )
		{
			
			PreSentence pSentence = new PreSentence();
			
			
			for ( CoreLabel token : sentence.get(TokensAnnotation.class) )
			{
				Word preWord		  = new Word();
				// Get token annotations
				String word 	= token.get(TextAnnotation.class);
				String pos 		= token.get(PartOfSpeechAnnotation.class);
				String ne 		= token.get(NamedEntityTagAnnotation.class);
				String lemma 	= token.get(LemmaAnnotation.class);
				String common	= token.get(CommonWordsAnnotation.class);
				//p.println("word: " + word);
				// add tokens to Word()   
				preWord.setWord(word);
				preWord.setPartOfSpeech(pos);
				preWord.setLemma(lemma);
				// add the Word to WordList
				
				words.add(preWord);
				// p.println("preword: " + preWord.getWord());
				// p.println("words: " + words.get(0).getWord());
			}
			
			p.println("wordsSize: " + words.size());
			
			pSentence.setWordList(words);
			sentenceList.add(pSentence);
		}
		
		
		return sentenceList;
	}
	
	public void iterateToTextFile( List<CoreMap> sentences )
	{
		// Variables for the Text object 
		Text documentText 				= new Text();
		ArrayList<Tree> treeList		= new ArrayList<Tree>();
		ArrayList<PreSentence> pSentList= new ArrayList<PreSentence>();
		ArrayList<Word> pWordList		= new ArrayList<Word>();
		// Variables for each token
		ArrayList<String> wordList		= new ArrayList<String>();
		ArrayList<String> posList		= new ArrayList<String>();
		ArrayList<String> neList		= new ArrayList<String>();
		ArrayList<String> lemmaList		= new ArrayList<String>();
		
		// To text file variables
		String temp 				= "";
		String finalOutput 			= "";

		for ( CoreMap sentence : sentences )
		{
			int sentenceID			= 0;
			Tree tree 				= sentence.get(TreeAnnotation.class);
			Word preWord 			= new Word();
			PreSentence pSentence 	= new PreSentence();
			ArrayList<Word> allWord = new ArrayList<Word>();

			for( CoreLabel token : sentence.get(TokensAnnotation.class) )
			{
				// Get token annotations
				String word 	= token.get(TextAnnotation.class);
				String pos 		= token.get(PartOfSpeechAnnotation.class);
				String ne 		= token.get(NamedEntityTagAnnotation.class);
				String lemma 	= token.get(LemmaAnnotation.class);
				String common	= token.get(CommonWordsAnnotation.class);
				// add each token to token lists
				p.println( word + " : " + common );
				wordList.add(word);
				posList.add(pos);
				neList.add(ne);
				lemmaList.add(lemma);		
				// add tokens to Word()   
				preWord.setWord(word);
				preWord.setPartOfSpeech(pos);
				preWord.setLemma(lemma);		
				// add the Word to WordList
				pWordList.add(preWord);

				/* Lines below is for printing it in a text file in the "<word>/<pos>/<lemma>" format. */
				temp = word + "/" + pos + "/" + lemma;
				//System.out.println("NER: "+ ne);
				finalOutput = finalOutput + temp;
				finalOutput = finalOutput + "\n";
			
				tree.printLocalTree();
			} // end for tokens
			
			treeList.add(tree);
			// Add to PreSentence
			/*
			pSentence.setId(sentenceID);
			pSentence.setWordList(pWordList);
			pSentence.setSentenceTree(tree);
			pSentence.setOpeningBoundary();
			pSentence.setClosingBoundary();
			pSentence.setNounPhrase();
			pSentence.setVerbPhrase();
			// Add to documentText
			documentText.setSentenceTrees(treeList);
			documentText.setPreSentences();
			documentText.setPhrases();
			documentText.setWords();
			 */
			// For transferring results to the tree
			p.println("finalOutput: \n " + finalOutput);
			
			// Update the class ArrayList variables
			this.wordList 	= wordList;
			this.posList 	= posList;
			this.neList		= neList;
			this.lemmaList = lemmaList;
			
			// Create NlpOutput.txt with the content in this format: <word>/<pos>/<lemma>
			rw.SetFileName("NlpOutput.txt");
			rw.SetFilePath(defaultFile); //redundant creation
			rw.CreateFile(finalOutput, defaultFile);
			
			// this is the parse tree. And I have no idea what it's for
			//Tree tree = sentence.get(TreeAnnotation.class);
			
			// this is the Stanford dependency graph of the current sentence
			SemanticGraph dependencies = sentence.get(CollapsedCCProcessedDependenciesAnnotation.class);
			// Also I have no idea what the one above is for
			
			
			// THIS PRINTS OUT THE MOTHA-F****** ROOT TREEEEEE #GROOT #ROOT #GOOT #OYEA #SOMUCHTIMEWASTEDTOGETTHISOUTPUT
			p.println("tree: " + tree.toString());
			
			
			ReaderWrite rootFile = new ReaderWrite();
			rootFile.SetFilePath("src/documents/root.txt");
			rootFile.AddNewWriteLine(tree.toString());

		} // end for sentences
	} // end method
	
	public void iterateToText( List<CoreMap> sentences )
	{
		// Variables for the Text object 
		Text documentText 				= new Text();
		ArrayList<Tree> treeList		= new ArrayList<Tree>();
		ArrayList<PreSentence> pSentList= new ArrayList<PreSentence>();
		ArrayList<Word> pWordList		= new ArrayList<Word>();
		// Variables for each token
		ArrayList<String> wordList		= new ArrayList<String>();
		ArrayList<String> posList		= new ArrayList<String>();
		ArrayList<String> neList		= new ArrayList<String>();
		ArrayList<String> lemmaList		= new ArrayList<String>();
		
		for ( CoreMap sentence : sentences )
		{
			int sentenceID			= 0;
			Tree tree 				= sentence.get(TreeAnnotation.class);
			Word preWord 			= new Word();
			PreSentence pSentence 	= new PreSentence();
			ArrayList<Word> allWord = new ArrayList<Word>();

			for( CoreLabel token : sentence.get(TokensAnnotation.class) )
			{
				// Get token annotations
				String word 	= token.get(TextAnnotation.class);
				String pos 		= token.get(PartOfSpeechAnnotation.class);
				String ne 		= token.get(NamedEntityTagAnnotation.class);
				String lemma 	= token.get(LemmaAnnotation.class);
				String common	= token.get(CommonWordsAnnotation.class);
				String ws		= token.get(WordSenseAnnotation.class);
				p.println("ws: " + ws);
				// add each token to token lists
				p.println( word + " : " + common );
				/*
				wordList.add(word);
				posList.add(pos);
				neList.add(ne);
				lemmaList.add(lemma);		
				// add tokens to Word()   
				preWord.setWord(word);
				preWord.setPartOfSpeech(pos);
				preWord.setLemma(lemma);		
				// add the Word to WordList
				pWordList.add(preWord); 
				*/
			} // end for tokens
			/*
			treeList.add(tree);
			// Add to PreSentence
			pSentence.setId(sentenceID);
			pSentence.setWordList(pWordList);
			pSentence.setSentenceTree(tree);
			pSentence.setOpeningBoundary(null);
			pSentence.setClosingBoundary(null);
			pSentence.setNounPhrase(null);
			pSentence.setVerbPhrase(null);
			// Add to documentText
			documentText.setSentenceTrees(treeList);
			documentText.setPreSentences(null);
			documentText.setPhrases(null);
			documentText.setWords(null);
			*/
		} // end for sentences
	} // end method
	
	/**
	 * Generates a tree-like structure and markup provided by Stanford CoreNLP only.
	 * Format is (ROOT (S ( ... ) ) )
	 * @param text
	 * 			An input text that needs the application of POS Tagging
	 * @return an ArrayList<String> that consists of strings of sentences that has been given a parsed tree notation by CoreNLP.
	 */
	public ArrayList<Tree> AcquireTree(String text)
	{				
		ArrayList<Tree> sentenceTree = new ArrayList<Tree>(); 
		
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
		
		
		//Creating a chunk out of nothing
		String chunk = document.get(ChunkAnnotation.class);
		
		
		
		//iterateSentences(sentences, sentenceTree);
		
		//Creates a txt file that contains the root
		p.println("Attempting to create tree.txt has been disabled.");
		
		// ReaderWrite treeFile = new ReaderWrite();
		// treeFile.SetFilePath("src/documents/tree.txt");
		// treeFile.CreateFile(sentenceTree.toString()); 
		
		return sentenceTree;
		
	}
	
	
	
	public void LaunchNLP()
	{
		//structure
		/*
		 * 
		 */
	}
	
	
	
	
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	public String[] LoadStopWordList()
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
		return this.stopWords;
	}
	
	/**
	 * DEPRECATED.
	 * Very slow when you run it.
	 * There is a stopWord identifier in Extractor.java or in PreAnalysis 
	 * @param wordList
	 * @return
	 */
	public Boolean isStopWord(ArrayList<Word> wordList)
	{
		ConstantsAndVariables cav 	= new ConstantsAndVariables();
		Set<String> stopWordsSet	= cav.getStopWords();
		Object[] stopWordsObject	= stopWordsSet.toArray();
		ArrayList<String> stopWords = objectArrayToArrayList(stopWordsObject);		
		
		
		return false;
	}
	
	public ArrayList<String> objectArrayToArrayList( Object[] array)
	{
		ArrayList<String> output = new ArrayList<String>();
		
		for ( Object o : array )
		{
			output.add(o.toString());
		}
		
		return output;
	}

	/**
	 * ArrayList of Words()
	 * @return an array list of the Word() class
	 */
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
	

	/**
	 * Set the filePath to be used by the entire class
	 */
	public void SetFilePath(String filePath)
	{
	
		// in case you need to update your file path.
		this.setFilePath(filePath);
	}
	
	/**
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
	
	/**
	 * Sets the file path to be used by the ReaderWriter which Nlp calls just in case.
	 * I prefer if you just send this class a new ReaderWrite with a filePath already set.
	 * @param filePathNew
	 */
	public void SetReaderWriterFilePath (String filePathNew)
	{
		
		this.rw.SetFilePath(filePathNew);
	}
	
	

	public ReaderWrite getRw() {
		return rw;
	}

	
	
	
		
	
	
}

