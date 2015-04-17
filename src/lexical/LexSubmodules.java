package lexical;
/*
 * CAScan.java
 * Class for complexity analysis module
 * 
 * Author: Joren Sorilla
 */

import it.uniroma1.lcl.babelfy.commons.annotation.SemanticAnnotation;
import it.uniroma1.lcl.babelfy.core.Babelfy;
import it.uniroma1.lcl.babelnet.BabelNet;
import it.uniroma1.lcl.babelnet.BabelSynset;
import it.uniroma1.lcl.jlt.util.Language;
import it.uniroma1.lcl.jlt.wordnet.WordNet;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

import language.PreSentence;
import language.Word;

import org.apache.commons.lang3.StringUtils;














import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import rita.RiWordNet;
import rita.wordnet.jwnl.wndata.POS;
import rita.wordnet.jwnl.wndata.Synset;
import shortcuts.Print;
import simplenlg.features.Feature;
import simplenlg.features.Form;
import simplenlg.features.NumberAgreement;
import simplenlg.features.Tense;
import simplenlg.framework.InflectedWordElement;
import simplenlg.framework.LexicalCategory;
import simplenlg.framework.WordElement;
import simplenlg.lexicon.XMLLexicon;
import simplenlg.realiser.english.Realiser;



public class LexSubmodules 
{
	private Map<String, Double> map;
	private Map<String, String> zipfMap;
	private Print p = new Print();
	private RiWordNet wordnet;
	private StanfordCoreNLP pipeline;
	public LexSubmodules(StanfordCoreNLP pipeline) {
		this.pipeline = pipeline;
	}



	public static void main(String args[])
	{	Locale.setDefault(Locale.ENGLISH);
		/*
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
			
		}*/
	
	}
	
	
	
	/**
	 * Determines if word is complex or not
	 * @param word whose complexity is determined
	 * @return true if complex, false otherwise
	 */
	public boolean isComplex(String word)
	{	/*
		if(map == null)
		{
			loadWordList();
		}
		Iterator iterator = map.entrySet().iterator();
	    while(iterator.hasNext()){
	    	Map.Entry<String, Double> pairs =(Map.Entry<String, Double>)iterator.next();
	    	if(pairs.getKey().equalsIgnoreCase(word))
	    		return false;
	    }
		return true;
		*/
		
		
		double zipfVal = 0;
		
		zipfVal = getZipfValue(word);
		
		if(zipfVal >= 4.0){
			return false;
		}else
			return true;
		
		
	} 
	
	public double getZipfValue(String word){
    	if(zipfMap == null){
    		zipfMap = new HashMap<String, String>();
    		File lemmacorpus = new File("src/lexical/Resources/zipf.csv");
    		String[] split;
    		try (BufferedReader reader = new BufferedReader(new FileReader(lemmacorpus.getAbsolutePath()))) 
    		{
    		    String line = null;
    		    int sum = 0;
    		    while ((line = reader.readLine()) != null) 
    		    {

    		        split  = line.split(",");   		        
    	        	String w = split[0];
    	        	String zipf = split[1];
    	        	zipfMap.put(w.toLowerCase(), zipf);
    			        
    		    }

    		} catch (IOException x) 
    		{
    		    System.err.format("IOException: %s%n", x);
    		} 
    	}else{
    		//if word in synset is compound, split then get the lowest zipf value of each word
    		String[] split = word.split("_");
    		String valString;
    		double valDouble;
    		double minZipfVal = 7.0;
    		if(split.length > 1){
	    		for(String s : split){
	    			valString = zipfMap.get(s.toLowerCase());
	    			if(valString != null){
	    				valDouble = Double.parseDouble(valString);
	    				if(valDouble < minZipfVal )
	    					minZipfVal = valDouble;
	    			}
	    				
	    		}
	    		return minZipfVal;
    		}else{
    		//if it is only one word then return that word's zipf value, if it is not in the list then return 0.0 zipf value
    			valString = zipfMap.get(split[0].toLowerCase());
    			double zipfVal = 0.0;
    			if(valString != null)
    				zipfVal = Double.parseDouble(valString);
    			
    			return zipfVal;
    		}
    			
    		
    		
    	    /*while(iterator.hasNext()){
    	    	Map.Entry<String, String> pairs =(Map.Entry<String, String>)iterator.next();
    	    	if(pairs.getKey().equalsIgnoreCase(word))
    	    		return pairs.getValue();
    	    	
    	    		
    	    }*/
    		
    	}
    	return 0.0;
    }
	
