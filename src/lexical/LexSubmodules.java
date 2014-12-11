package lexical;
/*
 * CAScan.java
 * Class for complexity analysis module
 * 
 * Author: Joren Sorilla
 */
import it.uniroma1.lcl.babelfy.Babelfy;
import it.uniroma1.lcl.babelfy.Babelfy.AccessType;
import it.uniroma1.lcl.babelfy.Babelfy.Matching;
import it.uniroma1.lcl.babelfy.data.Annotation;
import it.uniroma1.lcl.babelfy.data.BabelSynsetAnchor;
import it.uniroma1.lcl.babelnet.BabelSynset;
import it.uniroma1.lcl.jlt.util.Language;
import it.uniroma1.lcl.jlt.wordnet.WordNet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

import language.PreSentence;
import language.Word;

import org.apache.commons.lang3.StringUtils;

import rita.RiWordNet;
import rita.wordnet.jwnl.JWNLException;
import rita.wordnet.jwnl.wndata.POS;
import rita.wordnet.jwnl.wndata.Synset;
import shortcuts.Print;



public class LexSubmodules 
{
	private Map<String, Double> map;
	private static File configFile = new File("resources/jigsaw.properties");
	private Print p = new Print();
	private RiWordNet wordnet;
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
	{	
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
		wordnet.randomizeResults(false);
		
		
			try {
				sentences = generateSynId(sentences); // method sa baba
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

						System.out.println(w.getLemma()+" "+w.getPartOfSpeech().toUpperCase().charAt(0)+" "+w.getWord()+ " " + pos +" " +w.getSenseId());
						
						if(pos == null) {
							continue;
						} else {
							try {
								tmp =  wordnet.getDictionary().getSynsetAt(pos,w.getSenseId()); // hanapin yung SenseID
							} catch (JWNLException e) {
							// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						if(tmp != null)
							if(tmp.getWordsSize() > 0) 
							{
								for(rita.wordnet.jwnl.wndata.Word word:tmp.getWords())
								{
						    		System.out.println("Subs: "+word.getLemma());
						    		if(word.getLemma().equalsIgnoreCase(w.getLemma())){
						    			w.getSubstitute().add(w.getWord());
						    		}else
						    			w.getSubstitute().add(word.getLemma().replace('_', ' '));
						    	}
							}

					}
				}// end for
		}// end for
			
		return sentences;
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
	
	/**
	 * 
	 * @param word
	 * A string that requires a SynSet id to be retrieved.
	 * Requires configFile and JIGSAW to be set up as static variables.
	 * Please do make sure that the resource folder is set as src and in the build path and import the necessary JIGSAW libraries.
	 * @return
	 * 8 digit integer containing the wordnet id
	 * @throws Exception
	 */
	
	/*
	public ArrayList<PreSentence> generateSynId(ArrayList<PreSentence> sentences) throws Exception
    {	
        TokenGroup tg = null;
        ArrayList<String> posList = new ArrayList<String>();
        ArrayList<String> tokenList = new ArrayList<String>();
        
        for(PreSentence sentence: sentences)
		{				
			for(Word w: sentence.getWordList())
			{
				if(w.isComplex() && !w.isStopWord() && w.getWordType() != Word.COMPOUND_WORD && !w.isIgnore()){
					
					
				    
				    tokenList.add(w.getWord());
				    String pos = null;
				    switch(w.getPartOfSpeech().toUpperCase().charAt(0)){
					    case 'J': pos = "a"; 
					    	break;
					    case 'V': pos = "v";  
					    	break;
					    case 'N': pos = "n"; 
					    	break;
					    case 'R': pos = "r";
					    	break;
					    default : pos = "U";
				    }
				    posList.add(pos);

				}// end if
			}// end for
		}// end for
        
        tg = jig.mapText(tokenList.toArray(new String[tokenList.size()]), posList.toArray(new String[posList.size()]));
        if (tg != null) {
            System.out.println();
            
            for (int i = 0; i < tg.size(); i++) {
            	System.out.print(tg.get(i).getToken());
                System.out.print(" ");
                System.out.print(tg.get(i).getStem());
                System.out.print(" ");
                System.out.print(tg.get(i).getPosTag());
                System.out.print(" ");
                System.out.print(tg.get(i).getLemma());
                System.out.print(" ");
                System.out.print(tg.get(i).getSyn());
                System.out.println();
            	
            }            
        }
        
        int lastK = 0;
        int lastI = 0;
        point:
        for(Token token : tg.getTokens()){
	        for(int i = lastI; i < sentences.size(); i++){
	        	ArrayList<Word> sentence = sentences.get(i).getWordList();
	        	
	        	for(int k = lastK; k < sentence.size(); k++ ){
	        		if(token.getSyn().equalsIgnoreCase("U"))
	        			continue point;
	        		if(token.getToken().equalsIgnoreCase(sentence.get(k).getWord())){
	        			sentence.get(k).setSenseId(Long.valueOf(token.getSyn().substring(1)));
	        			if((lastK+1) < sentence.size())
	        				lastK = k++;
	        			lastI = i;
	        			continue point;
	        		}
	        	}
	        }
        }
       return sentences;
    }*/
	
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
		
		for ( PreSentence sentence : sentences)
		{
			PreSentence conditionedSentence 	= new PreSentence(); // contains sentence without any stopwords
			ArrayList<Word> conditionedWords 	= new ArrayList<Word>(); // words that are not stopwords
			
			for ( Word w : sentence.getWordList())
			{
				
				p.println("orgw: " + w.getWord());
				// Check if the word is a not a stopword etc.
				if ( checkConditionsForComplexity(w) ) 
				{
					String pos = null;
					if(getMainPOS(w) != null)
						pos = getMainPOS(w).toString();
					posList.add(pos);
					p.println("condWord: " + w.getWord());
					conditionedWords.add(w); // add if the word is not complex and not a stopword
				}
			}
			conditionedSentence.setWordList(conditionedWords); // set words list to trimmed sentnece
			condSentenceList.add(conditionedSentence); // add to list of sentences without any stopwords in it
			
			for ( PreSentence tempsent : condSentenceList) {
				for ( Word w : tempsent.getWordList()) {
					p.println("wts : " + w.getWord());
				}
			}
		}
		
		// Insert Babelfy API Code for disambiguation below this comment.
		for( PreSentence line : condSentenceList )
		{
			if(condSentenceList.size() == 0) {
				p.println("condSentenceList is 0 or null");
				System.exit(0);
			}
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
		tempSentList = updatedSentenceList;
		
		ArrayList<PreSentence> converted = new ArrayList<PreSentence>();
		for(PreSentence orig : sentences){
			PreSentence temps = new PreSentence();
			for(PreSentence temp: tempSentList ){
				temps = compareBabelToOriginalSentence(orig, temp);
				converted.add(temps);
			}
			
		}
				
		sentences = converted;
		
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
		Babelfy bfy 				= Babelfy.getInstance(AccessType.ONLINE);
		
		Annotation annotations = bfy.babelfy("", inputText, Matching.EXACT, Language.EN);
		
		for ( BabelSynsetAnchor annotation : annotations.getAnnotations() )
		{
			String temp 	= "";
			String anchor 	= "";
			Word babelWord 	= new Word();
			
			anchor = annotation.getAnchorText();
			p.println("anchor: " + anchor);
			babelWord.setWord( anchor );
			
			BabelSynset synset 			= annotation.getBabelSynset();
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
				if(word.getWord().equals(babel.getWord())) {
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
