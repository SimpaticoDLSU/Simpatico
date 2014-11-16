
package lexical;
/*
 * SimplexAdapter.java
 * Last Modified: October 23, 2014
 * Translates the sentence and word objects to a structure that is readable by simplex
 */

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import objects.PreSentence;
import objects.Word;

public class SimplexAdapter {

    //Directory where simplex is stored.
    private final String SIMPLEX_DIRECTORY = "src/lexical/Resources/Simplex/";
    //the name of the output text file to be read by simplex.
    private final String OUTPUT_NAME = "simpatico";
    //List of sentences to be processed
    private final ArrayList<PreSentence> sentences;

    //Contructor of SimplexAdapter
    public SimplexAdapter(ArrayList<PreSentence> sentences) {
        this.sentences = sentences;
    }
    
    /*
	 *	Start the translation of the sentences and simplex
	 * 	Run the RankingChooser Afterwards.
	 */
	public void start() {
		translateToTxtFile();
		runSimplex();
		
	}

    /*
     *  Translate to text file.
     *  Transforms the list of sentences
     *  It will output a file with a structure that will be readable in simplex
     */
    public void translateToTxtFile() {
        //Create the file for the output
        File file = new File(SIMPLEX_DIRECTORY + OUTPUT_NAME + "-test");
        System.out.println(file.getAbsoluteFile());
        try {
            //Initialize file writers
            BufferedWriter fileWriter = new BufferedWriter(new FileWriter(file));

            int id = 0; //The counter for the words

            //Traverse the sentences
            for (PreSentence sentence : sentences) {
                ArrayList<Word> wordList = sentence.getWordList();

                //Traverse the words contained in a sentence.
                for (Word word : wordList) {
                	if(word.isComplex() && !word.isStopWord() && !word.isIgnore()){
	                    ArrayList<String> substituteWords = word.getSubstitute();
	
	                    //Print out the next word that is being processed
	                    System.out.println("Processing word: " + word.getWord());
	
	                    //Check if the word has any substitutes
	                    if (!substituteWords.isEmpty()) {
	                    	System.out.print("sub");
	                        fileWriter.write("Sentence " + id + " rankings:");  //Initial printing before printing substitute words
	
	                        //Traverse list of substitute words.
	                        for (String s : substituteWords) {
	                            //Write the substitute word to the file
	                            fileWriter.write(" {" + s + "}");
	                        }
	                        fileWriter.write("\n");
	
	                        id++;
	                    }
                	}
                }
            }

            //Close the file wirter
            fileWriter.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Can not find file"); // Print error regarding finding the file.
        } catch (IOException ex) {
            Logger.getLogger(SimplexAdapter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void runSimplex() {
        try {
            //Insert command line input for simplex here
            //Insert Simplex Directory
            System.out.println("\"" + SIMPLEX_DIRECTORY + "simplex.bat\"");
            Process p = Runtime.getRuntime().exec("\"" + SIMPLEX_DIRECTORY + "simplex.bat\"");
            p.waitFor();
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(SimplexAdapter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
