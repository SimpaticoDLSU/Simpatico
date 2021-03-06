package lexical;

import it.uniroma1.lcl.babelfy.Babelfy;
import it.uniroma1.lcl.babelfy.Babelfy.AccessType;
import it.uniroma1.lcl.babelfy.Babelfy.Matching;
import it.uniroma1.lcl.babelfy.data.Annotation;
import it.uniroma1.lcl.babelfy.data.BabelSynsetAnchor;
import it.uniroma1.lcl.babelnet.BabelSynset;
import it.uniroma1.lcl.jlt.util.Language;
import it.uniroma1.lcl.jlt.wordnet.WordNet;

import java.util.List;

import edu.mit.jwi.item.ISynset;

public class BabelfyConnector {
	public static void main(String[] args) throws Exception 
	{
		WordNet wn = WordNet.getInstance();
		Babelfy bfy = Babelfy.getInstance(AccessType.ONLINE);
		String inputText = "It constitutes the actus reus of the crime.";
		Annotation annotations = bfy.babelfy("", inputText,
			Matching.EXACT, Language.EN);
		
		System.out.println("inputText: "+inputText+"\nannotations:");
		
		for(BabelSynsetAnchor annotation : annotations.getAnnotations())
		{
			System.out.println("OTEXT: "+annotation.getAnchorText()+"\t"+
					"id: : "+annotation.getBabelSynset().getId()+"\t"+
					"babsyn: "+annotation.getBabelSynset()+"\t"
				
					//"getmainsense: "+annotation.getBabelSynset().getMainSense()+"\t"+
					//"getsenses: "+annotation.getBabelSynset().getSynsetSource()+"\t"+
					//"gettrans: "+annotation.getBabelSynset().getTranslations()+"\t"
				);
			
			BabelSynset synset = annotation.getBabelSynset();
			List<String> wordnetOffsets = synset.getWordNetOffsets();

			System.out.println("Wordnet offsets: " + wordnetOffsets.toString());

			for (String offset : wordnetOffsets)
			{
				System.out.println("offset single: " + offset);
				ISynset wnSynset = wn.getSynsetFromOffset(offset); // id
		    	if(wnSynset != null) 
		    	{
		    		System.out.print("OFFSETS:"+wnSynset.getOffset());
		    	}
			     // code logic here
			}
		}
		
		
	}
	
	// return ng int offset: must match sa word
}


