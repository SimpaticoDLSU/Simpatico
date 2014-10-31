import java.util.ArrayList;

import lexical.LexSubmodules;
import lexical.RankingChooser;
import lexical.SimplexAdapter;
import preprocess.Adapter;
import preprocess.PreSentence;
import preprocess.Word;



public class Main {
	public static void main(String[] args)
	{
		Adapter Adapter = new Adapter();
		LexSubmodules lexSubmodules = new LexSubmodules();
		SimplexAdapter adapter;
		RankingChooser rankingChooser = new RankingChooser();
		ArrayList<PreSentence> sentenceList = new ArrayList<PreSentence>();
		
		//perform preprocessing. If returned list is empty then abort.
		if(!(sentenceList = Adapter.ConvertToSentenceList("src/preprocess/NlpOutput.txt")).isEmpty())
		{
			for(PreSentence sentence : sentenceList)
			{
				for(Word word:sentence.getWordList())
				{
					//determine if each word is complex or not
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
				System.out.print(w.getLemma()+" ");
			}
		}
		
		
		
	}
	

}

