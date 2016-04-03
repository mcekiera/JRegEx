package Model.Matching;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Matching {
    private String pattern;
    private Matcher matcher;
    private boolean global;

    private List<String> groupsID;
    private Map<Integer, Matched> regexMatch;
    private Map<Integer,List<Matched>> groupsMatch;

    public Matching(String pattern, String text) {
        match(text);
        prepareMap();
        getMatches();
    }

    private void getMatches() {
        try {
            while (matcher.find()) {
                for (int i = 0; i <= matcher.groupCount(); i++) {
                    groupsMatch.get(i).add(new Matched(matcher.start(i), matcher.end(i)));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void prepareMap() {
        for(int i = 0; i <= matcher.groupCount(); i++) {
            groupsMatch.put(i,new ArrayList<Matched>());
        }
    }

    private void match(String text) {
        try {
            matcher = Pattern.compile(pattern).matcher(text);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
