package Controller.HighlightManager;

import Model.Match.Overall;
import View.Color.GroupColor;

import javax.swing.text.BadLocationException;
import javax.swing.text.Highlighter;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DescriptionHighlightManager {
    private Highlighter highlighter;

    public DescriptionHighlightManager(Highlighter highlighter) {
        this.highlighter = highlighter;
    }

    public void process(String description, Overall overall) {                                   //TODO random group name creator!!
        Matcher matcher = Pattern.compile("(?<=\\u2009)#(?<NumOfGroup>\\w+)(?=\\s*\\[\\d+,\\d+\\])").matcher(description);
        while (matcher.find()) {
            int i;
            try {
                i = Integer.valueOf(matcher.group("NumOfGroup"));
            } catch (NumberFormatException e) {
                e.printStackTrace();
                i = getGroupByName(overall.getNamed(),matcher.group("NumOfGroup"));
            }
            try {
                highlighter.addHighlight(matcher.start(), matcher.end(), GroupColor.getPainters().get(i));
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }
    }

    private int getGroupByName(Map<Integer,String> named, String name) {
        for(Integer i : named.keySet()) {
             if(named.get(i).equals(name)) {
                 return i;
             }
        }
        return 0;
    }
}
