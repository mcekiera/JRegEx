package Controller.HighlightManager;

import Model.Match.Overall;
import Model.Segment;
import View.Color.GroupColor;
import View.Color.InputColor;

import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.util.Map;
import java.util.regex.PatternSyntaxException;

public class MatchingHighlightManager extends HighlightManager implements Runnable{
        private final Highlighter highlighter;
        private final Map<Integer,DefaultHighlighter.DefaultHighlightPainter> groupPainters;
        private DefaultHighlighter.DefaultHighlightPainter painter;
        private Overall overall = null;

    public MatchingHighlightManager(Highlighter highlighter) {
        this.highlighter = highlighter;
        groupPainters = GroupColor.getPainters();
    }

    public void process(Overall overall) {
        highlighter.removeAllHighlights();
        highlight(overall);
    }

    private void highlight(Overall overall) {
        this.overall = overall;
        try {
            for(int i = overall.groupCount(); i >= 0; i--){
                for(Segment matched : overall.getMatch(i)) {
                    painter = groupPainters.get(i)==null ? groupPainters.get(i%12) : groupPainters.get(i);
                    highlighter.addHighlight(matched.getStart(),matched.getEnd(),painter);
                }
            }
        }catch (BadLocationException | PatternSyntaxException | NullPointerException e) {
            //e.printStackTrace();
        }
    }

        @Override
        public void selectionHighlight(int position) {
            Segment s = overall.getSegmentByPosition(position);
            if(overall != null && s!=null) {
                highlighter.removeAllHighlights();
                try {
                    highlighter.addHighlight(s.getStart(),s.getEnd(),InputColor.getPainters().get(InputColor.SELECTION));
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }
                highlight(overall);
            }

        }

        @Override
        public void run() {

        }
    }
