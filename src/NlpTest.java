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
import simplenlg.features.NumberAgreement;
import simplenlg.features.Tense;
import simplenlg.framework.InflectedWordElement;
import simplenlg.framework.LexicalCategory;
import simplenlg.framework.WordElement;
import simplenlg.lexicon.XMLLexicon;
import simplenlg.realiser.english.Realiser;
import syntactic.Analysis;
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
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedDependenciesAnnotation;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;
import edu.stanford.nlp.semgraph.SemanticGraphFactory;
import edu.stanford.nlp.trees.GrammaticalRelation;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.trees.TreeGraphNode;
import edu.stanford.nlp.trees.TypedDependency;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.StringUtils;

public class NlpTest {
	private static final int SIMPLIFICATION_THRESHOLD = 25;
    protected StanfordCoreNLP pipeline;
    private ArrayList<Tree> treeList;
    public  int COMPOUND = 0;
    public  int RELATIVE = 0;
    public  int APPOSITIVE = 0;
    public  int PASSIVE = 0;
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
        
      /*  //read the rules
        submods.readRules();
        System.out.println(treeList.size());
        for(Tree sentenceTree : treeList){
        	submods.checkRules(sentenceTree);
        	
        }
        */
        
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
	      dependencies.add(sentence.get(CollapsedCCProcessedDependenciesAnnotation.class));
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
    	n.execute();
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
    	
        
    	//coref("A Certificate of Registration shall be issued to those who are registered after the payment of fees are prescribed by the Commission.");
    	//coref("A local government unit that receives a grant under this Act  shall use such funds for programs that reduce the rate of juvenile students who are arrested and incarcerated for violations of school rules or policies, and any other activity that the Director determines will further the purpose stated in paragraph.");
    	//coref("Qualifying Examination System for Scoring Students refers to the appropriate annual examination and assessment of potential grantees, which may be separately designed for undergraduate and graduate students and adopted by the Board for all prospective applicants for any one of the modalities of StuFAP, as referred to in Section 17.");
    	//coref("Due to recent Federal Communications Commission interpretations and court decisions, these features of the Internet are no longer certain, and erosion of these historic policies permits broadband network owners to claim they can control who can and who cannot offer content and services over the Internet utilizing their broadband networks.");
    	//coref("The cat, who killed my dog with its huge pointed and bloody claws while I was watching television at my aunts house and eating some french fries with a great big bottle of ketchup, is now eating my honey.");
    	//coref("A Digital Infrastructure Fund , hereinafter came to to as Fund , in the amount of Fifty Billion Pesos is hereby appropriated as seed capital and national opposite number from the national government for the establishment of the ICT Hub : Provided , That the Fund shall be payed out in a pro-rata manner based on the following standards : telephone density , degree of digital divide , ICT penetration ratio , presence of new alternative backbone networks , science and technology parks and industrial estates with an ICT focus , among others . Subsequent appropriations shall be every year provided for in the General 12 Appropriations Act .");
    	//coref("All incumbent sangguniang kabataan officials shall remain in office unless sooner removed or suspended for cause until their successors shall have been elected and qualified. Provided, however, that all sangguniang kabataan officials who are ex officio members of the sangguniang bayan, sangguniang panlungsod or sangguniang panlalawigan as the case may be shall continue to serve as such members in the sanggunian concerned until the next sangguniang kabataan elections.");
    	//coref("The Department shall classify and/or re-classify all existing allowances, incentives and other benefits  currently being received by all government employees, including incumbents, into Base Pay,  Allowances and Other Pay, pursuant to the Total Compensation Framework, and pursuant to the true nature of such, notwithstanding its existing nomenclature. Except for those in Section 18 and 21 hereof, all other allowances, incentives, and benefits, being enjoyed by incumbents prior to the effectivity of this Act, which shall not be continued to be given as a separate amount, including ad hoc, provisional, tentative, or improvised benefits being received by government employees which are really intended to  provide some form of economic assistance, in acknowledgement of the inadequacy of compensation in government, shall be deemed integrated, subsumed, incorporated, and included  in the Base Pay Schedule as herein provided, without need of further adjustment of the amounts.");
    	//coref("PROVIDED, THAT THE APPROPRIATION, FOR THE PURPOSES OF THIS ACT, SHALL NOT BE REDUCED BY CONGRESS BELOW THE AMOUNT APPROPRIATED FOR THE PREVIOUS YEAR AND, AFTER APPROVAL, SHALL BE AUTOMATICALLY AND REGULARLY RELEASED; PROVIDED FURTHER, THAT THE APPROPRIATION HEREIN SHALL BE INCREASED BY AT LEAST TWENTY (20%) PER CENTUM ANNUALLY.");
    	//System.out.println(dependencies);
    	//coref("The Director shall establish one account to be used to pay the Federal administrative costs of carrying out this Act, and not more than a total of 7 percent of the funds appropriated under sections 210, 214, and 275 shall be placed in such account.");
    	File input = new File("E:/Documents/GitHub/input.txt");
    	BufferedWriter writer=null;
		int compound = 0;
		int relative = 0;
		int passive = 0;
		int appositive = 0;
		try {
			 writer = new BufferedWriter(new FileWriter(new File("E:/Documents/GitHub/result.txt")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try (BufferedReader reader = new BufferedReader(new FileReader(input.getAbsolutePath()))) 
		{
		    String line = null;
		  
		    while ((line = reader.readLine()) != null) 
		    {
		        
		       COMPOUND = 0;
		       RELATIVE = 0;
		       PASSIVE = 0;
		       APPOSITIVE = 0;
		       coref(line);
		       Analysis analysis = new Analysis();
		    	for(int i = 0; i < tree.size(); i++){
		    		
		    			SemanticGraph g = dependencies.get(i);
		    			String resultSentences = null;
		    			ArrayList<SemanticGraph> resultDependencies = null;
		    			ArrayList<Tree> resultTrees = null;
		    			System.out.println("EXECUTING COMPOUND");
		    			//split compound sentences
		    			resultSentences = splitCompound(g, tree.get(i));
		    			
		    			System.out.println("EXECUTING RELATIVE");
		    			System.out.println(resultSentences);
		    			
		    			//if sentence is compound and returned results
		    			
		    			
		    				
		    				//get the dependencies of the resulting sentences outputted by the splitting of compound sentences
		    				analysis.StartAnalysis(resultSentences, pipeline);
	    					resultDependencies = analysis.getSemanticGraph();
		    				resultTrees = analysis.getTree();
		    				resultSentences = "";
		    				//split each sentence in the compound results
		    				for(int k = 0; k < resultTrees.size(); k++){
		    					resultSentences+=splitRelative(resultDependencies.get(k), resultTrees.get(k));
		    				}
		    				

		    			
		    			
		    			
	    				
		    			System.out.println("EXECUTING APPOSITIVE");
		    			System.out.println(resultSentences);
		    			
		    			
		    			
		    				//get the dependencies of the resulting sentences outputted by the splitting of compound sentences
			    			analysis.StartAnalysis(resultSentences, pipeline);
	    					resultDependencies = analysis.getSemanticGraph();
		    				resultTrees = analysis.getTree();
		    				resultSentences = "";
		    				//split each sentence in the compound results
		    				for(int k = 0; k < resultTrees.size(); k++){
		    					resultSentences+=splitAppositive(resultDependencies.get(k), resultTrees.get(k));
		    				}
		    				
	    				
	    				
		    			
		    			
		    			System.out.println("EXECUTING PASSIVE");
		    			System.out.println(resultSentences);
		    			
		    				analysis.StartAnalysis(resultSentences, pipeline);
	    					resultDependencies = analysis.getSemanticGraph();
		    				resultTrees = analysis.getTree();
		    				resultSentences = "";
		    				for(int k = 0; k < resultTrees.size(); k++){
		    					resultSentences+=toPassive(resultDependencies.get(k), resultTrees.get(k));
		    				}
			    				
		    				
		    				
		    				writer.write(COMPOUND+","+RELATIVE+","+APPOSITIVE+","+PASSIVE+","+resultSentences+"\n");
		    			
		    				
		    			
		    			
		    			
		    			
		    		
		    	}
			        
		    }
		    
		    writer.close();
		   
		} catch (IOException x) 
		{
		    System.err.format("IOException: %s%n", x);
		} 
		
    	
    	/*	for(SemanticGraph g : dependencies){
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
    			toPassiveTree res = toPassive(g,t);
    
    				System.out.println(res.getLeaves());
    			
    	
    	}*/
    	/*for(SemanticGraph g : dependencies){
		
			
			System.out.println(g.getChildList(g.getFirstRoot()));
			System.out.println(g.getChildList(g.getFirstRoot()).get(0).tag());
			System.out.println(g.getChildList(g.getFirstRoot()).get(0).index());
			System.out.println(g.getChildList(g.getFirstRoot()).get(0).word());
			System.out.println("toposlist: "+g.toPOSList());
			for(Tree t : tree){
			System.out.println("EXECUTING COMPOUND");
			splitCompound(g, t);
			System.out.println("EXECUTING RELATIVE");
			splitRelative(g, t);
			System.out.println("EXECUTING PASSIVE");
			toPassive(g,t);
			}

				
			
	
    	}*/
    	
    	
    }
    
 
public void writeTree(Tree tree, BufferedWriter writer){
    	
		String output="";
    	try {
			
		
			for(Tree leaf:tree.getLeaves()){
				output+=(" "+leaf.value().toString());
			}
			
			
		writer.write(output+"\n");
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
    }
    
    public String treeToString(Tree tree){
		
		String output = "";
			
			for(Tree leaf:tree.getLeaves()){
				output+=(" "+leaf.value().toString());
			}
			System.out.println("treeToString output: "+output);
		return output;
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
        System.out.println("POS IS: "+POS);
        switch (POS) {
        	case "VB":
        		infl.setFeature(Feature.FORM, Form.BARE_INFINITIVE);
        		break;
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
	public String toPassive(SemanticGraph graph, Tree tree){
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
    					if((i+1) < children.size())
    						if(children.get(i+1).value().equals("VP")){
    							nounPhrase = child;
    							verbPhrase = children.get(i+1);
    							found = true;
    						}
    				}
    			}
    			
    			
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
    				
    				boolean isVisit1=false;
    				boolean isVisit2=false;
    				boolean isVisit3=false;
    				//if all these dependencies exist in the sentence, then proceed
    				if(!agentGov.isEmpty() && !agentDep.isEmpty() && !passGov.isEmpty() && !auxDep.isEmpty() && treeHasLabel(nounPhrase, passDep)){
	    				Tree ppNode;
	    				Tree auxNode;
    					for(Tree leaf:tree.getLeaves()){
    						isVisit1=false;
    	    				isVisit2=false;
    	    				isVisit3=false;
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
	    							isVisit1=true;
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
	    						isVisit2=true;
    						}
    						
    						//Change tense of past participle to simple past tense
    						if(leaf.label().toString().equals(passGov)){
    							if(!leaf.parent(tree).value().equals("VBD")){
    								leaf.parent(tree).setValue("VBD");
    								
    								//lemmatize the word
    								leaf.setValue(changeTense(lemmatizeWord(leaf.value()).get(0), "VBD"));
    								isVisit3 = true;
    							}
    						}
    						
	    						
	    							
	    						
	    				}
    					String output = "";
						ArrayList<Tree> list = new ArrayList<Tree>();
						list.add(tree);
						for(Tree cleanTree : cleanClauses(list)){
							
							for(Tree l:cleanTree.getLeaves()){
								output+=(" "+l.value().toString());
							}
							
						}
						System.out.println("Passive output: "+output);
						PASSIVE++;
						return output;
	    			}
				}else
					continue;
			}
		}//end for loop
	
	
	
		
		String output = "";
		System.out.println("Passive simplification not executed.");
		for(Tree leaf:tree.getLeaves()){
			output+=(" "+leaf.value().toString());
		}
		return output;
		
    			
}
    	
    

