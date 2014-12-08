import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;




public class Controller {
	Model model;
	View view;
	
	public Controller(Model model, View view){
		this.model = model;
		this.view = view;
		view.initGUI();
		view.addTextPanel(model.loadTestCases());
		setActionListeners();
	}
	
	private void setActionListeners(){
		view.setProcessSelectedItemsActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				if(!view.getSelectedItems().isEmpty()){
					view.enableTestCaseList(false);
	            	view.showDialog("Simplifying. Please wait.");
	            	ArrayList<String> texts = new ArrayList<String>();	            	
	            	model.simplifyMultipleTexts(view.getSelectedItems());
					view.enableTestCaseList(true);
	            	view.clearOutputArea();
	            	
	                view.setMultipleOutput(model.getTestCases());
	                view.hideDialog();
	                view.refresh();
					
					
				}
				
			}
			
		});
		
		
		
		view.setProcessInputTextActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(!view.getInputText().isEmpty()){
					view.enableTestCaseList(false);
	            	view.showDialog("Simplifying. Please wait.");
	            	model.simplifyText(view.getInputText());
	            	view.enableTestCaseList(true);
	            	view.clearOutputArea();
	            	view.setOutput(model.getResults());
	            	view.hideDialog();
		            view.refresh();	        		
					
				}else
					view.showMessage("No input.");
				
				
				
			}//actionPerformed end
			
		});
	}
	
	
	
}
