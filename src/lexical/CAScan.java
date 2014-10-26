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

import preprocess.Sentence;
import preprocess.Word;
import rita.RiWordNet;

public class CAScan 
{
	public static void main(String args[])
	{	Locale.setDefault(Locale.ENGLISH);
		
		CAScan s= new CAScan();
		
		ArrayList<Sentence> sentences = new ArrayList<Sentence>();
		Sentence s_one = new Sentence(0);
		Sentence s_two = new Sentence(1);
		Word justice = new Word("justice");
		Word jury = new Word("jury");
		Word aftermath = new Word("aftermath");
		Word fiscal = new Word("fiscal");
		Word prevail = new Word("prevail");
		Word pursuant = new Word("pursuant");
		Word fly = new Word("fly");
		
		justice.setComplex(true);
		justice.setStopWord(false);
		jury.setComplex(true);
		jury.setStopWord(false);
		aftermath.setComplex(true);
		aftermath.setStopWord(false);
		fiscal.setComplex(true);
		fiscal.setStopWord(false);
		prevail.setComplex(true);
		prevail.setStopWord(false);
		pursuant.setComplex(true);
		pursuant.setStopWord(false);
		fly.setComplex(true);
		fly.setStopWord(false);
		
		
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
		
		
		
		ArrayList<Sentence> res = s.candidateSelection(sentences);
		for(Sentence sentence: res)
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
		File lemmacorpus = new File("Resources/lemmacorpus.txt");
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
		        	return true;
			        
		    }
		   
		} catch (IOException x) 
		{
		    System.err.format("IOException: %s%n", x);
		} 
		return false;
	} 
	
	
	public ArrayList<Sentence> candidateSelection(ArrayList<Sentence> sentences)
	{	
	
		RiWordNet wordnet = new RiWordNet("Resources/WordNet/2.1");
		wordnet.ignoreCompoundWords(true);
		wordnet.ignoreUpperCaseWords(true);
		wordnet.randomizeResults(false);
		
		for(Sentence sentence: sentences)
		{				
			for(Word w: sentence.getWordList())
			{
				if(w.isComplex() && !w.isStopWord()){
					
					String[] tmp = {};
				    String word = w.getLemma();
				    
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
				    
				    tmp =  wordnet.getSynonyms(word, pos);
				 
				    for(String s:tmp){
				    	 w.getSubstitute().add(s);
				    }
				}// end if
			}// end for
		}// end for
		
		
		return sentences;
 		
	}
	
	
	    
	
}
