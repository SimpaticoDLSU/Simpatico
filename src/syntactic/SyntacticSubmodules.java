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
import edu.stanford.nlp.trees.TypedDependency;
import edu.stanford.nlp.util.CoreMap;

public class SyntacticSubmodules {
	//Text format
    //Properties File syntacticLabels.properties (ROOT OF PROJECT)
    //Rules per sentence.
    //[sentence type].txt (replace space with "_").
    //Rule format
    //1 rule per line
    // label1 label2 label3 ..... n > outputlabel1 outputlabel2 ..... n

    ///GET DEPTH
    //head = tree
    //for loop
    // if(head.dephth(node) == [specified val]
    // add
    public static final String COMPOUND = SentenceType.COMPOUND.toString();
    public static final String RELATIVE_CLAUSE = SentenceType.RELATIVE_CLAUSE.toString();
    private StanfordCoreNLP pipeline;

    public SyntacticSubmodules(StanfordCoreNLP pipeline) {
        compoundRuleList = new ArrayList<RuleItem>();
        relativeRuleList = new ArrayList<RuleItem>();
        this.pipeline = pipeline;
    }

    private ArrayList<RuleItem> compoundRuleList;
    private ArrayList<RuleItem> relativeRuleList;

    //represets each symbol in the rule
    class Node {

        private int index;
        private Node head;
        private Node parent;
        private String value;
        private int depth;
        private boolean isRootNode;
        private boolean isLastNode;
        private ArrayList<Node> children;
        private int siblingIndex;
        private Tree leaves;
        private boolean isCoreNLP;
        private String action;
        
      
        
        public boolean isCoreNLP() {
			return isCoreNLP;
		}

		public void isCoreNLP(boolean isCoreNLP) {
			this.isCoreNLP = isCoreNLP;
		}

		

        public void setAction(String action) {
            this.action = action;
        }

        public String getAction() {
            return this.action;
        }
        
        //duplicates everything except child and nlptree and parent
        public Node duplicate(){
        	Node duplicate = new Node();
        	duplicate.setAction(this.action);
        	duplicate.setSiblingIndex(this.siblingIndex);
        	duplicate.setLastNode(this.isLastNode);
        	duplicate.setRootNode(this.isRootNode);
        	duplicate.setDepth(this.depth);
        	duplicate.setValue(this.value);
        	duplicate.setHead(this.head);
        	duplicate.setIndex(this.index);
        	return duplicate;
        }
        
        public void setCoreNlpSubTree(Tree leaves) {
            this.leaves = leaves;
        }

        public int getDepth() {
            return depth;
        }

        public String print() {
            StringBuilder string = new StringBuilder();
            string.append(this.value + " ");
            string = buildStringTraverse(this, string);
            if (string != null) {
                return string.toString();
            } else {
                return toString();
            }

        }

        private StringBuilder buildStringTraverse(Node child, StringBuilder string) { // post order traversal
            if (child.getChildren().isEmpty()) {
                return null;
            } else {
                string.append(" ( ");
            }
            for (Node each : child.getChildren()) {
                string.append(each.getValue() + " ");
                buildStringTraverse(each, string);
            }
            string.append(" ) ");
            return string;
        }

        public boolean isRootNode() {
            return isRootNode;
        }

        public void setRootNode(boolean isRootNode) {
            this.isRootNode = isRootNode;
        }

        public boolean isLastNode() {
            return isLastNode;
        }

        public void setLastNode(boolean isLastNode) {
            this.isLastNode = isLastNode;
        }

        public int getSiblingIndex() {
            return siblingIndex;
        }

