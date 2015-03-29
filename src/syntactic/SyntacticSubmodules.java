package syntactic;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import language.SentenceType;
import simplenlg.features.Feature;
import simplenlg.features.Form;
import simplenlg.features.NumberAgreement;
import simplenlg.features.Tense;
import simplenlg.framework.InflectedWordElement;
import simplenlg.framework.LexicalCategory;
import simplenlg.framework.WordElement;
import simplenlg.lexicon.XMLLexicon;
import simplenlg.realiser.english.Realiser;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeGraphNode;
import edu.stanford.nlp.trees.TypedDependency;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.StringUtils;

public class SyntacticSubmodules {
	private static final int SIMPLIFICATION_THRESHOLD = 25;
	private  int COMPOUND = 0;
	private  int RELATIVE = 0;
	private  int APPOSITIVE = 0;
	private  int PASSIVE = 0;
    private StanfordCoreNLP pipeline;

    public SyntacticSubmodules(StanfordCoreNLP pipeline) {
  
        this.pipeline = pipeline;
    }
    public int getCOMPOUND() {
		return COMPOUND;
	}

	public void setCOMPOUND(int cOMPOUND) {
		COMPOUND = cOMPOUND;
	}

	public int getRELATIVE() {
		return RELATIVE;
	}

	public void setRELATIVE(int rELATIVE) {
		RELATIVE = rELATIVE;
	}

	public int getAPPOSITIVE() {
		return APPOSITIVE;
	}

	public void setAPPOSITIVE(int aPPOSITIVE) {
		APPOSITIVE = aPPOSITIVE;
	}

	public int getPASSIVE() {
		return PASSIVE;
	}

	public void setPASSIVE(int pASSIVE) {
		PASSIVE = pASSIVE;
	}
    
    public Tree setDependencies(Tree in, SemanticGraph dependencies) {
        ArrayList<SemanticGraphEdge> RCMODList = new ArrayList();
        ArrayList<SemanticGraphEdge> NSUBJList = new ArrayList();
        
        System.out.println(in.toString());

        for (SemanticGraphEdge edge : dependencies.edgeIterable()) {
            if (edge.getRelation().toString().equals("rcmod")) {
                RCMODList.add(edge);
            } else if (edge.getRelation().toString().equals("nsubj")) {
                NSUBJList.add(edge);
            }
        }//end of for

        for (SemanticGraphEdge rcmod : RCMODList) {
            for (SemanticGraphEdge nsubj : NSUBJList) {
                if (rcmod.getDependent().equals(nsubj.getGovernor())) {
                    Tree dependent, replacement;
                    dependent = replacement = null;
                    System.out.println("===");

                    for (Tree leaf : in.getLeaves()) {
                        System.out.println(leaf.label().toString() + " " + nsubj.getDependent().word() + "-" + nsubj.getDependent().index());
                        if (leaf.label().toString().equals(nsubj.getDependent().word() + "-" + nsubj.getDependent().index())) {
                            dependent = leaf;
                        } else if (leaf.label().toString().equals(rcmod.getGovernor().word() + "-" + rcmod.getGovernor().index())) {
                            replacement = leaf;
                        }
                    }

                    while (!replacement.value().equals("NP")) {
                        replacement = replacement.parent(in);
                    }

                    dependent.setChildren(replacement.getChildrenAsList());
                    dependent.setValue(replacement.value());
                    dependent.setLabel(replacement.label());
                    dependent.setScore(replacement.score());
                }
            }
            System.out.println(in.toString());
        }
        return in;
    }
    
    public void traverseTree(Tree tree) {
        Tree[] children = tree.children();

        for (Tree child : children) {
            traverseTree(child);
            System.out.println("Depth:" + child.depth());
            System.out.println("Value:" + child.label());

            System.out.println("Children No.: " + child.numChildren());
            System.out.println("Children List: " + child.getChildrenAsList());
            System.out.println("LEAVES: " + child.getLeaves());

        }

        //if(head.depth(tree) == [val]) { add }
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
