package syntactic;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
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
    public SyntacticSubmodules (){
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
		private ArrayList<Node> children;
		private int siblingIndex;
        
		public int getDepth() {
			return depth;
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
			return children;
		}
		public void setChildren(ArrayList<Node> children) {
			this.children = children;
		}
		
		public Node skipRoot(){
			if(value.equals("ROOT")){
				if(!children.isEmpty() || children != null)
					return children.get(0);
				else{
					System.err.println("Method skipRoot() used on Root-only tree!");
					return null;
				}
			}
			else{
				System.err.println("Method skipRoot() used on non-root Node!");
				return null;
			}
		}
		
		public int getSibligIndex(){
			return this.siblingIndex;
		}
		public void setSiblingIndex(int index){
			this.siblingIndex = index;
		}
		
		public int getNumSiblings(){
			if(parent != null)
				return parent.getChildren().size();
			else
				return 0;
		}
		
		public Node getChild(int index){
			return children.get(index);
		}
    }
    
    
    //gets the children of a particular node depending on the current position of the cursor of the scanner
    public ArrayList<Node> getChildren(Scanner sc, Node head, Node parent, int depth) {
    	String label;
    	Node node = null;
    	Node prevNode = null;
    	int siblingIndex = 0;
    	ArrayList<Node> childList = new ArrayList<Node>();
    	while(sc.hasNext()){
    		label = sc.next();
    		if(node != null)
    			prevNode = node;
    		node = new Node();
    		 		
    		if(label.equals(")")){
    			return childList;
    		}
    		
    		if(label.equals("(")){
    			
    			prevNode.setChildren(getChildren(sc, head, prevNode, ++depth));
    			
    		} else{
    			node.setChildren(new ArrayList<Node>());
    			node.setSiblingIndex(siblingIndex++);
    			node.setHead(head);
    			node.setDepth(depth);
    			node.setParent(parent);
    			System.out.println("Setting value: "+label+ " Parent: "+parent.getValue()+ " Depth: " + depth+" SiblingIndex: "+(siblingIndex-1) );
    			node.setValue(label);
    			childList.add(node);
    		}
    		
    	}
    	
    	return childList;
     
    }
    
    //loads rules in their respective arraylists 
    public void readRules(){
    	 //Read the ruleList
        FileReader fileReader;
        BufferedReader bufferedReader;
		try {
			for(SentenceType sentenceType : SentenceType.values()){
				System.out.println(sentenceType.toString());
				fileReader = new FileReader("src/documents/rules/"+sentenceType.toString() + ".txt");
				bufferedReader = new BufferedReader(fileReader);
		        Scanner scanner;
		        String line;
		        int depth;
		        while ((line = bufferedReader.readLine()) != null) {
		        	RuleItem rule = new RuleItem();
		        	String[] splittedRule = line.split(">");
		        	Node rootNode;
		        	for(int i = 0; i < splittedRule.length; i++){
		        		depth = 0;
		        		scanner = new Scanner(splittedRule[i].trim());
		        		rootNode = new Node();
			        	rootNode.setValue("ROOT");
			        	rootNode.setDepth(depth);
			        	rootNode.setChildren(getChildren(scanner, rootNode, rootNode, ++depth));
			        	if(i == 0)
			        		rule.getRuleList().add(rootNode);
			        	else if(i == 1)
			        		rule.getEffectList().add(rootNode);
			        	
			        	String string = sentenceType.toString();
						if (COMPOUND.equals(string)) {
							compoundRuleList.add(rule);
						} else if (COMPOUND_COMPLEX.equals(string)) {
							compoundComplexRuleList.add(rule);
						} else if (RELATIVE_CLAUSE.equals(string)) {
							relativeRuleList.add(rule);
						} else if (PASSIVE_ACTIVE.equals(string)) {
							passiveActiveRuleList.add(rule);
						}
		        	}
		        	
		        }
			}
		}  catch (IOException ex) {
            Logger.getLogger(Framework.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        
       
    }

    
    

    //represents one rule
    class RuleItem {
    	private ArrayList<Node> ruleList;
		private ArrayList<Node> effectList;
    	
    	public RuleItem(){
    		ruleList = new ArrayList<Node>();
    		effectList = new ArrayList<Node>();
    	}
    	
    	public ArrayList<Node> getRuleList() {
			return ruleList;
		}

		public void setRuleList(ArrayList<Node> ruleList) {
			this.ruleList = ruleList;
		}

		public ArrayList<Node> getEffectList() {
			return effectList;
		}

		public void setEffectList(ArrayList<Node> effectList) {
			this.effectList = effectList;
		}


    }

    
    public static void main(String[] args) {
    	SyntacticSubmodules f = new SyntacticSubmodules();
    	f.readRules();
    	System.out.print(f.compoundRuleList.size());
    	for(RuleItem n : f.compoundRuleList){
    		System.out.print("||");
    		for(Node r : n.getRuleList()){
    			f.traverse(r);
    		}
    		System.out.print(" > ");
    		for(Node r : n.getEffectList()){
    			f.traverse(r);
    		}
    		
    	}
    	
    	
    	
       
    }
    
    public void traverse(Node child){ // post order traversal
    	if(child.getChildren().isEmpty()){
    		return;
    	}else
	        for(Node each : child.getChildren()){
	        	System.out.print(each.getValue()+" ");
	            traverse(each);
	        }
       
    }

	public void checkRules(Tree sentenceTree) {
		for(SentenceType sentenceType : SentenceType.values()){
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
			} else
				sentenceRules = null;
			
			for(RuleItem rule : sentenceRules){
				ArrayList<Node> preEffect = rule.getRuleList();
				for(Node node : preEffect){
					List<Tree> subtrees = sentenceTree.subTreeList();
					for(Tree nlpNode : subtrees){
						if(nlpNode.value().equalsIgnoreCase(node.skipRoot().getValue())){
							isSubTreeEqual(nlpNode, node);
						}
					}
				}
			}
			
		}
		
		
	}
	
	public boolean isSubTreeEqual(Tree nlptree, Node ruletree){
		
	
			
			
					
				
					if(getSameChildren(nlptree.getChildrenAsList(),ruletree.getChildren())){
						for(Node child : ruletree.getChildren()){
							for()
							isSubTreeEqual(, );
							
						}
						
					}else
						return false;
			
			return true;
		
		
	}
	
	p

	public boolean getSameChildren(List<Tree> childrenAsList,
			ArrayList<Node> children) {
		// TODO Auto-generated method stub
		return false;
	}

	public void traverseTree(Tree tree, String target) {
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
	

    //Get Tree from text
    //Read all ruleList
    //Traverse each sentence?
    //Check each sentence for its type.
    //Apply if needed
}
