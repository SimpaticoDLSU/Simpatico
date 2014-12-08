import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import language.PreSentence;
import language.TestCase;
import language.Word;
import net.miginfocom.swing.MigLayout;


public class View {
	 //main frame
	private JFrame frame;
	private JPanel textList;
	private JPanel mainPanel;
	private JTextArea inputArea;
	private JTextPane outputArea;
	private JScrollPane inputScrollPane;
	private JScrollPane outputScrollPane;
	private JScrollPane textCaseScrollPane;
	private JMenuBar menuBar;
	private JMenu tasksMenu;
	private JMenuItem processInputTextMenu;
	private JMenuItem processSelectedItemsMenu;
	private JMenuItem addTestCaseMenu;
	private JDialog dialog;
	private StyledDocument doc;
	public void initGUI(){
	    frame = new JFrame("Simpatico");
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    
	    double dwidth = (double)Toolkit.getDefaultToolkit().getScreenSize().width;
	    double dheight = (double)Toolkit.getDefaultToolkit().getScreenSize().height; 
	    frame.setSize(new Dimension((int)(dwidth*.80),(int)(dheight*.75)));
	    frame.setLocation((int)(dwidth*.10), (int)(dheight*.10));
	    frame.setLayout(new MigLayout("","[400::400][800::]","[800::800]"));
        frame.setVisible(true);
        frame.setResizable(true);
        
        menuBar = new JMenuBar();
        tasksMenu = new JMenu("Tasks");
        processSelectedItemsMenu = new JMenuItem("Simplify Selected Test Cases");
        processInputTextMenu = new JMenuItem("Simplify Input Text");
        addTestCaseMenu = new JMenuItem("Add test case");
        
        menuBar.add(tasksMenu);
        tasksMenu.add(processInputTextMenu);
        tasksMenu.add(processSelectedItemsMenu);
        tasksMenu.add(addTestCaseMenu);
        
        textList = new JPanel();
        textList.setLayout(new MigLayout());
        
        
        mainPanel = new JPanel();
        mainPanel.setLayout(new MigLayout());
        
        inputArea = new JTextArea();
        inputArea.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"Input Text"));
        inputArea.setLineWrap(true);
        inputArea.setWrapStyleWord(true);
        
        outputArea = new JTextPane();
        outputArea.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"Output Text"));
        
        inputScrollPane = new JScrollPane(inputArea);
        inputScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        inputScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        outputScrollPane = new JScrollPane(outputArea);
        outputScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        outputScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        textCaseScrollPane = new JScrollPane(textList);
        textCaseScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        textCaseScrollPane.setBorder(BorderFactory.createTitledBorder("Test Cases"));
        
        mainPanel.add(inputScrollPane, "wrap,grow, push");
        mainPanel.add(outputScrollPane, "grow,push");
        
        //textList.add(new TextPanel(0,"FILENAME","STRING"), "growx,pushx, wrap");
       
        frame.setJMenuBar(menuBar);
        frame.add(textCaseScrollPane, "grow");
        frame.add(mainPanel, "grow, push");
        doc = outputArea.getStyledDocument();
        
        
        frame.pack();
        frame.setVisible(true);
   
	}
	
	public void addTextPanel(int id, String text, String filename){
		textList.add(new TextPanel(id,text,filename), "growx,pushx, wrap");
		frame.repaint();
	}
	
	public void addTextPanel(ArrayList<TestCase> testcases){
		for(TestCase tc: testcases){
			textList.add(new TextPanel(tc.getId(),tc.getText(),tc.getFileName()), "growx,pushx, wrap");
		}
		frame.repaint();
		
	}
	
	public void showDialog(String text){
		dialog = new JDialog();
		dialog.add(new JLabel(text));
		dialog.setLocationRelativeTo(null);
		dialog.setVisible(true);
		dialog.toFront();
		dialog.pack();
	}
	
	public void hideDialog(){
		dialog.setVisible(false);
	}
	
	public void refresh(){
		frame.repaint();
		textList.repaint();
		mainPanel.repaint();
	}
	
	public String getInputText(){
		return inputArea.getText();
	}
	
	public ArrayList<Integer> getSelectedItems(){
		ArrayList<Integer> ids = new ArrayList<Integer>();
		for(Component component:textList.getComponents()){
			TextPanel panel;
			if(component instanceof TextPanel){
				panel = (TextPanel)component;
				if(panel.isSelected())
					ids.add(panel.getId());
			}
		}
		
		return ids;
	}
	public void clearAll(){
		outputArea.setText("");
		inputArea.setText("");
		doc = outputArea.getStyledDocument();
	}
	
	public void clearOutputArea(){
		outputArea.setText("");
		doc = outputArea.getStyledDocument();
	}
	
	public void setMultipleOutput(ArrayList<TestCase> testcases){
		Style def = StyleContext.getDefaultStyleContext().getStyle( StyleContext.DEFAULT_STYLE );
	    Style regular = doc.addStyle( "regular", def );
		
		ArrayList<Integer> ids = getSelectedItems();
		for(int id : ids){
			for(TestCase tc: testcases){
				if(tc.getId() == id && tc.getResult() != null){
					try {
						doc.insertString(doc.getLength(), "\n", regular);
						doc.insertString(doc.getLength(), "File: "+tc.getFileName()+"\n" , regular);
						doc.insertString(doc.getLength(), "ID: "+tc.getId()+"\n" , regular);
						doc.insertString(doc.getLength(), "Original Text: \n"+tc.getText()+"\n" , regular);
						doc.insertString(doc.getLength(), "Simplified Text: \n" , regular);
						setOutput(tc.getResult());
						doc.insertString(doc.getLength(), "\n\n", regular);
						
					} catch (BadLocationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				 
				}
			}
		}
	}
	
	public void setOutput(ArrayList<PreSentence> sentences){
		
		
		// Load the default style and add it as the "regular" text
	    Style def = StyleContext.getDefaultStyleContext().getStyle( StyleContext.DEFAULT_STYLE );
	    Style regular = doc.addStyle( "regular", def );
	    
		// Create a highlight style
	    Style highlightCyan = doc.addStyle( "highlight", regular );
	    StyleConstants.setBackground( highlightCyan, new Color(175,238,238) );
	    
	    Style highlightYellow = doc.addStyle( "highlight", regular );
	    StyleConstants.setBackground( highlightYellow, new Color(255,255,180) );
	    
		for(PreSentence s: sentences){
			for(Word w: s.getWordList()){
				try{
					if(w.getBestSubstitute() != null && !w.getBestSubstitute().equalsIgnoreCase(w.getWord()))
						doc.insertString(doc.getLength(),w.getBestSubstitute()+" ",highlightCyan);
					else if(w.isComplex() && w.getBestSubstitute() == null || w.getBestSubstitute().equalsIgnoreCase(w.getWord()))
						doc.insertString(doc.getLength(),w.getWord()+" ",highlightYellow);
					else if(w.getWord().equalsIgnoreCase("-LSB-"))
						doc.insertString(doc.getLength(),"["+" ", regular);
					else if(w.getWord().equalsIgnoreCase("-RSB-"))
						doc.insertString(doc.getLength(),"]"+" ", regular);
					else if(w.getWord().equalsIgnoreCase("-LRB-"))
						doc.insertString(doc.getLength(),"("+" ", regular);
					else if(w.getWord().equalsIgnoreCase("-RRB-"))
						doc.insertString(doc.getLength(),")"+" ", regular);
					else if(w.getWord().equalsIgnoreCase("``") || w.getWord().equalsIgnoreCase("''") )
						doc.insertString(doc.getLength(),"\""+" ", regular);
					else if(w.getWord().equalsIgnoreCase("`") || w.getWord().equalsIgnoreCase("'") )
						doc.insertString(doc.getLength(),"\'"+" ", regular);
					else 
						doc.insertString(doc.getLength(),w.getWord()+" ", regular);
				}catch(Exception e){
					System.err.print(e);
				}
			}
		}
		frame.repaint();
		textList.repaint();
	}
	
	public void setProcessSelectedItemsActionListener(ActionListener listener){
		processSelectedItemsMenu.addActionListener(listener);
	}
	
	public void setProcessInputTextActionListener(ActionListener listener){
		processInputTextMenu.addActionListener(listener);
	}
	
	public void setAddTestCaseActionListener(ActionListener listener){
		addTestCaseMenu.addActionListener(listener);
	}
	
	public static void main(String args[]){
		View w = new View();
		w.initGUI();
	}
	
	public void enableTestCaseList(boolean bool){
		textList.setEnabled(bool);
	}

	public void showMessage(String message) {
		JOptionPane.showMessageDialog(frame, message);
		
	}
}
