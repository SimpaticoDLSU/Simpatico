/*
 * This class was created to make reading and writing text files easier.
 * All classes that would require reading and writing of .txt and other files must interact with this class.
 * 
 * */
package preprocess;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import shortcuts.Print;

public class ReaderWrite {
	
	//testFileName is only used for testing the ReaderWrite methods
	final static String testFile		= "/Users/laurenztolentino/Eclipse/workspace/Simpatico/src/preprocess/sample.txt";
	final static String testFilePath    = "/Users/laurenztolentino/Eclipse/workspace/Simpatico/src/preprocess/";
	static Print p 						= new Print();
	String filePath 					= "";
	String textContent					= "";
	String fileName 					= "";
	String fileContent					= "";
	
	public ReaderWrite()
	{
		this.filePath = testFile;
		//Please manually invoke ReadFile at primary caller and not here.
		//ReadFile(filePath);
	}
	
	public ReaderWrite(String filePath)
	{
		this.filePath = filePath;

		//call ReadFile
		if(filePath == "") {
			
			p.println("No File Path Written");
			
		} else {
			p.println("Path registered. Please double check later.");
			//ReadFile(filePath);
		}
		
	}
	
	public static void main(String[] args)
	{
		
		p.println("Running ReaderWrite.java");
		
		/*
		String test;		
		
		p.println("ReaderWrite.main is for testing purposes only.");
		
		ReaderWrite rw = new ReaderWrite(testFile);
		test = rw.GetFileContent();
		
		p.println("Test output: ");
		p.println(test);
		*/
		
	}
	
	/*
	 * Call GetFileContent() to give you contents provided by the path file
	 */
	public String GetFileContent()
	{
		//Send it to the caller
		return this.fileContent;
	}
	
	/*
	 * Returns a false if there is an error in reading
	 * Returns true if ReadFile is a success
	 * */
	
	public Boolean ReadFile(String filePath, String fileName)
	{
		
		if(filePath.equals("") || fileName.equals("")) 
		{
			p.print("filePath or fileName is blank");
			return false;
		}
		else {
			ReadFile(filePath + "/" + fileName);
			return true;
		}
	}
	
	public Boolean ReadFile(String filePath)
	{
		//Read file
		p.println("ReadFile() now running");
			
		BufferedReader reader 	= null;
		String fileContent 	= "";
		String temp 			= "";
		
		try 
		{
			//Make sure that path to file is compatible to your computer
			File file = new File(filePath);			
			if (file.exists() == true)
			{	
				p.println("File found!");
				reader = new BufferedReader(new FileReader(file));
				
				String line;
				
				p.println("Now reading file : \n" );
				
				//read all lines
				while ((line = reader.readLine()) != null) 
				{
					p.println(line);
					fileContent = fileContent + line + " ";
				}
				
				p.println("FileContent" + fileContent);
				
				//Reader has finished reading
				this.fileContent = fileContent;
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
				p.println("\nFile Reading Finished. No Errors");
				p.println("ReaderWrite will now be closed");
				reader.close();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
			
		return false;
	}
	
	
	
	/* Only use when there is already a declared value in filePath. This was made in order to reduce code writing in other classes.*/
	public Boolean ReadFile()
	{
		
		// if filePath has a value
		if(!filePath.equals("")) {
			return ReadFile(this.filePath);
		}
		else {
			return false;
		}
	}
	
	public void SetWriteContent(String content)
	{
		//set value of textContent via the caller
		this.textContent = content;
	}
	
	public String GetWriteContent()
	{
		return this.textContent;
	}
	
	public void SetFilePath(String path)
	{
		
		//set value of filePath in class
		if(path.equals("") || path == "") {
			p.print("Invalid path. No path written.");
		}
		this.filePath = path;
	}
	
	public void SetFileName(String name)
	{
		
		this.fileName = name;
	}
	
	public String GetFilePath()
	{
		//Set the FilePath string in the class
		return this.filePath;
	}
	
	public Boolean CreateFile(String text)
	{
		
		//use only when filePath actually has an existing value.
		if (filePath.equals("")) {
			p.println("Cannot use this yet. Variable filePath still has no value.");
			return false;
		}
		
		CreateFile(text, filePath);
		return true;
	}
	
	public Boolean CreateFile(String text, String pathCur)
	{
		
		String textContent  = text;
		String path			= pathCur;				
		
		if(textContent.equals("")){
			p.println("You are creating a file without any content. Please add content before proceeding");
			return false;
		}
		
		try {
			
			String tempPath = path;
			File file = new File(tempPath) ;		
			//Also changed filePath content to tempPath in order to be able to test ReadFile()
			filePath = tempPath;
			fileName = "";
			
			if(!file.exists())
			{
				p.print("File not found. Creating a new one at: " + path);
				file.createNewFile();
			}
			
			FileWriter fw  = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(textContent);
			bw.close();
			
			//message for write file success and path
			p.println("Finished writing. [CreateFile]");
			p.println("Path is at: " + tempPath);
			
			//test if path and file actually exists
			
			return true;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//When error strikes
			return false;
		}
	}
	
	public Boolean TestCreateFile()
	{
		
		String textContent  = this.textContent; //template for the actual CreateFile
		String path			= testFilePath;		
		textContent 		= "This is a sample textContent for TestCreateFile. This string is needed in orderr to create a file.";
		String tempPath 	= path + "write.txt";
		
		return CreateFile(textContent, tempPath);		
	}
	
	public Boolean TestReadFile()
	{
		
		String path = testFile;
		if(ReadFile(path)) {
			return true;
		}
		return false;
	}
	
	@SuppressWarnings("resource")
	public Boolean AddNewWriteLine(String line, String path)
	{
		
		//check values of parameters passed to this method
		if(line.equals("")) 
		{
			p.print("No string found in variable line. Please add a line, otherwise it will be treated as a new line");
			//return false;
		} 
		else if (path.equals("")) 
		{
			p.print("path is blank. Please write the address for path.");
			
			return false;
		}
		
		
		try 
		{
			File file = new File(path);
			if(!file.exists()) 
			{
				p.print("File not found. Creating a new one at: " + path);
				file.createNewFile();
				BufferedWriter writer = new BufferedWriter(new FileWriter(file));
				
				writer.write("" + line);
				writer.newLine();
				
				return true;
			}
			
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			p.print("Error during writing a new line at an existing document");
			
			return false;
		}
		
		return false;
	}
	
	public Boolean AddNewWriteLine(String line)
	{
		
		if(line.equals("")) {
			p.print("There is no content in line. Line will be treated as a new line instead.");
		}
		if(filePath.equals("") || filePath == "") {
			p.print("filePath is not specified. Please use SetFilePath before proceeding");
			return false;
		}
		
		//calls AddNewWriteLine with 2 parameters. This will work as long as filePath exists in the class.
		AddNewWriteLine(line,filePath);
		
		return true;
	}
	
	public Boolean TestAddNewWriteLine()
	{
		
		String line = "Running TestAddNewWriteLine(). I hope that this runs well.";
		AddNewWriteLine(line, testFilePath);
		return false;
	}
	
	
	
}
