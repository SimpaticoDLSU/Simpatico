import edu.stanford.nlp.trees.Tree;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import language.PreSentence;
import language.TestCase;
import language.Word;
import net.miginfocom.swing.MigLayout;

public class SimpaticoView extends javax.swing.JFrame {

    private JDialog dialog;
    private StyledDocument doc;

    public SimpaticoView() {
        //initComponents();
    }

    public void initGUI() {
        initComponents();
        testCasePanel.setLayout(new MigLayout());
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        testCasePreview = new javax.swing.JTextArea();
        inputPanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        inputTextArea = new javax.swing.JTextArea();
        outputPanel = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        outputTextArea = new javax.swing.JTextPane();
        simplifyButton = new javax.swing.JButton();
        addTestCaseButton = new javax.swing.JButton();
        comboBox = new javax.swing.JComboBox();
        jScrollPane3 = new javax.swing.JScrollPane();
        testCasePanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Simpatico");

        testCasePreview.setEditable(false);
        testCasePreview.setColumns(20);
        testCasePreview.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
        testCasePreview.setLineWrap(true);
        testCasePreview.setRows(5);
        testCasePreview.setWrapStyleWord(true);
        jScrollPane1.setViewportView(testCasePreview);

        inputPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Input"));

        inputTextArea.setColumns(20);
        inputTextArea.setRows(5);
        inputTextArea.setWrapStyleWord(true);
        jScrollPane2.setViewportView(inputTextArea);

        javax.swing.GroupLayout inputPanelLayout = new javax.swing.GroupLayout(inputPanel);
        inputPanel.setLayout(inputPanelLayout);
        inputPanelLayout.setHorizontalGroup(
            inputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2)
        );
        inputPanelLayout.setVerticalGroup(
            inputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE)
        );

        outputPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Output"));

        outputTextArea.setEditable(false);
        jScrollPane4.setViewportView(outputTextArea);
        jScrollPane4.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        javax.swing.GroupLayout outputPanelLayout = new javax.swing.GroupLayout(outputPanel);
        outputPanel.setLayout(outputPanelLayout);
        outputPanelLayout.setHorizontalGroup(
            outputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4)
        );
        outputPanelLayout.setVerticalGroup(
            outputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE)
        );

        simplifyButton.setText("Simplify");

        addTestCaseButton.setText("Add Test Case");

        comboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Simplify Input Text", "Simplify Test Cases" }));

        testCasePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Test Cases"));

        javax.swing.GroupLayout testCasePanelLayout = new javax.swing.GroupLayout(testCasePanel);
        testCasePanel.setLayout(testCasePanelLayout);
        testCasePanelLayout.setHorizontalGroup(
            testCasePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        testCasePanelLayout.setVerticalGroup(
            testCasePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 221, Short.MAX_VALUE)
        );

        jScrollPane3.setViewportView(testCasePanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 312, Short.MAX_VALUE)
                    .addComponent(addTestCaseButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(outputPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(inputPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(comboBox, 0, 164, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(simplifyButton, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(addTestCaseButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(inputPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(outputPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(simplifyButton)
                            .addComponent(comboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void addTextPanel(int id, String text, String filename) {
        testCasePanel.add(new TextPanel(id, text, filename, this), "growx, pushx, wrap");
        testCasePanel.repaint();
        this.repaint();
    }

    public void addTextPanel(ArrayList<TestCase> testcases) {
        for (TestCase tc : testcases) {
            testCasePanel.add(new TextPanel(tc.getId(), tc.getText(), tc.getFileName(), this), "growx, pushx, wrap");
        }
        testCasePanel.repaint();
        this.repaint();
    }

    public void showDialog(String text) {
        dialog = new JDialog(this);
        JPanel dialogPanel = new JPanel(new GridBagLayout());
        dialogPanel.add(new JLabel(text), new GridBagConstraints());
        dialog.getContentPane().add(dialogPanel);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
        dialog.toFront();
        dialog.setSize(150, 75);

        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dialog.setModal(true);
        dialog.setAutoRequestFocus(true);
    }

    public void hideDialog() {
        dialog.dispose();
    }

    public void refresh() {
        this.repaint();
        testCasePanel.repaint();
        inputPanel.repaint();
        outputPanel.repaint();
    }

    public String getInputText() {
        return inputTextArea.getText();
    }

    public ArrayList<Integer> getSelectedItems() {
        ArrayList<Integer> ids = new ArrayList();
        for (Component component : testCasePanel.getComponents()) {
            TextPanel panel;
            if (component instanceof TextPanel) {
                panel = (TextPanel) component;

                if (panel.isSelected()) {
                    ids.add(panel.getId());
                }
            }
        }

        return ids;
    }

    public void clearAll() {
        outputTextArea.setText("");
        inputTextArea.setText("");
        doc = outputTextArea.getStyledDocument();
    }

    public void clearOutputArea() {
        outputTextArea.setText("");
        doc = outputTextArea.getStyledDocument();
    }

    public void setMultipleOutput(ArrayList<TestCase> testcases) {
        Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
        Style regular = doc.addStyle("regular", def);

        ArrayList<Integer> ids = getSelectedItems();
        for (int id : ids) {
            for (TestCase tc : testcases) {
                if (tc.getId() == id && tc.getResult() != null) {
                    try {
                        doc.insertString(doc.getLength(), "\n", regular);
                        doc.insertString(doc.getLength(), "File: " + tc.getFileName() + "\n", regular);
                        doc.insertString(doc.getLength(), "ID: " + tc.getId() + "\n", regular);
                        doc.insertString(doc.getLength(), "Original Text: \n" + tc.getText() + "\n\n", regular);
                        doc.insertString(doc.getLength(), "Lexical Simplification Text: \n", regular);
                        setOutput(tc.getResult());
                        doc.insertString(doc.getLength(), "\n\nSyntactic Simplification Text: \n", regular);
                        setSyntacticOutput(tc.getSyntacticResult());
                        doc.insertString(doc.getLength(), "\n\n", regular);

                    } catch (BadLocationException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void setSyntacticOutput(String syntacticOutput) {
        Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
        Style regular = doc.addStyle("regular", def);

        try {
            doc.insertString(doc.getLength(), syntacticOutput + "\n", regular);
        } catch (BadLocationException ex) {
            Logger.getLogger(SimpaticoView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    int simplified = 0;
	int notSimplified = 0;
	int words = 0;
	int sentencenum = 0;

    public void setOutput(ArrayList<PreSentence> sentences) {
    	
        // Load the default style and add it as the "regular" text
        Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
        Style regular = doc.addStyle("regular", def);

        // Create a highlight style
        Style highlightCyan = doc.addStyle("highlight", regular);
        StyleConstants.setBackground(highlightCyan, new Color(175, 238, 238));

        Style highlightYellow = doc.addStyle("highlight", regular);
        StyleConstants.setBackground(highlightYellow, new Color(255, 255, 180));
        if(sentences != null)
        for (PreSentence s : sentences) {
        	sentencenum++;
            for (Word w : s.getWordList()) {
            	words++;
                try {
                    if (w.getBestSubstitute() != null) {
                        if (!w.getBestSubstitute().equalsIgnoreCase(w.getWord())) {
                        	simplified++;
                            doc.insertString(doc.getLength(), w.getBestSubstitute() + " ", highlightCyan);
                        } else {
                        	notSimplified++;
                            doc.insertString(doc.getLength(), w.getWord() + " ", highlightYellow);
                        }
                    } else if (w.isComplex() && w.getBestSubstitute() == null) {
                        doc.insertString(doc.getLength(), w.getWord() + " ", highlightYellow);
                        notSimplified++;
                    } else if (w.getWord().equalsIgnoreCase("-LSB-")) {
                        doc.insertString(doc.getLength(), "[" + " ", regular);
                    } else if (w.getWord().equalsIgnoreCase("-RSB-")) {
                        doc.insertString(doc.getLength(), "]" + " ", regular);
                    } else if (w.getWord().equalsIgnoreCase("-LRB-")) {
                        doc.insertString(doc.getLength(), "(" + " ", regular);
                    } else if (w.getWord().equalsIgnoreCase("-RRB-")) {
                        doc.insertString(doc.getLength(), ")" + " ", regular);
                    } else if (w.getWord().equalsIgnoreCase("``") || w.getWord().equalsIgnoreCase("''")) {
                        doc.insertString(doc.getLength(), "\"" + " ", regular);
                    } else if (w.getWord().equalsIgnoreCase("`") || w.getWord().equalsIgnoreCase("'")) {
                        doc.insertString(doc.getLength(), "\'" + " ", regular);
                    } else {
                        doc.insertString(doc.getLength(), w.getWord() + " ", regular);
                    }
                } catch (Exception e) {
                    System.err.print(e);
                }
            }
        }
        try {
			doc.insertString(doc.getLength(), "Simplified: "+simplified+"\n"+"Not Simplified: "+notSimplified+"\n"+"WordNum: "+words+"\n"+"SentenceNum: "+sentencenum+"\n", regular);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        this.repaint();
        testCasePanel.repaint();
    }

    public void setPreviewText(String text) {
        testCasePreview.setText(text);
        testCasePreview.repaint();
    }

    public void setProcessInputTextActionListener(ActionListener listener) {
        simplifyButton.addActionListener(listener);
    }

    public void setAddTestCaseActionListener(ActionListener listener) {
        addTestCaseButton.addActionListener(listener);
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    //Enable / disable clickable or editable elements in the interface.
    public void enableInterface(boolean status) {
        addTestCaseButton.setEnabled(status);
        simplifyButton.setEnabled(status);
        comboBox.setEnabled(status);
        inputTextArea.setEditable(status);

        for (Component component : testCasePanel.getComponents()) {
            TextPanel panel;
            if (component instanceof TextPanel) {
                panel = (TextPanel) component;
                panel.enableTextPanel(status);
            }
        }
    }

    public void setOutputText(String text) {
        try {
            // Load the default style and add it as the "regular" text
            Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
            Style regular = doc.addStyle("regular", def);
            doc.insertString(doc.getLength(), text, regular);

            this.repaint();
            testCasePanel.repaint();
        } catch (BadLocationException ex) {
            Logger.getLogger(SimpaticoView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int getSelectedAction() {
        return comboBox.getSelectedIndex();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SimpaticoView().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addTestCaseButton;
    private javax.swing.JComboBox comboBox;
    private javax.swing.JPanel inputPanel;
    private javax.swing.JTextArea inputTextArea;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JPanel outputPanel;
    private javax.swing.JTextPane outputTextArea;
    private javax.swing.JButton simplifyButton;
    private javax.swing.JPanel testCasePanel;
    private javax.swing.JTextArea testCasePreview;
    // End of variables declaration//GEN-END:variables
}
