
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;

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
        testCasePanel = new javax.swing.JPanel();
        comboBox = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Simpatico");

        testCasePreview.setEditable(false);
        testCasePreview.setColumns(20);
        testCasePreview.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
        testCasePreview.setLineWrap(true);
        testCasePreview.setRows(5);
        testCasePreview.setWrapStyleWord(true);
        jScrollPane1.setViewportView(testCasePreview);
        inputTextArea.setWrapStyleWord(true);
        inputPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Input"));

        inputTextArea.setColumns(20);
        inputTextArea.setRows(5);
        inputTextArea.setLineWrap(true);
        jScrollPane2.setViewportView(inputTextArea);
        jScrollPane2.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
      

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

        testCasePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Test Cases"));

        javax.swing.GroupLayout testCasePanelLayout = new javax.swing.GroupLayout(testCasePanel);
        testCasePanel.setLayout(testCasePanelLayout);
        testCasePanelLayout.setHorizontalGroup(
            testCasePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        testCasePanelLayout.setVerticalGroup(
            testCasePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 228, Short.MAX_VALUE)
        );

        comboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Simplify Input Text", "Simplify Test Cases" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 312, Short.MAX_VALUE)
                    .addComponent(addTestCaseButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(testCasePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                        .addComponent(testCasePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
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
                        doc.insertString(doc.getLength(), "Original Text: \n" + tc.getText() + "\n", regular);
                        doc.insertString(doc.getLength(), "Simplified Text: \n", regular);
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

    public void setOutput(ArrayList<PreSentence> sentences) {

        // Load the default style and add it as the "regular" text
        Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
        Style regular = doc.addStyle("regular", def);

        // Create a highlight style
        Style highlightCyan = doc.addStyle("highlight", regular);
        StyleConstants.setBackground(highlightCyan, new Color(175, 238, 238));

        Style highlightYellow = doc.addStyle("highlight", regular);
        StyleConstants.setBackground(highlightYellow, new Color(255, 255, 180));
        
        for (PreSentence s : sentences) {
            for (Word w : s.getWordList()) {
                try {
                    if (w.getBestSubstitute() != null) {
                        if (!w.getBestSubstitute().equalsIgnoreCase(w.getWord())) {
                            doc.insertString(doc.getLength(), w.getBestSubstitute() + " ", highlightCyan);
                        } else {
                            doc.insertString(doc.getLength(), w.getWord() + " ", highlightYellow);
                        }
                    } else if (w.isComplex() && w.getBestSubstitute() == null) {
                        doc.insertString(doc.getLength(), w.getWord() + " ", highlightYellow);
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
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JPanel outputPanel;
    private javax.swing.JTextPane outputTextArea;
    private javax.swing.JButton simplifyButton;
    private javax.swing.JPanel testCasePanel;
    private javax.swing.JTextArea testCasePreview;
    // End of variables declaration//GEN-END:variables
}
