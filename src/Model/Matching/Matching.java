package Model.Matching;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Matching {
    private String pattern;
    private Matcher matcher;
    private boolean global;

    private List<String> groupsID;
    private Map<String, Matched> regexMatch;

    public Matching(String pattern, String text) {
         matcher = Pattern.compile(pattern).matcher(text);
    }

    public void extractGroupsID(String pattern, int count) {
        matcher.groupCount();

    }


}
