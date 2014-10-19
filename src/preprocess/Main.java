package preprocess;

import shortcuts.Print;

public class Main 
{
	static Print p = new Print();
	public static void main(String[] args)
	{
		/* if you are looking for CoreNLP code, it has been transfered to Nlp.java 
		 * Please keep this class as abstracted and as high level as possible :) 
		 *  
		 * Directives: 
		 * 1) Call GUI 
		 * 2) Input from GUI will be transferred to Nlp.java 
		 * 3) Nlp.java output will be transferred to Jmwe.java
		 * 4) Finish processing and finishing touches to input for the delight of other developers.
		 *
		 */		
		ReaderWrite rw = new ReaderWrite("filepath");
		Nlp nlp = new Nlp();
		GUI_Splash gs = new GUI_Splash();
		
	}
}
