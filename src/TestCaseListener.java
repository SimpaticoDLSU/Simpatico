import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author MC
 */
public class TestCaseListener implements FocusListener {

    private int id;
    private final Model model;
    private final SimpaticoView view;

    public TestCaseListener(Model model, SimpaticoView view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public void focusGained(FocusEvent e) {
        System.out.println();
        view.setPreviewText(model.getTestCase(id).getText());
    }

    @Override
    public void focusLost(FocusEvent e) {

    }

    public void setID(int id) {
        this.id = id;
    }
}
