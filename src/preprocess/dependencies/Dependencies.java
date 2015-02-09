package preprocess.dependencies;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import preprocess.Tester;
import language.PreSentence;
import language.Word;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.ling.CoreAnnotations.CommonWordsAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.Tokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreePrint;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import edu.stanford.nlp.trees.TypedDependency;
import edu.stanford.nlp.util.CoreMap;
import shortcuts.*;
import language.DependentPairs;
import language.Word;

public class Dependencies {

	private static Print p = new Print();
	
	/**
	 * Test class for the Stanford CoreNLP Dependencies
	 */
	public Dependencies () 
	{
		
	}
	
	/**
	 * Makes use of Lexicalized Dependencies Analysis from Parser.
	 * 
	 * @param sentence
	 * Input a string of sentence that will be used to analyze dependencies.
	 * @return
	 * An ArrayList of DependentPairs
	 */
	public ArrayList<DependentPairs> generateDependentPairsList(String sentence)
	{
		String parserModel = "edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz";
	    LexicalizedParser lp = LexicalizedParser.loadModel(parserModel);
	    
	    return generateDependencies(lp, sentence);
	}
	  
	/**
	 * Do not use this.
	 * Marked as private.
	 * Call getDependentPairsList instead.
	 * @param lp
	 * Generated by getDependentPairsList(String sentence)
	 * @param sentence
	 * @return
	 */
	private ArrayList<DependentPairs> generateDependencies(LexicalizedParser lp, String sentence)
	{
		ArrayList<DependentPairs> dpList = new ArrayList<DependentPairs>();
		String subj;
		String pointsTo;
		String relation;
	  
		// This option shows loading and using an explicit tokenizer
		TokenizerFactory<CoreLabel> tokenizerFactory = PTBTokenizer.factory(new CoreLabelTokenFactory(), "");
		
		Tokenizer<CoreLabel> tok = tokenizerFactory.getTokenizer(new StringReader(sentence));
		
		List<CoreLabel> rawWords2 = tok.tokenize();
		
		Tree parse = lp.apply(rawWords2);
		
		TreebankLanguagePack tlp = lp.treebankLanguagePack(); // PennTreebankLanguagePack for English
		GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
		GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
		List<TypedDependency> tdl = gs.typedDependenciesCCprocessed();
		System.out.println(tdl);
		System.out.println();
	  
		for ( TypedDependency temp : tdl )
		{
			DependentPairs dp;
			relation 	= temp.reln().toString();
			pointsTo	= temp.gov().value();
			subj		= temp.dep().value();
			// 	p.println("++" + subj + " " + pointsTo + " " + relation);
	
			dp = new DependentPairs(subj, pointsTo, relation, temp);
	  
			if ( !relation.equals("ROOT"))
			  dpList.add(dp);
		}
		return dpList;
	}
	
}