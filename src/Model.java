import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import language.PreSentence;
import language.TestCase;
import language.Word;
import lexical.LexSubmodules;

import org.apache.commons.io.FileUtils;

import postprocess.Cleaner;
import preprocess.Jmwe;
import preprocess.Nlp;
import preprocess.ReaderWrite;
import syntactic.Analysis;
import syntactic.SyntacticSubmodules;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.trees.Tree;
 
public class Model {
 
    private ArrayList<TestCase> testcases;
    private ArrayList<PreSentence> result;
    private String syntacticOutput;
    private ReaderWrite rw;
    private Nlp nlp;
    private Jmwe jmwe;
    private LexSubmodules lexSubmodules;
    private Analysis synanalysis;
    private SyntacticSubmodules syntacticSubmodules;
    public Model() {
        this.rw = new ReaderWrite();
        this.jmwe = new Jmwe();
        this.nlp = new Nlp(ReaderWrite.testPathComplete);
       
        this.synanalysis = new Analysis();
        
    }
    
    public void initialize() {
        nlp.loadAnnotators();
    }
 
    public void simplifyMultipleTexts(ArrayList<Integer> ids) {

        for (int id : ids) {
            TestCase tc = getTestCase(id);
            tc.setResult(simplifyText(tc.getText()));
            tc.setSyntacticResult(syntacticOutput);
        }

    }
 
    public TestCase getTestCase(int id) {
        for (TestCase tc : testcases) {
            if (tc.getId() == id) {
                return tc;
            }
        }
        return null;
 
    }
 
