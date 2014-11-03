package preprocess;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ling.DocumentReader;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import shortcuts.*;

public class PreAnalysis {
	
	// Just some shortcuts
	Print p = new Print();
	Scan s  = new Scan();
	
	
	public PreAnalysis()
	{
		
	}
	
	public static void Main(String[] args)
	{
		DocumentReader dr = new DocumentReader();
		
	}
	
	public void StartAnalysis(String text)
	{
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
			
		}
		
	}
	
	public void DecideNonRestrictiveRCBoundaries(ArrayList<Word> wordList, ArrayList<PreSentence> sentenceList)
	{
		
		ArrayList<Word> tWordList = wordList;
		int count	= 0;
		int size 	= 0;
		int n		= 0;
		// n be the number of commas
		int numCommas 	= getInstanceCount(",", wordList);
		
		if ( numCommas > 0 )
		{
			for ( int i = 0; i < size; i++ ) 
			{
				if ( wordList.get(i).equals(",") && i != size)
				{
					if ( wordList.get(i + 1).equals("which") || wordList.get(i + 1).equals("who") )
					{
						// perform apposition marking here
						tWordList = markAppositive(wordList); // marks the Words with the apposition attribute to true 
					}	
				}
			}
		}
		
		
		
		
	}
	
	public int getInstanceCount(String searchParameter, ArrayList<Word> wordList)
	{
		int count 	= 0;
		int size	= wordList.size();
		
		for (int i = 0; i < size; i++) 
		{
			if ( searchParameter.equals(wordList.get(i).getWord()))
			{
				count++;
			}
		}
		return count;
		
	}
	
	/*
	 * Marking of Appositives is needed for Boundary marking.
	 * Process: 
	 * 1) X was a noun phrase and surrounded by punctuation on the treebank.
	 * 2) The immediate enclosing bracket marked a noun phrase in the treebank.
	 * 3) X was the right-most phrase in the enclosing bracket 
	 */
	
	public ArrayList<Word> markAppositive(ArrayList<Word> wordList) 
	{
		int size 		= wordList.size();		
		int openComma 	= 0;
		int closeComma 	= 0;
		
		for ( int i = 0; i < size; i++ ) 
		{
			if ( wordList.get(i).equals(",") && i != size) 
			{
				openComma = i;
				for ( int j = i + 1; j < size; ) 
				{
					if ( wordList.get(j).equals(",") ) 
					{
						closeComma = j;
						for ( int k = 0; k < closeComma; k++ ) 
						{
							String temp = wordList.get(k).getPartOfSpeech();
							if ( temp.equals("NN") || temp.equals("NNS") || temp.equals("NNP") || temp.equals("WP") || temp.equals("WP$") ) 
							{
								wordList.get(k).setIsAppositive(true);
							}
						}						
						break;
					}
					else {
						j++;
					}
				}
			}
		}
		
		return wordList; 
	}
	
	
	
}





