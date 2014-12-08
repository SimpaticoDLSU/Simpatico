package syntactic;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;



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
        private ArrayList<Node> children;
        
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
    }
    
    
    //gets the children of a particular node depending on the current position of the cursor of the scanner
    public ArrayList<Node> getChildren(Scanner sc, Node head, Node parent) {
    	String label;
    	Node node = null;
    	Node prevNode = null;
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
    			
    			prevNode.setChildren(getChildren(sc, head, prevNode));
    			
    		} else{
    			node.setChildren(new ArrayList<Node>());
    			node.setHead(head);
    			node.setParent(parent);
    			System.out.println("Setting value: "+label+ " Parent: "+parent.getValue());
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
		        while ((line = bufferedReader.readLine()) != null) {
		        	RuleItem rule = new RuleItem();
		        	String[] splittedRule = line.split(">");
		        	scanner = new Scanner(splittedRule[0].trim());
		        	Node rootNode = new Node();
		        	rootNode.setValue("ROOT");
		        	rootNode.setChildren(getChildren(scanner, rootNode, rootNode));
		        	rule.getRuleList().add(rootNode);
		        	
		        	
		        	scanner = new Scanner(splittedRule[1].trim());
		        	rootNode = new Node();
		        	rootNode.setValue("ROOT");
		        	rootNode.setChildren(getChildren(scanner, rootNode, rootNode));
		        	rule.getEffectList().add(rootNode);
		        	
		        	switch(sentenceType.toString()){
			        	case "compound":  compoundRuleList.add(rule); break;
			        	case "compound_complex": compoundComplexRuleList.add(rule); break;
			        	case "relative_clause": relativeRuleList.add(rule); break;
			        	case "passive_active": passiveActiveRuleList.add(rule); break;
		        	}
	        	
		            
		        }
			}
		}  catch (IOException ex) {
            Logger.getLogger(Framework.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        
       
    }

    
    public enum SentenceType {

        RELATIVE_CLAUSE,
        COMPOUND,
        COMPOUND_COMPLEX,
        PASSIVE_ACTIVE;

        private ArrayList<RuleItem> output;

        @Override
        public String toString() {
            switch (this) {
                case RELATIVE_CLAUSE:
                    return "relative_clause";
                case COMPOUND:
                    return "compound";
                case COMPOUND_COMPLEX:
                    return "compound_complex";
                case PASSIVE_ACTIVE:
                    return "passive_active";

            }
            return super.toString();
        }

        public RuleItem getOutput(int index) {
            return output.get(index);
        }

        public void setOutput(ArrayList<RuleItem> output) {
            this.output = output;
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

    //Get Tree from text
    //Read all ruleList
    //Traverse each sentence?
    //Check each sentence for its type.
    //Apply if needed
}
