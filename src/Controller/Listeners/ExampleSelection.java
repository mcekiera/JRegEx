package Controller.Listeners;

import Controller.Main;

import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

/**
 * Caret listener for matching fields, serves to highlight matched fragments chosen by User.
 */
public class ExampleSelection implements CaretListener {
    /**
     * Main object controlling application.
     */
    private final Main main;

    public ExampleSelection(Main control) {
        this.main = control;
    }

    @Override
    public void caretUpdate(CaretEvent e) {
        int position = e.getDot();
        main.updateCompareView(position);
    }
}