    public ArrayList<PreSentence> simplifyText(String text) {
    	 lexSubmodules = new LexSubmodules(nlp.getPipeline());
        ArrayList<PreSentence> sentenceList = new ArrayList<PreSentence>();
        
 
        try {
            sentenceList = jmwe.ApplyMweDetector(nlp.preprocessText(text));
            printSentenceList(sentenceList);
            System.out.println("after jmwe");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        File compoundTextCorpus = new File("src/Documents/compoundprep.txt");
        
        sentenceList = lexSubmodules.directSubstitution(sentenceList,compoundTextCorpus);
        System.out.println("after direct subt");
        printSentenceList(sentenceList);
        sentenceList = lexSubmodules.identifyIgnorables(sentenceList);
        System.out.println("after ignorables");
        printSentenceList(sentenceList);
        //perform preprocessing. If returned list is empty then abort.
        if (!sentenceList.isEmpty()) {
            for (PreSentence sentence : sentenceList) {
                for (Word word : sentence.getWordList()) {
                    //determine if each word is complex or not
                    if (!word.isStopWord() && word.getWordType() != Word.COMPOUND_WORD && !word.isIgnore()) {
                        word.setComplex(lexSubmodules.isComplex(word.getLemma()));
                    }
 
                }//end Word for
            }//end Sentence for
 
            //select candidates for each word
            sentenceList = lexSubmodules.candidateSelection(sentenceList);
            System.out.println("after candidatesel");
            printSentenceList(sentenceList);
            //sentenceList = lexSubmodules.candidateRanking(sentenceList);
            File latinTextCorpus = new File("src/Documents/latin.txt");
            sentenceList = lexSubmodules.directSubstitution(sentenceList,latinTextCorpus);
            System.out.println("after latin corpus");
            printSentenceList(sentenceList);
            //adapter = new SimplexAdapter(sentenceList);
            //run simplex
            //adapter.start();
            //rank the substitute words and get the best substitute for each complex word
            //sentenceList = rankingChooser.getWords(sentenceList);
        } //end if
        String lexicalOutput = "";
       
        Cleaner c = new Cleaner();
        
        result = c.cleanSentences(sentenceList);
        
        for (PreSentence s : sentenceList) {
            for (Word w : s.getWordList()) {
                if (w.getBestSubstitute() != null) {
                                       
                    lexicalOutput += (w.getBestSubstitute() + " ");
 
                } else if (w.getWord().equalsIgnoreCase("-LSB-")) {
                    lexicalOutput += ("[" + " ");
                } else if (w.getWord().equalsIgnoreCase("-RSB-")) {
                    lexicalOutput += ("]" + " ");
                } else if (w.getWord().equalsIgnoreCase("-LRB-")) {
                    lexicalOutput += ("(" + " ");
                } else if (w.getWord().equalsIgnoreCase("-RRB-")) {
                    lexicalOutput += (")" + " ");
                } else if (w.getWord().equalsIgnoreCase("``") || w.getWord().equalsIgnoreCase("''")) {
                    lexicalOutput += ("\"" + " ");
                } else if (w.getWord().equalsIgnoreCase("`") || w.getWord().equalsIgnoreCase("'")) {
                    lexicalOutput += ("\'" + " ");
                } else {
                    lexicalOutput += (w.getWord() + " ");
                }
 
            }
        }
 
        System.out.println("Output:");
        System.out.print(lexicalOutput);
        
        syntacticSubmodules = new SyntacticSubmodules(nlp.getPipeline());
        synanalysis.StartAnalysis(lexicalOutput, nlp.getPipeline());
        ArrayList<Tree> trees = synanalysis.getTree();
        ArrayList<SemanticGraph> graphs = synanalysis.getSemanticGraph();
        

      

        File input = new File("E:/Documents/GitHub/input.txt");
    	BufferedWriter writer=null;
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
		        
		    	syntacticSubmodules.setCOMPOUND(0); 
		    	syntacticSubmodules.setRELATIVE(0); 
		    	syntacticSubmodules.setPASSIVE(0); 
		    	syntacticSubmodules.setAPPOSITIVE(0); 
		       
		       synanalysis.StartAnalysis(lexicalOutput, nlp.getPipeline());
		       Analysis analysis = new Analysis();
		    	for(int i = 0; i < trees.size(); i++){
		    		
		    			SemanticGraph g = graphs.get(i);
		    			String resultSentences = null;
		    			ArrayList<SemanticGraph> resultDependencies = null;
		    			ArrayList<Tree> resultTrees = null;
		    			System.out.println("EXECUTING COMPOUND");
		    			//split compound sentences
		    			resultSentences = syntacticSubmodules.splitCompound(g, trees.get(i));
		    			
		    			System.out.println("EXECUTING RELATIVE");
		    			System.out.println(resultSentences);
		    			
		    			//if sentence is compound and returned results
		    			
		    			
		    				
		    				//get the dependencies of the resulting sentences outputted by the splitting of compound sentences
		    				analysis.StartAnalysis(resultSentences, nlp.getPipeline());
	    					resultDependencies = analysis.getSemanticGraph();
		    				resultTrees = analysis.getTree();
		    				resultSentences = "";
		    				//split each sentence in the compound results
		    				for(int k = 0; k < resultTrees.size(); k++){
		    					resultSentences+=syntacticSubmodules.splitRelative(resultDependencies.get(k), resultTrees.get(k));
		    				}
		    				

		    			
		    			
		    			
	    				
		    			System.out.println("EXECUTING APPOSITIVE");
		    			System.out.println(resultSentences);
		    			
		    			
		    			
		    				//get the dependencies of the resulting sentences outputted by the splitting of compound sentences
			    			analysis.StartAnalysis(resultSentences, nlp.getPipeline());
	    					resultDependencies = analysis.getSemanticGraph();
		    				resultTrees = analysis.getTree();
		    				resultSentences = "";
		    				//split each sentence in the compound results
		    				for(int k = 0; k < resultTrees.size(); k++){
		    					resultSentences+=syntacticSubmodules.splitAppositive(resultDependencies.get(k), resultTrees.get(k));
		    				}
		    				
	    				
	    				
		    			
		    			
		    			System.out.println("EXECUTING PASSIVE");
		    			System.out.println(resultSentences);
		    			
		    				analysis.StartAnalysis(resultSentences, nlp.getPipeline());
	    					resultDependencies = analysis.getSemanticGraph();
		    				resultTrees = analysis.getTree();
		    				resultSentences = "";
		    				for(int k = 0; k < resultTrees.size(); k++){
		    					resultSentences+=syntacticSubmodules.toPassive(resultDependencies.get(k), resultTrees.get(k));
		    				}
			    				
		    				
		    				
		    				writer.write(syntacticSubmodules.getCOMPOUND()+","+syntacticSubmodules.getRELATIVE()+","+syntacticSubmodules.getAPPOSITIVE()+","+syntacticSubmodules.getPASSIVE()+","+resultSentences+"\n");
		    				syntacticOutput = resultSentences;
		    		
		    	}
			        
		    }
		    
		    writer.close();
		   
		} catch (IOException x) 
		{
		    System.err.format("IOException: %s%n", x);
		} 
        return sentenceList;
 
    }
 
   public void printSentenceList(ArrayList<PreSentence> sentences){
	   for(PreSentence sentence:sentences){
		   for(Word w : sentence.getWordList()){
			   System.out.print(w.getWord());
			   
		   }
		   System.out.println();
	   }
   }
 
    public int getMaxDepth(Tree t) {
        int max = 0;
        for (Tree child : t.getChildrenAsList()) {
            if (max > child.depth()) {
                max = child.depth();
            }
        }
 
        return max;
    }
 
    public void traverseTree(Tree tree) {
        List<Tree> children = tree.getChildrenAsList();
 
        for (Tree child : children) {
            traverseTree(child);
            System.out.println("Depth:" + child.depth());
            System.out.println("Parent Value:" + child.parent().value());
            System.out.println("Value:" + child.value());
            System.out.println("Children No.: " + child.numChildren());
        }
 
        //if(head.depth(tree) == [val]) { add }
    }
 
    public ArrayList<TestCase> loadTestCases() {
        File folder = new File("src/documents/Testcases");
        File[] listOfFiles = folder.listFiles();
        ArrayList<TestCase> tclist = new ArrayList<TestCase>();
        for (int i = 0; i < listOfFiles.length; i++) {
            File file = listOfFiles[i];
            TestCase testcase = new TestCase();
            if (file.isFile() && file.getName().endsWith(".txt")) {
                try {
                    String content = FileUtils.readFileToString(file);
                    testcase.setFileName(file.getName());
                    testcase.setId(i);
                    testcase.setText(content);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
 
            }
            tclist.add(testcase);
        }
        testcases = tclist;
        return tclist;
    }
 
    public ArrayList<String> getTestCaseUsingID(ArrayList<Integer> ids) {
        ArrayList<String> list = new ArrayList<String>();
        for (int id : ids) {
            for (TestCase tc : testcases) {
                if (tc.getId() == id) {
                    list.add(tc.getText());
                }
            }
        }
        return list;
    }
 
    /*if()
     if()
     Arraylist<Tree> splitNodes = splitTree(sentence, node.getSolution());
     */
    public ArrayList<TestCase> getTestCases() {
        return this.testcases;
    }
 
    public ArrayList<PreSentence> getResults() {
        return result;
    }
 
}