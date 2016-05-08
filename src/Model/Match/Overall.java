package Model.Match;

import Model.Segment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class Overall {
    private Map<Integer,List<Segment>> groups;
    private Matcher matcher;
    private String text;
    private Map<Integer,String> named;

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

    public Map<Integer,List<Segment>> matchMap() {
        return groups;
    }

    public void setNamed(Map<Integer,String> named) {
        this.named = named;
    }

    public Map<Integer,String> getNamed() {
        return named;
    }

    public int groupCount() {
        return matcher.groupCount();
    }

    public List<Segment> getMatch(int group) {
        return matchMap().get(group);
    }

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

    public Segment getSegmentByPosition(int position) {
        for(int i = groupCount(); i >=0; i--) {
            for(Segment segment : getMatch(i)) {
                if(segment.getStart() <= position && segment.getEnd() > position) {
                    return segment;
                }
            }
        }
        return null;
    }

    public String getMatchDescription() {
        StringBuilder result = new StringBuilder();
        result.append("<html>");
        String name = "";
        for(int k = 0; k < groups.get(0).size();k++) {
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

    private void init() {
        groups = new HashMap<>();
        prepareMap();
    }

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

    private void findMatches() {
        for (int i = 0; i <= matcher.groupCount(); i++) {
            int start = matcher.start(i) == -1 ? 0 : matcher.start(i);
            int end = matcher.end(i) == -1 ? 0 : matcher.end(i);
            Segment m = new Segment(text, start, end);
            groups.get(i).add(m);
        }
    }

    private void prepareMap() {
        for(int i = 0; i <= matcher.groupCount(); i++) {
            groups.put(i, new ArrayList<>());
        }
    }





}