        public void setDepth(int depth) {
            this.depth = depth;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public Node getHead() {
            return head;
        }

        public void setHead(Node head) {
            this.head = head;
        }

        public Node getParent() {
            return parent;
        }

        public void setParent(Node parent) {
            this.parent = parent;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public ArrayList<Node> getChildren() {
        	if(children == null)
        		return children = new ArrayList<Node>();
            return children;
        }

        public void setChildren(ArrayList<Node> children) {
            this.children = children;
        }

        public Node skipRoot() {
            if (value.equals("ROOT")) {
                if (!children.isEmpty() || children != null) {
                    return children.get(0);
                } else {
                    System.err.println("Method skipRoot() used on Root-only tree!");
                    return null;
                }
            } else {
                System.err.println("Method skipRoot() used on non-root Node!");
                return null;
            }
        }
        
        public Node deleteChild(int index){
        	return this.children.remove(index);
        	
        }

        public void deleteChild(Node node){
        	for(Node child : children){
        		if(child.equals(node))
        			children.remove(child);
        	}
        }
        
        public int getSibligIndex() {
            return this.siblingIndex;
        }

        public void setSiblingIndex(int index) {
            this.siblingIndex = index;
        }

        public int getNumSiblings() {
            if (parent != null) {
                return parent.getChildren().size();
            } else {
                return 0;
            }
        }

        public Node getChild(int index) {
            return children.get(index);
        }

        public void addChild(Node node) {
            if (children == null) {
                children = new ArrayList<Node>();
            }
            children.add(node);
        }
        
        /**
         * determines if one Node is a subset of the other
         * @param subset 
         * Node that could be a subset
         * @param node
         * Node which the other subset node is compared to
         * @return
         * true if 'subset' is a subset or equal to 'node', otherwise false.
         */
        public boolean isSubsetOfNode(Node subset, Node node){
        	List<Node> subsetChildren = subset.getChildren();
        	List<Node> nodeChildren = node.getChildren();
        	Node child;
        	int indexLeftOff = 0;
            for (int i = 0; i < nodeChildren.size(); i++) {
                child = nodeChildren.get(i);
                if(child.getValue().equalsIgnoreCase(subsetChildren.get(indexLeftOff).getValue())){
                	indexLeftOff++;
                	if(indexLeftOff == subset.getChildren().size())
                		return true;
                }
            }
            
            return false;
        }
        
        public Tree getCoreNlpTree(){
        	return this.leaves;
        }
        
        @Override
        public String toString() {

            return this.value;

        }
    }

    //gets the children of a particular node depending on the current position of the cursor of the scanner
    public ArrayList<Node> getChildren(Scanner sc, Node head, Node parent, int depth) {
        String label;
        Node node = null;
        Node prevNode = null;
        int siblingIndex = 0;
        int d = depth;
        String[] split;
        ArrayList<Node> childList = new ArrayList<Node>();
        while (sc.hasNext()) {
            label = sc.next();
            if (node != null) {
                prevNode = node;
            }
            node = new Node();

            if (label.equals(")")) {
                if (sc.hasNext() == false) {
                    prevNode.setLastNode(true);
                }

                return childList;
            }

            if (label.equals("(")) {

                prevNode.setChildren(getChildren(sc, head, prevNode, ++depth));

            } else {
                node.setChildren(new ArrayList<Node>());
                node.setSiblingIndex(siblingIndex++);
                node.setHead(head);
                node.setDepth(d);
                node.setParent(parent);
                //System.out.println("Setting value: " + label + " Parent: " + parent.getValue() + " Depth: " + d + " SiblingIndex: " + (siblingIndex - 1));
                split = label.split("-");
                if (split.length == 3) {

                    node.setValue(split[0]);
                    node.setIndex(Integer.valueOf(split[1]));
                    node.setAction(split[2]);
                } else {
                    node.setValue(split[0]);
                    node.setIndex(0);
                    node.setAction("x");
                }
                node.setRootNode(false);
                if (sc.hasNext() == false) {
                    node.setLastNode(true);
                } else {
                    node.setLastNode(false);
                }

                childList.add(node);

            }

        }

        return childList;

    }

    //loads rules in their respective arraylists 
    public void readRules() {
        //Read the ruleList
        FileReader fileReader;
        BufferedReader bufferedReader;
        List<String> nodeList;
        try {
            for (SentenceType sentenceType : SentenceType.values()) {
                System.out.println(sentenceType.toString());
                fileReader = new FileReader("src/documents/rules/" + sentenceType.toString() + ".txt");
                bufferedReader = new BufferedReader(fileReader);
                Scanner scanner;
                String line;
                int depth;
                while ((line = bufferedReader.readLine()) != null) {
                    System.out.println("Reading Rule: " + line);
                    nodeList = new ArrayList();
                    RuleItem rule = new RuleItem();
                    String[] splittedRule = line.split(">");
                    nodeList.add(splittedRule[0]);
                    splittedRule = splittedRule[1].split("\\|");
                    nodeList.add(splittedRule[0]);
                    if (splittedRule.length == 2) {
                        nodeList.add(splittedRule[1]);
                    }
                    Node rootNode;

                    for (int i = 0; i < nodeList.size(); i++) {
                        depth = 0;
                        scanner = new Scanner(nodeList.get(i).trim());
                        rootNode = new Node();
                        rootNode.setValue("ROOT");
                        rootNode.setDepth(depth);
                        rootNode.setRootNode(true);
                        rootNode.setParent(null);
                        rootNode.setChildren(getChildren(scanner, rootNode, rootNode, ++depth));

                        System.out.println("==");
                        if (i == 0) {
                            rule.setLeftHandSide(rootNode);
                        } else if (i == 1) {
                            rule.setRightHandSideOrigin(rootNode);
                        } else if (i == 2) {
                            rule.setRightHandSideSplit(rootNode);
                        }
                    }

                    rule.setType(sentenceType.toString());
                    System.out.println("Adding Rule: " + rule.getLeftHandSide().print());
                    if (COMPOUND.equals(rule.getType())) {
                        compoundRuleList.add(rule);
                    } else if (RELATIVE_CLAUSE.equals(rule.getType())) {
                        relativeRuleList.add(rule);
                    } 
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Framework.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    //represents one rule
    private class RuleItem {
    	private String type;
        private Node leftHandSide;
        private Node rightHandSideOrigin;
        private Node rightHandSideSplit;
        
        public Node getRightHandSideOrigin() {
			return rightHandSideOrigin;
		}

		public void setRightHandSideOrigin(Node rightHandSideOrigin) {
			this.rightHandSideOrigin = rightHandSideOrigin;
		}

		public Node getRightHandSideSplit() {
			return rightHandSideSplit;
		}

		public void setRightHandSideSplit(Node rightHandSideSplit) {
			this.rightHandSideSplit = rightHandSideSplit;
		}

		
        public void setType(String type){
        	this.type = type;
        }
        
        public String getType(){
        	return this.type;
        }
        
        public Node getLeftHandSide() {
            return leftHandSide;
        }

        public void setLeftHandSide(Node leftHandSide) {
            this.leftHandSide = leftHandSide;
        }

        

    }

    public static void main(String[] args) {
        /*SyntacticSubmodules f = new SyntacticSubmodules();
        f.readRules();
        System.out.print(f.compoundRuleList.size());
        for (RuleItem n : f.compoundRuleList) {
            System.out.print("||");

            f.traverse(n.getLeftHandSide());

            f.traverse(n.getRightHandSideOrigin());
            f.traverse(n.getRightHandSideSplit());
        }*/

    }

    public void traverse(Node child) { // post order traversal
        if (child.getChildren().isEmpty()) {
            return;
        } else {
            System.out.print(" (");
            for (Node each : child.getChildren()) {
                System.out.print(each.getValue() + " ");
                traverse(each);
            }
            System.out.print(") ");
        }

    }

    public void checkRules(Tree sentenceTree) {
        for (SentenceType sentenceType : SentenceType.values()) {
            ArrayList<RuleItem> sentenceRules;
            String string = sentenceType.toString();
            if (COMPOUND.equals(string)) {
                sentenceRules = compoundRuleList;
            }  else if (RELATIVE_CLAUSE.equals(string)) {
                sentenceRules = relativeRuleList;
            }  else {
                sentenceRules = null;
            }
            
            //System.out.println(sentenceTree.flatten());
            for (RuleItem rule : sentenceRules) {
                
                System.out.println("Rule: " + rule.getLeftHandSide().print());
                
                List<Tree> subtrees = sentenceTree.subTreeList();
                //for each subtree
                for (Tree nlpNode : subtrees) {
                   //System.out.println(rule.leftHandSide.skipRoot().getValue()+" is equal" + nlpNode.value());
                    if (nlpNode.value().equalsIgnoreCase(rule.getLeftHandSide().skipRoot().getValue())) {
                        //System.out.println("outcompare: "+nlpNode.value() +" is equals "+ rule.getLeftHandSide().skipRoot().getValue() + " "+ "nodechildren: "+rule.getLeftHandSide().skipRoot().getChildren()+"nlpChildren: "+nlpNode.getChildrenAsList());
                        Node duplicateNode;
                    	if ((duplicateNode=isTreeEqual(nlpNode, rule.getLeftHandSide().skipRoot()) )!= null) {
                            System.out.println("inside with rule: " + rule.getLeftHandSide().print());
                            System.out.println("inside with sentence: "+nlpNode);
                            System.out.println("duplicateNode: " +duplicateNode.print());
                            //Get generated sentences
                            ArrayList<Node> output = applyRule(duplicateNode, rule);
                            
                            for (Node outputNode : output) {
                                System.out.println("\nOutput Sentence: ");
                                printLeaves(outputNode);
                            }
                            break;
                            //applyRule(rule, nlpNode.deepCopy());
                            //System.out.println(rule.getLeftHandSide().print()+"FUCKING EQUAL TO :"+nlpNode.getLeaves() + " "+nlpNode.value());
                        }

                    }
                }

            }

        }

    }

    public ArrayList<Node> applyRule(Node node, RuleItem rule) {
    	 System.out.println("initialNode: " + node.print());
        ArrayList<Node> sentences = getSplitSentence(node, null);
        Node removedNodes = removeMarkedNodes(node);

        if (rule.getType().equals(RELATIVE_CLAUSE)) {

        }

        System.out.println("Nodes removed: " + removedNodes.print());
        for (Node child : removedNodes.getChildren()) {
            System.out.print("Tree of " + child.getValue() + " " + child.getCoreNlpTree());
        }

        System.out.println("\n\n\n");

        System.out.println("Final Tree:");
        printLeaves(removedNodes);
        for (Node child : sentences) {
            System.out.println(child.print() + " Tree: " + child.getCoreNlpTree().getLeaves());
        }

        //Set sentences to have "S" as a parent node and "." as the end of the tree
        for (int i = 0; i < sentences.size(); i++) {
            Node newParent = new Node();
            newParent.setValue("S");
            newParent.addChild(sentences.get(i));
            Node periodNode = new Node();
            periodNode.setValue(".");
            newParent.addChild(periodNode);
            sentences.set(i, newParent);
        }

        ArrayList<Node> outputTrees = new ArrayList();

        Node output1 = transferTree(removedNodes, deepCopy(rule.getRightHandSideOrigin().skipRoot()));
        if (output1 == null) {
            output1 = transferTree(removedNodes, deepCopy(rule.getRightHandSideSplit().skipRoot()));
        }
        if (output1 != null) {
            outputTrees.add(output1);
        }
        printLeaves(output1);
        output1 = null;

        for (Node child : sentences) {
            System.out.println("\n======111======");
            output1 = transferTree(child, deepCopy(rule.getRightHandSideOrigin().skipRoot()));
            if (output1 == null) {
                System.out.println("NULL");
                if (rule.getRightHandSideSplit() != null) {
                    output1 = transferTree(child, deepCopy(rule.getRightHandSideSplit().skipRoot()));
                    if (output1 == null) {
                        System.out.println("NULL");
                    } else {
                        printLeaves(output1);
                    }
                }
            } else {
                printLeaves(output1);
            }

            if (output1 != null) {
                outputTrees.add(output1);
            }
        }

        return outputTrees;
    }
    
     /**
     *
     * @param in the node that will be used to copy elements from
     * @param out the node that will be used to compare with in
     * @return returns out with the copied elements from in returns null if at
     * least one element from out is missing from in
     */
    public Node transferTree(Node in, Node out) {
        System.out.println("Comparing: " + out.getValue());
        if (in.getValue().equals(out.getValue())) {
            System.out.println("Match found: " + out.getValue());
            if (out.getChildren().isEmpty()) {
                out.setChildren(in.getChildren());
            } else {
                ArrayList<Node> outChildren = out.getChildren();
                for (int i = 0; i < outChildren.size(); i++) {
                    boolean hasMatch = false;
                    Node outChild = outChildren.get(i);
                    for (Node inChild : in.getChildren()) {
                        Node temp = transferTree(inChild, outChild);
                        if (temp != null) {
                            outChildren.set(i, temp);
                            hasMatch = true;
                            break;
                        }
                    }
                    if (hasMatch) {
                        out.setChildren(outChildren);
                    } else {
                        return null;
                    }
                }
            }
            out.setCoreNlpSubTree(in.getCoreNlpTree());
        } else {
            return null;
        }
        return out;
    }

    /**
     *
     * @param node - Node to be deep copied
     * @return returns a 1 to 1 copy of the node
     */
    public Node deepCopy(Node node) {
        Node duplicate = new Node();
        duplicate.setAction(node.getAction());
        duplicate.setSiblingIndex(node.getSiblingIndex());
        duplicate.setLastNode(node.isLastNode());
        duplicate.setRootNode(node.isRootNode());
        duplicate.setDepth(node.getDepth());
        duplicate.setValue(node.getValue());
        duplicate.setHead(node.getHead());
        duplicate.setIndex(node.getIndex());
        duplicate.setCoreNlpSubTree(node.getCoreNlpTree());
        if (!node.getChildren().isEmpty()) {
            for (Node n : node.getChildren()) {
                deepCopy(n);
                duplicate.addChild(n);
            }
        }
        return duplicate;
    }
    
    /**
     * prints the leaves of the Node parameter
     * @param tree Node whose leaves are printed
     */
    public void printLeaves(Node tree){
    	
    	if(tree.getChildren().isEmpty()){
    		System.out.print("Leaves: " +tree.getCoreNlpTree().getLeaves()+" ");
    	}else{
	        for (Node child : tree.getChildren()) {
	        	printLeaves(child);
	            
	        }
    	}
    }
    
    /**
     * gets the Node with 't' (transfer) as an action
     * @param node 
     * Node to be searched
     * @return
     * returns Node if found, otherwise null.
     */
    public ArrayList<Node> getSplitSentence(Node node, ArrayList<Node> list) {
    	
	    	if(list == null)
	    		list = new ArrayList<Node>();
	    	
	    	if(node.isLastNode()){
	    		
	    		return list;
	    	}else{
		        for (Node child : node.getChildren()) {
		            
		        		list = getSplitSentence(child, list);
		        		if(!child.isCoreNLP())
			        		if(child.getAction().equals("t")){
				            	list.add(child);
				            } 
		        }
	    	}
    	
    	   return list;
        
    }
	public Node removeMarkedNodes(Node node) {
	    	Node temp = node.duplicate();
	    	
	    	System.out.println("temp val: "+temp.getValue());
	    	
	    	
		    	if(node.isLastNode() || node.getChildren().isEmpty() ){
		    		return node;
		    	}else{
			        for (Node child : node.getChildren()) {
			        	
		        		if(child.getAction().equals("x")){
		        			System.out.println("adding "+child.getValue()+" as child of temp with val of "+temp.getValue());
		        			child.setParent(temp);	
		        			child = removeMarkedNodes(child);
		        			temp.addChild(child);
			            	
			            } 
		        		
		        					        }
		    	}
		    	
		    	System.out.println("Size of children: "+node.getChildren().size()+" "+ temp.getChildren());
		    	if(node.getChildren().size() > 0 && temp.getChildren().isEmpty()){
		    		temp.setCoreNlpSubTree(node.getCoreNlpTree());
		    		System.out.println("war NA");
		    		for (Node child : node.getChildren()) {

		        		if(!child.isCoreNLP() && !child.getAction().equals("x")){
		        			
		        			for(int i = 0 ; i < temp.getCoreNlpTree().getChildrenAsList().size(); i++){
		        				if(temp.getCoreNlpTree().getChild(i).value().equalsIgnoreCase(child.getValue())){
		        					System.out.println("DELETED NA:"+temp.getCoreNlpTree().getChild(i).value());
		        					temp.getCoreNlpTree().removeChild(i);
		        					if(i > 0)	
		        						i--;
		        				}
		       					
		        			}
		        		}     		
		        		
	
			        }
		    		temp.setChildren(new ArrayList<Node>());
		    	}
	    	
	    	return temp;
        
    }
	
	
	
	public boolean hasNormalNodes(ArrayList<Node> nodes){
		for(Node node : nodes){
			if(node.getAction().equals("x"))
				return true;
		}
		return false;
	}

	/**
	 * checks if Node is a subtree of Tree and merges both
	 * @param nlptree A CoreNLP Tree
	 * @param node A Node object
	 * @return A copy of Tree as Node
	 */
    public Node isTreeEqual(Tree nlptree, Node node) {
    	
    	int ctr = 0;
    	List<Tree> nlpChildren = nlptree.getChildrenAsList();
    	ArrayList<Tree> sameChildren = getSameChildren(nlpChildren, node.getChildren());
        Node outputNode = node.duplicate();
        if(node.isRootNode()){
    		outputNode.setParent(null);
    		outputNode.setRootNode(true);
    	}
       
        if (sameChildren == null) {
        	System.out.println("null children");
            return null;
        } else if (sameChildren.isEmpty()) {
        	System.out.println("no same children");
            return null;
        } else {
            for (int i = 0; i < node.getChildren().size(); i++) {
            	while(!nlpChildren.get(ctr).equals(sameChildren.get(i))) {
                    Node nonRuleNode = new Node();
                    nonRuleNode.setCoreNlpSubTree(nlpChildren.get(ctr));
                    nonRuleNode.setParent(outputNode);
                    nonRuleNode.setValue(nlpChildren.get(ctr).value());
                    nonRuleNode.isCoreNLP(true);
                    nonRuleNode.setAction("x");
                    outputNode.addChild(nonRuleNode);
                    
                    ++ctr;
                }
                ++ctr;
                if (!(node.getChild(i).getChildren().isEmpty())) {
                	
                    Node child = isTreeEqual(sameChildren.get(i), node.getChild(i));
                    
                    if (child == null) {
                        return null;
                    } else {
                    	System.out.println("added child: "+child.getValue());
                    	System.out.println("where children are: "+child.getChildren());
                    	child.setParent(outputNode);
                    	child.isCoreNLP(false);
                        outputNode.addChild(child);
                        
                    }
                } else {
                	System.out.println("Added new node to "+node.getValue());
                	node.getChild(i).setParent(outputNode);
                	node.getChild(i).isCoreNLP(false);
                    outputNode.addChild(node.getChild(i));
                }
                System.out.println("added nlp: "+sameChildren.get(i).value() + " to " + outputNode.getChild(ctr-1).getValue());               
                outputNode.getChild(ctr-1).setCoreNlpSubTree(sameChildren.get(i));
            }
        }
        
        for (int k = ctr; k  < nlpChildren.size(); k++) {
        	 Node nonRuleNode = new Node();
             nonRuleNode.setCoreNlpSubTree(nlpChildren.get(ctr));
             nonRuleNode.setParent(outputNode);
             nonRuleNode.setValue(nlpChildren.get(ctr).value());
             nonRuleNode.setAction("x");
             nonRuleNode.isCoreNLP(true);
             outputNode.addChild(nonRuleNode);
        }
        return outputNode;
    }

    /**
     * compares a List of Trees to an ArrayList of Nodes then returns children who are similar and are in order.
     * @param childrenAsList List of Tree objects to be compared to the List of Nodes
     * @param children List of Node objects
     * @return ArrayList containing the similar children without altering the order in which they are found.
     */
    public ArrayList<Tree> getSameChildren(List<Tree> childrenAsList, ArrayList<Node> children) {
        //System.out.println("getsamechildren: "+childrenAsList+" "+children);
        ArrayList<Tree> listOfEqualNodes = new ArrayList<Tree>();
        Node child;
        Tree nlptree;
        int indexLeftOff = 0;
        for (int i = 0; i < children.size(); i++) {
            child = children.get(i);
            
            for (int k = indexLeftOff; k < childrenAsList.size(); k++) {
                nlptree = childrenAsList.get(k);
                if (child.getValue().equalsIgnoreCase(nlptree.value())) {
                    System.out.println("Equal: " + child.getValue() + " " + nlptree.value());
                    listOfEqualNodes.add(nlptree);
                    if (k < childrenAsList.size()) {
                        indexLeftOff = k++;
                    }
                    break;
                }
            }

        }
        //System.out.println("Size: "+listOfEqualNodes.size() +" node has: "+ children.size() + " " + (children.size() > 0?children.get(0).getValue():"" + " nlp has: "+childrenAsList.size()));
        //System.out.println("children of nlp: "+childrenAsList + " children of node: "+children);
        if (listOfEqualNodes.size() == children.size()) {
            return listOfEqualNodes;
        } else {
            return null;
        }

    }
    
    public void mergeNodes(Node node1, Node node2) {

        for (Node node2child : node2.getChildren()) {
            if (node2child.getValue().equalsIgnoreCase(node1.getValue())) {
            }
        }
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
    					if((i+1) < children.size())
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

    //Get Tree from text
    //Read all ruleList
    //Traverse each sentence?
    //Check each sentence for its type.
    //Apply if needed
}