	public void loadWordList(){
		map = new HashMap<String, Double>();
		File lemmacorpus = new File("src/lexical/Resources/lemmacorpus.txt");
		
		try (BufferedReader reader = new BufferedReader(new FileReader(lemmacorpus.getAbsolutePath()))) 
		{
		    String line = null;
		    int sum = 0;
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
	
	/*
	public ArrayList<PreSentence> candidateSelection(ArrayList<PreSentence> sentences)
	{	
	 
		RiWordNet wordnet = new RiWordNet("src/lexical/Resources/WordNet-3.0");
		jig = new JIGSAW(configFile);
		wordnet.ignoreUpperCaseWords(true);
		wordnet.randomizeResults(false);
		
		
			try {
				sentences = generateSynId(sentences);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		System.out.println("Now performing candidate selection: ");
		for(PreSentence sentence: sentences)
		{				
			for(Word w: sentence.getWordList())
			{
				if(w.isComplex() && !w.isStopWord() && w.getWordType() != Word.COMPOUND_WORD && !w.isIgnore() && w.getSenseId() != 0){
					
					
		
					Synset tmp = null;
				    String lemma = w.getLemma();
				    
				    w.setSubstitute(new ArrayList<String>());
				    POS pos = null;
				    switch(w.getPartOfSpeech().toUpperCase().charAt(0)){
					    case 'J': pos = POS.ADJECTIVE; 
					    	break;
					    case 'V': pos = POS.VERB;  
					    	break;
					    case 'N': pos = POS.NOUN; 
					    	break;
					    case 'R': pos = POS.ADVERB;
					    	break;
					    default: pos = null;
				    }
				    
				  
				 
				    System.out.println(w.getLemma()+" "+w.getPartOfSpeech().toUpperCase().charAt(0)+" "+w.getWord()+ " " + pos +" " +w.getSenseId());
				    if(pos == null)
				    	continue;
					else
						try {
							tmp =  wordnet.getDictionary().getSynsetAt(pos,w.getSenseId());
						} catch (JWNLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				    
				    
				    if(tmp.getWordsSize() > 0)
					    for(rita.wordnet.jwnl.wndata.Word word:tmp.getWords()){
					    	System.out.println("Subs: "+word.getLemma());
					    	if(word.getLemma().equalsIgnoreCase(w.getLemma()))
					    		continue;
					    	else
					    		w.getSubstitute().add(word.getLemma().replace('_', ' '));
					    }
				}// end if
			}// end for
		}// end for
		
		
		return sentences;
 		
	}*/
	
	/**
	 * gets the appropriate synonyms of each complex word in the text
	 * @param sentences list of sentences with complex words
	 * @return updated list of sentences wherein each complex word has their substitutes identified
	 */
	public ArrayList<PreSentence> candidateSelection(ArrayList<PreSentence> sentences)
	{
		if(wordnet == null)
			wordnet = new RiWordNet("src/lexical/Resources/WordNet-3.0");
		//jig = new JIGSAW(configFile);
		wordnet.ignoreUpperCaseWords(true);
		wordnet.ignoreCompoundWords(false);
		wordnet.randomizeResults(false);
		
		
		
		
			try {
				sentences = generateSynId(sentences); 
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			for(PreSentence sentence: sentences)
			{				
				for(Word w: sentence.getWordList())
				{
					if(w.isComplex() && !w.isStopWord() && w.getWordType() != Word.COMPOUND_WORD && !w.isIgnore() && w.getSenseId() != 0)
					{
						Synset tmp 		= null;
						String lemma 	= w.getLemma();
						POS pos;
						w.setSubstitute(new ArrayList<String>());
						pos = getMainPOS(w); // check if it's a J,V,N,R

						System.out.println(w.getLemma()+" "+w.getPartOfSpeech().toUpperCase()+" "+w.getWord()+ " " + pos +" " +w.getSenseId());
						
						if(pos == null) {
							continue;
						} else {
							try {
								System.out.println(pos+" "+w.getSenseId());
								tmp =  wordnet.getDictionary().getSynsetAt(pos,w.getSenseId()); // hanapin yung SenseID
							} catch (Exception e) {
							// TODO Auto-generated catch block
								e.printStackTrace();
								
							}
						}
						
						
						if(tmp != null)
							if(tmp.getWordsSize() > 0) 
							{	
								//include original word as candidate
								String[] substitutes;
								if(!tmp.containsWord(w.getLemma().replace(' ', '_'))){
									
									substitutes = new String[tmp.getWordsSize()+1];
									
									for(int i = 0; i < substitutes.length-1; i++){
										substitutes[i] = tmp.getWords()[i].getLemma();
									}
									substitutes[substitutes.length-1] = w.getLemma();
								}else{
									
									substitutes = new String[tmp.getWordsSize()];
									
									for(int i = 0; i < substitutes.length; i++){
										substitutes[i] = tmp.getWords()[i].getLemma();
									}
									
								}
									
									
								String bestCandidate = getBestCandidate(substitutes);
					    		if(bestCandidate.equalsIgnoreCase(w.getLemma())){
					    			w.setBestSubstitute(w.getWord());
					    		}else{
					    			if(w.hasTense()){
					    				//if verb has more than one word (phrasal verb), split each word, convert tense of first word, then append the rest of the phrasal
					    				String[] split = bestCandidate.split("_");
					    				if(split.length > 1){
					    					String append = changeTense(split[0],w.getPartOfSpeech());
					    					for(int i = 1; i < split.length;i++){
					    						append+=(" "+split[i]);
					    					}
					    					w.setBestSubstitute(append);
					    				}else
					    					w.setBestSubstitute(changeTense(bestCandidate.replace('_', ' '),w.getPartOfSpeech()));
					    			}else if(w.hasNumberAgreement())
					    				w.setBestSubstitute(changeNumberAgreement(bestCandidate.replace('_', ' '),w.getPartOfSpeech()));
					    			else
					    				w.setBestSubstitute(bestCandidate.replace('_', ' '));
					    		}
					    		
					    		
							}
							
					}
				}// end for
		}// end for
			
		return sentences;
	}	
	
	public String getBestCandidate(String[] words){
		int size = words.length;
		
		System.out.println("Getting best candidate: "+size);
		double maxZipfScore = 0;
		int maxIndex = 0;
		double tempScore = 0;
		
		for(int i = 0; i < size; i++){
			//get zipf value
			tempScore = getZipfValue(words[i]);
			System.out.println(words[i]+" "+tempScore);
			
			//remember the index of the word with the highest zipf score
			if(tempScore > maxZipfScore){
				maxZipfScore = tempScore;
				maxIndex = i;
			}
			
			
		}

		return words[maxIndex];
		
		
	}
	
	 public String changeTense(String word, String POS) {
		 
	        XMLLexicon lexicon = new XMLLexicon("Imports/SimpleNLGResources/default-lexicon.xml");
	        WordElement wordElement = lexicon.getWord(word, LexicalCategory.VERB);
	        InflectedWordElement infl = new InflectedWordElement(wordElement);
	        System.out.println("POS IS: "+POS);
	        switch (POS) {
	        	case "VB":
	        		infl.setFeature(Feature.FORM, Form.BARE_INFINITIVE);
	        		break;
	            //Past Tense
	            case "VBD":
	                infl.setFeature(Feature.TENSE, Tense.PAST);
	                break;
	            //Gerund / Present Participle
	            case "VBG": 
	                infl.setFeature(Feature.FORM, Form.PRESENT_PARTICIPLE);
	                break;
	            //Past Participle
	            case "VBN":
	                infl.setFeature(Feature.FORM, Form.PAST_PARTICIPLE);
	                break;
	            //Present
	            case "VBP":
	                infl.setFeature(Feature.TENSE, Tense.PRESENT);
	                break;
	            //Present 3rd Person
	            case "VBZ":
	                infl.setFeature(Feature.FORM, Form.GERUND);
	 
	        }
	 
	        Realiser realiser = new Realiser(lexicon);
	        return realiser.realise(infl).getRealisation();
	    }
	 
	 public String changeNumberAgreement(String word, String POS) {
		 
	        XMLLexicon lexicon = new XMLLexicon("Imports/SimpleNLGResources/default-lexicon.xml");
	        WordElement wordElement = lexicon.getWord(word, LexicalCategory.NOUN);
	        InflectedWordElement infl = new InflectedWordElement(wordElement);
	 
	        switch (POS) {
	            //Noun singular/mass
	            case "NN":
	                infl.setFeature(Feature.NUMBER, NumberAgreement.SINGULAR);
	                break;
	            //Noun Plural
	            case "NNS": 
	                infl.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
	                break;
	            //Proper noun singular
	            case "NNP":
	                infl.setFeature(Feature.NUMBER, NumberAgreement.SINGULAR);
	                break;
	            //Proper noun Plural
	            case "NNPS":
	                infl.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
	                break;
	            
	        }
	 
	        Realiser realiser = new Realiser(lexicon);
	        return realiser.realise(infl).getRealisation();
	    }
	 public List<String> lemmatizeWord(String word)
	 {
	        List<String> lemmas = new LinkedList<String>();

	        // create an empty Annotation just with the given text
	        edu.stanford.nlp.pipeline.Annotation document = new edu.stanford.nlp.pipeline.Annotation(word);

	        // run all Annotators on this text
	        this.pipeline.annotate(document);

	        // Iterate over all of the sentences found
	        List<CoreMap> sentences = document.get(SentencesAnnotation.class);
	        for(CoreMap sentence: sentences) {
	            // Iterate over all tokens in a sentence
	            for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
	                // Retrieve and add the lemma for each word into the list of lemmas
	                lemmas.add(token.get(LemmaAnnotation.class));
	            }
	        
	    	}
	     
	        return lemmas;
	}
	public POS getMainPOS(Word w)
	{
		POS pos = null;
		switch(w.getPartOfSpeech().toUpperCase().charAt(0))
		{ //gets the first letter of the POS tag
		    case 'J': pos = POS.ADJECTIVE; 
    				return pos;
		    case 'V': pos = POS.VERB;  
		    		return pos;
		    case 'N': pos = POS.NOUN; 
		    		return pos;
		    case 'R': pos = POS.ADVERB;
		    		return pos;
		    default: return pos = null;
	    }
	}
	
	
	
	public String toString(ArrayList<PreSentence> sentences){
		String lexicalOutput="";
		
		for(PreSentence s: sentences){
			for(Word w: s.getWordList()){
				if(w.getBestSubstitute() != null)
					lexicalOutput+=(w.getBestSubstitute()+" ");
				else if(w.getWord().equalsIgnoreCase("-LSB-"))
					lexicalOutput+=("["+" ");
				else if(w.getWord().equalsIgnoreCase("-RSB-"))
					lexicalOutput+=("]"+" ");
				else if(w.getWord().equalsIgnoreCase("-LRB-"))
					lexicalOutput+=("("+" ");
				else if(w.getWord().equalsIgnoreCase("-RRB-"))
					lexicalOutput+=(")"+" ");
				else if(w.getWord().equalsIgnoreCase("``") || w.getWord().equalsIgnoreCase("''") )
					lexicalOutput+=("\""+" ");
				else if(w.getWord().equalsIgnoreCase("`") || w.getWord().equalsIgnoreCase("'") )
					lexicalOutput+=("\'"+" ");
				else 
					lexicalOutput+=(w.getWord()+" ");
				
			}
		}
	
		return lexicalOutput;
	}
	
	
	
	/**
	 * finds words (or a set of words) that can be simplified using direct substitution
	 * @param sentences list of sentences
	 * @return updated list of sentences wherein direct substitution has been applied
	 */
	public ArrayList<PreSentence> directSubstitution(ArrayList<PreSentence> sentences, File corpus)
	{
		
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
		    			
		    			if(word.getWordType() != Word.MULTI_WORD  && !word.isIgnore() ){
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
		    			}else{
		    				if(word.getWord().equalsIgnoreCase(splitted[0]) && !word.isIgnore() ){
			    				
			    				//set the substitute
			    				words.get(i).setBestSubstitute(splitted[1]);
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
				|| word.getPartOfSpeech().equalsIgnoreCase("NNPS"))
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
	
	
	public ArrayList<PreSentence> generateSynId(ArrayList<PreSentence> sentences) throws Exception
    {	
		// Nlp nlp = new Nlp();
		// String[] stopWords = nlp.LoadStopWordList();
		
		ArrayList<String> sentenceList 	= new ArrayList<String>();
		ArrayList<String> posList 		= new ArrayList<String>();
		ArrayList<String> tokenList  	= new ArrayList<String>();
		
		ArrayList<PreSentence> condSentenceList 	= new ArrayList<PreSentence>();
		ArrayList<PreSentence> updatedSentenceList 	= new ArrayList<PreSentence>();
		ArrayList<PreSentence> tempSentList 		= new ArrayList<PreSentence>();
		
		p.println("Running generateSynId");
		
		
		
		// Insert Babelfy API Code for disambiguation below this comment.
		for( PreSentence line : sentences )
		{
			
			PreSentence babelSentence 	= new PreSentence();
			PreSentence updatedSentence = new PreSentence();
			p.println("linePrint: " + line.toString());
			String sentString = convertWordListToString(line.getWordList());
			//String sentString = "I was walking across the street.";
			p.println("sentString: " + sentString);
			babelSentence = babelfyWSD(sentString);
			updatedSentence = compareBabelToOriginalSentence(line, babelSentence);
			updatedSentenceList.add(updatedSentence);
		}
		
				
		
		
		for ( PreSentence tempSent : condSentenceList) 
		{		
			for( Word tempWord : tempSent.getWordList())
			{
				p.println("tempWord: " + tempWord.getWord());
				if( tempWord.getSenseId() != 0 )
					p.println("offset: " + tempWord.getSenseId());
			}
		}
		
		
		
		return sentences;
    }
	
	
	
	/**
	 * For performing Word Sense Disambiguation
	 * @param inputText
	 * A single sentence in string format
	 * @return
	 * a PreSentence object containing all identified babelnetids and their corresponding word.
	 * All are converted to Word()
	 * @throws Exception
	 */
	public PreSentence babelfyWSD(String inputText) throws Exception
	{
		p.println("Running babelfyWSD");
		PreSentence babelSentence 	= new PreSentence();
		ArrayList<Word> babelList 	= new ArrayList<Word>();
		WordNet wn 					= WordNet.getInstance();
		Babelfy bfy = new Babelfy();
		BabelNet bn = BabelNet.getInstance();
		
		List<SemanticAnnotation> bfyAnnotations  = bfy.babelfy(inputText, Language.EN);
		
		for ( SemanticAnnotation  annotation :	bfyAnnotations)
		{
			String temp 	= "";
			String anchor 	= "";
			Word babelWord 	= new Word();
			String frag = inputText.substring(annotation.getCharOffsetFragment().getStart(),
			        annotation.getCharOffsetFragment().getEnd() + 1);
			
			p.println("anchor: " + anchor);
			babelWord.setWord( frag );
			
			BabelSynset synset 			= bn.getSynsetFromId(annotation.getBabelSynsetID());
			if(synset == null)
				continue;
			List<String> wordnetOffsets = synset.getWordNetOffsets();
			temp = removeExtraOffsets(wordnetOffsets.toString());
			if(!temp.equals(""))
			{
				p.println("temp ( with removeExtraOffsets): " + temp);
				babelWord.setSenseId(Long.parseLong(temp));
				babelList.add(babelWord);
			}
			//p.println("WordNet offsets: " + temp);
			
			/*
			for ( String offset : wordnetOffsets ) 
			{
				p.println("offsetsCount: " + wordnetOffsets.size()); // always 1
				ISynset wnSynset = wn.getSynsetFromOffset(offset); // id
				if(wnSynset != null) 
		    	{
					// this is always null
		    		System.out.print("OFFSETS:"+wnSynset.getOffset());
		    	}
			}
			*/
			
			
		}
		babelSentence.setWordList(babelList);
		return babelSentence;
	}
	
	/**
	 * A method for comparing and appending babelWSD data to the original PreSentence
	 * @param original
	 * The original PreSentence object that would contain an non-updated sentence.
	 * @param babelSent
	 * A sentence containing words with identified wordOffset through babelfy
	 * @return
	 * An updated PreSentence() that includes babelnet ids / word sense ids for the identified words only.
	 */
	public PreSentence compareBabelToOriginalSentence(PreSentence original, PreSentence babelSent)
	{
		ArrayList<Word> oWords = new ArrayList<Word>();
		ArrayList<Word> bWords = new ArrayList<Word>();
		
		oWords = original.getWordList();
		bWords = babelSent.getWordList();
		
		for( Word word : oWords )
		{
			for ( Word babel : bWords ) 
			{
				if(word.getWord().equalsIgnoreCase(babel.getWord())) {
					word.setSenseId(Long.parseLong("" + babel.getSenseId()));
				}
			}
		}
		original.setWordList(null);
		original.setWordList(oWords);
		return original;
	}
	
	
	
	public String removeExtraOffsets(String input)
	{
		String temp = input;
		temp = temp.replace('[', ' ');
		temp = temp.replace(']', ' ');
		temp = temp.replace('a', ' ');
		temp = temp.replace('v', ' ');
		temp = temp.replace('j', ' ');
		temp = temp.replace('r', ' ');
		temp = temp.replace('n', ' ');
		temp = temp.trim();
		return temp;
	}
	
	public void updatePreSentenceWithBabelfy()
	{
		
	}
	
	public String convertWordListToString(ArrayList<Word> wordList)
	{
		String result = ""; 
		
		// an error message that checks if wordList is blank 
		if(wordList.size() == 0) {
			p.println("Cannot convertWordListToString because wordList is blank");
		} else {
			for ( int i = 0; i < wordList.size(); i++ )
			{
				if( i == wordList.size() - 1) {
					result = result + wordList.get(i).getWord().toString();
				} else {
					result = result + wordList.get(i).getWord().toString() + " ";
				}
			}
		}
		return result;
	}
	
	public Boolean checkConditionsForComplexity(Word w)
	{
		Boolean result = false;
		
		if(w.isComplex() && !w.isStopWord() && w.getWordType() != Word.COMPOUND_WORD && !w.isIgnore())
			result = true;
		return result;
	}
	
	
	
	    
	
}
