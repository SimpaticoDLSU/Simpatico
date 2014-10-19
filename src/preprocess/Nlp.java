package preprocess;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import shortcuts.Print;
import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.dcoref.CorefCoreAnnotations.CorefChainAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.CoreMap;

public class Nlp {
	
	static Print p = new Print();
	static final String sampleLegalText = "Damage or destroy an electricity meter, equipment, "
			+ "wire or conduit or allow any of them to be so damaged or destroyed as to interfere with the proper or accurate metering of electric current, "
			+ "constitute the actus resus of the crime.";
	
	
	public Nlp()
	{
		// Temporary
	}
	
	public static void main(String[] args)
	{
		p.println("Running Nlp.java");
		Nlp nlp = new Nlp();
		nlp.TestNlp();
	}
	
	public Boolean StartNlp(String text)
	{
		
		/* Creates a StanfordCoreNLP Object, with POS Tagging, Lemmatization, NER, Parsing, and Corerefernce resolution*/
		Properties props = new Properties();
		props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		
		// read some text in the text variable 
		//String text = "She filed a case in the pambansang korte.";
		
		// Creates an empty Annotation just with the given text
		Annotation document = new Annotation(text);
		
		// run all Annotators on this text
		pipeline.annotate(document);
		
		// these are all the sentences in this document
		// a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
		List<CoreMap> sentences = document.get(SentencesAnnotation.class);
		
		for(CoreMap sentence: sentences)
		{
			//traversing the words in the current sentence
			// a CoreLabel is a CoreMap with additional token-specific methods
			
			for(CoreLabel token: sentence.get(TokensAnnotation.class))
			{
				// this is the text of the token
				String word	= token.get(TextAnnotation.class);
				p.println("word: " + word);
				// this is the POS tag of the token
				String pos	= token.get(PartOfSpeechAnnotation.class);
				p.println("pos: " + pos);
				// this is the NER label of the token
				String ne 	= token.get(NamedEntityTagAnnotation.class);
				p.println("ne: " + ne);
			}
			
			// this is the parse tree 
			Tree tree = sentence.get(TreeAnnotation.class);
			
			// this is the Stanford dependency graph of the current sentence
			SemanticGraph dependencies = sentence.get(CollapsedCCProcessedDependenciesAnnotation.class);
		}
		
		/*
		 * This is the coreference link graph
		 * Each chain stores a set of mentions that link to each other
		 * along with a method for getting the most representative mention
		 * Both sentence and token offsets start at 1!	
		 */
		Map<Integer, CorefChain> graph = document.get(CorefChainAnnotation.class);
		/*
		CorefChain test = graph.get(1);
		p.println("document: " + document.toString());
		p.println("Sample run: " + test.toString());
		p.println("end of run");
		*/
		return true;
	}
	
	public Boolean TestNlp()
	{
		return StartNlp(sampleLegalText);
	}
}
