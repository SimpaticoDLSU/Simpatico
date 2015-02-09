import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import syntactic.SyntacticSubmodules;
import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.dcoref.CorefChain.CorefMention;
import edu.stanford.nlp.dcoref.CorefCoreAnnotations.CorefChainAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedDependenciesAnnotation;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;
import edu.stanford.nlp.semgraph.SemanticGraphFactory;
import edu.stanford.nlp.trees.GrammaticalRelation;
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
        Map<Integer, CorefChain> coref = document.get(CorefChainAnnotation.class);

        for(Map.Entry<Integer, CorefChain> entry : coref.entrySet()) {
            CorefChain c = entry.getValue();

            //this is because it prints out a lot of self references which aren't that useful
            if(c.getMentionsInTextualOrder().size() <= 1)
                continue;

            CorefMention cm = c.getRepresentativeMention();
            String clust = "";
            List<CoreLabel> tks = document.get(SentencesAnnotation.class).get(cm.sentNum-1).get(TokensAnnotation.class);
            for(int i = cm.startIndex-1; i < cm.endIndex-1; i++)
                clust += tks.get(i).get(TextAnnotation.class) + " ";
            clust = clust.trim();
            System.out.println("representative mention: \"" + clust + "\" is mentioned by:");

            for(CorefMention m : c.getMentionsInTextualOrder()){
                String clust2 = "";
                tks = document.get(SentencesAnnotation.class).get(m.sentNum-1).get(TokensAnnotation.class);
                for(int i = m.startIndex-1; i < m.endIndex-1; i++)
                    clust2 += tks.get(i).get(TextAnnotation.class) + " ";
                clust2 = clust2.trim();
                //don't need the self mention
                if(clust.equals(clust2))
                    continue;

                System.out.println("\t" + clust2);
            }
        }
        System.out.println("dependencies");
        CoreMap sentence = (CoreMap) sentences.get(0);
        SemanticGraph dependencies1 = SemanticGraphFactory.generateUncollapsedDependencies(treeList.get(0));
              System.out.println("CollapsedDependenciesAnnotation ===>>");
              System.out.println("Sentence: "+sentence.toString());
              System.out.println("DEPENDENCIES: "+dependencies1.toList());
              System.out.println("DEPENDENCIES SIZE: "+dependencies1.size());
              
              int j=0;
        for(SemanticGraphEdge edge : dependencies1.edgeIterable())
        {
                  j++;
                  System.out.println("------EDGE DEPENDENCY: "+j);
                  IndexedWord dep = edge.getDependent();
                  String dependent = dep.word();
                  int dependent_index = dep.index();
                  IndexedWord gov = edge.getGovernor();
                  String governor = gov.word();
                  int governor_index = gov.index();
              GrammaticalRelation relation = edge.getRelation();
             System.out.println("No:"+j+" Relation: "+relation.toString()+" Dependent ID: "+dependent_index+" Dependent: "+dependent+" Governor ID: "+governor_index+" Governor: "+governor);

        }//end of for
        return lemmas;
    }
    
    public void test(){
    	treeList = new ArrayList<Tree>();
    	//lemmatize("The winds of Typhoon Ruby are so strong and the rain is very vicious.");
    	//lemmatize("The Secretary shall furnish the Secretary of Finance with a summary of the essential facts of the petition, "
    	//		+ "and the secretary shall request the latter to immediately inform the Commissioner of Customs.");
    	//lemmatize("The woman is my wife and she suddenly disappeared."); 
    	//lemmatize("There shall be an officer to be known as the secretary to the Governor-General, who shall be charged with the performance of such secretarial and administrative duties relating to the office of Governor-General, or the Executive Bureau, as shall be required of him by law or direction of the Governor-General."); 
    	lemmatize("The woman, who is my wife, suddenly disappeared."); 
    	
    	//instantiate syntactic module
        SyntacticSubmodules submods = new SyntacticSubmodules();
        
        //read the rules
        submods.readRules();
        System.out.println(treeList.size());
        for(Tree sentenceTree : treeList){
        	submods.checkRules(sentenceTree);
        	
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