public String splitCompound(SemanticGraph graph, Tree tree){
	String relation;
	String rootDep = null;
	ArrayList<TypedDependency> conjunctions = new ArrayList<TypedDependency>();	
	//find root and conj relations and add them in a list
	for(TypedDependency d : graph.typedDependencies()){
		relation = d.reln().toString();
		//System.out.println(relation +" "+d.gov().toString()+" "+ d.dep().toString());
		if(relation.equalsIgnoreCase("root")){
			rootDep = d.dep().toString();
			//System.out.println("rootdep found: "+rootDep);
		}
		if(relation.contains("conj")){
			//System.out.println("conj found: "+d.reln());
			conjunctions.add(d);
		}
		
		
		
		
	}
	
	String targetGov = "";
	String targetDep = "";
	String targetConj = "";
	
	//find a conjunction with a governor equal to the dependent of the root relation
	for(int i = 0; i < conjunctions.size(); i++){
		TypedDependency d = conjunctions.get(i);
		if(d.gov().toString().equalsIgnoreCase(rootDep)){
			targetGov = d.gov().toString();
			targetDep = d.dep().toString();
			
			System.out.println("targets found: "+targetGov+" "+targetDep);
		}
	}
	Tree treeDep = null;
	Tree treeGov = null;
	
	for(Tree subtree : tree.getLeaves()){
		
		
		if(subtree.label().toString().equals(targetGov) ){
			Tree parent = subtree.parent(tree);
			System.out.println(subtree.value());
			while(parent != null){
				
				if(parent.value().equals("S") && !parent.parent(tree).value().equalsIgnoreCase("ROOT")){
					System.out.println("gov found: "+parent);
					//deep copy the S node and delete it in the original tree
					treeGov = parent.deepCopy();					
					
					break;
					
				}
				parent=parent.parent(tree);
			}
		}
		
	}
	
	//starting from the leaf node, traverse up the tree until the first S node is found. Then deep copy that node then delete.
	for(Tree subtree : tree.getLeaves()){
		
		if(subtree.label().toString().equals(targetDep)){
			Tree parent = subtree.parent(tree);
			System.out.println(subtree.value());
			while(parent != null){
				
				if(parent.value().equals("S") && !parent.parent(tree).value().equalsIgnoreCase("ROOT")){
					System.out.println("dep found: "+parent);
					//deep copy the S node and delete it in the original tree
					treeDep = parent.deepCopy();
					subtree = replaceNodeEqualTo(tree, parent, tree, null);
					
					break;
					
					
				}
				parent=parent.parent(tree);
			}
		}
		
		
	}
	
	
	if(treeDep != null && treeGov != null )
		for(TypedDependency d : graph.typedDependencies()){
			relation = d.reln().toString();
			if(relation.equalsIgnoreCase("neg")){
				if(d.gov().toString().equalsIgnoreCase(targetDep)){
					boolean isWithinS = false;
					for(Tree child:treeDep.getLeaves()){
						if(child.label().toString().equalsIgnoreCase(d.dep().toString()))
							isWithinS = true;
					}
					if(!isWithinS){
						Tree newTree = TreeGraphNode.factory().newLeaf(d.dep().toString().split("-")[0]);
						treeDep.addChild(0, newTree);
					}
				}
			}
			
		}
	
		ArrayList<Tree> trees  = new ArrayList<Tree>();
		
		String output = "";
		
		if(treeDep != null && treeGov != null){
			
			trees.add(tree);
			trees.add(treeDep);
			for(Tree cleanTree : cleanClauses(trees)){
				
				for(Tree leaf:cleanTree.getLeaves()){
					output+=(" "+leaf.value().toString());
				}
				
			}
			System.out.println("Compound output: "+output);
			COMPOUND++;
			return output;
			
		}else{
			System.out.println("Compound simplification not executed.");
			for(Tree leaf:tree.getLeaves()){
				output+=(" "+leaf.value().toString());
			}
			return output;
		}
	}
	public ArrayList<Tree> cleanClauses(ArrayList<Tree> treeList){
		List<Tree> leaves;
		for(Tree tree : treeList){
			leaves = tree.getLeaves();
			//convert the start of sentence to uppercase
			leaves.get(0).setValue(StringUtils.capitalize(leaves.get(0).value().toString()));
			
			
			
			
			//loop for fixing the end of the sentence, removing wrong punctuation
			String currLeafVal;
			
			int size = leaves.size()-1;
			Tree parent;
		/*	do{
				
				if((parent=leaves.get(i).parent(tree)) != null)
					currLeafVal = parent.value();
				else
					continue;
				System.out.println("currLeaf: "+currLeafVal);
				//if value of current leaf is a period, CC, or comma then delete the parent of the parent of (this is not a typo) the leaf
				if(currLeafVal.equalsIgnoreCase(".") || currLeafVal.equalsIgnoreCase("CC") || currLeafVal.equalsIgnoreCase(",")){
					System.out.println("Deleting "+currLeafVal);
					parent.parent(tree).removeChild(parent.parent(tree).objectIndexOf(parent));
				}
				i--;
				
				
			}while(parent.value().equalsIgnoreCase(".") || parent.value().equalsIgnoreCase("CC") || parent.value().equalsIgnoreCase(","));*/
			for(int i = size; i > 0; i--){
				if((parent=leaves.get(i).parent(tree)) != null)
					currLeafVal = parent.value();
				else
					continue;
				if(parent.value().equalsIgnoreCase(".") || parent.value().equalsIgnoreCase("CC") || parent.value().equalsIgnoreCase(",") || parent.value().equalsIgnoreCase("RB")){
					System.out.println("Deleting "+currLeafVal);
					parent.parent(tree).removeChild(parent.parent(tree).objectIndexOf(parent));
				}else
					break;
				
			}
			
			System.out.println("Last leaf value: "+leaves.get(leaves.size()-1).value().toString());
			//put a period in the end of the sentence 
			if(!tree.getLeaves().get(tree.getLeaves().size()-1).value().toString().equals(".")){
				Tree newTree = TreeGraphNode.factory().newLeaf(".");
				tree.addChild(newTree);
			}
			
			
			//loop for removing consecutive commas
			boolean prevLeafIsComma = false;
			Tree prevLeaf = null;
			for(Tree leaf : leaves){
				if(!leaf.value().equals(",")){
					prevLeafIsComma=false;
				}
				if(leaf.value().equals(",") && !prevLeafIsComma){
					prevLeafIsComma = true;
					prevLeaf = leaf;
				}else if(leaf.value().equals(",") && prevLeafIsComma && prevLeaf != null){
					Tree parentOfLeaf = leaf.parent(tree).parent(tree);
					Tree parentOfPrevLeaf = prevLeaf.parent(tree).parent(tree);
					try{
						System.out.println("Deleting double comma");
						parentOfPrevLeaf.removeChild(parentOfPrevLeaf.objectIndexOf(prevLeaf.parent(tree)));
						parentOfLeaf.removeChild(parentOfLeaf.objectIndexOf(leaf.parent(tree)));
					}catch(Exception e){
						e.printStackTrace();
					}
					
				}
			}
		
		}
		
		return treeList;
	}
	public Tree getTargetClause(String nodeValue, String targetNode, Tree tree, boolean delete){
		Tree foundTree = null;
		for(Tree subtree : tree.getLeaves()){
			
			if(subtree.label().toString().equals(nodeValue)){
				Tree parent = subtree.parent(tree);
				//System.out.println(subtree.value());
				while(parent != null){
					
					if(parent.value().equals(targetNode)){
						System.out.println("dep found: "+parent);
						//deep copy the S node and delete it in the original tree
						foundTree = parent;	
						if(delete)
							subtree = replaceNodeEqualTo(subtree, parent, tree, null);
						break;
						
					}
					parent=parent.parent(tree);
				}
			}
		}
		return foundTree;
	}
	
	
	public String splitRelative(SemanticGraph graph, Tree tree){
		String relation;
		String rootDep = null;
		ArrayList<Tree> splitTrees = new ArrayList<Tree>();
		
		
		for(TypedDependency d : graph.typedDependencies()){
			relation = d.reln().toString();
			Tree temp;
			Tree nounPhrase;
			
			if(relation.equalsIgnoreCase("rcmod")){
				rootDep = d.dep().toString();
				System.out.println("rcmod found: "+rootDep);
				temp = getTargetClause(d.dep().toString(), "SBAR", tree, false);
				if(temp != null && temp.getLeaves().size() >= SIMPLIFICATION_THRESHOLD){
					nounPhrase = getTargetClause(d.gov().toString(), "NP", tree, false);
					for(Tree child : temp.getChildrenAsList()){
						if(child.value().equals("WHNP")){
							tree = replaceNodeEqualTo(tree, temp, tree, null);
							temp.setChild(temp.objectIndexOf(child), nounPhrase);
							
							if(splitTrees.isEmpty())
								splitTrees.add(tree);
							splitTrees.add(temp);
						}
					}
				}
					
			}
		}
	
		
		
		
		
		String output="";
		if(!splitTrees.isEmpty()){
			
			for(Tree t : cleanClauses(splitTrees)){
				
				//System.out.println(t.getLeaves());
				for(Tree leaf:t.getLeaves()){
					output+=(" "+leaf.value().toString());
				}
				
			}
			
			System.out.println("Relative output: "+output);
			RELATIVE++;
			return output;
		}else{
			System.out.println("Relative simplification not executed.");
			for(Tree leaf:tree.getLeaves()){
				output+=(" "+leaf.value().toString());
			}
			return output;
		}
		
			
	}
	
	public String splitAppositive(SemanticGraph graph, Tree tree){
		String relation;
		String apposDep = null;
		String apposGov = null;
		ArrayList<Tree> splitTrees = new ArrayList<Tree>();
		for(TypedDependency d : graph.typedDependencies()){
			relation = d.reln().toString();

			//find the appositive relation
			if(relation.equalsIgnoreCase("appos")){
				apposDep = d.dep().toString();
				apposGov = d.gov().toString();
				System.out.println("appos found: "+apposDep);
				
				for(TypedDependency g : graph.typedDependencies()){
					//if a prepositional phrase or a relative clause modifies the dependent of the appositive then 
					if((g.reln().toString().equalsIgnoreCase("prep") || g.reln().toString().equalsIgnoreCase("rcmod")) && g.gov().toString().equalsIgnoreCase(apposDep)){
						//the nounphrase of the appositive
						Tree nounPhrase1 = getTargetClause(apposDep, "NP", tree, false);
						
						//get the parent of the nounphrase that the dependent belongs to
						Tree nounPhrase2 = nounPhrase1.parent(tree);
						
						if(nounPhrase2.getLeaves().size() >= SIMPLIFICATION_THRESHOLD){
							tree = replaceNodeEqualTo(tree, nounPhrase2, tree, null);
							String beVerb="";
							
							for(Tree leaf:tree.getLeaves()){
								
								if(apposGov.equalsIgnoreCase(leaf.label().toString())){
									
									beVerb = changeNumberAgreement("be", leaf.parent(tree).value().toString(), LexicalCategory.VERB);
								}
							}
							
							Tree nounPhraseSubj = getTargetClause(apposGov, "NP",tree,false);
							treeToString(nounPhraseSubj);
							Tree newTree = TreeGraphNode.factory().newLeaf(beVerb);
							nounPhrase2.addChild(0,newTree);
							newTree = TreeGraphNode.factory().newLeaf(treeToString(nounPhraseSubj));
							nounPhrase2.addChild(0,newTree);
							
							if(splitTrees.isEmpty())
								splitTrees.add(tree);
							splitTrees.add(nounPhrase2);
							break;
						}
					}
					
				}
				
					
			}//end if
			
		}//end for
		String output="";
		if(!splitTrees.isEmpty()){
			
			for(Tree t : cleanClauses(splitTrees)){
				
				//System.out.println(t.getLeaves());
				for(Tree leaf:t.getLeaves()){
					output+=(" "+leaf.value().toString());
				}
				
			}
			System.out.println("Appositive output: "+output);
			APPOSITIVE++;
			return output;
			
		}else{
			System.out.println("Appositive simplification not executed.");
			
				
			//System.out.println(t.getLeaves());
			for(Tree leaf:tree.getLeaves()){
				output+=(" "+leaf.value().toString());
			}
			System.out.println("Appositive output: "+output);
			return output;
		}
		
	}
	
	 public String changeNumberAgreement(String word, String POS, LexicalCategory cat) {
		 
	        XMLLexicon lexicon = new XMLLexicon("Imports/SimpleNLGResources/default-lexicon.xml");
	        WordElement wordElement = lexicon.getWord(word, cat);
	        InflectedWordElement infl = new InflectedWordElement(wordElement);
	 
	        switch (POS) {
	            //Noun singular/mass
	            case "NN":
	                infl.setFeature(Feature.NUMBER, NumberAgreement.SINGULAR);
	                break;
	            //Noun Plural
	            case "NNS": 
	                infl.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
	                break;
	            //Proper noun singular
	            case "NNP":
	                infl.setFeature(Feature.NUMBER, NumberAgreement.SINGULAR);
	                break;
	            //Proper noun Plural
	            case "NNPS":
	                infl.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
	                break;
	            
	        }
	 
	        Realiser realiser = new Realiser(lexicon);
	        return realiser.realise(infl).getRealisation();
	   }
	
}
