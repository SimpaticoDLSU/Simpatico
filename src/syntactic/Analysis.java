package syntactic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import preprocess.ReaderWrite;
import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.dcoref.CorefCoreAnnotations.CorefChainAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedDependenciesAnnotation;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.CoreMap;

public class Analysis {
	
	private Map<Integer, CorefChain> graph;
	private ArrayList<SemanticGraph> dependencies;
	private ArrayList<Tree> tree;

	public void StartAnalysis(String text, StanfordCoreNLP pipeline)
	{
		
		// creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution 
	    
	    tree = new ArrayList<Tree>();
	    dependencies = new ArrayList<SemanticGraph>();
	    // read some text in the text variable
	     // Add your text here!
	    
	    // create an empty Annotation just with the given text
	    Annotation document = new Annotation(text);
	    
	    // run all Annotators on this text
	    pipeline.annotate(document);
	    
	    // these are all the sentences in this document
	    // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
	    List<CoreMap> sentences = document.get(SentencesAnnotation.class);
	    
	    for(CoreMap sentence: sentences) {
	      // traversing the words in the current sentence
	      // this is the parse tree of the current sentence
	      tree.add(sentence.get(TreeAnnotation.class));

	      // this is the Stanford dependency graph of the current sentence
	     dependencies.add(sentence.get(CollapsedCCProcessedDependenciesAnnotation.class));
	    }

	    // This is the coreference link graph
	    // Each chain stores a set of mentions that link to each other,
	    // along with a method for getting the most representative mention
	    // Both sentence and token offsets start at 1!
	   graph = document.get(CorefChainAnnotation.class);
	}
	
	
	
	public  ArrayList<SemanticGraph> getSemanticGraph(){
		return this.dependencies;
	}
	public  ArrayList<Tree> getTree(){
		return this.tree;
	}
	
	public  Map<Integer, CorefChain> getCoreferenceLinkGraph(){
		return this.graph;
	}
	
	public void applyRule(){
		
	}
	
}
