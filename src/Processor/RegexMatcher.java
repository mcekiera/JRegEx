package Processor;

import GUI.Highlight;
import GUI.InputFieldWrapper;
import GUI.MatchingFieldWrapper;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class RegexMatcher{
    private Matcher matcher;
    private MatchingFieldWrapper area;
    private InputFieldWrapper field;



    public RegexMatcher() {
    }

    public List<Highlight> match(String pattern, String text) {
        List<Highlight> result = new ArrayList<>();
        try {
            matcher = Pattern.compile(pattern).matcher(text);
            if (matcher.find()) {
                 result.add(new Highlight(matcher.start(),matcher.end(),new Color(160,200,255)));
            }
        } catch (PatternSyntaxException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return result;
    }


}
