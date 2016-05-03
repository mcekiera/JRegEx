package Model.Lib;

import Model.Regex.Construct;
import Model.Regex.Type.Type;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;

public class DescLib {
    private static final DescLib INSTANCE = new DescLib();
    private final LinkedHashMap<String, String> basic;
    private final LinkedHashMap<String, String> mode;

    private DescLib() {
        basic = loadElements("descSimple.txt");
        mode = loadElements("descMode.txt");
    }

    public static DescLib getInstance() {
        return INSTANCE;
    }

    private LinkedHashMap<String, String> loadElements(String fileName){
        LinkedHashMap<String, String> elements = new LinkedHashMap<String,String>();
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
            case MODE:
                return describeMode(construct);
            case SPECIFIC_CHAR:
                return describeSpecificChar(construct);
            default:
                return "Match character: " + construct.getText();
        }
    }

    public String describeBackreference(Construct construct) {
        return "<HTML>" + Type.BACKREFERENCE.toString() + "<br>" + "coœtam</HTML>";
    }

    public String describeSimple(String text) {
        return "<HTML><b>" + text + "</b><i>" + basic.get(text);
    }

    public String describeMode(Construct construct) {
        String result = "<HTML>Modifier:";
        String prefix;
        String pattern = construct.getText();
        boolean disable = false;
        for(int i = pattern.indexOf('?')+1; i<pattern.length()-1; i++) {
            if (pattern.charAt(i) == '-') {
                disable = true;
                continue;
            }
            prefix = disable ?  "<br>" + pattern.charAt(i) + " - Disables " : "<br>" + pattern.charAt(i) + " - Enables ";
            result += pattern.charAt(i) + prefix + mode.get(String.valueOf(pattern.charAt(i)));
        }

        return result + "</html>";
    }

    public String describeSpecificChar(Construct construct) {
        String pattern = construct.getText();
        if(pattern.startsWith("\\0")) {
            return describeSimple("\\0") + " " + pattern;
        } else if(pattern.startsWith("\\x")) {
            return "";
        } else if(pattern.startsWith("\\u")) {
            return describeSimple("\\u") + " " + pattern;
        } else if(pattern.startsWith("\\c")) {
            return "<HTML><b>" + pattern + "</b><i>" + basic.get("\\c") + " ctrl + " + pattern.charAt(pattern.length()-1);
        } else {
            return describeSimple(pattern);
        }
    }

    public String desc(Type type) {
        return type.toString();
    }
}
