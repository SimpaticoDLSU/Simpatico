import java.util.ArrayList;

import lexical.LexSubmodules;
import lexical.RankingChooser;
import lexical.SimplexAdapter;
import preprocess.Bridge;
import preprocess.Sentence;
import preprocess.Word;



public class Main {
	public static void main(String[] args)
	{
		Bridge bridge = new Bridge();
		LexSubmodules lexSubmodules = new LexSubmodules();
		SimplexAdapter adapter;
		RankingChooser rankingChooser = new RankingChooser();
		ArrayList<Sentence> sentenceList = new ArrayList<Sentence>();
		
		//perform preprocessing. If returned list is empty then abort.
		if(!(sentenceList = bridge.TestConnectNlpToJmwe()).isEmpty())
		{
			for(Sentence sentence : sentenceList)
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
			
			//convert sentenceList into a format that Simplex can understand
			adapter.translateToTxtFile();
			
			//run simplex
			adapter.runSimplex();
			
			//rank the substitute words and get the best substitute for each complex word
			rankingChooser.getWords(sentenceList);
			
			
			
		} //end if
		
		
		
		
		
	}
}
