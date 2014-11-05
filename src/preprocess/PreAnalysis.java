/*
 * Perform all custom marking such as Apposition marking on the text before proceeding.
 */

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
	
	// Global variables
	String xmlContent = "";
	
	public PreAnalysis()
	{
		
	}
	
	public static void main(String[] args)
	{
		//DocumentReader dr = new DocumentReader();
		PreAnalysis pa = new PreAnalysis();
		pa.samplePutSequence();
	}
	
	public void StartAnalysis(ArrayList<Word> wordList)
	{
		ArrayList<Word> wordListNew	= new ArrayList<Word>();
		
		/* Start applying markers */
		
		/*  */
		
	}
	
	
	/**
	 * Declares the boundaries of the text.
	 * Unfinished and probably will be deprecated once a new version is put out.
	 * @param wordList
	 * @param sentenceList
	 */
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
						// make sure that all appositives have been marked before proceeding here
						
						// 
					}	
				}
			}
		}
		
		
		
		
	}
	
	
	/**
	 * Counts the number of instances that a certain string appeared on the ArrayList of Word()s
	 * @param searchParameter
	 * 		Is the string that will be used to search and count the number of instances
	 * @param wordList
	 * 		This should be coming from an already POS-tagged word list fron NLP.startNLP()
	 * @return
	 * 	returns an integer whose value is the number of instances searchParameter appeared in thw wordList
	 */
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
	
	public void samplePutSequence()
	{
		String noun = "I";
		String verb	= "ran.";
		
		putSentence(putNounPhrase(noun) + putVerbPhrase(verb));
		p.println(putSentence(putNounPhrase(noun) + putVerbPhrase(verb)));
		
	}
	
	/**
	 * Put a sentence markup inside the original XML file.
	 * Automatically generates <sentence> & </sentence> mark-ups when writing.
	 * @param text
	 * 	will be put between the markup tags
	 */
	public String putSentence(String text)
	{
		String sStart 	= "<sentence>";
		String sEnd 	= "</sentence>";
		
		xmlContent		= sStart + text + sEnd;
		return sStart + text + sEnd;
	}
	
	/**
	 * Put a Noun Phrase markup inside the original XML file.
	 * Automatically generates <nounphrase> & </nounphrase> mark-ups when writing.
	 * @param text
	 * 	goes between the mark-up tags
	 * @author Laurenz Tolentino
	 */
	public String putNounPhrase(String text)
	{
		String npStart	= "<nounphrase>";
		String npEnd	= "</nounphrase>";
		
		xmlContent		= npStart + text + npEnd;
		return npStart + text + npEnd;
	}
	
	/**
	 * Put a Verb Phrase mark-up inside the original XML file.
	 * Automatically generates <verbphrase> & </verbphrase> mark-ups when writing.
	 * @param text
	 * 	goes between the mark-up tags
	 * @author Laurenz Tolentino
	 */
	public String putVerbPhrase(String text)
	{
		String vpStart	= "<verbphrase>";
		String vpEnd	= "</verbphrase>";
		
		xmlContent		= vpStart + text + vpEnd;
		return vpStart + text + vpEnd;
	}
	
	
}

/*
 * I was walking across the street
	
	<sentence>
		
		<nounphrase>
			<noun>
				I
			<noun>		
		</nounphrase>
		
		<verbphrase>
			<verb>
				was 
			</verb>
			<verb>
				walking 
			</verb>
			<preposition>
				across
			</prepositon>
		</verbphrase>
		
		<nounphrase>
		
			<determiner>
				the 
			</determiner>
			
			<noun>
				street
			</noun>
				
		</nounphrase>
		
	</sentence>
 */




