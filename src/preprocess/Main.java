package preprocess;

import java.io.IOException;

import shortcuts.Print;
import shortcuts.Scan;
public class Main 
{
	static Print p = new Print();
	static Scan s  = new Scan();
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
		
		int choice;
		
		choice = s.GetInt();
		
		p.println("Test the ff: 1)ReaderWriter 2)CoreNLP 3)RwToNlp 4)Jmwe 5) CoreNLP2Jmwe");
		switch (choice)
		{
			case 1	:	TestReaderWriter();
						break;
			case 2	: 	TestCoreNLP();
						break;
			case 3 	: 	TestRwToNlp();
						break;
			case 4  : 	TestReadFile();
						break;
			default	:	p.println("None yet.");
		}
		//TestReaderWriter();
		
	}
	
	public static void TestReadFile()
	{
		ReaderWrite rw = new ReaderWrite();
		rw.TestReadFile("sample.txt");
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
	
	public static void TestCoreNLP()
	{
		ReaderWrite rw = new ReaderWrite();
		Adapter adapter = new Adapter();
		@SuppressWarnings("static-access")
		Nlp nlp = new Nlp(rw.testPathComplete);
<<<<<<< HEAD
		// nlp.TestNlp();
=======
		nlp.TestNlp();
>>>>>>> FETCH_HEAD
		try {
			adapter.NLPtoJMWE("NlpOutput.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void TestRwToNlp()
	{
		
		p.println("Running test cases for ReaderWrite to NLP integration");
		// something
	}
}
