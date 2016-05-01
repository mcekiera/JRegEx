package Controller.Listeners;

import Controller.HighlightManager.HighlightManager;

import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

public class SelectionHighlighter  implements CaretListener {
    private HighlightManager manager;

    public SelectionHighlighter(HighlightManager manager) {
        this.manager = manager;
    }

    @Override
    public void caretUpdate(CaretEvent e) {
        int position = e.getDot();
        manager.selectionHighlight(position);
    }
}


