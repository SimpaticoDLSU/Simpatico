/*
 * This was created in order to smoothen and ease the problem of abstracting the integration between CoreNLP and JMWE.
 * */
package preprocess;

import shortcuts.Print;
import shortcuts.Scan;

public class Bridge {

	Print p = new Print();
	Scan s  = new Scan();
	String	nlpFilePath;	
	
	String filePath = "";
	
	public Bridge()
	{
		// None yet
	}
	
	public static void main(String[] args) 
	{
		Bridge m = new Bridge();
		m.TestConnectNlpToJmwe();
	}
	
	public Boolean TestConnectNlpToJmwe()
	{
			
		// Uses defaultFilePath instead.
		Nlp nlp = new Nlp();
		p.println("defaultFilePath has been set as nlpFilePath");
		//set nlpFilePath as the same as the defaultFilePath
		this.nlpFilePath = nlp.GetDefaultFilePath();
		//set filePath of nlp to defaultFilePath
		nlp.filePath	 = this.nlpFilePath;
		// set filePath of this class to defaultFilePath
		this.filePath	 = nlpFilePath;
		p.println("filePath = " + this.filePath);
		//ReaderWrite rw = new ReaderWrite(nlp.GetDefaultFilePath());
		p.println("Running TestNlp() at the Bridge."); 
		nlp.TestNlp();
		
		
		//Run Converter
		p.println("Running ConverttextToString");
		ConvertTxtToString(nlpFilePath);
		
		return true;
	}
	
	public void ConvertTxtToString(String filePath)
	{
		p.println("Getting content from: " + filePath);
		ReaderWrite rw = new ReaderWrite(filePath);
		rw.ReadFile(nlpFilePath);
		String originalText = rw.GetFileContent();
		//p.println("oringinalText is: " + originalText);
		String[] splittedText = originalText.split("/");
		
		for(int i = 0; i < splittedText.length; i++) {
			p.println(splittedText[i]);
		}
	}
	
	public Boolean ValidateNlpToJmwe()
	{
		return true;
	}
	
	
}
