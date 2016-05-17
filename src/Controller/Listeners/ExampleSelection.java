package Controller.Listeners;

import Controller.Main;

import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

public class ExampleSelection implements CaretListener {
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
