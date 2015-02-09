package syntactic;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import language.SentenceType;
import edu.stanford.nlp.trees.Tree;

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
    public static final String COMPOUND_COMPLEX = SentenceType.COMPOUND_COMPLEX.toString();
    public static final String RELATIVE_CLAUSE = SentenceType.RELATIVE_CLAUSE.toString();
    public static final String PASSIVE_ACTIVE = SentenceType.PASSIVE_ACTIVE.toString();

    public SyntacticSubmodules() {
        compoundRuleList = new ArrayList<RuleItem>();
        compoundComplexRuleList = new ArrayList<RuleItem>();
        passiveActiveRuleList = new ArrayList<RuleItem>();
        relativeRuleList = new ArrayList<RuleItem>();
    }

    private ArrayList<RuleItem> compoundRuleList;
    private ArrayList<RuleItem> compoundComplexRuleList;
    private ArrayList<RuleItem> passiveActiveRuleList;
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
            if(string != null)
            	return string.toString();
            else return toString();

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
                System.out.println("Setting value: " + label + " Parent: " + parent.getValue() + " Depth: " + d + " SiblingIndex: " + (siblingIndex - 1));
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
        List<String> nodeList = new ArrayList<String>();
        try {
            for (SentenceType sentenceType : SentenceType.values()) {
                System.out.println(sentenceType.toString());
                fileReader = new FileReader("src/documents/rules/" + sentenceType.toString() + ".txt");
                bufferedReader = new BufferedReader(fileReader);
                Scanner scanner;
                String line;
                int depth;
                while ((line = bufferedReader.readLine()) != null) {
                    RuleItem rule = new RuleItem();
                    String[] splittedRule = line.split(">");
                    nodeList.add(splittedRule[0]);
                    splittedRule = splittedRule[1].split("|");
                    nodeList.add(splittedRule[0]);
                    nodeList.add(splittedRule[1]);
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
                        if (i == 0) {
                            rule.setLeftHandSide(rootNode);
                        } else if (i == 1) {
                            rule.setRightHandSideOrigin(rootNode);
                        } else if (i == 2) {
                            rule.setRightHandSideSplit(rootNode);
                        }

                    }
                   
                    rule.setType(sentenceType.toString());
                    if (COMPOUND.equals(rule.getType())) {
                        compoundRuleList.add(rule);
                    } else if (COMPOUND_COMPLEX.equals(rule.getType())) {
                        compoundComplexRuleList.add(rule);
                    } else if (RELATIVE_CLAUSE.equals(rule.getType())) {
                        relativeRuleList.add(rule);
                    } else if (PASSIVE_ACTIVE.equals(rule.getType())) {
                        passiveActiveRuleList.add(rule);
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
        SyntacticSubmodules f = new SyntacticSubmodules();
        f.readRules();
        System.out.print(f.compoundRuleList.size());
        for (RuleItem n : f.compoundRuleList) {
            System.out.print("||");

            f.traverse(n.getLeftHandSide());

            f.traverse(n.getRightHandSideOrigin());
            f.traverse(n.getRightHandSideSplit());
        }

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
            } else if (COMPOUND_COMPLEX.equals(string)) {
                sentenceRules = compoundComplexRuleList;
            } else if (RELATIVE_CLAUSE.equals(string)) {
                sentenceRules = relativeRuleList;
            } else if (PASSIVE_ACTIVE.equals(string)) {
                sentenceRules = passiveActiveRuleList;
            } else {
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
                            applyRule(duplicateNode,rule);
                            // put regeneration module here
                            System.out.println("Simplification done.");
                            break;
                            //applyRule(rule, nlpNode.deepCopy());
                            //System.out.println(rule.getLeftHandSide().print()+"FUCKING EQUAL TO :"+nlpNode.getLeaves() + " "+nlpNode.value());
                        }

                    }
                }

            }

        }

    }

    public void applyRule(Node node, RuleItem rule) {
    	System.out.println("initialNode: "+node.print());
    	ArrayList<Node> sentences = getSplitSentence(node, null);
    	Node removedNodes = removeMarkedNodes(node);
    	
    	if(rule.getType().equals(RELATIVE_CLAUSE)){
    		
    	}
    	
    	System.out.println("Nodes removed: "+removedNodes.print());
    	for(Node child : removedNodes.getChildren()){
    		System.out.print("Tree of "+child.getValue()+" "+child.getCoreNlpTree());
    	}
    	
    	System.out.println("Final Tree: \n\n\n");
    	printLeaves(removedNodes);
    	for(Node child : sentences){
    	System.out.println(child.print() + " Tree: " +child.getCoreNlpTree().getLeaves() );
    	}
    	System.out.println("Final Output:");
    	printLeaves(removedNodes);
    	printLeaves(sentences.get(0));
    	
    	System.out.println("\n\n\n");
    }
    
    public void getFinalTree(){
    	
    }
    
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
				            	continue;
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
    
    
    
    
    public void mergeNodes(Node node1, Node node2){
    	
    	
    	for(Node node2child : node2.getChildren()){
    		
    		if(node2child.getValue().equalsIgnoreCase(node1.getValue())){
    			
    		}
    	}
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

    //Get Tree from text
    //Read all ruleList
    //Traverse each sentence?
    //Check each sentence for its type.
    //Apply if needed
}
