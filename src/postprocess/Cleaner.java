package postprocess;

import java.util.ArrayList;
import java.util.List;

import edu.stanford.nlp.util.CoreMap;
import language.*;
import preprocess.*;

public class Cleaner { 
	
	/**
	 * Post Processing includes: 
	 * - Removing duplicate words
	 */
	public Cleaner()
	{
		
	}
	
	/**
	 * For mode selection
	 * @param mode
	 * "test" to run test case
	 */
	public Cleaner(String mode)
	{
		Cleaner c = new Cleaner();
		
		if ( mode.equalsIgnoreCase("test") == true) {
			c.testMultipleSentences();
		}
	}
		
	public static void main(String[] args)
	{
		Cleaner c = new Cleaner("test");
		//c.testMultipleSentences();
	}
	
	
	private void testSingleSentence() 
	{
		Nlp nlp = new Nlp();
		List<CoreMap> coreMaps;
		ArrayList<PreSentence> sentences;
		ArrayList<PreSentence> outputSentences = new ArrayList<PreSentence>();
		String problemSample 	= "integrated, subsumed, integrated, and included.";
		coreMaps 				= nlp.generateSentenceCoreMapList(problemSample);
		sentences 				= nlp.generatePreSentences(coreMaps);
		
		for( int i = 0; i < sentences.size(); i++ ) 
		{
			//checkDuplicates(sentences.get(i));
			outputSentences.add( checkEnumerationDuplicates( sentences.get(i) ) );
			System.out.println("Testing output of CheckDuplicates()");
			for( int k = 0; k < outputSentences.size(); k++ ) {
				ArrayList<Word> temp = outputSentences.get(k).getWordList();
				for( int m = 0; m < temp.size(); m++ ) {
					System.out.print( temp.get(m).getWord() + " " );
				}
			}
		}
		
	}
	
	private void testMultipleSentences()
	{
		Nlp nlp = new Nlp();
		List<CoreMap> coreMaps;
		ArrayList<PreSentence> sentences; // PreProcessed PreSentences
		ArrayList<PreSentence> outputSentences = new ArrayList<PreSentence>();
		String problemSample 	= "The Department shall classify and/or re-classify all existing allowances, "
				+ "incentives and other benefits  currently being received by all government employees, "
				+ "including incumbents, into Base Pay,  Allowances and Other Pay, pursuant to the Total Compensation Framework, "
				+ "and pursuant to the true nature of such, notwithstanding its existing nomenclature. "
				+ "Except for those in Section 18 and 21 hereof, all other allowances, incentives, and benefits, "
				+ "being enjoyed by incumbents prior to the effectivity of this Act, "
				+ "which shall not be continued to be given as a separate amount, including ad hoc, provisional, "
				+ "tentative, or improvised benefits being received by government employees which are really intended to "
				+ "provide some form of economic assistance, in acknowledgement of the inadequacy of compensation in government, "
				+ "shall be deemed integrated, subsumed, incorporated, and included  in the Base Pay Schedule as herein provided, "
				+ "without need of further adjustment of the amounts.";
		coreMaps 				= nlp.generateSentenceCoreMapList(problemSample);
		sentences 				= nlp.generatePreSentences(coreMaps);
		
		outputSentences = cleanSentences(sentences);
		
		for( int k = 0; k < outputSentences.size(); k++ ) {
			ArrayList<Word> temp = outputSentences.get(k).getWordList();
			for( int m = 0; m < temp.size(); m++ ) {
				System.out.print( temp.get(m).getWord() + " " );
			}
		}
		
	}
	
	/**
	 * Removes duplicate words from enumeration phrases.
	 * @param sentences
	 * ArrayList of PreSentence(s) that would possibly need cleaning
	 * @return
	 * ArrayList of PreSentence(s) that have been run through checkers and cleaning methods.
	 */
	public ArrayList<PreSentence> cleanSentences(ArrayList<PreSentence> sentences)
	{
		ArrayList<PreSentence> resultSentences = new ArrayList<PreSentence>();
		
		for ( int i = 0; i < sentences.size(); i++ ) 
		{
			resultSentences.add( checkEnumerationDuplicates( sentences.get(i) ) );			
						
		}
		
		if ( resultSentences.size() == sentences.size() ) 
		{
			return resultSentences;
		}
		else  {
			System.out.println("Error at cleanSentences(). The number of output sentences does not match "
					+ "the original sentence count");
			return sentences;
		}
	}
	
	public PreSentence checkEnumerationDuplicates(PreSentence original)
	{
		PreSentence tempSentence = original;
		PreSentence resSentence  = new PreSentence();
		ArrayList<Word> tempWord = tempSentence.getWordList();
		ArrayList<Word> duplWord = tempWord;
		ArrayList<Word> resWord	 = new ArrayList<Word>();
		Word pairWord = new Word();
				
		System.out.println("Running checkDuplicates()");
		
		for ( int i = 0; i < tempWord.size(); i++ ) 
		{				
			for (int j = i + 1; j < tempWord.size(); j++ ) {
				if(tempWord.get(i++).getWord().equals(",") == true && tempWord.get(i--).getWord().equals(","))
				{
					if( tempWord.get(i).getWord().equals( tempWord.get(j).getWord() ) && tempWord.get(i).getWord().equals(",") == false ) 
					{
						System.out.println("Match found for " + tempWord.get(j).getWord() );
						tempWord.remove(j);
						if( tempWord.get(j).getWord().equals(",") == true ) 
						{
							tempWord.remove(j++);
						}
					}
				}
				
			}
			
			// System.out.println( "Test" );
			//System.out.print(tempWord.get(i).getWord() + " ");
		}
		
		resSentence = original;
		resSentence.setWordList(tempWord);
		
		return resSentence;
	}
}
