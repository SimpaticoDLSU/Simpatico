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
import java.util.Locale;
import java.util.Scanner;

import preprocess.PreSentence;
import preprocess.Word;
import rita.RiWordNet;



public class LexSubmodules 
{
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
	/*	Determines if word is complex or not
	 * 	word - word whose complexity is determined
	 * returns true if complex, false otherwise
	 */
	public boolean isComplex(String word)
	{
		File lemmacorpus = new File("src/lexical/Resources/lemmacorpus.txt");
		try (BufferedReader reader = new BufferedReader(new FileReader(lemmacorpus.getAbsolutePath()))) 
		{
		    String line = null;
		    while ((line = reader.readLine()) != null) 
		    {
		        
		        Scanner s = new Scanner(line.trim());
	        	String s1 = s.next();
	        	int i = 0;
	        	
	        	if(s.hasNextInt())
	        		i = s.nextInt();
	        	
	        	if(s1.trim().toLowerCase().equals(word.toLowerCase()))
		        	return false;
			        
		    }
		   
		} catch (IOException x) 
		{
		    System.err.format("IOException: %s%n", x);
		} 
		return true;
	} 
	
	
	public ArrayList<PreSentence> candidateSelection(ArrayList<PreSentence> sentences)
	{	
	
		RiWordNet wordnet = new RiWordNet("src/lexical/Resources/WordNet-3.1");
		wordnet.ignoreCompoundWords(true);
		wordnet.ignoreUpperCaseWords(true);
		wordnet.randomizeResults(false);
		
		for(PreSentence sentence: sentences)
		{				
			for(Word w: sentence.getWordList())
			{
				if(w.isComplex() && !w.isStopWord()){
					
					String[] tmp = {};
				
				
				    String word = w.getLemma();
				    System.out.println(w.getLemma());
				    w.setSubstitute(new ArrayList<String>());
				    String pos = null;
				    System.out.println(w.getPartOfSpeech().toUpperCase().charAt(0));
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
				    
				  
				    
				    tmp =  wordnet.getSynonyms(word, pos);
				 
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
	
	
	    
	
}
