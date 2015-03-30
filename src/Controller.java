import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controller {

    private final int INPUT_TEXT = 0;
    private final int TEST_CASE = 1;

    Model model;
    SimpaticoView view;

    public Controller(Model model, SimpaticoView view) {
        this.model = model;
        this.view = view;

        //Thread for initializing assets.
        Process worker = new Process(model, view, this);

        //Initialize Interface and disable interface items while initializing assets.
        view.initGUI();
        view.addTextPanel(model.loadTestCases());
        view.enableInterface(false);
        view.showDialog("Loading Assets");

        //Start Loading Assets
        worker.execute();
    }

    //Set the action Listeners for the interface
    public void setActionListeners() {
        view.setProcessInputTextActionListener(new SimplifyButtonAction());
    }

    //Simplify the input text
    //Type: INPUT_TEXT : simplify the text given in the input field
    //Type: TEST_CASE : simplify the selected test cases
    private void simplifyText(int type) {
        view.enableInterface(false);
        view.clearOutputArea();
        view.setOutputText("Simplifying Text, Please Wait.");
        view.refresh();

        switch (type) {
            case INPUT_TEXT:
                model.simplifyText(view.getInputText());
                break;
            case TEST_CASE:
                model.simplifyMultipleTexts(view.getSelectedItems());
                break;
        }

        view.clearOutputArea();
        switch (type) {
            case INPUT_TEXT:
                view.setOutput(model.getResults());
                view.setSyntacticOutput(model.getSyntacticOutput());
                break;
            case TEST_CASE:
                view.setMultipleOutput(model.getTestCases());
                break;
        }

        view.enableInterface(true);
        view.hideDialog();
        view.refresh();
    }

    public class SimplifyButtonAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            switch (view.getSelectedAction()) {
                case INPUT_TEXT:
                    if (!view.getInputText().isEmpty()) {
                        simplifyText(INPUT_TEXT);

                    } else {
                        view.showMessage("No input.");
                    }
                    break;
                case TEST_CASE:
                    simplifyText(TEST_CASE);
                    break;
            }
        }
    }
}
