/*
 * Author: Laurenz Tolentino
 * Description: this, i forgot what this thing does so i am sorry huhu
 * */
package preprocess;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

	public String ApplyMweDetector(ArrayList<String> word, ArrayList<String> pos) throws IOException
	{
		String mwesFound 	= "";
		int tagPosition		= 0;			
		int arrayListSize	= word.size();
		File idxData 		= getMWEIndexDataFile();
		IMWEIndex index 	= new MWEIndex(idxData);
		index.open();
		p.println("Trace[ApplyMweDetector]: word: " + word.get(0).toString());
		// make a basic detectors
		IMWEDetector detector = new Consecutive(index);

		List<IToken> sentence = new ArrayList<IToken>();
		// add the words to the sentence
		for(int i = 0; i < arrayListSize; i++) {
			sentence.add( new Token (word.get(i), pos.get(i)));
		}
		
		List<IMWE<IToken>> mwes = detector.detect(sentence);
		
		for(IMWE<IToken> mwe : mwes) 
		{
			mwesFound = mwesFound + mwe + "\n";
		}
				
		return mwesFound;
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
		sentence.add(new Token ("She", 		"PRP"));
		sentence.add(new Token ("filed", 	"VBD", "file"));
		sentence.add(new Token ("a", 		"DT"));
		sentence.add(new Token ("case", 	"NN"));
		sentence.add(new Token ("in", 		"IN"));
		sentence.add(new Token ("the", 		"DT"));
		sentence.add(new Token ("supreme", 	"JJ"));
		sentence.add(new Token ("court", 	"NN"));
		sentence.add(new Token (".", 	"."));

		// run detector and print out results
		List<IMWE<IToken>> mwes = detector.detect(sentence);
		for(IMWE<IToken> mwe : mwes) {
			p.println("mwe: " + mwe);
		}
	}

	private File getMWEIndexDataFile() {
		//File file = new File("src/jwme_test/mweindex_wordnet3.0_Semcor1.6.data");
		File file 	= new File("/Users/laurenztolentino/Eclipse/workspace/Simpatico/src/preprocess/mweindex_wordnet3.0_Semcor1.6.data");
		return file;
	}
	
	
}
