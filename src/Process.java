

import javax.swing.SwingWorker;


public class Process extends SwingWorker<Integer, Integer>
{
	 Model model;
	 SimpaticoView view;
	 Controller con;
	public Process(Model model, SimpaticoView view, Controller con) {
		  this.model = model;
		  this.view = view;
		  this.con = con;
	}
	protected Integer doInBackground() throws InterruptedException {
        model.initialize();
        return null;
    }

    protected void process() {
    }

    //Enable Action Listeners and enable the interface
    protected void done() {
        con.setActionListeners();
        view.hideDialog();
        view.enableInterface(true);
    }
}
