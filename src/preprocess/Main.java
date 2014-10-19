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
		
		/*
		ReaderWrite rw = new ReaderWrite("filepath");
		Nlp nlp = new Nlp();
		GUI_Splash gs = new GUI_Splash();
		*/
		
		TestReaderWriter();
		
	}
	
	public static void TestReaderWriter()
	{
		int score = 0;
		
		Boolean test1 = false, test2 = false, test3 = false;
		
		//A no parameter ReaderWrite will make use of test parameters instead
		ReaderWrite rw = new ReaderWrite();
		p.println("Running TestCreateFile");
		test1 = rw.TestCreateFile();
		p.println("Running TestReadFile");
		test2 = rw.TestReadFile();
		//Read file coming from TestCreateFile
		test3 = rw.ReadFile(rw.GetFilePath());
		
		p.println("");
		if(test1 == true && test2 == true && test3 == true) {
			p.println("All 3 test cases for Read and Write are all passed. Congrats!");
		}
	}
	
	public static void TestRwToNlp()
	{
		
		p.println("Running test cases for ReaderWrite to NLP integration");
		// something
	}
}
