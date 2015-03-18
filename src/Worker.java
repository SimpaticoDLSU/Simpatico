import javax.swing.SwingWorker;


public class Worker extends SwingWorker<Void, Void>{
	private final int INPUT_TEXT = 0;
    private final int TEST_CASE = 1;
	private int type;
	private Model model;
	private SimpaticoView view;
	public Worker(Model model, SimpaticoView view, int type) {
		  this.model = model;
		  this.view = view;
	}
	 //Process (Simplify) while thread is running
    protected Void doInBackground() throws InterruptedException {
        switch (type) {
            case INPUT_TEXT:
                model.simplifyText(view.getInputText());
                break;
            case TEST_CASE:
                model.simplifyMultipleTexts(view.getSelectedItems());
                break;
        }
        return null;
    }

    protected void process() {
        
    }

    //Show output and re-enable the interface after simplifying.
    protected void done() {
        view.clearOutputArea();
        switch (type) {
            case INPUT_TEXT:
                view.setOutput(model.getResults());
                break;
            case TEST_CASE:
                view.setMultipleOutput(model.getTestCases());
                break;
        }
       
        view.enableInterface(true);
        view.hideDialog();
        view.refresh();
    }

}
