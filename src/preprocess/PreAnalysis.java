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
		int count	= 0;
		int size 	= 0;
		
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
	
	
}





