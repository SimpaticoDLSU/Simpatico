package lexical;
/*
 * CAScan.java
 * Class for complexity analysis module
 * 
 * Author: Joren Sorilla
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

import objects.PreSentence;
import objects.Word;

import org.apache.commons.lang3.StringUtils;

import rita.RiWordNet;



public class LexSubmodules 
{
	private Map<String, Double> map;

	public static void main(String args[])
	{	Locale.setDefault(Locale.ENGLISH);
		
		LexSubmodules s= new LexSubmodules();
		
		ArrayList<PreSentence> sentences = new ArrayList<PreSentence>();
		PreSentence s_one = new PreSentence();
		PreSentence s_two = new PreSentence();
		s_one.setId(0);
		s_two.setId(1);
		Word justice = new Word("justice");
		Word jury = new Word("jury");
		Word aftermath = new Word("aftermath");
		Word fiscal = new Word("fiscal");
		Word prevail = new Word("prevail");
		Word pursuant = new Word("pursuant");
		Word fly = new Word("fly");
	
		
		justice.setComplex(true);
		justice.setStopWord(false);
		justice.setPartOfSpeech("NN");
		jury.setComplex(true);
		jury.setStopWord(false);
		jury.setPartOfSpeech("NN");
		aftermath.setComplex(true);
		aftermath.setStopWord(false);
		aftermath.setPartOfSpeech("NN");
		fiscal.setComplex(true);
		fiscal.setStopWord(false);
		fiscal.setPartOfSpeech("NN");
		prevail.setComplex(true);
		prevail.setStopWord(false);
		prevail.setPartOfSpeech("NN");
		pursuant.setComplex(true);
		pursuant.setStopWord(false);
		pursuant.setPartOfSpeech("NN");
		fly.setComplex(true);
		fly.setStopWord(false);
		fly.setPartOfSpeech("NN");
		
		
		justice.setLemma("justice");
		jury.setLemma("jury");
		aftermath.setLemma("aftermath");
		fiscal.setLemma("fiscal");
		prevail.setLemma("prevail");
		pursuant.setLemma("pursuant");
		fly.setLemma("fly");
		
		s_one.getWordList().add(justice);
		s_one.getWordList().add(jury);
		s_one.getWordList().add(aftermath);
		s_one.getWordList().add(fiscal);
		s_two.getWordList().add(prevail);
		s_two.getWordList().add(pursuant);
		s_two.getWordList().add(fly);
		
		sentences.add(s_one);
		sentences.add(s_two);
		
		
		
		ArrayList<PreSentence> res = s.candidateSelection(sentences);
		for(PreSentence sentence: res)
		{				
			for(Word w: sentence.getWordList())
			{
				
				
			    
				System.out.println("\nWord:"+w.getLemma()+"\n"+"Synonyms:");
			    
			    for(String st:w.getSubstitute())
			    {
			    	 System.out.println(st);
			    	 
			    }
			}
			
		}
	}
	
	
	
	/**
	 * Determines if word is complex or not
	 * @param word whose complexity is determined
	 * @return true if complex, false otherwise
	 */
	public boolean isComplex(String word)
	{	
		if(map == null)
		{
			map = new HashMap<String, Double>();
			File lemmacorpus = new File("src/lexical/Resources/lemmacorpus.txt");
			double sum = 0;
			try (BufferedReader reader = new BufferedReader(new FileReader(lemmacorpus.getAbsolutePath()))) 
			{
			    String line = null;
			    while ((line = reader.readLine()) != null) 
			    {
			        
			        Scanner s = new Scanner(line.trim());
		        	String s1 = s.next();
		        	double freq = s.nextDouble();
		        	sum+=freq;
		        	map.put(s1, freq);
				        
			    }
			    
			    
			   
			} catch (IOException x) 
			{
			    System.err.format("IOException: %s%n", x);
			} 
		}
		Iterator iterator = map.entrySet().iterator();
	    while(iterator.hasNext()){
	    	Map.Entry<String, Double> pairs =(Map.Entry<String, Double>)iterator.next();
	    	if(pairs.getKey().equalsIgnoreCase(word))
	    		return false;
	    }
		return true;
		
	} 
	
	/**
	 * gets the appropriate synonyms of each complex word in the text
	 * @param sentences list of sentences with complex words
	 * @return updated list of sentences wherein each complex word has their substitutes identified
	 */
	public ArrayList<PreSentence> candidateSelection(ArrayList<PreSentence> sentences)
	{	
	 
		RiWordNet wordnet = new RiWordNet("src/lexical/Resources/WordNet-3.1");
		wordnet.ignoreUpperCaseWords(true);
		wordnet.randomizeResults(false);
	
		System.out.println("Now performing candidate selection: ");
		for(PreSentence sentence: sentences)
		{				
			for(Word w: sentence.getWordList())
			{
				if(w.isComplex() && !w.isStopWord() && w.getWordType() != Word.COMPOUND_WORD && !w.isIgnore()){
					
					String[] tmp = {};
		
				
				    String lemma = w.getLemma();
				    
				    w.setSubstitute(new ArrayList<String>());
				    String pos = null;
				    switch(w.getPartOfSpeech().toUpperCase().charAt(0)){
					    case 'J': pos = RiWordNet.ADJ; 
					    	break;
					    case 'V': pos = RiWordNet.VERB;  
					    	break;
					    case 'N': pos = RiWordNet.NOUN; 
					    	break;
					    case 'R': pos = RiWordNet.ADV;
					    	break;
					    default: pos = wordnet.getBestPos(w.getLemma());
				    }
				    
				  
				 
				    System.out.println(w.getLemma()+" "+w.getPartOfSpeech().toUpperCase().charAt(0)+" "+w.getWord()+ " " + pos);
				    if(pos == null)
				    	continue;
				    else
				    	tmp =  wordnet.getSynset(w.getLemma().toLowerCase(), pos);
				    
				    
				    if(tmp.length > 0)
					    for(String s:tmp){
					    	System.out.println("Subs: "+s);
					    	 w.getSubstitute().add(s);
					    }
				}// end if
			}// end for
		}// end for
		
		
		return sentences;
 		
	}
	
	/**
	 * finds words (or a set of words) that can be simplified using direct substitution
	 * @param sentences list of sentences
	 * @return updated list of sentences wherein direct substitution has been applied
	 */
	public ArrayList<PreSentence> directSubstitution(ArrayList<PreSentence> sentences)
	{
		File corpus = new File("src/Documents/compoundprep.txt");
		try (BufferedReader reader = new BufferedReader(new FileReader(corpus.getAbsolutePath()))) 
		{
		    String line = null;
		    String[] splitted;
		    ArrayList<Word> words;
		    Scanner scanner = null;
		    while ((line = reader.readLine()) != null) 
		    {	
		    	splitted = line.split(":");
		    	scanner = new Scanner(splitted[0]);
		    	String firstToken = scanner.next();
		    	ArrayList<String> listOfSubstitutes = new ArrayList<String>();
		    	listOfSubstitutes.add(firstToken);
		    	
		    	while(scanner.hasNext())
		    		listOfSubstitutes.add(scanner.next());
		    	
		    	for(PreSentence sentence: sentences)
		    	{	
		    		words = sentence.getWordList();
		    		for(int i = 0; i < words.size(); i++)
		    		{	Word word = words.get(i);
		    			
		    			if(word.getWord().equalsIgnoreCase(firstToken) && isSubstitutableAtIndex(words,i,listOfSubstitutes))
		    			{	
		    				int addIndex = 1;
		    				//set the substitute
		    				words.get(i).setBestSubstitute(splitted[1]);
		    				
		    				//set wordType of the word
		    				words.get(i).setWordType(Word.COMPOUND_WORD);
		    				
		    				//append the succeeding words to the first token
							for(int substituteNum = 0 ; substituteNum < listOfSubstitutes.size()-1; substituteNum++){
								words.get(i).appendWord(words.get(i+addIndex).getWord());
								addIndex++;
							}
							
							//remove the words that have already been merged to the first token
							for(int d = i+1; d <= i+(listOfSubstitutes.size()-1); d++){
								words.remove(i+1);
							}
		    			}
		    		}
		    		
		    		sentence.setWordList(words);
		    	}
		        
			        
		    }
		    if(scanner != null)
		    	scanner.close();
		   
		} catch (IOException x) 
		{
		    System.err.format("IOException: %s%n", x);
		} 
		
		return sentences;
	}
	/**
	 * Checks if a set of words in one list matches another set of words in another list. Assumes that list of Word classes are sorted according to how the actual sentence is ordered.
	 * @param words list of Word classes 
	 * @param index index in words where checking is started
	 * @param subs list of words to be matched with the list of Word classes
	 * @return
	 */
	public boolean isSubstitutableAtIndex(ArrayList<Word> words, int index, ArrayList<String> subs)
	{
		for(int i = index; i < (index + subs.size()); i++){
			System.out.println("isSubstitutableAtIndex: "+ words.get(i).getWord()+" "+subs.get(i-index));
			if(!words.get(i).getWord().equalsIgnoreCase(subs.get(i-index))){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Identifies words to be ignored. 
	 * Ignorables: 
	 * Parentheses |
	 * Words within parentheses |
	 * Quotation marks |
	 * Words within quotation marks |
	 * Numeric characters |
	 * All uppercase words |
	 * All words tagged as proper nouns(Singular/Plural) 
	 * @param sentences list of sentences
	 * @return updated list of sentences wherein direct substitution has been applied
	 */
	public ArrayList<PreSentence> identifyIgnorables(ArrayList<PreSentence> sentences)
	{
		
		
		boolean isWithinParenthesis = false;
		boolean isWithinQuotations = false;
		
		
		for(PreSentence sentence : sentences)
		{
			
			for(Word word : sentence.getWordList())
			{	
				if(StringUtils.isNumeric(word.getWord()) || word.getPartOfSpeech().equalsIgnoreCase("NNP") 
				|| word.getPartOfSpeech().equalsIgnoreCase("NNPS") || StringUtils.isAllUpperCase(word.getWord()))
				{
					word.isIgnore(true);
					continue;
				}
				
				if(isWithinParenthesis == true || isWithinQuotations == true)
				{
					if(word.getWord().equalsIgnoreCase("-RRB-"))
					{
						isWithinParenthesis = false;
					} else if (word.getWord().equalsIgnoreCase("''"))
					{
						isWithinQuotations = false;
					} else if (word.getWord().equalsIgnoreCase("'"))
					{
						isWithinQuotations = false;
					}
					System.out.println("Word: " + word.getWord() + " Ignored.");
					word.isIgnore(true);
					continue;
				}
				
				if(isWithinParenthesis != true && isWithinQuotations != true)
				{	
					if(word.getWord().equalsIgnoreCase("-LRB-"))
					{
						System.out.println("Word: " + word.getWord() + " Ignored.");
						word.isIgnore(true);
						isWithinParenthesis = true;
					} else if(word.getWord().equalsIgnoreCase("``"))
					{	
						System.out.println("Word: " + word.getWord() + " Ignored.");
						word.isIgnore(true);
						isWithinQuotations = true;
					} else if(word.getWord().equalsIgnoreCase("`"))
					{
						System.out.println("Word: " + word.getWord() + " Ignored.");
						word.isIgnore(true);
						isWithinQuotations = true;
					}
				}
				
				
					
			}
		}
		
		return sentences;
	}
	
	
	
	    
	
}
