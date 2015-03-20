import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import language.PreSentence;
import language.TestCase;
import language.Word;
import lexical.LexSubmodules;

import org.apache.commons.io.FileUtils;

import preprocess.Jmwe;
import preprocess.Nlp;
import preprocess.ReaderWrite;
import simplenlg.features.Feature;
import simplenlg.features.Form;
import simplenlg.features.Tense;
import simplenlg.framework.InflectedWordElement;
import simplenlg.framework.LexicalCategory;
import simplenlg.framework.WordElement;
import simplenlg.lexicon.XMLLexicon;
import simplenlg.realiser.english.Realiser;
import syntactic.Analysis;
import syntactic.SyntacticSubmodules;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.trees.Tree;
 
public class Model {
 
    private ArrayList<TestCase> testcases;
    private ArrayList<PreSentence> result;
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
        this.lexSubmodules = new LexSubmodules();
        this.synanalysis = new Analysis();
        
    }
    
    public void initialize() {
        nlp.loadAnnotators();
    }
 
    public void simplifyMultipleTexts(ArrayList<Integer> ids) {
 
        for (int id : ids) {
            TestCase tc = getTestCase(id);
            tc.setResult(simplifyText(tc.getText()));
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
 
        ArrayList<PreSentence> sentenceList = new ArrayList<PreSentence>();
 
        try {
            sentenceList = jmwe.ApplyMweDetector(nlp.preprocessText(text));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        File compoundTextCorpus = new File("src/Documents/compoundprep.txt");
        sentenceList = lexSubmodules.directSubstitution(sentenceList,compoundTextCorpus);
        sentenceList = lexSubmodules.identifyIgnorables(sentenceList);
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
            //sentenceList = lexSubmodules.candidateRanking(sentenceList);
            for (PreSentence sentence : sentenceList) {
                for (Word word : sentence.getWordList()) {
                    if (lexSubmodules.checkConditionsForComplexity(word)) {
                    	
                        if (word.getSubstitute() != null && !word.getSubstitute().isEmpty()) {
                        	
                        	if(!word.getBestSubstitute().isEmpty())	
                        		break;
                        	else if(word.hasTense())
                        		word.setBestSubstitute(changeTense(word.getSubstitute().get(0), word.getPartOfSpeech()));
                        	else if(word.getBestSubstitute().isEmpty())
                        		word.setBestSubstitute(word.getSubstitute().get(0));
                        	
                        }
                        System.out.println(word.getBestSubstitute()+" "+word.getPartOfSpeech());
                    }
                }
            }
            File latinTextCorpus = new File("src/Documents/latin.txt");
            sentenceList = lexSubmodules.directSubstitution(sentenceList,latinTextCorpus);
            //adapter = new SimplexAdapter(sentenceList);
            //run simplex
            //adapter.start();
            //rank the substitute words and get the best substitute for each complex word
            //sentenceList = rankingChooser.getWords(sentenceList);
        } //end if
        String lexicalOutput = "";
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
        result = sentenceList;
        
       /* syntacticSubmodules = new SyntacticSubmodules(nlp.getPipeline());
        synanalysis.StartAnalysis(lexicalOutput, nlp.getPipeline());
        ArrayList<Tree> trees = synanalysis.getTree();
        ArrayList<SemanticGraph> graphs = synanalysis.getSemanticGraph();
        syntacticSubmodules.readRules();
        for(int i = 0; i < trees.size(); i++){
        	Tree tree = trees.get(i);
        	SemanticGraph graph = graphs.get(i);
        	syntacticSubmodules.checkRules(tree);
        	tree = syntacticSubmodules.toPassive(graph, tree);
        	System.out.println(tree);
        }*/
        
        //for each sentence
        //check rules
        //after: check matching
        //transform
        //traverseTree(tree);
        return sentenceList;
 
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