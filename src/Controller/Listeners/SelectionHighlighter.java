package Controller.Listeners;

import Controller.HighlightManager.HighlightManager;

import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

/**
 * Caret listener for HighlightManager implementation, serves as a part of color changing on selection process.
 */
public class SelectionHighlighter implements CaretListener {
    /**
     * Object managing JTextComponent highlighting.
     */
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


