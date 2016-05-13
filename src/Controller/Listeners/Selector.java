package Controller.Listeners;

import Controller.HighlightManager.HighlightManager;
import View.Color.InputColor;

import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.DefaultHighlighter;

public class Selector implements CaretListener {
    private HighlightManager manager;

    private DefaultHighlighter.DefaultHighlightPainter backup;
    private DefaultHighlighter.DefaultHighlightPainter selection;

    public Selector(HighlightManager manager) {
        selection = new DefaultHighlighter.DefaultHighlightPainter(InputColor.SELECTION.getColor());
    }

    @Override
    public void caretUpdate(CaretEvent e) {


    }
}
