package preprocess;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import shortcuts.Print;

public class ReaderWrite {
	
	//testFilePath is only used for testing the ReaderWrite methods
	final static String testFilePath	= "/Users/laurenztolentino/Eclipse/workspace/Simpatico/src/preprocess/sample.txt";
	static Print p 						= new Print();
	String filePath 					= "";
	String finishedText;
	
	public ReaderWrite(String filePath)
	{
		this.filePath = filePath;

		//call ReadFile
		if(filePath == "") {
			
			p.println("No File Path Written");
		} else {
			
			ReadFile(filePath);
		}
		
	}
	
	public static void main(String[] args)
	{
		
		String test;
		
		p.println("Running ReaderWrite.java");
		p.println("ReaderWrite.main is for testing purposes only.");
		
		ReaderWrite rw = new ReaderWrite(testFilePath);
		test = rw.GetFileContent();
		
		p.println("Test output: ");
		p.println(test);
		
		
	}
	
	/*
	 * Call GetFileContent() to give you contents provided by the path file
	 */
	public String GetFileContent()
	{
		//Send it to the caller
		return this.finishedText;
	}
	
	/*
	 * Returns a false if there is an error in reading
	 * Returns true if ReadFile is a success
	 * */
	
	public Boolean ReadFile(String filePath)
	{
		//Read file
		p.println("Running ReaderWrite.java");
			
		BufferedReader reader 	= null;
		String finishedText 	= "";
		String temp 			= "";
		
		try 
		{
			//Make sure that path to file is compatible to your computer
			File file = new File("/Users/laurenztolentino/Eclipse/workspace/Simpatico/src/preprocess/sample.txt");			
			if (file.exists() == true)
			{	
				p.print("File found!");
				reader = new BufferedReader(new FileReader(file));
				
				String line;
				
				p.print("Now reading file : \n \n" );
				
				//read all lines
				while ((line = reader.readLine()) != null) 
				{
					p.println(line);
					finishedText = finishedText + line + " ";
				}
				
				//Reader has finished reading
				this.finishedText = finishedText;
				return true;
			} else {
				
				p.println("Problem with reading file. Error [0]");
				p.println("File: " + file.toString() + " does not exists");
				System.exit(1);
			}
			
			
		} catch (IOException e) {
			
			e.printStackTrace();
			return false;
		} finally {
			
			try {
				p.println("File Reading Finished. No Errors");
				p.println("\nReaderWrite will now be closed");
				reader.close();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
			
		return false;
	}
	
	
	
}
