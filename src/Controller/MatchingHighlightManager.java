package Controller;

import Model.Match.Overall;
import Model.Segment;
import View.Color.GroupColor;

import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.PatternSyntaxException;

public class MatchingHighlightManager {
    private final Highlighter highlighter;
    private final Map<Integer,DefaultHighlighter.DefaultHighlightPainter> groupPainters;
    private DefaultHighlighter.DefaultHighlightPainter painter;
    private int level;

    public MatchingHighlightManager(Highlighter highlighter) {
        this.highlighter = highlighter;
        groupPainters = getGroupPainters();
    }

    public void process(Overall overall) {
        highlighter.removeAllHighlights();
        highlight(overall);
    }

    private void highlight(Overall overall) {
        try {
            for(int i = overall.groupCount(); i >= 0; i--){
                for(Segment matched : overall.getMatch(i)) {
                    painter = groupPainters.get(i);
                    highlighter.addHighlight(matched.getStart(),matched.getEnd(),painter);
                }
            }
        }catch (BadLocationException | PatternSyntaxException e) {
            e.printStackTrace();
        }
    }


    private Map<Integer,DefaultHighlighter.DefaultHighlightPainter> getGroupPainters() {
        Map<Integer,DefaultHighlighter.DefaultHighlightPainter> temp = new HashMap<>();
        int i = 0;
        for(GroupColor color : GroupColor.values()) {
            temp.put(i,new DefaultHighlighter.DefaultHighlightPainter(color.getColor()));
            i++;
        }
        return temp;
    }
}
