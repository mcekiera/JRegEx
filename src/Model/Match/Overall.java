package Model.Match;

import Model.Segment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Provides a data about all matched by given expression fragments within particular test string.
 */
public class Overall {
    /**
     * Map containing all matched fragments, by all capturing groups - named and standard.
     */
    private Map<Integer,List<Segment>> groups;
    /**
     * Matcher for particular pattern on given test text.
     */
    private Matcher matcher;
    /**
     * Test text for matching.
     */
    private String text;
    /**
     * Map with names for named capturing groups.
     */
    private Map<Integer,String> named;
    /**
     * Object referring to matched fragment currently chosen by User on matching field.
     */
    private Segment selected = null;

    /**
     * Constructor of class.
     * @param pattern currently tested regular expression
     * @param text currently tested string to match
     * @param global modifier for single/global match
     */
    public Overall(String pattern, String text, boolean global) {
        this.text = text;
        try {
            matcher = Pattern.compile(pattern).matcher(text);
        } catch (PatternSyntaxException e) {
            matcher = Pattern.compile("").matcher("");
        }
        init();
        getMatches(global);
    }

    /**
     *
     * @return Map with matched by pattern fragments.
     */
    public Map<Integer,List<Segment>> matchMap() {
        return groups;
    }

    /**
     * Sets map with names for named capturing groups.
     * @param named map with names.
     */
    public void setNamed(Map<Integer,String> named) {
        this.named = named;
    }

    /**
     * @return map with names of named capturing groups.
     */
    public Map<Integer,String> getNamed() {
        return named;
    }

    /**
     * @return integer, number of capturing groups in pattern.
     */
    public int groupCount() {
        return matcher.groupCount();
    }

    /**
     * Provide results of matching by particular groups.
     * @param group number of group.
     * @return List of Segment objects with captured fragments
     */
    public List<Segment> getMatch(int group) {
        return matchMap().get(group);
    }

    /**
     * Provide String object representing matched fragment chosen by User.
     * @param position chosen by mouse click by User.
     * @return String with matched fragment.
     */
    public String getMatchByPosition(int position) {
        for(int i = groupCount(); i >=0; i--) {
            int match = 0;
            String name = "";
            for(Segment segment : getMatch(i)) {
                match++;
                if(segment.getStart() <= position && segment.getEnd() > position) {
                    if(named!=null && named.get(i)!=null) {
                        name = named.get(i);
                    }
                    return "<html>Match <b># " + match + "</b><br>" + "Group <b>#" + (name.equals("") ? i : name) + "</b><br>" + segment.getDescription();
                }
            }
        }
        return null;
    }

    /**
     * Provide Segment object representing matched fragment chosen by User.
     * @param position chosen by mouse click by User.
     * @return Segment with matched fragment.
     */
    public Segment getSegmentByPosition(int position) {
        if(selected!=null && selected.getStart() <= position && selected.getEnd() > position) {
            return selected;
        } else {
            for (int i = 0; i <= groupCount(); i++) {
                for (Segment segment : getMatch(i)) {
                    if (segment.getStart() <= position && segment.getEnd() > position) {
                        return segment;
                    }
                }
            }
            return null;
        }

    }

    /**
     * Determines if on given index of tested String, there is matched fragment.
     * @param position index of tested String
     * @return true if on given position is matched fragment.
     */
    public boolean hasSegment(int position) {
        try {
            selected = getSegmentByPosition(position);
            return true;
        } catch (NullPointerException e) {
            return false;
        }
    }

    /**
     * Provide explanatory description of given match, providing information about content, start and end index.
     * @return String with description.
     */
    public String getMatchDescription() {
        StringBuilder result = new StringBuilder();
        result.append("<html>");
        String name;
        for(int k = 0; k < groups.get(0).size();k++) {
            name = "";
            result.append(("<b>Match #")).append(k + 1).append(":</b><br>");
            for(int j = 0; j <= groupCount(); j++) {
                if(named!=null && named.get(j)!=null) {
                    name = named.get(j);
                }
                result.append("\u2009#").append(name.equals("") ? j : name).append("   ")
                        .append(groups.get(j).get(k).getDescription()).append("<br>");
            }
            result.append("<br>");
        }
        result.append("</html>");
        return result.toString();
    }

    /**
     * Initialize necessary fields.
     */
    private void init() {
        groups = new HashMap<>();
        prepareMap();
    }

    /**
     * Depending on passed value, it decide which kind of matching to use: single or global.
     * @param global flag for global match.
     */
    private void getMatches(boolean global) {
        try {
            if (global) {
                while (matcher.find()) {
                    findMatches();
                }
            } else {
                if (matcher.find()) {
                    findMatches();
                }
            }
        } catch (NullPointerException e) {
            //e.printStackTrace();
        }
    }

    /**
     * Matches current pattern against current test string.
     */
    private void findMatches() {
        for (int i = 0; i <= matcher.groupCount(); i++) {
            int start = matcher.start(i) == -1 ? 0 : matcher.start(i);
            int end = matcher.end(i) == -1 ? 0 : matcher.end(i);
            Segment m = new Segment(text, start, end);
            groups.get(i).add(m);
        }
    }

    /**
     * Prepares groups map, to avoid NullPointerException
     */
    private void prepareMap() {
        for(int i = 0; i <= matcher.groupCount(); i++) {
            groups.put(i, new ArrayList<>());
        }
    }





}
