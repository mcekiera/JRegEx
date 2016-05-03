package Model.Lib;

import Model.Regex.Construct;
import Model.Regex.Type.Type;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;

public class DescLib {
    private static final DescLib INSTANCE = new DescLib();
    private final LinkedHashMap<String, String> basic;

    private DescLib() {
        basic = loadElements();
    }

    public static DescLib getInstance() {
        return INSTANCE;
    }

    private LinkedHashMap<String, String> loadElements(){
        LinkedHashMap<String, String> elements = new LinkedHashMap<String,String>();
        try{
            String line;
            InputStreamReader stream = new InputStreamReader(getClass().getResourceAsStream("desc.txt"));
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
                return basic.get(construct.getText());
            case BACKREFERENCE:
                return describeBackreference(construct);
            case MODE:
                return describeMode(construct);
            default:
                return desc(construct.getType());
        }
    }

    public String describeBackreference(Construct construct) {
        return Type.BACKREFERENCE.toString();
    }

    public String describeMode(Construct construct) {
        return Type.MODE.toString();
    }

    public String desc(Type type) {
        return type.toString();
    }
}
