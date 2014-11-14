/*
 * Author: Laurenz Tolentino
 * Description: this, i forgot what this thing does so i am sorry huhu
 * */
package preprocess;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import objects.PreSentence;
import objects.Word;
import shortcuts.Print;
import edu.mit.jmwe.data.IMWE;
import edu.mit.jmwe.data.IToken;
import edu.mit.jmwe.data.Token;
import edu.mit.jmwe.detect.*;
import edu.mit.jmwe.index.IMWEIndex;
import edu.mit.jmwe.index.MWEIndex;


public class Jmwe {

	static Print p = new Print();

	public Jmwe()
	{

	}

	public static void main(String[] args)
	{
		
		p.println("Running Jmwe.java!!");
		Jmwe jm = new Jmwe();
		try {
			jm.simpleDetectorExample();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		Jmwe main = new Jmwe();
		try {
			main.simpleDetectorExample();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			p.println("\n Problem with the program. Sorry about that. huhu");
		}
		*/
	}

	public ArrayList<PreSentence> ApplyMweDetector(ArrayList<PreSentence> sentences) throws IOException
	{
		String mwesFound 	= "";			
		File idxData 		= getMWEIndexDataFile();
		IMWEIndex index 	= new MWEIndex(idxData);
		index.open();
		
		// make a basic detectors
		IMWEDetector detector = new Consecutive(index);
		List<List<IToken>> MWESentences = new ArrayList<List<IToken>>();
		List<IToken> sentence = null; 
		// add the words to the sentence
		for(int sentenceIndex = 0; sentenceIndex < sentences.size(); sentenceIndex++){
			PreSentence presentence = sentences.get(sentenceIndex);
			sentence = new ArrayList<IToken>();
			for(Word word : presentence.getWordList()) {
				sentence.add( new Token (word.getWord(), word.getPartOfSpeech()));
			}
			List<IMWE<IToken>> mwes = detector.detect(sentence);
			
			System.out.println("Starting word merges: ");
			// merges together all words in the 'sentences' list that are MWEs
			for(IMWE<IToken> mwe : mwes){
				
				List<IToken> tokens = mwe.getTokens();
				//first token is only used
				IToken token = tokens.get(0);
				
				ArrayList<Word> tempSentence = presentence.getWordList();
				
				//index where the equal word of the first token is found 
				int headIndex = 0;
				
				for(int i = headIndex ; i < tempSentence.size(); i++ ){
						if(tempSentence.get(i).getWord().equalsIgnoreCase(token.getForm())){
							
							System.out.println("Merging word group: "+tempSentence.get(i).getWord()+ " " + token.getForm());
							
							int addIndex = 1;
							if(isTokensEqualToIndex(tempSentence,tokens, i)){
								
								// change part of speech of the first token to the pos of the MWE
								tempSentence.get(i).setPartOfSpeech(""+mwe.getEntry().getPOS().getIdentifier());
								
								//append the succeeding words to the first token
								for(int tokenNum = 0 ; tokenNum < tokens.size()-1; tokenNum++){
									tempSentence.get(i).appendWord(tempSentence.get(i+addIndex).getWord());
									addIndex++;
								}
								tempSentence.get(i).setLemma(tempSentence.get(i).getWord());
								
								//remove the words that have already been merged to the first token
								for(int d = i+1; d <= i+(tokens.size()-1); d++){
									tempSentence.remove(i+1);
								}
								
							}
							
							//to start off where the last set of tokens left
							if((i+tokens.size()) <= tempSentence.size())
								headIndex = i+tokens.size();
							
							break;
						}
					
				}					
					
				presentence.setWordList(tempSentence);

			}

		}
		
		return sentences;
	}
	
	public boolean isTokensEqualToIndex(ArrayList<Word> words, List<IToken> tokens, int index){
		
		for(int i = index; i < (index + tokens.size()); i++){
			System.out.println("isTokensEqualToIndex: "+words.get(i).getWord()+" "+tokens.get(i-index).getForm());
			if(!words.get(i).getWord().equalsIgnoreCase(tokens.get(i-index).getForm())){
				return false;
			}
		}
		return true;
		
		
	}
	
	

	public void simpleDetectorExample() throws IOException
	{
		// get handle to file containing MWE indexdatam
		// e.g., mweindex_wordnet3.0
		File idxData = getMWEIndexDataFile();
		IMWEIndex index = new MWEIndex(idxData);
		index.open();

		// make a basic detector
		IMWEDetector detector = new Consecutive(index);

		// construct a test sentence:
		// "She looked up the world record."
		List <IToken> sentence = new ArrayList<IToken>();
		/*
		sentence.add(new Token ("She", 		"PRP"));
		sentence.add(new Token ("filed", 	"VBD", "file"));
		sentence.add(new Token ("a", 		"DT"));
		sentence.add(new Token ("case", 	"NN"));
		sentence.add(new Token ("in", 		"IN"));
		sentence.add(new Token ("the", 		"DT"));
		sentence.add(new Token ("supreme", 	"JJ"));
		sentence.add(new Token ("court", 	"NN"));
		sentence.add(new Token (".", 	"."));
		*/
		sentence.add(new Token ("It", 		"PRP"));
		sentence.add(new Token ("constitutes", 	"VBZ"));
		sentence.add(new Token ("the", 		"DT"));
		sentence.add(new Token ("actus", 	"NN"));
		sentence.add(new Token ("reus", 		"NN"));
		sentence.add(new Token ("of", 		"IN"));
		sentence.add(new Token ("the", 	"DT"));
		sentence.add(new Token ("crime", 	"NN"));
		sentence.add(new Token (".", 	"."));

		// run detector and print out results
		List<IMWE<IToken>> mwes = detector.detect(sentence);
		for(IMWE<IToken> mwe : mwes) {
			p.println("mwe: " + mwe.getEntry() + " " + mwe.getEntry().getPOS().getIdentifier());
		}
	}

	private File getMWEIndexDataFile() {
		//File file = new File("src/jwme_test/mweindex_wordnet3.0_Semcor1.6.data");
		File file 	= new File("src/documents/mweindex_wordnet3.0_Semcor1.6.data");
		return file;
	}
	
	
}
