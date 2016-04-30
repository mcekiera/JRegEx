package Re.Match;

import Re.Segment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Overall {
    private Map<Integer,List<Segment>> groups;
    private Map<Integer,String> names;
    private Matcher matcher;

    public Overall(String pattern, String text, boolean global) {
        matcher = Pattern.compile(pattern).matcher(text);
        init();
        getMatches(global);

    }

    private void init() {
        groups = new HashMap<>();
        names = new HashMap<>();
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
            e.printStackTrace();
        }
    }

    private void findMatches() {
        for (int i = 0; i <= matcher.groupCount(); i++) {
            int start = matcher.start(i) == -1 ? 0 : matcher.start(i);
            int end = matcher.end(i) == -1 ? 0 : matcher.end(i);
            Segment m = new Segment(matcher.group(), start, end);
            groups.get(i).add(m);
        }
    }

    private void prepareMap() {
        for(int i = 0; i <= 9; i++) {
            groups.put(i, new ArrayList<>());
        }
    }



}
