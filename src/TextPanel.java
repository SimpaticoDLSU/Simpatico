import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.UIManager;

import net.miginfocom.swing.MigLayout;

public class TextPanel extends JPanel implements ItemListener{
	 private int id;
	 private String fileName;
	 private String text;
	 private JCheckBox checkBox;
	 private Color highightColor = new Color(27,161,226);
	 
	
	 public TextPanel (int id, String text, String fileName){
		 super(new MigLayout());
		 this.id = id;
		 this.text = text;
		 this.fileName = fileName;
		 initPanel();
		 
	 }
	 
	 public void initPanel(){
		 
		 checkBox = new JCheckBox(fileName);
		 checkBox.addItemListener(this);
		 this.add(checkBox, "growx, pushx");
		 
		 
	 }
	 
	 public boolean isSelected(){
		 return checkBox.isSelected();
	 }
	 
	 public int  getId(){
		 return this.id;
	 }
	 
	 public String getFileName(){
		 return this.fileName;
	 }
	 
	 public String getText() {
		 return this.text;
	 }

	@Override
	public void itemStateChanged(ItemEvent e) {
		if(e.getStateChange() == ItemEvent.SELECTED){
			this.setBackground(highightColor);
			checkBox.setBackground(highightColor);
		}
		else {
			this.setBackground(UIManager.getColor("Panel.background"));
			checkBox.setBackground(UIManager.getColor("Panel.background"));
		}
	}

	
	 
	 
}
