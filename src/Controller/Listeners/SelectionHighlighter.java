package Controller.Listeners;

import Controller.HighlightManager.HighlightManager;

import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

/**
 * Caret listener for HighlightManager implementation, serves as a part of process for color changing on selection.
 */
public class SelectionHighlighter implements CaretListener {
    /**
     * Object managing JTextComponent highlighting.
     */
    private HighlightManager[] managers;

    public SelectionHighlighter(HighlightManager ... managers) {
        this.managers = managers;
    }

    @Override
    public void caretUpdate(CaretEvent e) {
        int position = e.getDot();
        for(HighlightManager manager : managers) {
            manager.selectionHighlight(position);
        }
    }
}


