import java.util.ArrayList;

import language.PreSentence;



public class TestCase {

    private int id;
    private String text;
    private String fileName;
    private boolean isSelected;
    private String syntacticResult;
    private ArrayList<PreSentence> result;

    public String getSyntacticResult() {
        return syntacticResult;
    }

    public void setSyntacticResult(String syntacticResult) {
        this.syntacticResult = syntacticResult;
    }

    public ArrayList<PreSentence> getResult() {
        return result;
    }

    public void setResult(ArrayList<PreSentence> result) {
        this.result = result;
    }

    public void isSelected(boolean bool) {
        this.isSelected = bool;
    }

    public boolean isSelected() {
        return this.isSelected;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

}
