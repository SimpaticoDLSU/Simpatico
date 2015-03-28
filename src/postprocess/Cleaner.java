package postprocess;

import java.util.ArrayList;
import java.util.List;

import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
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
		String problemSample 	= "integrated, subsumed, included, integrated, and included.";
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
		String problemSample 		= "The department shall classify and/or re-classify all existing allowances, bonuses and other benefits currently being received by all government employees, including incumbents, into Base Pay, Allowances, and Other Pay, pursuant to the Total Compensation Framework, and pursuant to the true nature of such, notwithstanding its existing language. Except for those Section 18 and 21 hereof, all other allowances, incentives, and benefits being received by government employees which are really intended to provide some form of economic assistance, in acknowledgement of the want of compensation in government, shall be taken for integrated, subsumed, integrated, and included in the Base Pay Schedule as herein provided, without need of further fitting of the amounts.";
		String problemSampleShort 	= "The persons were integrated, subsumed, included, integrated, and included.";
		coreMaps 					= nlp.generateSentenceCoreMapList(problemSampleShort);
		sentences 					= nlp.generatePreSentences(coreMaps);
		
		//checkTree(coreMaps);
		
		// ArrayList<Tree> trees = new ArrayList<Tree>();
		// trees = checkTree(coreMaps);
		// traverseTree(trees.get(0));
		// dependencyView(coreMaps);
		
		outputSentences = cleanSentences(sentences);
		
		/*for( int k = 0; k < outputSentences.size(); k++ ) 
		  {
			ArrayList<Word> temp = outputSentences.get(k).getWordList();
			for( int m = 0; m < temp.size(); m++ ) {
				System.out.print( temp.get(m).getWord() + " " );
			}
		}*/
		
	}
	
	public void dependencyView(List<CoreMap> sentences)
	{
		// SemanticGraph dependencies = sentence.get(CollapsedCCProcessedDependenciesAnnotation.class);
		// dependencies.getNodeByIndex(0).toString(); // prints that pair thingy
		for(CoreMap sentence : sentences)
		{
			SemanticGraph dependencies = sentence.get(CollapsedCCProcessedDependenciesAnnotation.class);
			dependencies.prettyPrint();
			IndexedWord iword = new IndexedWord();
			String testString = dependencies.toList(); // prints a tree like structure
			System.out.println("testString: " + testString);
			System.out.println("indexValue" +dependencies.getNodeByIndex(0).value());
			dependencies.getNodeByIndex(0).toString(); // prints that pair thingy
			// finish the nodebyindex identifiieeeeeeeehr			
			
		}
	}
	
	public void traverseTree(Tree tree) {
        Tree[] children = tree.children();

        for (Tree child : children) {
            //traverseTree(child);
            System.out.println("Depth:" + child.depth());
            System.out.println("Value:" + child.label());

            System.out.println("Children No.: " + child.numChildren());
            System.out.println("Children List: " + child.getChildrenAsList());
            System.out.println("LEAVES: " + child.getLeaves());
            traverseTree(child);

        }

        //if(head.depth(tree) == [val]) { add }
    }
	
	// Test tree traversal
	public ArrayList<Tree> checkTree(List<CoreMap> sentences)
	{
		Nlp nlp 						= new Nlp();
		ArrayList<Tree> treeList		= new ArrayList<Tree>();
		ArrayList<PreSentence> pSentList= new ArrayList<PreSentence>();
		
		System.out.println("Running checkTree()");
		
		for( CoreMap sentence : sentences )
		{
			Tree tree = sentence.get(TreeAnnotation.class);
			Tree[] tree1 = tree.children();
			System.out.println("TreeArray");
			System.out.println("Tree depth: " + tree.depth());
			for(int i = 0; i < tree1.length; i++) 
			{
				System.out.println("tree1 length = " + tree1.length);
				System.out.println("tree1[" + i +"] = " + tree1[i]);
				Tree[] tree2 = tree1[i].children();
				for(int j = 0; j < tree2.length; j++)
				{
					System.out.println("tree2[" + j +"] = " + tree2[i]);
				}
			}
			
			tree.indentedListPrint();			
			treeList.add(tree);			
		}
		return treeList;
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
		PreSentence tempSentence 	= original;
		PreSentence resSentence  	= new PreSentence();
		ArrayList<Word> tempWord 	= tempSentence.getWordList();		
		ArrayList<Word> checkWords 	= new ArrayList<Word>();				
		Word pairWord 				= new Word();
		
		ArrayList<PreSentence> enumPhrases  = new ArrayList<PreSentence>(); // Represents a series of ",word1, word2, word1, word3" enumerations
		PreSentence phrase 					= new PreSentence(); // a single enumeration: ",word1,"
		
		int enumerationSize = 0;
		
		//System.out.println("Running checkDuplicates()");
		
		enumPhrases = splitIntoCommaPhrases(original);
		enumPhrases = commaPhraseCompare(enumPhrases);
		enumPhrases = returnCommasToSentences(enumPhrases);
		
		System.out.println("Printing production output");
		for( PreSentence sentence : enumPhrases)
		{
			for( Word word : sentence.getWordList() )
			{
				System.out.print("" + word.getWord() + " ");
			}
		}
		
		resSentence = original;
		resSentence.setWordList(tempWord);
		
		return resSentence;
	}
	
	public ArrayList<PreSentence> commaPhraseCompare(ArrayList<PreSentence> phrases)
	{
		ArrayList<PreSentence> finalPhrases = new ArrayList<PreSentence>();
		PreSentence compareFrom 	= new PreSentence(); 		
		PreSentence compareTo 		= new PreSentence();
		PreSentence backTrackPhrase = new PreSentence();
		PreSentence lastTrackPhrase = new PreSentence();
		int compareFromSize	= 0;
		int compareToSize	= 0;
		int sameSizeCount 	= 0;
		Boolean match = false;
		compareFrom = phrases.get(0);
		
		
		for( int i = 0; i < phrases.size(); i++ ) 
		{
			
			compareFrom = phrases.get(i);				
			compareFromSize = compareFrom.getWordList().size();
			
			// Add the first phrase into the arraylist
			if ( i == 0 )
			{
				PreSentence temp = phrases.get(0);				
				finalPhrases.add(phrases.get(0));
			}
			
			
			
			// Add the remaining phrases
			for( int k = i + 1; k < phrases.size() - 1; k++ ) 
			{
				match = false;
				// Add content to compareTo which will be used to compare with compareFrom
				compareTo = phrases.get(k); 
				// number of words in compareTo
				compareToSize = compareTo.getWordList().size();
				
				//System.out.println("compareToSize: " + compareToSize + " first word is: " + compareTo.getWordList().get(0).getWord());				
				
				/* Compare the sizes of each wordList. Same size means they could have the same content.
				 * If they are of the same size and contents are the same, then they are not added into the finalPhrase.
				 */
				if( compareFromSize == compareToSize ) 
				{
					// Compares the first phrase with compareTo.
					backTrackPhrase = getBackTrackPhrase( phrases.get(0), compareFrom.getWordList().size() );
					// Compares the last phrase with compareTo
					lastTrackPhrase = getLastTrackPhrase( phrases.get( phrases.size() - 1 ),  compareFrom.getWordList().size());
					System.out.println("compareFromSize == compareToSize");
					
					
					// check if the current compareTo matches the first phrase
					if (comparePhrases(getBackTrackPhrase( phrases.get(0), compareFrom.getWordList().size() ), compareTo) == true)
					{
						System.out.println("Found match with backTrack");
						match = true;
					} // check if the current compareTo matches the last phrase
					else if ( comparePhrases(getLastTrackPhrase( phrases.get( phrases.size() - 1 ),  compareFrom.getWordList().size()), compareTo) == true)
					{
						System.out.println("Found match with lastTrack");
						match = true;
					} // check if compareFrom matches compareTo
					else if (comparePhrases(compareFrom, compareTo) == true )
					{
						System.out.println("Found match with compareTo");
						match = true;
					}
					
					// add to finalPhrase if match is still false 
					if ( match == false )
					{
						System.out.println("Match is false. Will add to finalPhrase");
						for( Word word : compareTo.getWordList()) 
						{
							System.out.print("" + word.getWord().toString());
						}
						System.out.println("");
						//finalPhrases.add(compareTo);
					}		
					
					
					/*// Compares the first phrase with compareTo.
					backTrackPhrase = getBackTrackPhrase( phrases.get(0), compareFrom.getWordList().size() );
					// Compares the last phrase with compareTo
					lastTrackPhrase = getLastTrackPhrase( phrases.get( phrases.size() - 1 ),  compareFrom.getWordList().size());
										
					System.out.println("Same size found.");
					//System.out.println("From: " + compareFrom.getWordList().size() + " To: " + compareTo.getWordList().size());
					
					// check if the current compareTo matches the first phrase
					if (comparePhrases(getBackTrackPhrase( phrases.get(0), compareFrom.getWordList().size() ), compareTo) == true)
					{
						System.out.println("Found match with backTrack");
						phrases.remove(k);
						match = true;
					} // check if the current compareTo matches the last phrase
					else if ( comparePhrases(getLastTrackPhrase( phrases.get( phrases.size() - 1 ),  compareFrom.getWordList().size()), compareTo) == true)
					{
						System.out.println("Found match with lastTrack");
						phrases.remove(k);
						match = true;
					} // check if compareFrom matches compareTo
					else if (comparePhrases(compareFrom, compareTo) == true )
					{
						System.out.println("Found match with compareTo");
						phrases.remove(k);
						match = true;
					}
					
					// add to finalPhrase if match is still false 
					if ( match == false )
					{
						System.out.println("Match is false. Will add to finalPhrase");
						for( Word word : compareTo.getWordList()) 
						{
							System.out.print("" + word.getWord().toString());
						}
						System.out.println("");
						//finalPhrases.add(compareTo);
					}			*/		
				} 
				else 
				{		
					System.out.println("");
					System.out.println("Else for adding to finalPhrases");
					for( Word word : compareTo.getWordList()) 
					{
						System.out.print("" + word.getWord().toString());
					}
					finalPhrases.add(compareTo);
					System.out.println("");
				}
			}
			
			//comparePhrases(backTrackPhrase, lastTrackPhrase);
		}			
		
		return finalPhrases;
	}
	
	public PreSentence addCommaWord(PreSentence original)
	{
		PreSentence result = new PreSentence();		
		Word comma = new Word();
		comma.setWord(",");
		result = original;
		result.addWord(comma);
		
		return result;
	}
	
	public ArrayList<PreSentence> returnCommasToSentences(ArrayList<PreSentence> sentences)
	{
		ArrayList<PreSentence> result = new ArrayList<PreSentence>();
		Word comma = new Word();
		comma.setWord(",");
		for( int i = 0; i < sentences.size() - 1; i++ )
		{
			PreSentence sentence = new PreSentence();
			sentence = sentences.get(i);
			sentence.addWord(comma);
			result.add(sentence);
		}
		result.add(sentences.get(sentences.size()-1));
		return result;
	}
	
	
	
	public PreSentence getBackTrackPhrase(PreSentence origPhrase, int size)
	{
		PreSentence tempPhrase		= new PreSentence();
		PreSentence reversePhrase 	= new PreSentence();
		ArrayList<Word> origWords		= origPhrase.getWordList();
		ArrayList<Word> tempWords		= new ArrayList<Word>();
		ArrayList<Word> reverseWords	= new ArrayList<Word>();
		int origSize = origPhrase.getWordList().size();
		int count = 0;
		
		//System.out.println("Running getBackTrackPhrase() with size" + size);
		
		for( int i = origSize - 1; count < size; i++) 
		{
			reverseWords.add(origWords.get(i));
			count++;
		}
		
		reversePhrase.setWordList(reverseWords);
		
		//System.out.println("Checking getBackTrackPhrase Result: ");
		
		//for(int i = 0; i < reverseWords.size(); i++) 
		//{
		//	System.out.print(reverseWords.get(i).getWord() + " ");
		//}
		
		//System.out.println("");
		
		return reversePhrase;
	}
	
	public PreSentence getLastTrackPhrase(PreSentence origPhrase, int size) 
	{		
		PreSentence reversePhrase 	= new PreSentence();
		ArrayList<Word> origWords		= origPhrase.getWordList();	
		ArrayList<Word> reverseWords	= new ArrayList<Word>();
		// int origSize = origPhrase.getWordList().size();
		int count = 0;
		
		//System.out.println("Running getLastTrackPhrase() with size" + size);
		
		for( int i = 0; count < size; i++) 
		{
			if( origWords.get(i).toString().equalsIgnoreCase("and")  == false ) 
			{
				reverseWords.add(origWords.get(i));
				count++;
			}			
		}
		
		reversePhrase.setWordList(reverseWords);
		
		//System.out.println("Checking getLastTrackPhrase Result: ");
		
		for(int i = 0; i < reverseWords.size(); i++) 
		{
			System.out.print(reverseWords.get(i).getWord() + " ");
		}
		
		//System.out.println("");
		
		return reversePhrase;
	}
	
	public Boolean comparePhrases(PreSentence compareFrom, PreSentence compareTo)
	{
		PreSentence resultPhrase 			= new PreSentence();
		ArrayList<Word> compareFromWords  	= new ArrayList<Word>();
		ArrayList<Word> compareToWords		= new ArrayList<Word>();
		ArrayList<Word> resultWords			= new ArrayList<Word>();
		int size;
		int matchCount = 0;
		
		compareFromWords = compareFrom.getWordList();
		compareToWords	 = compareTo.getWordList();
		size 			 = compareFromWords.size();
		
		System.out.println("Running comparePhrases");
		
		if(size != compareToWords.size()) {
			if( compareToWords.get(0).toString().equalsIgnoreCase("and") == true ) {
				compareToWords.remove(0);
			}
		}
		if ( size != compareToWords.size()) {
			System.out.println("Error at comparePhrases. Size of compareFrom and compareTo is not equal.");
		} else {		
			for( int i = 0; i < size; i++ ) {
				System.out.println("Comparing: " + compareFromWords.get(i).getWord() + " and " + compareToWords.get(i).getWord());
				if(compareFromWords.get(i).getWord().equals(compareToWords.get(i).getWord())) {
					matchCount++;
				}
			}
			if ( matchCount == size ) {
				System.out.println("A DUPLICATE PHRASE/WORD WAS FOUND");
				return true; // if a duplicate phrase/words was found, return true
			}
			System.out.println("Somehow it's about to return false");
		}			
		return false;		
	}
	
	public ArrayList<PreSentence> splitIntoCommaPhrases(PreSentence original)
	{
		ArrayList<Word> originalWords 	= original.getWordList();
		ArrayList<Word> phraseWords		= new ArrayList<Word>();
		PreSentence phrase				= new PreSentence();
		ArrayList<PreSentence> enums	= new ArrayList<PreSentence>();
		
		for ( int i = 0; i < originalWords.size(); i++ )
		{
			// System.out.print(originalWords.get(i).getWord() + " ");
			
			phraseWords.add(originalWords.get(i)); // Get every word and add it to a new arraylist
			
			// If a comma was found
			if( originalWords.get(i).getWord().equals(",") == true || originalWords.get(i).getWord().equals(".") == true) 
			{
				
				if( originalWords.get(i).getWord().equals(",") == true || originalWords.get(i).getWord().equals(".")) {
					phraseWords.remove(phraseWords.size() - 1); // because I do not want to store the comma and the period
				}
								
				
				// simply store everything into a sentence then an arraylist of sentences
				phrase.setWordList(phraseWords); 
				enums.add(phrase);
				System.out.println("phraseWords-Size: " + phraseWords.size() + " enums-size: " + enums.size());
				//re-initialize everything again
				phraseWords = null;
				phrase		= null; 			
				phraseWords = new ArrayList<Word>();
				phrase 		= new PreSentence();
			}

			System.out.println("");
		}
		
		return enums;
	}
	
}

/*for ( int i = 0; i < phrases.size(); i++ ) 
{
	compareFrom = phrases.get(i);
	
	if( i != phrases.size() - 1) { 
		compareTo = phrases.get(i+1);
	}				
	
	System.out.println("compareFrom Size: " + compareFrom.getWordList().size() + " and compareTo Size: " + compareTo.getWordList().size());
	
	// If the same size of words are found
	if( compareFrom.getWordList().size() == compareTo.getWordList().size() ) 
	{
		System.out.println("Size From: " + compareFrom.getWordList().size() + " Size To: " + compareTo.getWordList().size());
		
		for(int j = 0; j < compareFrom.getWordList().size(); j++) {
			System.out.print(compareFrom.getWordList().get(j).getWord() + " ");
		}
		
		
		
		for(int k = i+1; k < phrases.size(); k++ )
		{
			System.out.println("Duplicate length in phrases found.");
			System.out.println("Comparing i = " + i + " and k = " + k);
			System.out.println("Their size cFrom:" + compareFrom.getWordList().size()  + " || cTo: " + compareTo.getWordList().size());
			compareTo = phrases.get(k);
								
			comparePhrases(compareFrom, compareTo);
		}	
	}
												
	
}*/
