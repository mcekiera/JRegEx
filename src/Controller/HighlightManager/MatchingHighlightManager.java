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

/**
 * Controls highlighting of JTextComponent using its Highlighter object. It diverse color of highlight on
 * particular matches and its elements, on field displaying matching of given pattern.
 */
public class MatchingHighlightManager extends HighlightManager{
    /**
     * Highlighter of chosen JTextComponent
     */
    private final Highlighter highlighter;
    /**
     * Map of ready-to-use DefaultHighlightPainter objects, which colors chosen for particular Type object.
     */
    private final Map<Integer,DefaultHighlighter.DefaultHighlightPainter> groupPainters;
    /**
     * Currently processed Overall object, which holds whole matching data for given regular expression.
     */
    private Overall overall = null;

    public MatchingHighlightManager(Highlighter highlighter) {
        this.highlighter = highlighter;
        groupPainters = GroupColor.getPainters();
    }

    /**
     * Highlights all matched by given regular expression fragments in JTextComponent.
     * @param overall
     */
    public void process(Overall overall) {
        highlighter.removeAllHighlights();
        highlight(overall);
    }

    /**
     * Highlight all matched fragments: captured groups and whole matches, from given Overall object.
     * @param overall object with matching data.
     */
    private void highlight(Overall overall) {
        DefaultHighlighter.DefaultHighlightPainter painter;
        this.overall = overall;
        try {
            for(int i = overall.groupCount(); i >= 0; i--){
                for(Segment matched : overall.getMatch(i)) {
                    painter = groupPainters.get(i) == null ? groupPainters.get(i % 12) : groupPainters.get(i);
                    highlighter.addHighlight(matched.getStart(),matched.getEnd(), painter);
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
    }
