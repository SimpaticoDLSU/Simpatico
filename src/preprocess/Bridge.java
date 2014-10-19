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
		this.nlpFilePath = nlp.defaultFilePath;
		nlp.filePath	 = this.nlpFilePath;
		//ReaderWrite rw = new ReaderWrite(nlp.GetDefaultFilePath());
		nlp.TestNlp();
		
		
		//Run Converter
		ConvertTxtToString(nlpFilePath);
		
		return true;
	}
	
	public void ConvertTxtToString(String filePath)
	{
		ReaderWrite rw = new ReaderWrite(filePath);
		String originalText = rw.GetFileContent();
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
