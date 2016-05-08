package Controller.HighlightManager;

import Model.Match.Overall;
import View.Color.GroupColor;

import javax.swing.text.BadLocationException;
import javax.swing.text.Highlighter;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Controls highlighting of JTextComponent using its Highlighter object. It diverse color of highlight on
 * Type property of given Construct represented in text. It is dedicated for highlighting description field.
 */
public class DescriptionHighlightManager{
    private Highlighter highlighter;

    public DescriptionHighlightManager(Highlighter highlighter) {
        this.highlighter = highlighter;
    }

    /**
     * Highlight representation of group number in description field, with color proper for given group.
     * @param description String with description text
     * @param overall OverallMatch object for obtaining groups data
     */
    public void process(String description, Overall overall) {                                   //TODO random group name creator!!
        Matcher matcher = Pattern.compile("(?<=\\u2009)#(?<NumOfGroup>\\w+)(?=\\s*\\[\\d+,\\d+\\])").matcher(description);
        while (matcher.find()) {
            int i = getGroupNumber(matcher.group("NumOfGroup"),overall);

            try {
                highlighter.addHighlight(matcher.start(), matcher.end(), GroupColor.getPainters().get(i));
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Provide group number string with group number or name.
     * @param group String with representation of group number/name
     * @param overall OverallMatch object for obtaining groups data
     * @return int representing given group
     */
    public int getGroupNumber(String group, Overall overall) {
        try {
            return Integer.valueOf(group);
        } catch (NumberFormatException e) {
            //e.printStackTrace();
            return getGroupByName(overall.getNamed(),group);
        }
    }

    /**
     * Provide group number from name of named group construct.
     * @param named Map of names from given pattern.
     * @param name String with name of group.
     * @return int representing given named group
     */
    private int getGroupByName(Map<Integer,String> named, String name) {
        for(Integer i : named.keySet()) {
             if(named.get(i).equals(name)) {
                 return i;
             }
        }
        return 0;
    }
}
