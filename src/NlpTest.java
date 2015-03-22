import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;




import language.Word;
import simplenlg.features.Feature;
import simplenlg.features.Form;
import simplenlg.features.Tense;
import simplenlg.framework.InflectedWordElement;
import simplenlg.framework.LexicalCategory;
import simplenlg.framework.WordElement;
import simplenlg.lexicon.XMLLexicon;
import simplenlg.realiser.english.Realiser;
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
import edu.stanford.nlp.trees.TypedDependency;
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
    
    public List<String> lemmatizeWord(String word)
    {
        List<String> lemmas = new LinkedList<String>();

        // create an empty Annotation just with the given text
        Annotation document = new Annotation(word);

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
        
    	}
     
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
        SyntacticSubmodules submods = new SyntacticSubmodules(pipeline);
        
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
    static ArrayList<SemanticGraph> dependencies;
	ArrayList<Tree> tree;
    public List<String> coref(String documentText)
    {
        List<String> lemmas = new LinkedList<String>();

        // create an empty Annotation just with the given text
        Annotation document = new Annotation(documentText);

        // run all Annotators on this text
        this.pipeline.annotate(document);
        tree = new ArrayList<Tree>();
	    dependencies = new ArrayList<SemanticGraph>();
        List<CoreMap> sentences = document.get(SentencesAnnotation.class);
	    
	    for(CoreMap sentence: sentences) {
	      // traversing the words in the current sentence
	      // this is the parse tree of the current sentence
	      tree.add(sentence.get(TreeAnnotation.class));

	      // this is the Stanford dependency graph of the current sentence
	      dependencies.add(sentence.get(CollapsedDependenciesAnnotation.class));
	    }

	    // This is the coreference link graph
	    // Each chain stores a set of mentions that link to each other,
	    // along with a method for getting the most representative mention
	    // Both sentence and token offsets start at 1!
	   document.get(CorefChainAnnotation.class);

        return lemmas;
    }
    
    
    public static void main(String[] args){
    	NlpTest n = new NlpTest();
    	//n.scan(new File("src/lexical/Resources/lemmacorpus2.txt"));
    	n.test();
    }
    Map<String, String> map;
    public String getZipfValue(String word){
    	if(map == null){
    		map = new HashMap<String, String>();
    		File lemmacorpus = new File("C:/Users/juanito/OneDrive/Documents/zipf.csv");
    		String[] split;
    		try (BufferedReader reader = new BufferedReader(new FileReader(lemmacorpus.getAbsolutePath()))) 
    		{
    		    String line = null;
    		    int sum = 0;
    		    while ((line = reader.readLine()) != null) 
    		    {
    		        
    		       
    		        split  = line.split(",");
    		        
    	        	String w = split[0];
    	        	String zipf = split[1];
    	        	
    	        	map.put(w, zipf);
    			        
    		    }
    		    
    		    
    		   
    		} catch (IOException x) 
    		{
    		    System.err.format("IOException: %s%n", x);
    		} 
    	}else{
    		Iterator iterator = map.entrySet().iterator();
    		String s  =map.get(word);
    		if(s != null)
    			return s;
    		else
    			return null;
    	    /*while(iterator.hasNext()){
    	    	Map.Entry<String, String> pairs =(Map.Entry<String, String>)iterator.next();
    	    	if(pairs.getKey().equalsIgnoreCase(word))
    	    		return pairs.getValue();
    	    	
    	    		
    	    }*/
    		
    	}
    	return null;
    }
    
    public void scan(File corpus)
	{
		BufferedWriter writer=null;
		try {
			writer = new BufferedWriter(new FileWriter(new File("zipfresult2.csv")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try (BufferedReader reader = new BufferedReader(new FileReader(corpus.getAbsolutePath()))) 
		{
		    String line = null;
		    String[] splitted;
		    ArrayList<Word> words;
		    Scanner scanner = null;
		    int curr = 0;
		    int count = 1;
		    int freq;
		    while ((line = reader.readLine()) != null) 
		    {	
		    	splitted = line.split(" ");
		    	freq = Integer.parseInt(splitted[1]);
		    	/*
		    	if(curr != freq){
		    		curr = freq;
		    		//System.out.println("Frequency: "+curr+" Count: "+count);
		    		writer.write(curr+","+count+"\n");
		    		count = 1;
		    		
		    	}else
		    		count++;
		    		
		    	
		    	*/
	           
	           	 //writer.write("Word: "+splitted[0]+" Count: "+splitted[1]+" Zipf: "+getZipfValue(splitted[0])+"\n");
	           	writer.write(splitted[0]+","+splitted[1]+","+getZipfValue(splitted[0])+"\n");
	           	//writer.write(splitted[0]+","+splitted[1]+"\n");
		    	
		    	//System.out.println("Word: "+splitted[0]+" Count: "+splitted[1]+" Zipf: "+getZipfValue(splitted[0]));
		    }
		    writer.close();
		   
		} catch (IOException x) 
		{
		    System.err.format("IOException: %s%n", x);
		} 
		
		
		
		
	}
    
    public void execute(){
    	
        
    	coref("A Certificate of Registration shall be issued to those who are registered after the payment of fees are prescribed by the Commission.");
    	System.out.println(dependencies);
    	
    	for(SemanticGraph g : dependencies){
    			if(isPassiveVoice(g)){
    				System.out.println("is passive");
    			}else
    				System.out.println("not passive");
    			
    			System.out.println(g.getChildList(g.getFirstRoot()));
    			System.out.println(g.getChildList(g.getFirstRoot()).get(0).tag());
    			System.out.println(g.getChildList(g.getFirstRoot()).get(0).index());
    			System.out.println(g.getChildList(g.getFirstRoot()).get(0).word());
    			System.out.println("toposlist: "+g.toPOSList());
    			Tree  t = tree.get(0);
    			System.out.println(t.getLeaves().get(0).label());
    			Tree res = toPassive(g,t);
    
    				System.out.println(res.getLeaves());
    			
    	
    	}
    	
    }
    
    
    public boolean isPassiveVoice(SemanticGraph g){
    	String relation;
    	String relation2;
    	for(TypedDependency d : g.typedDependencies()){
    		relation = d.reln().toString();
			
			if(relation.equals("agent")){
				//System.out.println("Agent found: gov - "+d.gov() +" dep - "+d.dep());
				for(TypedDependency d2: g.typedDependencies()){
					relation2 = d2.reln().toString();
					if(relation2.equals("nsubjpass") && d2.gov().toString().equals(d.gov().toString())){
						//System.out.println("equal gov: "+d2.gov());
						return true;
					}
				}
			}
				
		}
    	return false;
    }
    public boolean hasEqualLeaves(Tree t1, Tree t2){
    	if(t1.value().equals(t2.value())){
    		List<Tree> t1Leaves = t1.getLeaves();
    		List<Tree> t2Leaves = t2.getLeaves();
    		
    		if(t1Leaves.size() == t2Leaves.size())
    			for(int i = 0; i < t1Leaves.size(); i++){
    				if(!t1Leaves.get(i).value().equals(t2Leaves.get(i).value())){
    					//System.out.println("nottrue "+ t1Children+" "+t2Children);
    					return false;
    				}
    				
    			}
    		else
    			return false;
    	}else
    		return false;
    	
    	return true;
    	
    }
    
    
    
    public Tree replaceNodeEqualTo(Tree mainTree, Tree delTree, Tree root, Tree replacement) {
        Tree[] children = mainTree.children();
        Tree returnNode = null;
        
	        for (Tree child : children) {
	        	//System.out.println("child: "+ child);
	        	//System.out.println("delTree: "+ delTree);
	        	if(hasEqualLeaves(child, delTree)){
	        		System.out.println("found "+ child);
	        		int index = mainTree.objectIndexOf(child);
	        		if(replacement != null)
	        			mainTree.setChild(index, replacement);
	        		else
	        			mainTree.removeChild(index);
	        		//System.out.println("deleted result "+ mainTree);
	        		//System.out.println("deleted result "+ root);
	        		return root;
	        		
	        	}else{
	        		Tree temp;
	        		if((temp=replaceNodeEqualTo(child, delTree, root, replacement)) == null)
	        			continue;
	        		else
	        			return temp;
	        	}
	         
	        }
        
        //System.out.println("return node: " + returnNode);
        return returnNode;
        
    }
    
    public String changeTense(String word, String POS) {
    	 
        XMLLexicon lexicon = new XMLLexicon("Imports/SimpleNLGResources/default-lexicon.xml");
        WordElement wordElement = lexicon.getWord(word, LexicalCategory.VERB);
        InflectedWordElement infl = new InflectedWordElement(wordElement);
 
        switch (POS) {
            //Past Tense
            case "VBD":
                infl.setFeature(Feature.TENSE, Tense.PAST);
                break;
            //Gerund / Present Participle
            case "VBG":
                infl.setFeature(Feature.FORM, Form.PRESENT_PARTICIPLE);
                break;
            //Past Participle
            case "VBN":
                infl.setFeature(Feature.FORM, Form.PAST_PARTICIPLE);
                break;
            //Present
            case "VBP":
                infl.setFeature(Feature.TENSE, Tense.PRESENT);
                break;
            //Present 3rd Person
            case "VBZ":
                infl.setFeature(Feature.FORM, Form.GERUND);
 
        }
 
        Realiser realiser = new Realiser(lexicon);
        return realiser.realise(infl).getRealisation();
    }
    
    public boolean treeHasLabel(Tree tree, String label){
    	for(Tree subtree: tree.subTreeList()){
    		if(label.equals(subtree.label().toString()))
    			return true;
    	}
    	return false;
    }
    
    @SuppressWarnings("serial")
	public Tree toPassive(SemanticGraph graph, Tree tree){
    	for(Tree subtree: tree.subTreeList()){
    		if(subtree.value().equals("S")){
    			boolean found = false;
    			
    			Tree verbPhrase = null;
    			Tree nounPhrase = null;
    			
    			//check if a both nounphrase and verbphrase next to each other exist in the subtree
    			List<Tree> children =  subtree.getChildrenAsList();
    			Tree child;
    			for(int i = 0; i < children.size(); i++ ){
    				child = children.get(i);
    				if(child.value().equals("NP")){
    					if((i+1) <= children.size())
    						if(children.get(i+1).value().equals("VP")){
    							nounPhrase = child;
    							verbPhrase = children.get(i+1);
    							found = true;
    						}
    				}
    			}
    			
    			System.out.println(nounPhrase);
    			System.out.println(verbPhrase);
    			if(found == true){
    				String relation;
    			
    				String agentGov = "";
    				String agentDep = "";
    				String passGov = "";
    				String passDep = "";
    				String auxDep = "";
    				//check if certain dependencies exist
    				for(TypedDependency d : graph.typedDependencies()){
    		    		relation = d.reln().toString();
    					
    					if(relation.equals("agent")){
    						System.out.println("Agent found: gov - "+d.gov().toString() +" dep - "+d.dep());
    						agentGov = d.gov().toString();
							agentDep = d.dep().toString();
							for(TypedDependency d2: graph.typedDependencies()){
								relation = d2.reln().toString();
								if(relation.equals("nsubjpass") && d2.gov().toString().equals(agentGov)){
									System.out.println("equal gov: "+d2.gov());
									passGov = d2.gov().toString();
									passDep = d2.dep().toString();
									continue;
								}
								
								if(relation.equals("auxpass") && d2.gov().toString().equals(agentGov)){
									System.out.println("equal auxDep: "+d2.dep());
									auxDep = d2.dep().toString();
									continue;
								}
								
								
							}
    					}	
    				}
    				
    				//if all these dependencies exist in the sentence, then proceed
    				if(!agentGov.isEmpty() && !agentDep.isEmpty() && !passGov.isEmpty() && !auxDep.isEmpty() && treeHasLabel(nounPhrase, passDep)){
	    				Tree ppNode;
	    				Tree auxNode;
    					for(Tree leaf:tree.getLeaves()){
    						
	    					if(leaf.label().toString().equals(agentDep)){
	    						ppNode = leaf;
	    						//get nearest ancestor with PP value
	    						while(!ppNode.value().equals("PP")){
	    							System.out.println(ppNode.value());
	    							ppNode = ppNode.parent(tree);
	    						}
	    						
	    							//delete "by"
	    							subtree = replaceNodeEqualTo(subtree, ppNode.firstChild(), tree, null);
	    							//replace noun phrase with the prepositional phrase minus "by"
	    							subtree = replaceNodeEqualTo(subtree, nounPhrase, tree, ppNode.firstChild());
	    							//replace the prepositional phrase with the old noun phrase
	    							subtree = replaceNodeEqualTo(subtree, ppNode, tree, nounPhrase);
	    							
	    						}
	    						//System.out.println("is equal?: "+leaf.label()+" "+auxDep);
	    						//delete the auxiliary verb
	    						if(leaf.label().toString().equals(auxDep)){
	    							auxNode = leaf;
	    							
	    							//get nearest VP ancestor
		    						while(!auxNode.value().equals("VP")){
		    							System.out.println(auxNode.value());
		    							auxNode = auxNode.parent(tree);
		    						}
		    						//delete node
		    						subtree = replaceNodeEqualTo(subtree, auxNode.firstChild(), tree, null);
	    						}
	    						
	    						//Change tense of past participle to simple past tense
	    						if(leaf.label().toString().equals(passGov)){
	    							if(!leaf.parent(tree).value().equals("VBD")){
	    								leaf.parent(tree).setValue("VBD");
	    								
	    								//lemmatize the word
	    								leaf.setValue(changeTense(lemmatizeWord(leaf.value()).get(0), "VBD"));
		    						
	    							}
	    						}
	    							
	    						
	    					}
	    				}
    				}else
    					continue;
    			}
    		}
    		
    		return tree;
    			
    	}
    	
    }
