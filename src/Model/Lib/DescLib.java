package Model.Lib;

import Model.Regex.Construct;
import Model.Regex.Type.Type;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.TreeMap;

public class DescLib {
    private static final DescLib INSTANCE = new DescLib();
    private final TreeMap<String, String> basic;
    private final TreeMap<String, String> mode;
    private final TreeMap<String, String> group;

    private DescLib() {
        basic = loadElements("descSimple.txt");
        mode = loadElements("descMode.txt");
        group = loadElements("descGroup.txt");
    }

    public static DescLib getInstance() {
        return INSTANCE;
    }

    private TreeMap<String, String> loadElements(String fileName){
        TreeMap<String, String> elements = new TreeMap<String,String>();
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
                return describeSimple(construct,construct.getText());
            case BACKREFERENCE:
                return describeBackreference(construct);
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
            case INTERVAL:
                return describeInterval(construct);
            case EXPRESSION:
                return "<HTML>" + construct.getText() + "<br>";
            default:
                return "Match character: " + construct.getText();
        }
    }

    private String describeBackreference(Construct construct) {
        return "<HTML>" + construct.getText() + "<br>";    //TODo dokonczyæ, zmieniæ typy logiczne zamiast komponentów
    }

    private String describeSimple(Construct c, String text) {
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
            return describeSimple(construct,"\\0") + " " + pattern;
        } else if(pattern.startsWith("\\x{")) {
            try {
                System.out.println(pattern);
                return getBold(construct) + basic.get("\\x") + " from range "
                        + pattern.substring(pattern.indexOf('{')+1,pattern.indexOf('.'))
                        + " to " + pattern.substring(pattern.lastIndexOf('.')+1, pattern.indexOf('}'));
            }catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        } else if(pattern.startsWith("\\x")) {
            return describeSimple(construct,"\\x") + " " + pattern;
        } else if(pattern.startsWith("\\u")) {
            return describeSimple(construct,"\\u") + " " + pattern;
        } else if(pattern.startsWith("\\c")) {
            return getBold(construct) + "<i>" + basic.get("\\c") + " ctrl + " + pattern.charAt(pattern.length()-1);
        } else {
            return describeSimple(construct,pattern);
        }
    }

    private String describeGrouping(Construct construct) {
        String result = "";
        String pattern = construct.getText();
        for(String prefix : group.keySet()) {
            if(pattern.startsWith(prefix)) {
                result = getBold(construct) + " " + group.get(prefix);
                if(construct.getType() == Type.LOOK_AROUND) {
                    result += " " + pattern.substring(prefix.length(), pattern.length() - 1);
                    break;
                }
            }
            if(construct.getType()==Type.NON_CAPTURING) {
                result += getModes(pattern,pattern.indexOf('?')+1,pattern.indexOf(':'));
                break;
            }
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

    private String getBold(Construct construct) {
        return "<HTML><b>" + construct.getText() + "</b><i>";
    }
}
