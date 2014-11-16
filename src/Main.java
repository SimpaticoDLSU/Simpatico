import java.io.IOException;
import java.util.ArrayList;

import objects.PreSentence;
import objects.Word;
import lexical.LexSubmodules;
import lexical.RankingChooser;
import lexical.SimplexAdapter;
import preprocess.Adapter;
import preprocess.Extractor;
import preprocess.Nlp;
import preprocess.ReaderWrite;



public class Main {
	public static void main(String[] args)
	{	ReaderWrite rw = new ReaderWrite();
		Adapter ad = new Adapter();
		Nlp nlp = new Nlp(rw.testPathComplete);
		Extractor ex	= new Extractor();
		LexSubmodules lexSubmodules = new LexSubmodules();
		SimplexAdapter adapter;
		RankingChooser rankingChooser = new RankingChooser();
		ArrayList<PreSentence> sentenceList = new ArrayList<PreSentence>();
		
		
		//nlp.TestNlpFileGenerate();
		try {
			sentenceList = ad.NLPtoJMWE("NlpOutput.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		sentenceList = lexSubmodules.directSubstitution(sentenceList);
		sentenceList = lexSubmodules.identifyIgnorables(sentenceList);
		//perform preprocessing. If returned list is empty then abort.
		if(!sentenceList.isEmpty())
		{
			for(PreSentence sentence : sentenceList)
			{
				for(Word word:sentence.getWordList())
				{
					//determine if each word is complex or not
					if(!word.isStopWord() && word.getWordType() != Word.COMPOUND_WORD)
						word.setComplex(lexSubmodules.isComplex(word.getLemma()));
					
				}//end Word for
			}//end Sentence for
			
			//select candidates for each word
			sentenceList =	lexSubmodules.candidateSelection(sentenceList);
			adapter = new SimplexAdapter(sentenceList);
			
			//run simplex
			adapter.start();
						
			//rank the substitute words and get the best substitute for each complex word
			sentenceList = rankingChooser.getWords(sentenceList);
			
			
			
		} //end if
		System.out.println("Output:");
		for(PreSentence s: sentenceList){
			for(Word w: s.getWordList()){
				if(w.getBestSubstitute() != null)
					System.out.print(w.getBestSubstitute()+" ");
				else
					System.out.print(w.getWord()+" ");
			}
		}
		
		
		
	}
	

}

