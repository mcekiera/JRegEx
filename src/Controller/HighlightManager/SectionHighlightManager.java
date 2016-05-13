package Controller.HighlightManager;

import Model.Regex.Construct;
import Model.Segment;

import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class SectionHighlightManager {
    private final Highlighter pattern;
    private final Highlighter text;


    public SectionHighlightManager(Highlighter pattern, Highlighter text) {
        this.pattern = pattern;
        this.text = text;
    }

    public void process(Map<Construct, List<Segment>> map) {

        for(Construct construct : map.keySet()) {
            int r = new Random().nextInt(255);
            int g = new Random().nextInt(255);
            int b = new Random().nextInt(255);
            DefaultHighlighter.DefaultHighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(new Color(r,g,b));
            for(Segment segment : map.get(construct)) {
                try {
                    if(segment == null) {
                        pattern.addHighlight(construct.getStart(), construct.getEnd(), new DefaultHighlighter.DefaultHighlightPainter(Color.LIGHT_GRAY));
                    } else {

                            pattern.addHighlight(construct.getStart(), construct.getEnd(), painter);
                            text.addHighlight(segment.getStart(), segment.getEnd(), painter);

                    }
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
