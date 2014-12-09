import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import syntactic.SyntacticSubmodules;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.CoreMap;

public class NlpTest {

    protected StanfordCoreNLP pipeline;
    private ArrayList<Tree> treeList;
    public NlpTest() {
        // Create StanfordCoreNLP object properties, with POS tagging
        // (required for lemmatization), and lemmatization
        Properties props;
        props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos,  lemma, ner, parse, dcoref");

        // StanfordCoreNLP loads a lot of models, so you probably
        // only want to do this once per execution
        this.pipeline = new StanfordCoreNLP(props);
    }

    public List<String> lemmatize(String documentText)
    {
        List<String> lemmas = new LinkedList<String>();

        // create an empty Annotation just with the given text
        Annotation document = new Annotation(documentText);

        // run all Annotators on this text
        this.pipeline.annotate(document);

        // Iterate over all of the sentences found
        List<CoreMap> sentences = document.get(SentencesAnnotation.class);
        for(CoreMap sentence: sentences) {
            // Iterate over all tokens in a sentence
            for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
                // Retrieve and add the lemma for each word into the list of lemmas
                lemmas.add(token.get(LemmaAnnotation.class));
            }
           
            // get the corenlp parse tree
            Tree tree = sentence.get(TreeAnnotation.class);
            
            treeList.add(tree);
           
                       
    	}

        return lemmas;
    }
    
    public void test(){
    	treeList = new ArrayList<Tree>();
    	lemmatize("The winds of Typhoon Ruby are so strong and the rain is very vicious.");
    	
    	 //instantiate syntactic module
        SyntacticSubmodules submods = new SyntacticSubmodules();
        
        //read the rules
        submods.readRules();
        
        for(Tree sentenceTree : treeList){
        	List<Tree> list = sentenceTree.subTreeList();
        	for(Tree tree : list){
        		System.out.println("Tree: "+tree.value()+" "+" LEAVES: "+tree.getLeaves());
        	}
        	//submods.applyRule();
        	
        }
        
        
    }
	
    public void traverseTree(Tree tree) {
        Tree[] children = tree.children();
       
        for(Tree child : children) {
            traverseTree(child);
            System.out.println("Depth:"+child.depth());
            System.out.println("Value:"+child.label());
           
            System.out.println("Children No.: "+child.numChildren());
            System.out.println("Children List: "+child.getChildrenAsList());
            System.out.println("LEAVES: "+child.getLeaves());
            
            
        }
        
        //if(head.depth(tree) == [val]) { add }
        
        
    }
    
    
    
    public void getDepthInfo(Tree tree) {
    	
    	for(Tree child : tree.children()) {
    		treeList.get(child.depth()).add(child);
    		System.out.println("Depth: "+child.depth()+" "+"LABEL: "+child.label().value());
    		for(Tree subchild:child.children()){
    			System.out.println("CHILD LABEL: "+subchild.label().value()+" "+"LEAVES: "+subchild.getLeaves());
    			if(subchild.parent() != null)
    				System.out.println("DADDY I FOUND YOU <3");
    		}
    		getDepthInfo(child);
           /*
            System.out.println("Depth:"+child.depth());
            System.out.println("Value:"+child.label());
           
            System.out.println("Children No.: "+child.numChildren());
            System.out.println("Children List: "+child.getChildrenAsList());
            System.out.println("LEAVES: "+child.getLeaves());
            */
            
        }
        
      
        
        
    }
    
    
    
    public static void main(String[] args){
    	NlpTest n = new NlpTest();
    
    	n.test();
    	
    }
}