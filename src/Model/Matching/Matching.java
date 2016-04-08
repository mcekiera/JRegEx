package Model.Matching;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Matching {
    private String pattern;
    private Matcher matcher;
    private boolean global = true;

    private List<String> groupsID;
    private Map<Integer,List<Matched>> groupsMatch;

    public Matching(String pattern, String text) {
        this.pattern = pattern;
        groupsMatch = new HashMap<>();
        match(text);
        prepareMap();
        getMatches();
    }

    public void setGlobalMode(boolean mode) {
        global = mode;
    }


    public List<Matched> getMatches(int group) {
        return groupsMatch.get(group);
    }

    public int groupCount() {
        try {
            return matcher.groupCount();
        } catch (Exception e) {
            System.out.println("exe!");
            return 0;
        }
    }


    private void getMatches() {
        if(global) {
            while (matcher.find()) {
                findMatches();
            }
        } else {
            if (matcher.find()) {
                findMatches();
            }
        }
    }

    private void findMatches() {
        try {
            for (int i = 0; i <= matcher.groupCount(); i++) {
                int start = matcher.start(i) == -1 ? 0 : matcher.start(i);
                int end = matcher.end(i) == -1 ? 0 : matcher.end(i);
                Matched m = new Matched(start, end);
                groupsMatch.get(i).add(m);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void prepareMap() {
        for(int i = 0; i <= 9; i++) {
            groupsMatch.put(i,new ArrayList<Matched>());
        }
    }

    private void match(String text) {
        try {
            matcher = Pattern.compile(pattern).matcher(text);
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

}
