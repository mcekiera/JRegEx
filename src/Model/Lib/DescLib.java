package Model.Lib;

import Model.Regex.Construct;
import Model.Regex.Type.Type;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Map;

public class DescLib {
    private static final DescLib INSTANCE = new DescLib();
    private final Map<String, String> basic;
    private final Map<String, String> mode;
    private final Map<String, String> group;

    private DescLib() {
        basic = loadElements("descSimple.txt");
        mode = loadElements("descMode.txt");
        group = loadElements("descGroup.txt");
    }

    public static DescLib getInstance() {
        return INSTANCE;
    }

    private LinkedHashMap<String, String> loadElements(String fileName){
        LinkedHashMap<String, String> elements = new LinkedHashMap<>();
        try{
            String line;
            InputStreamReader stream = new InputStreamReader(getClass().getResourceAsStream(fileName));
            BufferedReader reader = new BufferedReader(stream);
            while((line = reader.readLine()) != null){
                String[] temp = line.split("    ");
                elements.put(temp[0],temp[1]);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return elements;
    }

    public String getDescription(Construct construct) {
        switch (construct.getType()) {
            case BOUNDARY:
            case PREDEFINED:
            case QUANTIFIER:
                return describeSimple(construct.getText());
            case BACKREFERENCE:
                return describeBackreference(construct);
            case QUOTATION:
                return describeQuotation(construct);
            case MODE:
                return describeMode(construct);
            case SPECIFIC_CHAR:
                return describeSpecificChar(construct);
            case RANGE:
                return describeRange(construct);
            case LOOK_AROUND:
            case ATOMIC:
            case CAPTURING:
            case NON_CAPTURING:
                return describeGrouping(construct);
            case CHAR_CLASS:
                return describeCharacterClass(construct);
            case COMPONENT:
                return describeComponent(construct);
            case INTERVAL:
                return describeInterval(construct);
            case EXPRESSION:
                return "<HTML><b>" + construct.getType() + ":<br>" + construct.getText() + "</b><br>";
            case ALTERNATION:
                return "<HTML><b>ALTERNATIVE</b> " + construct.getText();
            case UNBALANCED:
                return "Unbalanced structure on index " + construct.getStart() + ":" + construct.getText();
            case INCOMPLETE:
                return "Incomplete structure on index " + construct.getStart() + ":" + construct.getText();
            case INVALID_BACKREFERENCE:
                return "Invalid backreference" + ":" + construct.getText();
            case INVALID_RANGE:
                return "Invalid range" + ":" + construct.getText();
            case INVALID_INTERVAL:
                return "Invalid interval" + ":" + construct.getText();
            default:
                return "<html><b>" + construct.getText() + "</b><i> Match character: " + construct.getText();
        }
    }

    private String describeBackreference(Construct construct) {
    String group = construct.getText().contains("\\k<") ?
            " named '" + construct.getText().substring(3,construct.getText().length()-1) + "'":
            " #" + construct.getText().substring(1);
        return "<HTML><b>" + construct.getText() + "</b><i> Backreference to captured group" + group;
    }

    private String describeSimple(String text) {
        return "<HTML><b>" + text + "</b><i>" + basic.get(text);
    }

    private String describeMode(Construct construct) {
        String result = "<HTML>Modifier:";
        String pattern = construct.getText();
        result += getModes(pattern,pattern.indexOf('?')+1,pattern.length()-1);
        return result + "</html>";
    }

    private String getModes(String pattern, int start, int end) {
        String prefix;
        boolean disable = false;
        String result = "";
        for(int i = start; i<end; i++) {
            if (pattern.charAt(i) == '-') {
                disable = true;
                continue;
            }
            prefix = disable ?  "<br>" + pattern.charAt(i) + " - Disables " : "<br>" + pattern.charAt(i) + " - Enables ";
            result += prefix + mode.get(String.valueOf(pattern.charAt(i)));
        }
        return result;
    }

    private String describeCharacterClass(Construct construct) {
        if(construct.getText().startsWith("[^")) {
            return getBold(construct) + " Negative character class.";
        } else {
            return getBold(construct) + " Character class";
        }
    }

    private String describeSpecificChar(Construct construct) {
        String pattern = construct.getText();
        if(pattern.startsWith("\\0")) {
            return describeSimple("\\0") + " " + pattern;
        } else if(pattern.startsWith("\\x{")) {
            try {
                System.out.println(pattern);
                return getBold(construct) + basic.get("\\x") + " of: "
                        + pattern.substring(pattern.indexOf('{')+1, pattern.indexOf('}'));
            }catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        } else if(pattern.startsWith("\\x")) {
            return describeSimple("\\x") + " " + pattern;
        } else if(pattern.startsWith("\\u")) {
            return describeSimple("\\u") + " " + pattern;
        } else if(pattern.startsWith("\\c")) {
            return getBold(construct) + "<i>" + basic.get("\\c") + " ctrl + " + pattern.charAt(pattern.length()-1);
        } else {
            return describeSimple(pattern);
        }
    }

    private String describeQuotation(Construct construct) {
        if(construct.getText().startsWith("\\Q")) {
            int end = !construct.getText().contains("\\E") ? construct.getText().length() : construct.getText().indexOf("\\E");
            return "<html><b>" + construct.getText() + " </b><i> Match quotation: "
                    + construct.getText().substring(2,end);
        } else {
            return "<html><b>" + construct.getText() + " </b><i> Literally match: " + construct.getText().substring(1);
        }

    }

    private String describeGrouping(Construct construct) {
        String result = "<html>";
        String pattern = construct.getText();
        int length = 0;
        for(String prefix : group.keySet()) {
            if(construct.getText().startsWith(prefix)) {
                result += group.get(prefix);
                length = prefix.length();
                break;
            }
        }
        if(construct.getType()==Type.NON_CAPTURING) {
            result += getModes(pattern, pattern.indexOf('?') + 1, pattern.indexOf(':'));
        } else if(construct.getType() == Type.LOOK_AROUND) {
            result += " for: " + pattern.substring(length,pattern.length()-1);
        }
        return result;
    }

    private String describeRange(Construct construct) {
        return getBold(construct) + " Character from a range: " + construct.getText();
    }

    private String describeInterval(Construct construct) {
        String pattern = construct.getText();
        String prefix;
        if(pattern.endsWith("}+")) {
            prefix = "Possessive interval, ";
        } else if (pattern.endsWith("}?")) {
            prefix = "Reluctant interval, ";
        } else {
            prefix = "Greedy interval, ";
        }
        return prefix + describeIntervalInterior(pattern.substring(1, pattern.indexOf('}')));

    }

    private String describeIntervalInterior(String string) {
        if(!string.contains(",")) {
            return "exactly " + string + " times.";
        } else if (string.endsWith(",")) {
            return "at least " + string.substring(0,string.indexOf(',')) + " times.";
        } else {
            String[] split = string.split(",");
            return "at least " + split[0] + " but not more than " + split[1] + " times.";
        }
    }

    private String describeComponent(Construct construct) {
        if (group.keySet().contains(construct.getText())) {
            return "Beginning of " + group.get(construct.getText());
        } else if (construct.getText().equals( ")")){
            return "Closing bracket of composite construct";
        } else if (construct.getText().equals("|")) {
            return "OR";
        }
            return "Component of group.";
    }

    public boolean contains(String construct) {
        return basic.containsKey(construct);
    }

    private String getBold(Construct construct) {
        return "<HTML><b>" + construct.getText() + "</b><i>";
    }
}
