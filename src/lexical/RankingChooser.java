/*
 * RankingChooser.java
 * Last Modified: September 29, 2014
 * Chooses the best substitute word from the output file provided by Simplex
 */

package lexical;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import language.PreSentence;
import language.Word;

public class RankingChooser {

    //The ranking that will be used to choose the best among the candidate substitutes.
    private final double RANK_VALUE = 1.0;
    //This is the list of best words (rank of 1.0)
    private ArrayList<String> replacementWords;

    /* Skips the qid:XX and the 1:XXXXX parts of the output
     * s - the input stream used for skipping
     */
    private Scanner skip(Scanner s) {
        s.next();
        s.next();
        return s;
    }

    /* Gets the best candidates based from RANK_VALUE
     */
    public ArrayList<PreSentence> getWords(ArrayList<PreSentence> sentenceList) {
        Scanner inputScanner;
        String substituteWord;

        //Variable instantiation
        replacementWords = new ArrayList<>();

        try {
            //Get the ranking file
            inputScanner = new Scanner(new File("src/lexical/Resources/Simplex/test-input"));

            //Scan through the file.
            while (inputScanner.hasNext()) {
                //If it is the best rank, add to the list
                if (inputScanner.nextDouble() == RANK_VALUE) {
                    inputScanner = skip(inputScanner);

                    //Get the word from the input stream
                    substituteWord = inputScanner.next();

                    //Check if the appropriate substitute is more than one word i.e "people person", "the man", "a place".
                    //inputscanner.hasNext to check if the input stream has reached the end of the file.
                    while (!inputScanner.hasNextFloat() && inputScanner.hasNext()) {
                        substituteWord += " " + inputScanner.next();
                    }
                    
                    //Removes the # character from the candidate word
                    substituteWord = substituteWord.substring(1);
                    //Add the word to the list.
                    replacementWords.add(substituteWord);
                } else {
                    //Else, skip.
                    inputScanner = skip(inputScanner);
                    //inputscanner.hasNext to check if the input stream has reached the end of the file.
                    while (!inputScanner.hasNextFloat() && inputScanner.hasNext()) {
                        inputScanner.next();
                    }
                }
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        
        int index = 0;
       
        for (PreSentence sentence : sentenceList) {
            ArrayList<Word> wordList = sentence.getWordList();
            
            //Traverse the words contained in a sentence.
            for (Word word : wordList) {
                ArrayList<String> substituteWords = word.getSubstitute();

                //if word has a list of substitutes, then add its corresponding replacement word.
                if(substituteWords != null)
	                if (!substituteWords.isEmpty()) {
	                    word.setBestSubstitute(replacementWords.get(index++));
	                }
            }
        }
        
        return sentenceList;
    }

}
